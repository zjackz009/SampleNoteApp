package nguyen.lam.samplenoteapp.Utilities;

import android.content.Context;
import android.provider.Settings;


public class Utils {

    public static String getDeviceId(Context context){
        String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return android_id;
    }

    public static String[] splitString(String data, String splitText){
        return data.split(splitText);
    }
}
