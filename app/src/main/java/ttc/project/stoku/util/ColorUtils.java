package ttc.project.stoku.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import ttc.project.stoku.R;

public class ColorUtils {

    public int getSelectedColor(int position, Context context){
        switch (position){
            case 0:
              return ContextCompat.getColor(context, R.color.red);
            case 1:
                return ContextCompat.getColor(context, R.color.pink);
            case 2:
                return ContextCompat.getColor(context, R.color.purple);
            case 3:
                return ContextCompat.getColor(context, R.color.deep_purple);
            case 4:
                return ContextCompat.getColor(context, R.color.indigo);
            case 5:
                return ContextCompat.getColor(context, R.color.blue);
            case 6:
                return ContextCompat.getColor(context, R.color.light_blue);
            case 7:
                return ContextCompat.getColor(context, R.color.cyan);
            case 8:
                return ContextCompat.getColor(context, R.color.teal);
            case 9:
                return ContextCompat.getColor(context, R.color.green);
            case 10:
                return ContextCompat.getColor(context, R.color.light_green);
            case 11:
                return ContextCompat.getColor(context, R.color.lime);
            case 12:
                return ContextCompat.getColor(context, R.color.yellow);
            case 13:
                return ContextCompat.getColor(context, R.color.amber);
            case 14:
                return ContextCompat.getColor(context, R.color.orange);
            case 15:
                return ContextCompat.getColor(context, R.color.deep_orange);
            case 16:
                return ContextCompat.getColor(context, R.color.brown);
            case 17:
                return ContextCompat.getColor(context, R.color.grey);
            case 18:
                return ContextCompat.getColor(context, R.color.blue_grey);
                default:
                    return ContextCompat.getColor(context, R.color.colorPrimary);
        }
    }

}
