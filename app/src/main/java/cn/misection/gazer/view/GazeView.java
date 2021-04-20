package cn.misection.gazer.view;

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
    private static WindowManager.LayoutParams windowParams;
    private static WindowManager windowManager;
    private static View view;

    private volatile static GazeView instance = null;

    private GazeView() {

    }

    public static GazeView getInstance() {
        if (instance == null) {
            synchronized (GazeView.class) {
                if (instance == null) {
                    instance = new GazeView();
                }
            }
        }
        return instance;
    }

    public static void init(final Context context) {
        windowManager = (WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);

        windowParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT <= Build.VERSION_CODES.N ?
                        WindowManager.LayoutParams.TYPE_TOAST : WindowManager.LayoutParams.TYPE_PHONE, 0x18,
                PixelFormat.TRANSLUCENT);
        windowParams.gravity = Gravity.LEFT + Gravity.TOP;
        view = LayoutInflater.from(context).inflate(R.layout.window_tasks,
                null);
    }

    public static void show(Context context, final String text) {
        if (windowManager == null) {
            init(context);
        }
        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText(text);
        windowManager.addView(view, windowParams);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SettingTileService.updateTile(context);
        }
    }

    public static void dismiss(Context context) {
        windowManager.removeView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SettingTileService.updateTile(context);
        }
    }
}
