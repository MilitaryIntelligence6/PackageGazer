package cn.misection.gazer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import cn.misection.gazer.constant.common.EnumString;
import cn.misection.gazer.dao.SharedPrefHelper;
import cn.misection.gazer.service.GazeAccessibilityService;
import cn.misection.gazer.service.GazeService;
import cn.misection.gazer.receiver.NotificationActionReceiver;
import cn.misection.gazer.system.AppSystem;
import cn.misection.gazer.view.GazeView;

/**
 * @author Administrator
 */
public class MainActivity extends Activity
        implements OnCheckedChangeListener {

    private CompoundButton mWindowSwitch;

    private CompoundButton mNotificationSwitch;

    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWindowSwitch = (CompoundButton) findViewById(R.id.sw_window);
        mWindowSwitch.setOnCheckedChangeListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (!getResources().getBoolean(R.bool.qs_tile_service_availability)) {
                findViewById(R.id.useNotificationPref).setVisibility(View.GONE);
                findViewById(R.id.divider_useNotificationPref).setVisibility(View.GONE);
            }
        }
        mNotificationSwitch = (CompoundButton) findViewById(R.id.sw_notification);
        if (mNotificationSwitch != null) {
            mNotificationSwitch.setOnCheckedChangeListener(this);
        }
        if (getResources().getBoolean(R.bool.use_watching_service)) {
            AppSystem.out.printt(this, EnumString.EMPTY.value());
            startService(new Intent(this, GazeService.class));
        }
        if (getIntent().getBooleanExtra(
                EnumString.EXTRA_FROM_QS_TILE.value(),
                false)) {
            mWindowSwitch.setChecked(true);
        }
        mReceiver = new UpdateSwitchReceiver();
        registerReceiver(mReceiver, new IntentFilter(
                EnumString.ACTION_STATE_CHANGED.value()));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (getIntent().getBooleanExtra(
                EnumString.EXTRA_FROM_QS_TILE.value(),
                false)) {
            mWindowSwitch.setChecked(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshWindowSwitch();
        refreshNotificationSwitch();
        NotificationActionReceiver.cancelNotification(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (SharedPrefHelper.hasWindowShown(this) && !(getResources().getBoolean(R.bool.use_accessibility_service) && GazeAccessibilityService.requireInstance() == null)) {
            NotificationActionReceiver.showNotification(this, false);
        }
    }

    private void refreshWindowSwitch() {
        mWindowSwitch.setChecked(SharedPrefHelper.hasWindowShown(this));
        if (getResources().getBoolean(R.bool.use_accessibility_service)) {
            if (GazeAccessibilityService.requireInstance() == null) {
                mWindowSwitch.setChecked(false);
            }
        }
    }

    private void refreshNotificationSwitch() {
        if (mNotificationSwitch != null) {
            mNotificationSwitch.setChecked(
                    !SharedPrefHelper.hasNotificationToggleEnabled(this));
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // TODO 这里应该解耦成多个listener;
        // 遇到 notification button;
        if (buttonView == mNotificationSwitch) {
            if (SharedPrefHelper.hasSettingTileAdded(this)) {
                SharedPrefHelper.putNotificationToggleEnabled(this, !isChecked);
            } else if (isChecked) {
                Toast.makeText(
                        this,
                        R.string.toast_add_tile,
                        Toast.LENGTH_LONG)
                        .show();
                buttonView.setChecked(false);
            } else {
                // idCheck == false; 这里put true;
                SharedPrefHelper
                        .putNotificationToggleEnabled(this, true);
            }
            return;
        }
        if (isChecked && buttonView == mWindowSwitch
                && getResources().getBoolean(R.bool.use_accessibility_service)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1 && !Settings.canDrawOverlays(this)) {
                new AlertDialog.Builder(this)
                        .setMessage(R.string.dialog_enable_overlay_window_msg)
                        .setPositiveButton(R.string.dialog_enable_overlay_window_positive_btn
                                , (dialog, which) -> {
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                                    intent.setData(Uri.parse(String.format("package:%s", getPackageName())));
                                    startActivity(intent);
                                    dialog.dismiss();
                                })
                        .setNegativeButton(android.R.string.cancel
                                , (dialog, which) -> {
                                    SharedPrefHelper.putWindowShown(MainActivity.this, false);
                                    refreshWindowSwitch();
                                })
                        .setOnCancelListener(dialog -> {
                            SharedPrefHelper.putWindowShown(MainActivity.this, false);
                            refreshWindowSwitch();
                        })
                        .create()
                        .show();
                buttonView.setChecked(false);
                return;
            } else if (GazeAccessibilityService.requireInstance() == null) {
                new AlertDialog.Builder(this)
                        .setMessage(R.string.dialog_enable_accessibility_msg)
                        .setPositiveButton(R.string.dialog_enable_accessibility_positive_btn
                                , (dialog, which) -> {
                                    SharedPrefHelper.putWindowShown(MainActivity.this, true);
                                    Intent intent = new Intent();
                                    intent.setAction("android.settings.ACCESSIBILITY_SETTINGS");
                                    startActivity(intent);
                                })
                        .setNegativeButton(android.R.string.cancel
                                , (dialog, which) -> refreshWindowSwitch())
                        .setOnCancelListener(dialog -> refreshWindowSwitch())
                        .create()
                        .show();
                SharedPrefHelper.putWindowShown(this, true);
                return;
            }
        }
        if (buttonView == mWindowSwitch) {
            SharedPrefHelper.putWindowShown(this, isChecked);
            if (!isChecked) {
                GazeView.dismiss(this);
            } else {
                AppSystem.out.printt(this,
                        this.getClass().getName());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    class UpdateSwitchReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshWindowSwitch();
            refreshNotificationSwitch();
        }
    }
}
