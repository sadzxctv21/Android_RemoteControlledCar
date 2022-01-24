package com.example.sssss.replacemouse;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    //   TextView t01;
    Button b01, b02;
    float multiple = 0;//倍數
    int Width = 0, Height = 0;
    ImageViewAll imageView0;
    String IP = "172.18.148.27";
    int upDataN = 5, Port = 1234, upTimeN = 10;
    TextView textView;
    LinearLayout L01, L02, L03;

    String[] temp = {
            "N,,x,x",//1
            "B,△,X,+t5",//2
            "B,U,x,+t6",//3
            "B,☐,x,x",//4
            "L,1,0,false,false,0,0",//5
            "L,U,0,false,false,0,0",//6
            "N,,0,false,false,0,0",//7
            "B,▽,5,false,false,10,0",//8
            "B,W,6,false,true,0,0"//9
    };
    String Code = "A5 50 FF FF FF FA,A5 70 RE FF FF FA,A5 81 FF FF FF FA," +
            "A5 60 FF FF FF FA,A5 50 FF FF FF FA,A5 50 FF FF FF FA," +
            "A5 50 FF FF FF FA,A5 90 RE FF FF FA,A5 80 FF FF FF FA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setTheme(android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);//固定橫向
        //這邊設定 固定橫向  會造成 無法傳值 要到AndroidManifest.xml 設定橫向
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        L01 = (LinearLayout) findViewById(R.id.L01);
        L02 = (LinearLayout) findViewById(R.id.L02);
        L03 = (LinearLayout) findViewById(R.id.L03);
        textView = (TextView) findViewById(R.id.textView);

        setL01();
        setL02();
        setL03();
        imageView0 = (ImageViewAll) findViewById(R.id.image0);
        imageView0.setOnTouchListener(ButImage(imageView0));
        imageView0.setMod(0);


        String t = "", t2 = "", ip = "", port = "";
        t = Tool.read(this, "updataN");
        t2 = Tool.read(this, "upTimeN");
        if (t.length() != 0) {
            upDataN = Integer.parseInt(t);
        } else {
            Tool.save(getApplicationContext(), 5 + "", "updataN");
        }
        if (t2.length() != 0) {
            upTimeN = Integer.parseInt(t2);
        } else {
            Tool.save(getApplicationContext(), 5 + "", "upTimeN");
        }

        String CutInHalf = Tool.read(this, "cutInHalf");
        String TimecastMsg = Tool.read(this, "timecastMsg");

        if (CutInHalf.length() != 0) {
            cutInHalf = CutInHalf;
        } else {
            Tool.save(getApplicationContext(), "OFF", "cutInHalf");

        }
        if (TimecastMsg.length() != 0) {
            if (TimecastMsg.equals("F")) {
                timecastMsg = false;
            } else {
                timecastMsg = true;
            }

        } else {
            Tool.save(getApplicationContext(), "F", "timecastMsg");
        }


        IP = Tool.read(this, "IP");
        port = Tool.read(this, "Port");
        try {
            Port = Integer.parseInt(port);
        } catch (Exception e) {

        }
        new upTime().start();
    }

    // Type(T):Button
    TextView t01, t02, t03, t04, t05, t06, t07, t08, t09;
    Integer i01 = 1, i02 = 1, i03 = 1, i04 = 1, i05 = 1, i06 = 1, i07 = 1, i08 = 1, i09 = 1;

    private void setL01() {
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1f);
        t01 = setText(temp[0], Code.split(",")[0]);
        t02 = setText(temp[1], Code.split(",")[1]);
        t03 = setText(temp[2], Code.split(",")[2]);
        L01.addView(t01, param1);
        L01.addView(t02, param1);
        L01.addView(t03, param1);
    }

    private void setL02() {
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1f);
        t04 = setText(temp[3], Code.split(",")[3]);
        t05 = setText(temp[4], Code.split(",")[4]);
        t06 = setText(temp[5], Code.split(",")[5]);
        L02.addView(t04, param1);
        L02.addView(t05, param1);
        L02.addView(t06, param1);
    }

    private void setL03() {
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1f);
        t07 = setText(temp[6], Code.split(",")[6]);
        t08 = setText(temp[7], Code.split(",")[7]);
        t09 = setText(temp[8], Code.split(",")[8]);
        L03.addView(t07, param1);
        L03.addView(t08, param1);
        L03.addView(t09, param1);
    }

    //按鈕/文字，
    private TextView setText(String temp, String Code) {

        Boolean Type = Boolean.parseBoolean(temp.split(",")[0]);
        String text1 = temp.split(",")[1];
        Integer mod = Integer.parseInt(temp.split(",")[2]);
        Boolean ad_re = Boolean.parseBoolean(temp.split(",")[3]);
        Boolean type = Boolean.parseBoolean(temp.split(",")[4]);
        Integer max = Integer.parseInt(temp.split(",")[5]);
        Integer min = Integer.parseInt(temp.split(",")[6]);
        TextView textView = new TextView(this);
        textView.setText(text1);
        textView.setTextSize(64);
        textView.setGravity(Gravity.CENTER);

        textView.setTextColor(Color.parseColor("#FFFFFF"));
        if (Type) {//按鈕
            textView.setBackground(getResources().getDrawable(R.drawable.ic_imageu1));
        } else {//標籤
            textView.setBackground(getResources().getDrawable(R.drawable.ic_black));
        }
        textView.setOnTouchListener(textView(textView, mod, ad_re, type, max, min, Code,Type));
        return textView;
    }

    //-------------------------------------------------------------------



    View.OnTouchListener textView(final TextView textView, final int mod, final boolean ad_re,
                                  final boolean type, final int max, final int min, final String Code, final boolean Type) {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {  //按下的時候改變背景及顏色
                    if (root.equals("ON")) {
                        setRoot();
                    } else {
                        if (Type){
                            textView.setBackground(getResources().getDrawable(R.drawable.ic_imageu2));

                            switch (mod) {
                                case 1:
                                    if (type) {//切換
                                        t01.setText(textView.getText());
                                    } else {//計數
                                        i01 = Calculation(ad_re, max, min, i01);

                                        code = Code.replace("RE", Integer.toHexString(i01) + "");

                                        t01.setText(i01 + "");
                                    }

                                    break;
                                case 2:
                                    if (type) {//切換
                                        t02.setText(textView.getText());
                                    } else {//計數
                                        i02 = Calculation(ad_re, max, min, i02);
                                        code = Code.replace("RE", Integer.toHexString(i02) + "");
                                        t02.setText(i02 + "");
                                    }

                                    break;
                                case 3:
                                    if (type) {//切換
                                        t03.setText(textView.getText());
                                    } else {//計數
                                        i03 = Calculation(ad_re, max, min, i03);
                                        code = Code.replace("RE", Integer.toHexString(i03) + "");
                                        t03.setText(i03 + "");
                                    }

                                    break;
                                case 4:
                                    if (type) {//切換
                                        t04.setText(textView.getText());
                                    } else {//計數
                                        i04 = Calculation(ad_re, max, min, i04);
                                        code = Code.replace("RE", Integer.toHexString(i04) + "");
                                        t04.setText(i04 + "");
                                    }

                                    break;
                                case 5:
                                    if (type) {//切換
                                        t05.setText(textView.getText());
                                    } else {//計數
                                        i05 = Calculation(ad_re, max, min, i05);
                                        code = Code.replace("RE", Integer.toHexString(i05) + "");
                                        t05.setText(i05 + "");
                                    }

                                    break;
                                case 6:
                                    if (type) {//切換
                                        t06.setText(textView.getText());
                                    } else {//計數
                                        i06 = Calculation(ad_re, max, min, i06);
                                        code = Code.replace("RE", Integer.toHexString(i06) + "");
                                        t06.setText(i06 + "");
                                    }

                                    break;
                                case 7:
                                    if (type) {//切換
                                        t07.setText(textView.getText());
                                    } else {//計數
                                        i07 = Calculation(ad_re, max, min, i07);
                                        code = Code.replace("RE", Integer.toHexString(i07) + "");
                                        t07.setText(i07 + "");
                                    }

                                    break;
                                case 8:
                                    if (type) {//切換
                                        t08.setText(textView.getText());
                                    } else {//計數
                                        i08 = Calculation(ad_re, max, min, i08);
                                        code = Code.replace("RE", Integer.toHexString(i08) + "");
                                        t08.setText(i08 + "");
                                    }

                                    break;
                                case 9:
                                    if (type) {//切換
                                        t09.setText(textView.getText());
                                    } else {//計數
                                        i09 = Calculation(ad_re, max, min, i09);
                                        code = Code.replace("RE", Integer.toHexString(i09) + "");
                                        t09.setText(i09 + "");
                                    }

                                    break;
                                case 0:
                                    code = Code;
                                    break;
                            }

                            if (type) {
                                code = Code;
                            }
                            Log.d("zzzzzzzz", code + "  " + Code);

                            castMsg(code);
                        }

                    }
                    return true;

                    //       code = "A5 " + (mod*10) + " FF FF FF FA";
                    //       castMsg(code);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {  //按下的時候改變背景及顏色
                    if (Type){
                        textView.setBackground(getResources().getDrawable(R.drawable.ic_imageu1));
                    }

                        /*
                        if (frequency==10){
                            t="0A";
                        }else {
                            t=frequency+"";
                        }
                        code = "A5 " + t + " 00 00 00 FA";
                        castMsg(code);
                       */
                    return true;
                }


                return true;
            }
        };
    }

    private int Calculation(boolean ad_re, int max, int min, int i0x) {
        if (ad_re) {
            i0x++;
        } else {
            i0x--;
        }
        if (i0x > max) {
            i0x = max;
        } else if (i0x < min) {
            i0x = min;
        }
        return i0x;
    }


    private void setRoot() {
        View item = LayoutInflater.from(MainActivity.this).inflate(R.layout.set_root, null);
        TextView t01=(TextView)item.findViewById(R.id.t01);
        EditText e01 = (EditText) item.findViewById(R.id.e01);
        EditText e02 = (EditText) item.findViewById(R.id.e02);
        EditText e03 = (EditText) item.findViewById(R.id.e03);
        EditText e04 = (EditText) item.findViewById(R.id.e04);

        RadioButton r01 = (RadioButton) item.findViewById(R.id.r01);
        RadioButton r02 = (RadioButton) item.findViewById(R.id.r02);
        r01.setOnClickListener(AAAA(r02));
        r02.setOnClickListener(AAAA(r01));

        new AlertDialog.Builder(MainActivity.this)
                .setView(item)
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
    View.OnClickListener AAAA(final RadioButton r0x) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                r0x.setChecked(false);
            }
        };
    }
    View.OnClickListener t01(final TextView textView) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                String t=textView.getText().toString();
                if (t.equals("無")){
                    textView.setText("按鈕");
                }else if (t.equals("按鈕")){
                    textView.setText("標籤");
                }else if (t.equals("標籤")){
                    textView.setText("無");
                }
            }
        };
    }
    //------------------------------------------------------------------

    int count = 0;
    String code = "A5 50 00 00 00 FA";
    boolean timecastMsg = false;//每秒傳輸

    int ss = 0;//

    View.OnTouchListener ButImage(final ImageViewAll imageView) {
        return new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("aaaaa", event.getX() + "   " + event.getY());
                //       Log.d("aaaaaaaaaa", event.getAction() + " mod:" + mod);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        limit(v.getWidth() / 2, event.getX(), v.getHeight() / 2, event.getY(), v.getWidth() / 3, imageView);

                        imageView.changeColour(true);


                        break;
                    case MotionEvent.ACTION_MOVE:

                        count++;

                        limit(v.getWidth() / 2, event.getX(), v.getHeight() / 2, event.getY(), v.getWidth() / 3, imageView);

                        imageView.changeColour(true);
                        try {
                            Thread.sleep(20);
                        } catch (Exception e) {

                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        imageView.setX1Y1(v.getWidth() / 2, v.getHeight() / 2);

                        code = "A5 50 00 00 00 FA";
                        if (!timecastMsg) {
                            castMsg(code);
                        }

                        imageView.changeColour(false);

                        break;
                }


                return true;
            }
        };

    }


    private void limit(float ViewW, float eventX, float ViewH, float eventY, int mR, final ImageViewAll imageView) {
        float MaxX = 0, MaxY = 0;
        float line = (float) Math.sqrt((Math.pow(Math.abs(eventX - ViewW), 2) +
                Math.pow(Math.abs(eventY - ViewH), 2))
        );
        float a = (float) (Math.acos(Math.abs(eventX - ViewW) / line));//角度
        if (eventX > ViewW) {
            MaxX = ViewW + (float) Math.cos(a) * mR;
        } else {
            MaxX = ViewW - (float) Math.cos(a) * mR;
        }
        a = (float) (Math.asin(Math.abs(eventY - ViewH) / line));
        if (eventY > ViewH) {
            MaxY = ViewH + (float) Math.sin(a) * mR;
            //     Log.d("zzzzzzzzz","aaaaaaaaaaaa");
        } else {
            MaxY = ViewH - (float) Math.sin(a) * mR;
            //         Log.d("zzzzzzzzz","bbbbbbbbbbbbb"+a+"   "+ViewH);
        }

        Log.d("aaaaaaa", "eventX:" + eventX + "  " + "MaxX:" + MaxX + "  " + "eventY:" + eventY + "  " + "MaxY:" + MaxY + "  "
                + "ViewW:" + ViewW + "  " + "ViewH:" + ViewH);
        if (eventX - ViewW > 0 & eventY - ViewH > 0) {
            if (eventX > MaxX) {
                eventX = MaxX;
            }
            if (eventY > MaxY) {
                eventY = MaxY;
            }
        } else if (eventX - ViewW < 0 & eventY - ViewH > 0) {
            if (eventX < MaxX) {
                eventX = MaxX;
            }
            if (eventY > MaxY) {
                eventY = MaxY;
            }
        } else if (eventX - ViewW < 0 & eventY - ViewH < 0) {
            Log.d("zzzzzzzzzzz", "aaaaaaaaaa");
            if (eventX < MaxX) {
                eventX = MaxX;
            }
            if (eventY < MaxY) {
                eventY = MaxY;
            }
        } else if (eventX - ViewW > 0 & eventY - ViewH < 0) {
            if (eventX > MaxX) {
                eventX = MaxX;
            }
            if (eventY < MaxY) {
                eventY = MaxY;
            }
        }
        int bX = 0, bY = 0;
        boolean aaa = false;
        if (aaa) {
            bX = (int) ((float) ((eventX - ViewW) / mR) * 100);
            bY = -(int) ((float) ((eventY - ViewH) / mR) * 100);

            if (bX > 0) {
                if (bX > 100) {
                    bX = 100;
                }

                if (bY > 0) {
                    if (bY > 100) {
                        bY = 100;
                    }
                    if (count % upDataN == 0) {
                        count = 0;
                        code = "A5 20 " + Conversion(bX) + " 30 " + Conversion(bY) + " FA";
                        if (!timecastMsg) {
                            castMsg(code);
                        }

                    }
                    textView.setText("搖桿紀錄:" + "向右" + bX + "%  " + "向上" + bY + "% ");
                } else {
                    if (bY < -100) {
                        bY = -100;
                    }
                    if (count % upDataN == 0) {
                        count = 0;
                        code = "A5 20 " + Conversion(bX) + " 40 " + Conversion(-bY) + " FA";
                        if (!timecastMsg) {
                            castMsg(code);
                        }

                    }
                    textView.setText("搖桿紀錄:" + "向右" + bX + "%  " + "向下" + -bY + "% " + "次數:");

                }
            } else {
                if (bX < -100) {
                    bX = -100;
                }

                if (bY > 0) {
                    if (bY > 100) {
                        bY = 100;
                    }
                    if (count % upDataN == 0) {
                        count = 0;
                        code = "A5 10 " + Conversion(-bX) + " 30 " + Conversion(bY) + " FA";
                        if (!timecastMsg) {
                            castMsg(code);
                        }

                    }
                    textView.setText("搖桿紀錄:" + "向左" + -bX + "%  " + "向上" + bY + "% ");
                } else {
                    if (bY < -100) {
                        bY = -100;
                    }
                    if (count % upDataN == 0) {
                        count = 0;
                        code = "A5 10 " + Conversion(-bX) + " 40 " + Conversion(-bY) + " FA";
                        if (!timecastMsg) {
                            castMsg(code);
                        }

                    }
                    textView.setText("搖桿紀錄:" + "向左" + -bX + "%  " + "向下" + -bY + "% ");
                }
            }
        } else {
            bX = (int) (eventX - ViewW);
            bY = -(int) (eventY - ViewH);
            if (cutInHalf.equals("ON")) {
                bX = bX / 2;
                bY = bY / 2;
            }
            if (bX > 0) {
                if (bX > (int) MaxX) {
                    bX = (int) MaxX;
                }

                if (bY > 0) {
                    if (bY > (int) MaxY) {
                        bY = (int) MaxY;
                    }
                    if (count % upDataN == 0) {
                        count = 0;
                        code = "A5 20 " + Conversion(bX) + " 30 " + Conversion(bY) + " FA";
                        if (!timecastMsg) {
                            castMsg(code);
                        }

                    }
                    textView.setText("搖桿紀錄:" + "向右" + bX + "(" + (int) (MaxX - ViewW) + ")" + "向上" + bY + "(" + (int) -(MaxY - ViewH) + ")");
                } else {
                    if (bY < -(int) MaxY) {
                        bY = -(int) MaxY;
                    }
                    if (count % upDataN == 0) {
                        count = 0;
                        code = "A5 20 " + Conversion(bX) + " 40 " + Conversion(-bY) + " FA";
                        if (!timecastMsg) {
                            castMsg(code);
                        }

                    }
                    textView.setText("搖桿紀錄:" + "向右" + bX + "(" + (int) (MaxX - ViewW) + ")" + "向下" + -bY + "(" + (int) (MaxY - ViewH) + ")");

                }
            } else {
                if (bX < -(int) MaxX) {
                    bX = -(int) MaxX;
                }

                if (bY > 0) {
                    if (bY > (int) MaxY) {
                        bY = (int) MaxY;
                    }
                    if (count % upDataN == 0) {
                        count = 0;
                        code = "A5 10 " + Conversion(-bX) + " 30 " + Conversion(bY) + " FA";
                        if (!timecastMsg) {
                            castMsg(code);
                        }

                    }
                    textView.setText("搖桿紀錄:" + "向左" + -bX + "(" + (int) -(MaxX - ViewW) + ")" + "向上" + bY + "(" + (int) -(MaxY - ViewH) + ")");
                } else {
                    if (bY < -(int) MaxY) {
                        bY = -(int) MaxY;
                    }
                    if (count % upDataN == 0) {
                        count = 0;
                        code = "A5 10 " + Conversion(-bX) + " 40 " + Conversion(-bY) + " FA";
                        if (!timecastMsg) {
                            castMsg(code);
                        }

                    }
                    textView.setText("搖桿紀錄:" + "向左" + -bX + "(" + (int) -(MaxX - ViewW) + ")" + "向下" + -bY + "(" + (int) (MaxY - ViewH) + ")");
                }
            }
        }

        imageView.setX1Y1(eventX, eventY);

    }

    //--------------------------------------------------------------------

    public class upTime extends Thread {
        String t = "";

        public upTime() {
        }

        @Override
        public void run() {
            while (true) {
                try {
                    if (timecastMsg) {
                        runOnUiThread(new Runnable() {             //將內容交給UI執行緒做顯示
                            public void run() {

                                castMsg(code);
                            }
                        });

                    }
                    Thread.sleep(upTimeN * 10);
                } catch (Exception e) {

                }
            }
        }
    }

    Socket clientSocket;

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
                byte[] bis2;
                InputStream in = clientSocket.getInputStream();
                Tool.save(getApplicationContext(), IP, "IP");
                Tool.save(getApplicationContext(), Port + "", "Port");
                runOnUiThread(new Runnable() {             //將內容交給UI執行緒做顯示
                    public void run() {
                        Toast.makeText(MainActivity.this, "連線成功", Toast.LENGTH_SHORT).show();
                    }
                });
                // 取得網路輸入串流
                int n = 0;
                while ((n = in.read(bis)) != -1) {
                }
                Log.v("連線狀況", "連線結束");
                runOnUiThread(new Runnable() {             //將內容交給UI執行緒做顯示
                    public void run() {
                        Toast.makeText(MainActivity.this, "連線結束", Toast.LENGTH_SHORT).show();
                    }
                });
                clientSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
                Log.v("連線狀況", "連線失敗");
                runOnUiThread(new Runnable() {             //將內容交給UI執行緒做顯示
                    public void run() {
                        Toast.makeText(MainActivity.this, "連線失敗", Toast.LENGTH_SHORT).show();
                    }
                });
                clientSocket = null;
            }
        }


    }

    private String Conversion(int data) {
        String t = "";

        if (data > 0) {
            t = Integer.toHexString(data);
        } else if (data < 0) {
            t = Integer.toHexString(data + 256);
        } else {
            t = "00";
        }
        return t;
    }

    public void castMsg(final String text) {
        new Thread(new Runnable() {// Android 4.0 之后不能在主线程中请求HTTP请求
            @Override
            public void run() {
                // 取得網路輸出串流
                //傳送資料
                if (clientSocket != null) {
                    //       Log.d("aaaaaText:", text);
                    byte[] temp = new byte[6];
                    try {
                        for (int a = 0; a < 6; a++) {
                            temp[a] = (byte) Integer.parseInt(text.split(" ")[a], 16);
                        }

                        OutputStream out = clientSocket.getOutputStream();// 寫入訊息
                        out.write(temp, 0, temp.length);// 立即發送   //  "|n"
                        out.flush();
                        //     Log.d("aaaaaaaa",temp.length+"  aa");


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

    }

    private boolean haveInternet() {
        boolean result = false;
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            result = false;
        } else {
            if (!info.isAvailable()) {
                result = false;
            } else {
                result = true;
            }
        }
        return result;
    }//是否連線

    View.OnClickListener b01() {
        return new View.OnClickListener() {
            public void onClick(View v) {

                String IP = e01.getText().toString();
                int Port = 0;
                try {
                    Port = Integer.parseInt(e02.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "請輸入正確Port", Toast.LENGTH_SHORT).show();
                }
                Log.d("aaaaaaaaaaa", "zzzzzzzzzz" + "  a  " + IP + "  a " + Port);
                if (haveInternet()) {
                    if (clientSocket == null) {
                        if (IP.length() != 0 & Port != 0) {
                            new readData(IP, Port).start();
                        }

                    }

                } else {
                    Toast.makeText(MainActivity.this, "請開啟WIFI功能", Toast.LENGTH_SHORT).show();
                }

            }
        };
    }//連線客戶端

    EditText e01, e02;
    //--------------------------------------------------------------------
    String cutInHalf = "OFF";//參數減半

    View.OnClickListener t01(final TextView textView, final SeekBar seekBar, final TextView MaxText, final TextView MinText) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timecastMsg) {
                    MaxText.setText("10");
                    MinText.setText("1");
                    seekBar.setMax(9);
                    seekBar.setProgress(upDataN);
                    textView.setText("每" + upDataN + "筆傳輸一次");
                    timecastMsg = false;
                    Tool.save(getApplicationContext(), "F", "timecastMsg");
                } else {
                    MaxText.setText("1000");
                    MinText.setText("10");
                    seekBar.setMax(99);
                    seekBar.setProgress(upTimeN);
                    textView.setText("每" + (upTimeN * 10) + "秒傳輸一次");
                    Tool.save(getApplicationContext(), "T", "timecastMsg");
                    timecastMsg = true;

                }
            }
        };
    }



    View.OnClickListener t02(final TextView textView) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cutInHalf.equals("OFF")) {
                    cutInHalf = "ON";
                    Tool.save(getApplicationContext(), cutInHalf, "cutInHalf");
                    textView.setText("參數減半 " + cutInHalf);
                } else {
                    cutInHalf = "OFF";
                    Tool.save(getApplicationContext(), cutInHalf, "cutInHalf");
                    textView.setText("參數減半 " + cutInHalf);
                }
            }
        };
    }

    String root = "ON";

    View.OnClickListener t03(final TextView textView) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (root.equals("OFF")) {
                    root = "ON";
                } else {
                    root = "OFF";
                }
                textView.setText("開發者模式 " + root);
            }
        };
    }



    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
            String UpDataN = "", ip = "", port = "", CutInHalf = "", UpTimeN = "", TimecastMsg = "";
            UpTimeN = Tool.read(this, "upTimeN");
            UpDataN = Tool.read(this, "updataN");
            CutInHalf = Tool.read(this, "cutInHalf");
            TimecastMsg = Tool.read(this, "timecastMsg");
            if (UpDataN.length() != 0) {
                upDataN = Integer.parseInt(UpDataN);
            } else {
                Tool.save(getApplicationContext(), 5 + "", "updataN");
            }
            if (UpTimeN.length() != 0) {
                upTimeN = Integer.parseInt(UpTimeN);
            } else {
                Tool.save(getApplicationContext(), 10 + "", "upTimeN");
            }
            if (CutInHalf.length() != 0) {
                cutInHalf = CutInHalf;
            } else {
                Tool.save(getApplicationContext(), "OFF", "cutInHalf");

            }
            if (TimecastMsg.length() != 0) {
                if (TimecastMsg.equals("F")) {
                    timecastMsg = false;
                } else {
                    timecastMsg = true;
                }

            } else {
                Tool.save(getApplicationContext(), "F", "timecastMsg");
            }
            ip = Tool.read(this, "IP");
            port = Tool.read(this, "Port");

            View item = LayoutInflater.from(MainActivity.this).inflate(R.layout.set_parameter, null);
            Button b01 = (Button) item.findViewById(R.id.b01);

            e01 = (EditText) item.findViewById(R.id.e01);
            e02 = (EditText) item.findViewById(R.id.e02);


            e01.setText(ip);
            e02.setText(port);

            b01.setOnClickListener(b01());

            SeekBar seekBar = (SeekBar) item.findViewById(R.id.seekBar);
            seekBar.setProgress(upDataN - 1);

            final TextView MaxText = (TextView) item.findViewById(R.id.maxText);
            final TextView MinText = (TextView) item.findViewById(R.id.minText);

            final TextView textView = (TextView) item.findViewById(R.id.t01);
            if (timecastMsg) {
                MaxText.setText("1000");
                MinText.setText("10");
                seekBar.setMax(99);
                seekBar.setProgress(upTimeN);
                textView.setText("每" + (upTimeN * 10) + "秒傳輸一次");

            } else {
                MaxText.setText("10");
                MinText.setText("1");
                seekBar.setMax(9);
                seekBar.setProgress(upDataN);
                textView.setText("每" + upDataN + "筆傳輸一次");
            }


            final TextView textView2 = (TextView) item.findViewById(R.id.t02);
            textView2.setText("參數減半 " + cutInHalf);
            textView2.setOnClickListener(t02(textView2));

            final TextView textView3 = (TextView) item.findViewById(R.id.t03);
            textView3.setText("開發者模式 " + root);
            textView3.setOnClickListener(t03(textView3));

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                                                   boolean situation;

                                                   public void onStopTrackingTouch(SeekBar seekBar) {
                                                       situation = false;
                                                       //停止拖曳時觸發事件
                                                   }


                                                   public void onStartTrackingTouch(SeekBar seekBar) {
                                                       situation = true;

                                                       //開始拖曳時觸發事件
                                                   }

                                                   public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                                       if (situation == true) {
                                                           if (timecastMsg) {
                                                               textView.setText("每" + ((progress + 1) * 10) + "秒傳輸一次");
                                                               Tool.save(getApplicationContext(), (progress + 1) + "", "upTimeN");

                                                               upTimeN = (progress + 1);

                                                           } else {
                                                               textView.setText("每" + (progress + 1) + "筆傳輸一次");
                                                               Tool.save(getApplicationContext(), (progress + 1) + "", "updataN");
                                                               upDataN = (progress + 1);


                                                           }

                                                       }

                                                       //拖曳途中觸發事件，回傳參數 progress 告知目前拖曳數值
                                                   }
                                               }
            );
            textView.setOnClickListener(t01(textView, seekBar, MaxText, MinText));


            new AlertDialog.Builder(MainActivity.this)
                    .setView(item)
                    .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
            return true;
        }

        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
            new readData(IP, Port).start();
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
