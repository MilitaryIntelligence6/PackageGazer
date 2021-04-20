package cn.misection.gazer.service;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.misection.gazer.dao.SharedPrefHelper;
import cn.misection.gazer.util.ToastUtil;
import cn.misection.gazer.view.GazeView;

/**
 * @author Administrator
 */
public class GazeService extends Service {

    private Handler mHandler = new Handler();

    private ActivityManager mActivityManager;

    private String text = null;

    private Timer timer;

    private NotificationManager mNotiManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mActivityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        mNotiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    public GazeService getInstance() {
        return this;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // scheduledExecutorService最好;
        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new RefreshTask(), 0, 500);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("FLAGX : ", ServiceInfo.FLAG_STOP_WITH_TASK + "");
        Intent restartServiceIntent = new Intent(getApplicationContext(),
                this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(
                getApplicationContext(), 1, restartServiceIntent,
                PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext()
                .getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 500,
                restartServicePendingIntent);
        super.onTaskRemoved(rootIntent);
    }

    class RefreshTask extends TimerTask {
        @Override
        public void run() {
            List<RunningTaskInfo> rtis = mActivityManager.getRunningTasks(1);
            String act = rtis.get(0).topActivity.getPackageName();
            if (!act.equals(text)) {
                text = act;
                if (SharedPrefHelper.isShowWindow(GazeService.this)) {
                    // TODO 线程池;
                    mHandler.post(() -> {
                                GazeView.show(GazeService.this, text);
                                ToastUtil.show(GazeService.this, text);
                            }
                    );
                }
            }
        }
    }
}
