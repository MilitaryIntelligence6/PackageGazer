package cn.misection.gazer.util;

import android.content.Context;
import android.widget.Toast;

import cn.misection.gazer.service.GazeService;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName ToastUtil
 * @Description TODO
 * @CreateTime 2021年04月20日 14:18:00
 */
public final class ToastUtil {

    private ToastUtil() {

    }

    public static void show(Context context, final String text) {
        Toast.makeText(
                context,
                text,
                Toast.LENGTH_SHORT
        ).show();
    }
}
