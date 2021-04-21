package cn.misection.gazer.service;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import cn.misection.gazer.MainActivity;
import cn.misection.gazer.constant.common.EnumString;
import cn.misection.gazer.receiver.NotificationActionReceiver;
import cn.misection.gazer.dao.SharedPrefHelper;
import cn.misection.gazer.system.AppSystem;
import cn.misection.gazer.view.GazeView;


/**
 * @author Administrator
 * 下拉框磁贴;
 */
@TargetApi(Build.VERSION_CODES.N)
public class QuickSettingTileService extends TileService {

    private UpdateTileReceiver mReceiver;

    public static void updateTile(Context context) {
        TileService.requestListeningState(
                context.getApplicationContext(),
                new ComponentName(
                        context,
                        QuickSettingTileService.class));
        context.sendBroadcast(new Intent(
                EnumString.ACTION_UPDATE_TITLE.value()
        ));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mReceiver = new UpdateTileReceiver();
    }

    @Override
    public void onTileAdded() {
        SharedPrefHelper.putSettingTileAdded(this, true);
        sendBroadcast(new Intent(
                EnumString.ACTION_STATE_CHANGED.value()
        ));
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
        SharedPrefHelper.putSettingTileAdded(this, false);
        sendBroadcast(new Intent(
                EnumString.ACTION_STATE_CHANGED.value()
        ));
    }

    @Override
    public void onStartListening() {
        registerReceiver(mReceiver, new IntentFilter(
                EnumString.ACTION_UPDATE_TITLE.value()
        ));
        super.onStartListening();
        updateTile();
    }

    @Override
    public void onStopListening() {
        unregisterReceiver(mReceiver);
        super.onStopListening();
    }

    @Override
    public void onClick() {
        if (GazeAccessibilityService.requireInstance() == null || !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(
                    EnumString.EXTRA_FROM_QS_TILE.value(),
                    true);
            startActivityAndCollapse(intent);
        } else {
            SharedPrefHelper.putWindowShown(this, !SharedPrefHelper.hasWindowShown(this));
            if (SharedPrefHelper.hasWindowShown(this)) {
                AppSystem.out.println(this, EnumString.EMPTY.value());
                NotificationActionReceiver.showNotification(this, false);
            } else {
                GazeView.dismiss(this);
                NotificationActionReceiver.showNotification(this, true);
            }
            sendBroadcast(new Intent(
                    EnumString.ACTION_STATE_CHANGED.value()
            ));
        }
    }

    private void updateTile() {
        if (GazeAccessibilityService.requireInstance() == null) {
            getQsTile().setState(Tile.STATE_INACTIVE);
        } else {
            getQsTile().setState(SharedPrefHelper.hasWindowShown(this) ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
        }
        getQsTile().updateTile();
    }

    class UpdateTileReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateTile();
        }
    }
}
