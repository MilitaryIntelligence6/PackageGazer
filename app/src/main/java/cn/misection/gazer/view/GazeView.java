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

import java.util.Objects;

import cn.misection.gazer.service.QuickSettingTileService;
import cn.misection.gazer.R;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName GazeView
 * @Description TODO
 * @CreateTime 2021年04月19日 22:58:00
 */
public class GazeView {

    private volatile static GazeView instance = null;

    private WindowManager.LayoutParams mWindowParams;

    private WindowManager mWindowManager;

    private View mView;

    private Context mContext;

    private GazeView(Context context) {
        this.mContext = context;
        init();
    }

    @SuppressLint("NewApi")
    public static GazeView requireInstance(Context context) {
        if (instance == null) {
            synchronized (GazeView.class) {
                if (instance == null) {
                    instance = new GazeView(context);
                }
            }
        }
        // 空安全equals;
        if (!Objects.equals(instance.mContext, context)) {
            instance.mContext = context;
            instance.init();
        }
        return instance;
    }

    public void init() {
        mWindowManager = (WindowManager) mContext
                .getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);

        mWindowParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT <= Build.VERSION_CODES.N ?
                        WindowManager.LayoutParams.TYPE_TOAST : WindowManager.LayoutParams.TYPE_PHONE, 0x18,
                PixelFormat.TRANSLUCENT);
        mWindowParams.gravity = Gravity.LEFT + Gravity.TOP;
        mView = LayoutInflater
                .from(mContext)
                .inflate(R.layout.window_tasks, null);
    }

    private void showText(final String text) {
        TextView textView = (TextView) mView.findViewById(R.id.text);
        textView.setText(text);
        try {
            mWindowManager.addView(mView, mWindowParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dismissal(Context context) {
        try {
            mWindowManager.removeView(mView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTile() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            QuickSettingTileService.updateTile(mContext);
        }
    }

    public static void show(Context context, final String text) {
        requireInstance(context).showText(text);
        instance.updateTile();
    }

    public static void dismiss(Context context) {
        requireInstance(context).dismissal(context);
        instance.updateTile();
    }
}
