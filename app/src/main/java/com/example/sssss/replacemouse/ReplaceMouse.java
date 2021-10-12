package com.example.sssss.replacemouse;

import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ReplaceMouse extends AppCompatActivity {
    int x=0,y=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setTheme(android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);//固定橫向
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replace_mouse);
        ConstraintLayout C01=(ConstraintLayout)findViewById(R.id.C01);
     //   new time().start();
    }
    String temp="";
    float flexible=3f;
    boolean socket=false;
    String KeyCode="00";
    public boolean onTouchEvent(MotionEvent e) {
        if (socket==false){
            socket=true;
            new readData("192.168.1.106", 1234).start();
        }else {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //      Log.d("aaaa",zero((int)e.getX())+"  a  "+zero((int)e.getY()));
                    x=(int)e.getX();
                    y=(int)e.getY();
                    temp=zero((int)((e.getX()-x)*flexible))+","+zero((int)((e.getY()-y)*flexible))+","+KeyCode;
                    castMsg(temp);

                    break;
                case MotionEvent.ACTION_MOVE:

                    //      Log.d("aaaa",zero((int)e.getX())+"  a  "+zero((int)e.getY()));
                    temp=zero((int)((e.getX()-x)*flexible))+","+zero((int)((e.getY()-y)*flexible))+","+KeyCode;
                    castMsg(temp);
                    x=(int)e.getX();
                    y=(int)e.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    //      Log.d("aaaa",zero((int)e.getX())+"  a  "+zero((int)e.getY()));
                    temp=zero((int)((e.getX()-x)*flexible))+","+zero((int)((e.getY()-y)*flexible))+","+KeyCode;
                    castMsg(temp);
                    x=(int)e.getX();
                    y=(int)e.getY();
                    break;
            }
        }
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
            KeyCode="11";
            temp="+0000"+","+"+0000"+","+KeyCode;
            castMsg(temp);
            return true;
        }else if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
            KeyCode="31";
            temp="+0000"+","+"+0000"+","+KeyCode;
            castMsg(temp);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }//返回建 音量按鍵(按下)

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
            KeyCode="10";
            temp="+0000"+","+"+0000"+","+KeyCode;
            castMsg(temp);
            return true;
        }else if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
            KeyCode="30";
            temp="+0000"+","+"+0000"+","+KeyCode;
            castMsg(temp);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }//返回建 音量按鍵(按下)

    private String zero(int a){
        if (a>=0){
            if (a<10){
                return "+000"+a;
            }else if (a<100){
                return "+00"+a;
            }else if (a<1000){
                return "+0"+a;
            }else {
                return "+"+a;
            }
        }else {
            a=a*-1;
            if (a<10){
                return "-000"+a;
            }else if (a<100){
                return "-00"+a;
            }else if (a<1000){
                return "-0"+a;
            }else {
                return "-"+a;
            }
        }


    }

    public class time extends Thread {
        String t="";
        public time() {
        }

        @Override
        public void run() {
            while (true){
                if (t.equals(temp)!=true){
                    t=temp;
                    castMsg(temp);
                    runOnUiThread(new Runnable() {             //將內容交給UI執行緒做顯示
                        public void run() {

                        }
                    });

              //      Log.d("aaaaaa",temp);
                }

                try {
                    Thread.sleep(10);
                }catch (Exception e){

                }
            }
        }
    }

    Socket clientSocket=null;
    int n = 0;

    public class readData extends Thread {
        String IP = "";
        int Port = 0;
        String line = "";

        public readData(String IP, int Port) {
            this.IP = IP;
            this.Port = Port;
        }

        @Override
        public void run() {
            InetAddress serverIp;// server端的IP
            try {
                serverIp = InetAddress.getByName(IP);// 以內定(本機電腦端)IP為Server端
                int serverPort = Port;
                clientSocket = new Socket(serverIp, serverPort);
                Log.v("連線狀況", "已連線");
                byte[] bis = new byte[1024];
                //接收資料
                // 取得網路輸入串流
                InputStream in = clientSocket.getInputStream();
                runOnUiThread(new Runnable() {             //將內容交給UI執行緒做顯示
                    public void run() {
                        Toast.makeText(ReplaceMouse.this, "連線成功", Toast.LENGTH_SHORT).show();
                    }
                });

                // 取得網路輸入串流
                int n = 0;
                while (n != -1) {
                    n = in.read(bis);
                    line = new String(bis);
                    bis = new byte[1024];

                    if (line.equals("") != true) {
                        runOnUiThread(new Runnable() {             //將內容交給UI執行緒做顯示
                            public void run() {
                                Toast.makeText(ReplaceMouse.this, "接收 : " + line, Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                    try {
                        Thread.sleep(200);
                    } catch (Exception e) {

                    }


                }
                runOnUiThread(new Runnable() {             //將內容交給UI執行緒做顯示
                    public void run() {
                        Toast.makeText(ReplaceMouse.this, "連線失敗", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.v("連線狀況", "連線失敗");
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {             //將內容交給UI執行緒做顯示
                    public void run() {
                        Toast.makeText(ReplaceMouse.this, "連線失敗", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.v("連線狀況", "連線失敗");
            }


        }
    }

    public void castMsg(String text) {
        if (clientSocket!=null){
            byte[] temp;
            Log.d("aaaaaa",text);
            try {
                temp = (text).getBytes();
                Log.wtf("aaaaa", "aaaaa"+temp.length);
                OutputStream out = clientSocket.getOutputStream();// 寫入訊息
                out.write(temp, 0, temp.length);// 立即發送   //  "|n"
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
