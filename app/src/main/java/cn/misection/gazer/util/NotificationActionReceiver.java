package cn.misection.gazer.util;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import cn.misection.gazer.MainActivity;
import cn.misection.gazer.R;
import cn.misection.gazer.dao.SharedPrefHelper;
import cn.misection.gazer.service.SettingTileService;
import cn.misection.gazer.view.GazeView;

import java.util.List;

/**
 * Created by Wen on 4/18/15.
 */
public class NotificationActionReceiver extends BroadcastReceiver {
    public static final int NOTIFICATION_ID = 1;
    public static final String ACTION_NOTIFICATION_RECEIVER = "cn.misection.gazer.ACTION_NOTIFICATION_RECEIVER";
    public static final int ACTION_PAUSE = 0;
    public static final int ACTION_RESUME = 1;
    public static final int ACTION_STOP = 2;
    public static final String EXTRA_NOTIFICATION_ACTION = "command";

    public static void showNotification(Context context, boolean isPaused) {
        if (!SharedPrefHelper.isNotificationToggleEnabled(context)) {
            return;
        }
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(context.getString(R.string.is_running,
                        context.getString(R.string.app_name)))
                .setSmallIcon(R.drawable.ic_notification)
                .setContentText(context.getString(R.string.touch_to_open))
                .setColor(0xFFe215e0)
                .setVisibility(NotificationCompat.VISIBILITY_SECRET)
                .setOngoing(!isPaused);
        if (isPaused) {
            builder.addAction(R.drawable.ic_noti_action_resume, context.getString(R.string.noti_action_resume),
                    getPendingIntent(context, ACTION_RESUME));
        } else {
            builder.addAction(R.drawable.ic_noti_action_pause,
                    context.getString(R.string.noti_action_pause),
                    getPendingIntent(context, ACTION_PAUSE));
        }

        builder.addAction(R.drawable.ic_noti_action_stop,
                context.getString(R.string.noti_action_stop),
                getPendingIntent(context, ACTION_STOP))
                .setContentIntent(pIntent);

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(NOTIFICATION_ID, builder.build());

    }

    public static PendingIntent getPendingIntent(Context context, int command) {
        Intent intent = new Intent(ACTION_NOTIFICATION_RECEIVER);
        intent.putExtra(EXTRA_NOTIFICATION_ACTION, command);
        return PendingIntent.getBroadcast(context, command, intent, 0);
    }

    public static void cancelNotification(Context context) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(NOTIFICATION_ID);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int command = intent.getIntExtra(EXTRA_NOTIFICATION_ACTION, -1);
        switch (command) {
            case ACTION_RESUME: {
                showNotification(context, false);
                SharedPrefHelper.setIsShowWindow(context, true);
                boolean lollipop = Build.VERSION.SDK_INT >= 21;
                if (!lollipop) {
                    ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                    List<ActivityManager.RunningTaskInfo> rtis = am.getRunningTasks(1);
                    String act = rtis.get(0).topActivity.getPackageName();
                    GazeView.show(context, act);
                } else {
                    GazeView.show(context, null);
                }
                break;
            }
            case ACTION_PAUSE: {
                showNotification(context, true);
                GazeView.dismiss(context);
                SharedPrefHelper.setIsShowWindow(context, false);
                break;
            }
            case ACTION_STOP: {
                GazeView.dismiss(context);
                SharedPrefHelper.setIsShowWindow(context, false);
                cancelNotification(context);
            }
                break;
            default: {
                break;
            }
        }
        context.sendBroadcast(new Intent(SettingTileService.ACTION_UPDATE_TITLE));
    }
}
