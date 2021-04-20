package cn.misection.gazer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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

import cn.misection.gazer.dao.SharedPrefHelper;
import cn.misection.gazer.service.GazeAccessibilityService;
import cn.misection.gazer.service.GazeService;
import cn.misection.gazer.util.NotificationActionReceiver;
import cn.misection.gazer.view.GazeView;

public class MainActivity extends Activity implements OnCheckedChangeListener {
    public static final String EXTRA_FROM_QS_TILE = "from_qs_tile";
    public static final String ACTION_STATE_CHANGED = "cn.misection.gazer.ACTION_STATE_CHANGED";
    CompoundButton mWindowSwitch, mNotificationSwitch;
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
            GazeView.show(this, "");
            startService(new Intent(this, GazeService.class));
        }
        if (getIntent().getBooleanExtra(EXTRA_FROM_QS_TILE, false)) {
            mWindowSwitch.setChecked(true);
        }
        mReceiver = new UpdateSwitchReceiver();
        registerReceiver(mReceiver, new IntentFilter(ACTION_STATE_CHANGED));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (getIntent().getBooleanExtra(EXTRA_FROM_QS_TILE, false)) {
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
        if (SharedPrefHelper.isShowWindow(this) && !(getResources().getBoolean(R.bool.use_accessibility_service) && GazeAccessibilityService.getInstance() == null)) {
            NotificationActionReceiver.showNotification(this, false);
        }
    }

    private void refreshWindowSwitch() {
        mWindowSwitch.setChecked(SharedPrefHelper.isShowWindow(this));
        if (getResources().getBoolean(R.bool.use_accessibility_service)) {
            if (GazeAccessibilityService.getInstance() == null) {
                mWindowSwitch.setChecked(false);
            }
        }
    }

    private void refreshNotificationSwitch() {
        if (mNotificationSwitch != null) {
            mNotificationSwitch.setChecked(!SharedPrefHelper.isNotificationToggleEnabled(this));
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == mNotificationSwitch) {
            if (SharedPrefHelper.hasSettingTileAdded(this)) {
                SharedPrefHelper.putNotificationToggleEnabled(this, !isChecked);
            } else if (isChecked) {
                Toast.makeText(this, R.string.toast_add_tile, Toast.LENGTH_LONG).show();
                buttonView.setChecked(false);
            } else {
                SharedPrefHelper.putNotificationToggleEnabled(this, !isChecked);
            }
            return;
        }
        if (isChecked && buttonView == mWindowSwitch && getResources().getBoolean(R.bool.use_accessibility_service)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1 && !Settings.canDrawOverlays(this)) {
                new AlertDialog.Builder(this)
                        .setMessage(R.string.dialog_enable_overlay_window_msg)
                        .setPositiveButton(R.string.dialog_enable_overlay_window_positive_btn
                                , new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                                        intent.setData(Uri.parse("package:" + getPackageName()));
                                        startActivity(intent);
                                        dialog.dismiss();
                                    }
                                })
                        .setNegativeButton(android.R.string.cancel
                                , new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SharedPrefHelper.setIsShowWindow(MainActivity.this, false);
                                        refreshWindowSwitch();
                                    }
                                })
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                SharedPrefHelper.setIsShowWindow(MainActivity.this, false);
                                refreshWindowSwitch();
                            }
                        })
                        .create()
                        .show();
                buttonView.setChecked(false);
                return;
            } else if (GazeAccessibilityService.getInstance() == null) {
                new AlertDialog.Builder(this)
                        .setMessage(R.string.dialog_enable_accessibility_msg)
                        .setPositiveButton(R.string.dialog_enable_accessibility_positive_btn
                                , new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SharedPrefHelper.setIsShowWindow(MainActivity.this, true);
                                        Intent intent = new Intent();
                                        intent.setAction("android.settings.ACCESSIBILITY_SETTINGS");
                                        startActivity(intent);
                                    }
                                })
                        .setNegativeButton(android.R.string.cancel
                                , new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        refreshWindowSwitch();
                                    }
                                })
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                refreshWindowSwitch();
                            }
                        })
                        .create()
                        .show();
                SharedPrefHelper.setIsShowWindow(this, true);
                return;
            }
        }
        if (buttonView == mWindowSwitch) {
            SharedPrefHelper.setIsShowWindow(this, isChecked);
            if (!isChecked) {
                GazeView.dismiss(this);
            } else {
                GazeView.show(this, getPackageName() + "\n" + getClass().getName());
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
