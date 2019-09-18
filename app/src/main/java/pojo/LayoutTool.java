package pojo;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Joe on 2018/1/6.
 */

public class LayoutTool {

    // Dp 轉 Px
    public static float convertDpToPx(float dp, Context context) {
        float px = dp * getDensity(context);
        return px;
    }

    // Px 轉 Dp
    public static float convertPxToDp(float px, Context context) {
        float dp = px / getDensity(context);
        return dp;
    }

    public static float getDensity(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.density;
    }

}
