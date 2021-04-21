package cn.misection.gazer.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;

import cn.misection.gazer.MainActivity;
import cn.misection.gazer.constant.common.EnumStringPool;
import cn.misection.gazer.receiver.NotificationActionReceiver;
import cn.misection.gazer.dao.SharedPrefHelper;
import cn.misection.gazer.service.GazeAccessibilityService;
import cn.misection.gazer.system.AppSystem;
import cn.misection.gazer.view.GazeView;

/**
 * @author Administrator
 */
@TargetApi(Build.VERSION_CODES.N)
public class ShortcutsActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (GazeAccessibilityService.requireInstance() == null
                || !Settings.canDrawOverlays(this)) {
            SharedPrefHelper.putWindowShown(this, true);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        boolean shown = !SharedPrefHelper.hasWindowShown(this);
        SharedPrefHelper.putWindowShown(this, shown);
        if (!shown) {
            GazeView.dismiss(this);
            NotificationActionReceiver.showNotification(this, true);
        } else {
            AppSystem.out.println(this,
                    this.getClass().getName());
            NotificationActionReceiver.showNotification(this, false);
        }
        sendBroadcast(new Intent(
                EnumStringPool.ACTION_STATE_CHANGED.value()));
        finish();
    }
}
