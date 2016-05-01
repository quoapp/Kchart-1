package com.cuimengqi.kchart;

import android.content.Context;

/**
 * Created by treycc on 2016/5/2.
 */
public class DenstyUtils {

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);

    }
}
