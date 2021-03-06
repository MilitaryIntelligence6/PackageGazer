package cn.misection.gazer.receiver;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import cn.misection.gazer.MainActivity;
import cn.misection.gazer.R;
import cn.misection.gazer.constant.common.EnumString;
import cn.misection.gazer.constant.receiver.EnumActionCode;
import cn.misection.gazer.constant.receiver.EnumActionString;
import cn.misection.gazer.constant.receiver.EnumNotification;
import cn.misection.gazer.dao.SharedPrefHelper;
import cn.misection.gazer.system.AppSystem;
import cn.misection.gazer.view.GazeView;

import java.util.List;


/**
 * @author Administrator
 * 状态栏通知接收者;
 * 状态栏磁贴;
 */
public class NotificationActionReceiver extends BroadcastReceiver {

    public static void showNotification(
            Context context,
            boolean paused) {
        if (!SharedPrefHelper.hasNotificationToggleEnabled(context)) {
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
                .setOngoing(!paused);
        if (paused) {
            builder.addAction(R.drawable.ic_noti_action_resume, context.getString(R.string.noti_action_resume),
                    getPendingIntent(context, EnumActionCode.RESUME.value()));
        } else {
            builder.addAction(R.drawable.ic_noti_action_pause,
                    context.getString(R.string.noti_action_pause),
                    getPendingIntent(context, EnumActionCode.PAUSE.value()));
        }

        builder.addAction(R.drawable.ic_noti_action_stop,
                context.getString(R.string.noti_action_stop),
                getPendingIntent(context, EnumActionCode.STOP.value()))
                .setContentIntent(pIntent);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(
                EnumNotification.ID.value(),
                builder.build());
    }

    public static PendingIntent getPendingIntent(Context context, int command) {
        Intent intent = new Intent(
                EnumActionString.NOTIFICATION_RECEIVER.value());
        intent.putExtra(
                EnumActionString.EXTRA_NOTIFICATION.value(),
                command);
        return PendingIntent.getBroadcast(context, command, intent, 0);
    }

    public static void cancelNotification(Context context) {
        NotificationManager manager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(
                EnumNotification.ID.value()
        );
    }

    /**
     * 监听消息中心的按钮;
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        int command = intent.getIntExtra(
                EnumActionString.EXTRA_NOTIFICATION.value(),
                -1);
        switch (EnumActionCode.selectByOrdinal(command)) {
            case RESUME: {
                showNotification(context, false);
                SharedPrefHelper.putWindowShown(context, true);
                boolean lollipop = Build.VERSION.SDK_INT >= 21;
                if (!lollipop) {
                    ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                    List<ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(1);
                    ComponentName topActivity = runningTasks.get(0).topActivity;
                    AppSystem.out.printt(context,
                            this.getClass().getName());
                } else {
                    AppSystem.out.printt(context, EnumString.EMPTY.value());
                }
                break;
            }
            case PAUSE: {
                showNotification(context, true);
                GazeView.dismiss(context);
                SharedPrefHelper.putWindowShown(context, false);
                break;
            }
            case STOP: {
                GazeView.dismiss(context);
                SharedPrefHelper.putWindowShown(context, false);
                cancelNotification(context);
                break;
            }
            default: {
                break;
            }
        }
        context.sendBroadcast(new Intent(
                EnumString.ACTION_UPDATE_TITLE.value()
        ));
    }
}
