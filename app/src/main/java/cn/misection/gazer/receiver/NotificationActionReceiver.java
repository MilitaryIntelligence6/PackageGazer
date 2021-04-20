package cn.misection.gazer.receiver;

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
import cn.misection.gazer.constant.common.EnumStringPool;
import cn.misection.gazer.constant.receiver.EnumActionCode;
import cn.misection.gazer.constant.receiver.EnumActionString;
import cn.misection.gazer.constant.receiver.EnumNotification;
import cn.misection.gazer.dao.SharedPrefHelper;
import cn.misection.gazer.util.ToastUtil;
import cn.misection.gazer.view.GazeView;

import java.util.List;


/**
 * @author Administrator
 */
public class NotificationActionReceiver extends BroadcastReceiver {

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

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(
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
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(
                EnumNotification.ID.value()
        );
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int command = intent.getIntExtra(
                EnumActionString.EXTRA_NOTIFICATION.value(),
                -1);
        switch (EnumActionCode.selectByOrdinal(command)) {
            case RESUME: {
                showNotification(context, false);
                SharedPrefHelper.setIsShowWindow(context, true);
                boolean lollipop = Build.VERSION.SDK_INT >= 21;
                if (!lollipop) {
                    ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                    List<ActivityManager.RunningTaskInfo> rtis = am.getRunningTasks(1);
                    String act = rtis.get(0).topActivity.getPackageName();
                    GazeView.show(context, act);
                    ToastUtil.show(context, act);
                } else {
                    GazeView.show(context, EnumStringPool.EMPTY.value());
                    ToastUtil.show(context, EnumStringPool.EMPTY.value());
                }
                break;
            }
            case PAUSE: {
                showNotification(context, true);
                GazeView.dismiss(context);
                SharedPrefHelper.setIsShowWindow(context, false);
                break;
            }
            case STOP: {
                GazeView.dismiss(context);
                SharedPrefHelper.setIsShowWindow(context, false);
                cancelNotification(context);
                break;
            }
            default: {
                break;
            }
        }
        context.sendBroadcast(new Intent(
                EnumStringPool.ACTION_UPDATE_TITLE.value()
        ));
    }
}
