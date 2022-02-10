package com.example.sssss.replacemouse;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    public static Context context;
    public static MainActivity mainActivity;
    public static ReadData readData;
    public static SetToolView setToolView;
    public static RightButton rightButton ;
    public static LeftButton leftButton ;



    TextView textView, textView2;

    String IP = "172.18.148.27";
    int Port = 0;
    Button button;
    Button button2;
    //ReadData readData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setTheme(android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);//固定橫向
        //這邊設定 固定橫向  會造成 無法傳值 要到AndroidManifest.xml 設定橫向
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        mainActivity=this;

        readData = new ReadData();
        setToolView = new SetToolView();
        rightButton = new RightButton();
        leftButton = new LeftButton();



        //搖桿紀錄
        textView = (TextView) findViewById(R.id.textView);
        //接收訊號
        textView2 = (TextView) findViewById(R.id.textView2);
        //連線
        button = (Button) findViewById(R.id.button);
        //參數設定
        button2 = (Button) findViewById(R.id.button2);

        button.setOnClickListener(setToolView.ReplaceVolumeButton(true));
        button2.setOnClickListener(setToolView.ReplaceVolumeButton(false));

        new upTime().start();
    }

    //-------------------------------------------------------------------

    public class upTime extends Thread {
        String t = "";

        public upTime() {
        }

        @Override
        public void run() {
            while (true) {
                try {
                    if (setToolView.SendDataMode) {
                        runOnUiThread(new Runnable() {             //將內容交給UI執行緒做顯示
                            public void run() {

                                readData.sendData(leftButton.code);
                            }
                        });

                    }
                    Thread.sleep(setToolView.UpTime * 10);
                } catch (Exception e) {

                }
            }
        }
    }

    private boolean haveInternet() {
        boolean result = false;
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            result = false;
        } else {
            result = info.isAvailable();
        }
        return result;
    }//是否連線




    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
            setToolView.VolumeButtonRe();
            return true;
        }

        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
            setToolView.VolumeButtonAd();
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