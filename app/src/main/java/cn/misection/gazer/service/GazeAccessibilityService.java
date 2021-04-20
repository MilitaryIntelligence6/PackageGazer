package cn.misection.gazer.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;

import cn.misection.gazer.util.NotificationActionReceiver;
import cn.misection.gazer.dao.SharedPrefHelper;
import cn.misection.gazer.view.GazeView;

/**
 * Created by Wen on 1/14/15.
 */
public class GazeAccessibilityService extends AccessibilityService {
    private static GazeAccessibilityService sInstance;

    public static GazeAccessibilityService getInstance() {
        return sInstance;
    }

    @SuppressLint("NewApi")
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (SharedPrefHelper.isShowWindow(this)) {
                GazeView.show(this, event.getPackageName() + "\n" + event.getClassName());
            }
        }
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    protected void onServiceConnected() {
        sInstance = this;
        if (SharedPrefHelper.isShowWindow(this)) {
            NotificationActionReceiver.showNotification(this, false);
        }
        sendBroadcast(new Intent(SettingTileService.ACTION_UPDATE_TITLE));
        super.onServiceConnected();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        sInstance = null;
        GazeView.dismiss(this);
        NotificationActionReceiver.cancelNotification(this);
        sendBroadcast(new Intent(SettingTileService.ACTION_UPDATE_TITLE));
        return super.onUnbind(intent);
    }
}
