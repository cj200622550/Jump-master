package com.cj.jump;

import android.app.Application;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * 自定义Application，来实现简单临时的数据缓存
 * Created by CooLoongWu on 2018-1-5 10:02.
 */

public class MyApplication extends Application {

    private static MyApplication myApplication;

    //各屏幕速度配置（单位：px/ms）
    private final double SPEED_720P = 0.479;
    private final double SPEED_1080P = 1.44;
    private final double SPEED_2K = 1.7114;

    private int screenWidth;
    private int screenHeight;
    private double speed;

    public static MyApplication getInstance() {
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        setScreenHeight(displayMetrics.heightPixels);
        setScreenWidth(displayMetrics.widthPixels);
        Log.e("屏幕分辨率", getScreenWidth() + " * " + getScreenHeight());
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        if (screenWidth == 720) {
            setSpeed(SPEED_720P);
        } else if (screenWidth == 1080) {
            setSpeed(SPEED_1080P);
        } else {
            setSpeed(SPEED_2K);
        }
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        Log.e("该分辨率下速度", "" + speed);
        this.speed = speed;
    }
}
