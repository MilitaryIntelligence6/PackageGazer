package cn.misection.gazer.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import cn.misection.gazer.service.SettingTileService;
import cn.misection.gazer.R;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName GazeView
 * @Description TODO
 * @CreateTime 2021年04月19日 22:58:00
 */
public class GazeView {
    private static WindowManager.LayoutParams sWindowParams;
    private static WindowManager sWindowManager;
    @SuppressLint("StaticFieldLeak")
    private static View sView;

    public static void init(final Context context) {
        sWindowManager = (WindowManager) context
                .getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);

        sWindowParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT <= Build.VERSION_CODES.N ?
                        WindowManager.LayoutParams.TYPE_TOAST : WindowManager.LayoutParams.TYPE_PHONE, 0x18,
                PixelFormat.TRANSLUCENT);
        sWindowParams.gravity = Gravity.LEFT + Gravity.TOP;
        sView = LayoutInflater.from(context).inflate(R.layout.window_tasks,
                null);
    }

    public static void show(Context context, final String text) {
        if (sWindowManager == null) {
            init(context);
        }
        TextView textView = (TextView) sView.findViewById(R.id.text);
        textView.setText(text);
        try {
            sWindowManager.addView(sView, sWindowParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SettingTileService.updateTile(context);
        }
    }

    public static void dismiss(Context context) {
        try {
            sWindowManager.removeView(sView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SettingTileService.updateTile(context);
        }
    }
}
