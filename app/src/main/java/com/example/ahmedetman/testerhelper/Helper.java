package com.example.ahmedetman.testerhelper;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.DisplayMetrics;

/**
 * Created by Ahmed Etman on 11/21/2017.
 */

public class Helper {

    /**
     * getting the display matrix
     *
     * @param context
     * @return
     */
    private static DisplayMetrics getDisplayMetrics(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return metrics;
    }

    /**
     * converting pixels to density pixels
     *
     * @param px
     * @param context
     * @return
     */
    private static float convertPixelsToDp(float px, Context context) {
        DisplayMetrics metrics = getDisplayMetrics(context);
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    /**
     * getting the display density
     *
     * @param activity
     * @return
     */
    private static int getScreenDensity(Activity activity) {
        return getDisplayMetrics(activity).densityDpi;
    }

    /**
     * getting the diagonal screen inches
     *
     * @param activity
     * @return
     */
    private static String getScreenInches(Activity activity) {
        DisplayMetrics dm = getDisplayMetrics(activity);
        int dens = dm.densityDpi;
        double wi = (double) dm.widthPixels / (double) dens;
        double hi = (double) dm.heightPixels / (double) dens;
        double x = Math.pow(wi, 2);
        double y = Math.pow(hi, 2);
        double screenInches = Math.sqrt(x + y);

        return String.format("%.2f", screenInches) + " inches";
    }

    /**
     * collecting the extra data that should be send with the attached image
     *
     * @return extra information about the device and display properties
     */
    static String prepareExtraData(Activity context) {
        String deviceModel = Build.MODEL;
        String deviceMan = Build.MANUFACTURER;
        String screenDensity = String.valueOf(Helper.getScreenDensity(context));
        String screenInches = Helper.getScreenInches(context);
        String extraData = "";
        extraData += "Device Model: " + deviceModel + "\n" +
                "Device Manufacturer: " + deviceMan + "\n" +
                "Screen Density: " + screenDensity + "dpi\n" +
                "Screen Inches: " + screenInches + "\"\n" +
                "screen with in px: " +
                String.valueOf(getDisplayMetrics(context).widthPixels) + "\n" +
                "screen height in px: " +
                String.valueOf(getDisplayMetrics(context).heightPixels) + "\n" +
                "screen with in dp: " + String.valueOf(convertPixelsToDp(
                getDisplayMetrics(context).widthPixels, context)) + "\n" +
                "screen height in dp: " + String.valueOf(convertPixelsToDp(
                getDisplayMetrics(context).heightPixels, context));
        return extraData;
    }

    /**
     * converting the URI of the selected image to absolute path
     *
     * @param context
     * @param contentUri
     * @return
     */
    static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null,
                    null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
