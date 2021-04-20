package cn.misection.gazer.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;

import cn.misection.gazer.MainActivity;
import cn.misection.gazer.util.NotificationActionReceiver;
import cn.misection.gazer.dao.SharedPrefHelper;
import cn.misection.gazer.view.GazeView;
import cn.misection.gazer.service.GazeAccessibilityService;

/**
 * Created by Wen on 16/02/2017.
 */
@TargetApi(Build.VERSION_CODES.N)
public class AppShortcutsActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (GazeAccessibilityService.getInstance() == null || !Settings.canDrawOverlays(this)) {
            SharedPrefHelper.setIsShowWindow(this, true);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        boolean shown = !SharedPrefHelper.isShowWindow(this);
        SharedPrefHelper.setIsShowWindow(this, shown);
        if (!shown) {
            GazeView.dismiss(this);
            NotificationActionReceiver.showNotification(this, true);
        } else {
            GazeView.show(this, getPackageName());
            NotificationActionReceiver.showNotification(this, false);
        }
        sendBroadcast(new Intent(MainActivity.ACTION_STATE_CHANGED));
        finish();
    }
}
