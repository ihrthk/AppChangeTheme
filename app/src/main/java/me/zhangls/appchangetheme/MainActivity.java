package me.zhangls.appchangetheme;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";
    private View mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mainView = findViewById(R.id.view);
        mainView.setBackgroundResource(R.color.main_view_color);

    }

    public void theme1(View view) {
        try {
            File directory = Environment.getExternalStorageDirectory();
            File file = new File(directory, "theme1.apk");
            Log.d(TAG, "file exist:" + file.exists());
            Resources apkResources = getAPKResources(this, file.getPath());
            Drawable color = apkResources.getDrawable(R.color.main_view_color);
            mainView.setBackground(color);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void theme2(View view) {
        try {
            File directory = Environment.getExternalStorageDirectory();
            File file = new File(directory, "theme2.apk");
            Log.d(TAG, "file exist:" + file.exists());
            Resources apkResources = getAPKResources(this, file.getPath());
            Drawable color = apkResources.getDrawable(R.color.main_view_color);
            mainView.setBackground(color);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Resources getAPKResources(Context context, String apkPath) throws Exception {
        //1.create AssetManager object(hide)
        String PATH_AssetManager = "android.content.res.AssetManager";
        Class assetMagCls = Class.forName(PATH_AssetManager);
        Constructor assetMagCt = assetMagCls.getConstructor((Class[]) null);
        Object assetMag = assetMagCt.newInstance((Object[]) null);
        //2.invoke AssetManager.addAssetPath(String path)(hide)
        Method assetMag_addAssetPathMtd = assetMagCls.getDeclaredMethod("addAssetPath", String.class);
        assetMag_addAssetPathMtd.invoke(assetMag, apkPath);
        //3.create Constructor of Resources
        Resources res = context.getResources();
        Class[] typeArgs = new Class[3];
        typeArgs[0] = assetMag.getClass();
        typeArgs[1] = res.getDisplayMetrics().getClass();
        typeArgs[2] = res.getConfiguration().getClass();
        Constructor resCt = Resources.class.getConstructor(typeArgs);
        //4.invoke newInstance of Resources
        Object[] valueArgs = new Object[3];
        valueArgs[0] = assetMag;
        valueArgs[1] = res.getDisplayMetrics();
        valueArgs[2] = res.getConfiguration();
        res = (Resources) resCt.newInstance(valueArgs);
        return res;
    }
}
