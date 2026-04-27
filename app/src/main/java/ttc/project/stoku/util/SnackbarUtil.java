package ttc.project.stoku.util;

import android.content.Context;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import ttc.project.stoku.R;

public class SnackbarUtil {
    public static void showSnackbar(Context context, View rootView, String message, Snackbar snackbar, int length){
        if(snackbar != null){
            snackbar.dismiss();
        }
        snackbar = Snackbar.make(rootView, message, length);
        snackbar.show();
    }
}
