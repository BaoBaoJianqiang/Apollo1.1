package jianqiang.com.activityhook1;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;

import jianqiang.com.activityhook1.ams_hook.AMSHookHelper;
import jianqiang.com.activityhook1.classloder_hook.BaseDexClassLoaderHookHelper;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button t = new Button(this);
        t.setText("test button");

        setContentView(t);

        Log.d(TAG, "context classloader: " + getApplicationContext().getClassLoader());
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent t = new Intent();
                    t.setComponent(
                            new ComponentName("com.example.jianqiang.testactivity",
                                    "com.example.jianqiang.testactivity.MainActivity"));

                    startActivity(t);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        try {
            Utils.extractAssets(newBase, "testactivity.apk");

            File dexFile = getFileStreamPath("testactivity.apk");
            File optDexFile = getFileStreamPath("testactivity.dex");
            BaseDexClassLoaderHookHelper.patchClassLoader(getClassLoader(), dexFile, optDexFile);
            
            AMSHookHelper.hookAMN();
            AMSHookHelper.hookActivityThread();

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
