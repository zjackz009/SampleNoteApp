package nguyen.lam.samplenoteapp;

import android.app.Application;

import nguyen.lam.samplenoteapp.Utilities.PreferenceUtil;


public class SampleNoteApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceUtil.getInstance().init(getApplicationContext());
    }
}
