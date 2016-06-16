package zsy.androidtranstest;

import android.app.Application;

/**
 * Created by zsy on 16/6/16.
 */
public class MyApplication extends Application{

    public String serverIP;

    @Override
    public void onCreate() {
        super.onCreate();
        serverIP = "192.168.1.100";
    }


}
