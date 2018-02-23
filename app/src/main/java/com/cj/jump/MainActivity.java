package com.cj.jump;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.Screen;

import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private Button mShowFloat;
    public static final String TAG = "cj";
    private OutputStream os = null;
    private boolean isShow; //悬浮窗口是否显示
    private boolean isOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mShowFloat = (Button) findViewById(R.id.showFloat);
        mShowFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    mShowFloat.setText("打开辅助");
                    FloatWindow.destroy("logo");
                } else {
                    mShowFloat.setText("关闭辅助");
                    showFloat();
                }
                isOpen = !isOpen;
            }
        });
    }

    //执行shell命令
    private void exec(String cmd) {
        try {
            if (os == null) {
                os = Runtime.getRuntime().exec("su").getOutputStream();
            }
            os.write(cmd.getBytes());
            os.flush();
        } catch (IOException e) {
            Toast.makeText(this, "ROOT权限获取失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private TextView textView;

    private int jici = -1;
    private String zi = "";
    private void showFloat() {
        final View view = View.inflate(MainActivity.this, R.layout.float_view, null);
        textView = (TextView) view.findViewById(R.id.tv);
        ImageView imageView = new ImageView(MainActivity.this);
        imageView.setImageResource(R.mipmap.logo);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShow) {
                    Log.d(TAG, "onClick: 创建了");
                    FloatWindow
                            .with(getApplicationContext())
                            .setView(view)
                            .setWidth(Screen.width, 1f)
                            .setHeight(Screen.height, 0.5f)
                            .setX(100)
                            .setTag("window")
                            .setDesktopShow(true)
                            .setMoveType(MoveType.inactive)
                            .setY(Screen.height, 0.3f)
                            .build();
                    onResume();
                } else {
                    FloatWindow.get("window").hide();
                    FloatWindow.destroy("window");
                }
                isShow = !isShow;
            }
        });

        showLogoFloat(imageView);
        view.setOnTouchListener(new View.OnTouchListener() {

            private float mStartY;
            private float mStartX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: //按下

                        if (jici==-1) {
                            Log.d(TAG, "开始位置: " + event.getRawX() + " " + event.getRawY());
                            mStartX = event.getRawX();
                            mStartY = event.getRawY();
                            jici = 1;
                            zi = "棋子坐标：" + mStartX+","+mStartY;
                            textView.setText(zi);
                        } else if (jici == 1){
                            float endX = event.getRawX();
                            float endY = event.getRawY();
                            //三角形边长1
                            float length1 = Math.abs(endX - mStartX);
                            //三角形边长2
                            float length2 = Math.abs(endY - mStartY);
                            //通过勾股定理计算间距
                            int distance = (int) Math.sqrt(Math.pow(length1, 2) + Math.pow(length2, 2));
                            int temp = (int) (distance * MyApplication.getInstance().getSpeed()); //这里需要多尝试几次 找到最佳时间

                            zi = zi + "\n落地坐标：" + endX+","+endY + "\n距离：" + distance + "    时间：" + temp;
                            textView.setText(zi);
//                            textView.setText("棋子坐标：" + mStartX + "," + mStartY + "落地坐标：" + endX + "," + endY + "距离：" + distance + "    时间：" + temp);
                            String CMD_TOUCH_LONG = "input swipe " + Config.getTouchX() + " " + Config.getTouchY() + " " + Config.getTouchX() + " " + Config.getTouchY() + " " + temp + "\n";
//                        exec("input swipe 600 1800 600 1800 " + (temp) + "\n");
                            exec(CMD_TOUCH_LONG);
                            jici = -1;
                        }
                        break;
//                    case MotionEvent.ACTION_UP: //松开
//                        Log.d(TAG, "结束位置: " + event.getRawX() + " " + event.getRawY());
//                        float endX = event.getRawX();
//                        float endY = event.getRawY();
//                        //三角形边长1
//                        float length1 = Math.abs(endX - mStartX);
//                        //三角形边长2
//                        float length2 = Math.abs(endY - mStartY);
//                        //通过勾股定理计算间距
//                        int distance = (int) Math.sqrt(Math.pow(length1, 2) + Math.pow(length2, 2));
//                        Log.d(TAG, "距离: " + distance);
//                        int temp = (int) (distance * MyApplication.getInstance().getSpeed()); //这里需要多尝试几次 找到最佳时间
//                        textView.setText("棋子坐标：" + mStartX + "," + mStartY + "落地坐标：" + endX + "," + endY + "距离：" + distance + "    时间：" + temp);
//                        String CMD_TOUCH_LONG = "input swipe " + Config.getTouchX() + " " + Config.getTouchY() + " " + Config.getTouchX() + " " + Config.getTouchY() + " " + temp + "\n";
////                        exec("input swipe 600 1800 600 1800 " + (temp) + "\n");
//                        exec(CMD_TOUCH_LONG);
//                        break;
                }
                return true;
            }
        });

    }

    //显示logo悬浮图标
    private void showLogoFloat(View view) {
        FloatWindow
                .with(getApplicationContext())
                .setView(view)
                .setY(Screen.height, 0.1f)
                .setDesktopShow(true)
                .setTag("logo")
                .build();
        onResume();
    }
}
