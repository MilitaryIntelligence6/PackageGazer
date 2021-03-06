package cn.misection.gazer.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;

import cn.misection.gazer.constant.common.EnumString;
import cn.misection.gazer.receiver.NotificationActionReceiver;
import cn.misection.gazer.dao.SharedPrefHelper;
import cn.misection.gazer.system.AppSystem;
import cn.misection.gazer.view.GazeView;

/**
 * @author Administrator
 * accesibility 服务;
 */
public class GazeAccessibilityService extends AccessibilityService {

    private static GazeAccessibilityService sInstance;

    public static GazeAccessibilityService requireInstance() {
        return sInstance;
    }

    @SuppressLint("NewApi")
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (SharedPrefHelper.hasWindowShown(this)) {
                AppSystem.out.printt(
                        this,
                        String.valueOf(event.getClassName()));
            }
        }
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    protected void onServiceConnected() {
        sInstance = this;
        if (SharedPrefHelper.hasWindowShown(this)) {
            NotificationActionReceiver.showNotification(this, false);
        }
        sendBroadcast(new Intent(
                EnumString.ACTION_UPDATE_TITLE.value()
        ));
        super.onServiceConnected();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        sInstance = null;
        GazeView.dismiss(this);
        NotificationActionReceiver.cancelNotification(this);
        sendBroadcast(new Intent(
                EnumString.ACTION_UPDATE_TITLE.value()
        ));
        return super.onUnbind(intent);
    }
}
