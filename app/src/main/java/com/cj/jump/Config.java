package com.cj.jump;

import android.util.Log;

import java.util.Random;

/**
 * 一些简单的配置
 * Created by CooLoongWu on 2018-1-4 14:10.
 */

public class Config {

    /**
     * @return 获取[100, 200]之间随机的触摸区域，小于100的话有一个分享按钮可能会被点击
     */
    public static int getTouchX() {
        return new Random().nextInt(200) + 300;
    }

    /**
     * @return 获取屏幕高度的85%以下的随机触摸区域
     */
    public static int getTouchY() {
        return (MyApplication.getInstance().getScreenHeight() / 100 * (new Random().nextInt(16) + 85));
    }


    public String touchCMD(int time) {
        String CMD_TOUCH_LONG = "input swipe " + getTouchX() + " " + getTouchY() + " " + getTouchX() + " " + getTouchY() + " time";
        String cmd = CMD_TOUCH_LONG.replace("time", String.valueOf(time));
        Log.e("CMD", cmd);
        return cmd;
    }
}
