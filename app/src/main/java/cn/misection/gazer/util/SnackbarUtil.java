package cn.misection.gazer.util;

import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName SnackbarUtil
 * @Description TODO
 * @CreateTime 2021年04月24日 21:53:00
 */
public class SnackbarUtil {

    private SnackbarUtil() {

    }

    public static void show(View view, String msg) {
        Snackbar.make(
                view,
                msg,
                BaseTransientBottomBar.LENGTH_SHORT
        ).show();
    }
}
