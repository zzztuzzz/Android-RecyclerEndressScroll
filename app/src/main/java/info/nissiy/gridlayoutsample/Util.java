package info.nissiy.gridlayoutsample;

import android.content.Context;
import android.content.res.Configuration;

public class Util {

    private Util() {
    }
    //横回転の2-3列化
    public static boolean isLandscape(Context context) {
        System.out.println("isLandscape:context"+ context.getResources().getConfiguration().orientation );
        System.out.println("isLandscape:configuration"+ Configuration.ORIENTATION_LANDSCAPE);
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

}
