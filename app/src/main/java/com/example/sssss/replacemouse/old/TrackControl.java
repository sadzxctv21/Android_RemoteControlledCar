package com.example.sssss.replacemouse.old;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sssss.replacemouse.R;

public class TrackControl extends AppCompatActivity {
    int WidthO = 4958;
    int HeightO = 1796;
    float proportion = 0;//比例

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setTheme(android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);//固定橫向
        //這邊設定 固定橫向  會造成 無法傳值 要到AndroidManifest.xml 設定橫向
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_control);
        TrackControlView TCView = (TrackControlView) findViewById(R.id.TCView);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int Width = dm.widthPixels;   //螢幕的寬
        int Height = dm.heightPixels;   //螢幕的高
        //  proportion=(float) Height/(float)HeightO;
        //  Log.d("zzzz",proportion+"   aa"+Height);

        TCView.setLayoutParams(new LinearLayout.LayoutParams((int) (Width), Height));
        TCView.setOnTouchListener(ButImage(TCView));
    }

    float tempX = 0, tempY = 0;
    float mobileX = 0, mobileY = 0;
    float tempX2 = 0, tempY2 = 0;
    float mobileX2 = 0, mobileY2 = 0;
    View.OnTouchListener ButImage(final TrackControlView TCView) {
        return new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getPointerCount()!=2){
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (lock==false){
                                mobileX = tempX - event.getX();
                                mobileY = tempY - event.getY();
                                Log.d("aaaaa", event.getX() + "   " + event.getY());
                                TCView.enter(mobileX * 10, mobileY * 10);
                            }else {
                                TCView.enter2(event.getX(),event.getY());
                            }

                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                    }

                }else {
               //     mobileX2 = tempX2 - event.getX(0);
             //       TCView.enter3(mobileX2/1000);
               //     Log.d("zzzzzzzzzz",event.getX(0)+"  aaaa"+event.getX(1));
                }

                if (lock==false){
                    tempX = event.getX();
                    tempY = event.getY();
                    tempX2 = event.getX();
                }

                return true;
            }
        };

    }

    boolean lock = false;//鎖定視窗

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
            lock = !lock;
            if (lock) {
                Toast.makeText(TrackControl.this, "鎖定視窗", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(TrackControl.this, "解除視窗", Toast.LENGTH_SHORT).show();
            }

            return true;
        }

        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }//返回建 音量按鍵(按下)

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {

            return true;
        }
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }//返回建 音量按鍵(按下)
}