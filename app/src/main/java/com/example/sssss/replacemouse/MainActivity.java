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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView textView, textView2;

    String IP = "172.18.148.27";
    int Port = 0;
    Button button;
    Button button2;
    RightButton rightButton = new RightButton(this);
    LeftButton leftButton = new LeftButton(this);
    SetButtonView setButtonView = new SetButtonView(this);
    SetToolView setToolView = new SetToolView(this);
    ReadData readData = new ReadData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setTheme(android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);//固定橫向
        //這邊設定 固定橫向  會造成 無法傳值 要到AndroidManifest.xml 設定橫向
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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


        rightButton.display();
        leftButton.display();
        loadData();


        new upTime().start();
    }

    /**
     * 載入以前設定的參數
     */
    private void loadData() {
        String updata = "";//每N筆傳輸一次
        String upTime = "";//每N秒傳輸一次
        updata = Tool.read(this, "Updata");
        upTime = Tool.read(this, "UpTime");
        if (updata.length() != 0) {
            rightButton.UpData = Integer.parseInt(updata);
        } else {//預設 每5筆傳輸一次
            Tool.save(getApplicationContext(), 5 + "", "updataN");
        }
        Log.i("UpData(每N筆傳輸一次)", String.valueOf(rightButton.UpData));
        if (upTime.length() != 0) {
            rightButton.UpTime = Integer.parseInt(upTime);
        } else {//預設 每5秒傳輸一次
            Tool.save(getApplicationContext(), 5 + "", "upTimeN");
        }
        Log.i("UpTime(每N秒傳輸一次)", String.valueOf(rightButton.UpTime));
        //-----------------------------------------------------------------------
        String cutInHalf = "";//參數減半  T:開啟 F:關閉
        cutInHalf = Tool.read(this, "CutInHalf");
        String sendDataMode = "";//傳輸模式 T:每秒傳輸  F:每筆傳輸
        sendDataMode = Tool.read(this, "SendDataMode");

        if (cutInHalf.length() != 0) {
            rightButton.CutInHalf = Boolean.parseBoolean(cutInHalf);
        } else {//預設 關閉
            Tool.save(getApplicationContext(), "false", "CutInHalf");
        }
        Log.i("CutInHalf", String.valueOf(rightButton.CutInHalf));
        if (sendDataMode.length() != 0) {
            rightButton.SendDataMode = Boolean.parseBoolean(sendDataMode);
        } else {//預設 關閉
            Tool.save(getApplicationContext(), "false", "SendDataMode");
        }
        Log.i("SendDataMode(傳輸模式)", String.valueOf(rightButton.SendDataMode));
        //-----------------------------------------------------------------------
        String ip = Tool.read(this, "IP");
        String port = Tool.read(this, "Port");
        if (ip.length() != 0) {
            IP = ip;
        }
        try {
            Port = Integer.parseInt(port);
        } catch (Exception e) {
        }
        Log.i("IP", IP);
        Log.i("Port", String.valueOf(Port));
        //-----------------------------------------------------------------------
        for (int a = 0; a < 9; a++) {
            String ViewT = Tool.read(getApplicationContext(), "View" + a);
            String CodeT = Tool.read(getApplicationContext(), "Code" + a);
            if (ViewT.length() == 0) {
                Tool.save(getApplicationContext(), rightButton.View_[a], "View" + a);
                Tool.save(getApplicationContext(), rightButton.Code[a], "Code" + a);
            } else {
                rightButton.View_[a] = Tool.read(getApplicationContext(), "View" + a);
                rightButton.Code[a] = Tool.read(getApplicationContext(), "Code" + a);

            }
        }
    }
    //-------------------------------------------------------------------

    /**
     * 設定右區域按鈕
     */
    public class RightButton {
        LinearLayout L01, L02, L03;
        int UpData = 5, UpTime = 10;
        boolean CutInHalf = false;//參數減半  T:開啟 F:關閉
        boolean SendDataMode = false;//傳輸模式 T:每秒傳輸  F:每筆傳輸

        String[] View_ = {
                "N,,x,xt0",//1
                "B,△,x,+t5",//2
                "B,U,x,ct6",//3
                "B,☐,x,xt0",//4
                "L,1,M10m0,xt0",//5
                "L,U,x,xt0",//6
                "N,,x,xt0",//7
                "B,▽,x,-t5",//8
                "B,W,x,ct6"//9
        };
        String[] Code = {"A5 50 FF FF FF FA", "A5 70 RE FF FF FA", "A5 81 FF FF FF FA",
                "A5 60 FF FF FF FA", "A5 50 FF FF FF FA", "A5 50 FF FF FF FA",
                "A5 50 FF FF FF FA", "A5 90 RE FF FF FA", "A5 80 FF FF FF FA"};
        int[] Max = {10, 10, 10, 10, 10, 10, 10, 10, 10};
        int[] min = {0, 0, 0, 0, 0, 0, 0, 0, 0};
        Context context;

        public RightButton(Context context) {
            this.context = context;
        }

        /**
         * 顯示右邊按鈕
         */
        public void display() {
            L01 = (LinearLayout) findViewById(R.id.L01);
            L02 = (LinearLayout) findViewById(R.id.L02);
            L03 = (LinearLayout) findViewById(R.id.L03);
            setL0X(L01, 0, 1, 2);
            setL0X(L02, 3, 4, 5);
            setL0X(L03, 6, 7, 8);

        }

        TextView[] t0x = new TextView[9];
        Integer[] i0x = {1, 1, 1, 1, 1, 1, 1, 1, 1};

        /**
         * 動態產生元件框架
         *
         * @param L0x
         * @param a   按鍵1(左邊)
         * @param b   按鍵2(中間)
         * @param c   按鍵3(右邊)
         */
        private void setL0X(LinearLayout L0x, int a, int b, int c) {
            L0x.removeAllViews();
            LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1f);
            t0x[a] = setText(a);
            t0x[b] = setText(b);
            t0x[c] = setText(c);
            L0x.addView(t0x[a], param1);
            L0x.addView(t0x[b], param1);
            L0x.addView(t0x[c], param1);
        }

        ArrayList<String> Label = new ArrayList<String>();

        /**
         * 按鈕初始化
         *
         * @param id 設定按鈕ID
         * @return
         */
        private TextView setText(int id) {

            String Type = View_[id].split(",")[0];//類別 B:按鈕 L:標籤 N:無
            String text = View_[id].split(",")[1];//傳輸字串
            String setMaxMin = View_[id].split(",")[2];//設定最大值和做小值
            String control = View_[id].split(",")[3];//設定控制元件

            //  Integer Max=Integer.parseInt(mod.substring(0,1));
            if (setMaxMin.startsWith("M")) {
                Integer Max = Integer.parseInt(setMaxMin.substring(setMaxMin.indexOf("M") + 1, setMaxMin.indexOf("m")));
                Integer min = Integer.parseInt(setMaxMin.substring(setMaxMin.indexOf("m") + 1));
                this.Max[id] = Max;
                this.min[id] = min;
                // Log.d("aaaaa", "id" + id + " Max:" + Max + " min:" + min);
            }
            TextView textView = new TextView(context);
            textView.setText(text);
            textView.setTextSize(64);
            textView.setGravity(Gravity.CENTER);

            textView.setTextColor(Color.parseColor("#FFFFFF"));
            textView.setBackground(getResources().getDrawable(R.drawable.ic_black));
            if (Type.equals("B")) {//按鈕
                textView.setBackground(getResources().getDrawable(R.drawable.ic_imageu1));
            } else if (Type.equals("L")) {
                Label.add("t" + id);
            }
            textView.setOnTouchListener(textView(textView, id, Type, control));
            return textView;
        }

        View.OnTouchListener textView(final TextView textView,
                                      final int id, final String Type,
                                      final String control) {
            return new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    String Code_t = "";
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {  //按下的時候改變背景及顏色
                        if (setToolView.root.equals("ON")) {
                            setButtonView.setRoot(id, View_[id]);
                        } else {
                            if (Type.equals("B")) {
                                int i0x = 0;
                                textView.setBackground(getResources().getDrawable(R.drawable.ic_imageu2));
                                Integer Co_id = Integer.parseInt(control.split("t")[1]);
                                if (control.startsWith("+")) {

                                    i0x = Calculation(Co_id - 1, true);
                                } else if (control.startsWith("-")) {
                                    i0x = Calculation(Co_id - 1, false);
                                } else if (control.startsWith("c")) {
                                    t0x[Co_id - 1].setText(t0x[id].getText().toString());
                                }
                                Code_t = Code[id].replace("RE", i0x + "");
                                readData.sendData(Code_t);
                            }
                        }


                        return true;


                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {  //按下的時候改變背景及顏色
                        if (Type.equals("B")) {
                            textView.setBackground(getResources().getDrawable(R.drawable.ic_imageu1));
                        } else if (Type.equals("L")) {//計數
                            //      code = Code.replace("RE", Integer.toHexString(i01) + "");
                        }
                        return true;
                    }


                    return true;
                }
            };
        }

        private int Calculation(int id, boolean ad_re) {
            if (ad_re) {
                i0x[id]++;
            } else {
                i0x[id]--;
            }
            if (i0x[id] > Max[id]) {
                i0x[id] = Max[id];
            } else if (i0x[id] < min[id]) {
                i0x[id] = rightButton.min[id];
            }
            t0x[id].setText(i0x[id] + "");
            return i0x[id];
        }


    }

    //-------------------------------------------------------------------

    /**
     * 設定左區域按鈕
     */
    public class LeftButton {
        Context context;
        ImageViewAll imageView0;

        public LeftButton(Context context) {
            this.context = context;
        }

        /**
         * 顯示左邊按鈕
         */
        public void display() {
            imageView0 = (ImageViewAll) findViewById(R.id.image0);
            imageView0.setOnTouchListener(ButImage(imageView0));
            imageView0.setMod(0);
        }

        int count = 0;
        String code = "A5 50 00 00 00 FA";

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
                            if (!rightButton.SendDataMode) {
                                readData.sendData(code);
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
                        if (count % rightButton.UpData == 0) {
                            count = 0;
                            code = "A5 20 " + readData.Conversion(bX) + " 30 " + readData.Conversion(bY) + " FA";
                            if (!rightButton.SendDataMode) {
                                readData.sendData(code);
                            }

                        }
                        textView.setText("搖桿紀錄:" + "向右" + bX + "%  " + "向上" + bY + "% ");
                    } else {
                        if (bY < -100) {
                            bY = -100;
                        }
                        if (count % rightButton.UpData == 0) {
                            count = 0;
                            code = "A5 20 " + readData.Conversion(bX) + " 40 " + readData.Conversion(-bY) + " FA";
                            if (!rightButton.SendDataMode) {
                                readData.sendData(code);
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
                        if (count % rightButton.UpData == 0) {
                            count = 0;
                            code = "A5 10 " + readData.Conversion(-bX) + " 30 " + readData.Conversion(bY) + " FA";
                            if (!rightButton.SendDataMode) {
                                readData.sendData(code);
                            }

                        }
                        textView.setText("搖桿紀錄:" + "向左" + -bX + "%  " + "向上" + bY + "% ");
                    } else {
                        if (bY < -100) {
                            bY = -100;
                        }
                        if (count % rightButton.UpData == 0) {
                            count = 0;
                            code = "A5 10 " + readData.Conversion(-bX) + " 40 " + readData.Conversion(-bY) + " FA";
                            if (!rightButton.SendDataMode) {
                                readData.sendData(code);
                            }

                        }
                        textView.setText("搖桿紀錄:" + "向左" + -bX + "%  " + "向下" + -bY + "% ");
                    }
                }
            } else {
                bX = (int) (eventX - ViewW);
                bY = -(int) (eventY - ViewH);
                if (rightButton.CutInHalf) {
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
                        if (count % rightButton.UpData == 0) {
                            count = 0;
                            code = "A5 20 " + readData.Conversion(bX) + " 30 " + readData.Conversion(bY) + " FA";
                            if (!rightButton.SendDataMode) {
                                readData.sendData(code);
                            }

                        }
                        textView.setText("搖桿紀錄:" + "向右" + bX + "(" + (int) (MaxX - ViewW) + ")" + "向上" + bY + "(" + (int) -(MaxY - ViewH) + ")");
                    } else {
                        if (bY < -(int) MaxY) {
                            bY = -(int) MaxY;
                        }
                        if (count % rightButton.UpData == 0) {
                            count = 0;
                            code = "A5 20 " + readData.Conversion(bX) + " 40 " + readData.Conversion(-bY) + " FA";
                            if (!rightButton.SendDataMode) {
                                readData.sendData(code);
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
                        if (count % rightButton.UpData == 0) {
                            count = 0;
                            code = "A5 10 " + readData.Conversion(-bX) + " 30 " + readData.Conversion(bY) + " FA";
                            if (!rightButton.SendDataMode) {
                                readData.sendData(code);
                            }

                        }
                        textView.setText("搖桿紀錄:" + "向左" + -bX + "(" + (int) -(MaxX - ViewW) + ")" + "向上" + bY + "(" + (int) -(MaxY - ViewH) + ")");
                    } else {
                        if (bY < -(int) MaxY) {
                            bY = -(int) MaxY;
                        }
                        if (count % rightButton.UpData == 0) {
                            count = 0;
                            code = "A5 10 " + readData.Conversion(-bX) + " 40 " + readData.Conversion(-bY) + " FA";
                            if (!rightButton.SendDataMode) {
                                readData.sendData(code);
                            }

                        }
                        textView.setText("搖桿紀錄:" + "向左" + -bX + "(" + (int) -(MaxX - ViewW) + ")" + "向下" + -bY + "(" + (int) (MaxY - ViewH) + ")");
                    }
                }
            }

            imageView.setX1Y1(eventX, eventY);

        }

    }

    //-------------------------------------------------------------------


    /**
     * 設定按鈕參數
     */
    public class SetButtonView {
        Context context;

        public SetButtonView(Context context) {
            this.context = context;
        }

        private void setRoot(final int id, final String Temp) {
            String Type = Temp.split(",")[0];//類別 B:按鈕 L:標籤 N:無
            String text = Temp.split(",")[1];//傳輸字串
            String setMaxMin = Temp.split(",")[2];//設定最大值和做小值
            final String control = Temp.split(",")[3];//設定控制元件
            //------------------------------------------------------------------------
            View item = LayoutInflater.from(MainActivity.this).inflate(R.layout.set_root, null);
            final LinearLayout L01 = (LinearLayout) item.findViewById(R.id.L01);
            final EditText e01 = (EditText) item.findViewById(R.id.e01);
            e01.setText(rightButton.Code[id].split(" ")[1]);
            //------------------------------------------------------------------------
            LinearLayout L02 = (LinearLayout) item.findViewById(R.id.L02);
            final EditText e02 = (EditText) item.findViewById(R.id.e02);
            e02.setText(text);
            //------------------------------------------------------------------------
            LinearLayout L03 = (LinearLayout) item.findViewById(R.id.L03);
            final EditText e03 = (EditText) item.findViewById(R.id.e03);
            final EditText e04 = (EditText) item.findViewById(R.id.e04);
            final RadioButton r01 = (RadioButton) item.findViewById(R.id.r01);
            RadioButton r02 = (RadioButton) item.findViewById(R.id.r02);
            r01.setOnClickListener(RadioButton(r02));
            r02.setOnClickListener(RadioButton(r01));
            if (setMaxMin.startsWith("M")) {
                e03.setText(setMaxMin.substring(setMaxMin.indexOf("M") + 1, setMaxMin.indexOf("m")));
                e04.setText(setMaxMin.substring(setMaxMin.indexOf("m") + 1));
                r01.setChecked(true);
            } else {
                r02.setChecked(true);
            }

            //------------------------------------------------------------------------
            LinearLayout L04 = (LinearLayout) item.findViewById(R.id.L04);

            final TextView t01 = (TextView) item.findViewById(R.id.t01);
            final TextView t02 = (TextView) item.findViewById(R.id.t02);
            final TextView t03 = (TextView) item.findViewById(R.id.t03);


            t02.setText(control.substring(0, 1));
            if (control.split("t")[1].equals("0")) {
                t03.setText("無");
            } else {
                t03.setText("元件" + control.split("t")[1]);
            }
            //------------------------------------------------------------------------
            if (Type.equals("N")) {
                t01.setText("無");
                L01.setVisibility(View.INVISIBLE);
                L02.setVisibility(View.INVISIBLE);
                L03.setVisibility(View.INVISIBLE);
                L04.setVisibility(View.INVISIBLE);

            } else if (Type.equals("B")) {
                t01.setText("按鈕");
                L04.setVisibility(View.VISIBLE);
                L03.setVisibility(View.INVISIBLE);
            } else if (Type.equals("L")) {
                t01.setText("標籤");
                L03.setVisibility(View.VISIBLE);
                L04.setVisibility(View.INVISIBLE);
            }


            t01.setOnClickListener(tt01(t01, L01, L02, L03, L04));
            t02.setOnClickListener(tt02(t02, t03, id));
            t03.setOnClickListener(tt03(t02, t03));

            new AlertDialog.Builder(MainActivity.this)
                    .setView(item)
                    .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            judge(id, t01, t02, t03, r01, e02, e03, e04);
                            changeCode(id, e01);
                        }
                    })
                    .show();
        }

        View.OnClickListener tt01(final TextView textView, final LinearLayout L01, final LinearLayout L02, final LinearLayout L03, final LinearLayout L04) {
            return new View.OnClickListener() {
                public void onClick(View v) {
                    String t = textView.getText().toString();
                    if (t.equals("無")) {
                        textView.setText("按鈕");
                        L01.setVisibility(View.VISIBLE);
                        L02.setVisibility(View.VISIBLE);
                        L04.setVisibility(View.VISIBLE);
                        L03.setVisibility(View.INVISIBLE);
                    } else if (t.equals("按鈕")) {
                        textView.setText("標籤");
                        L03.setVisibility(View.VISIBLE);
                        L04.setVisibility(View.INVISIBLE);
                    } else if (t.equals("標籤")) {
                        textView.setText("無");
                        L01.setVisibility(View.INVISIBLE);
                        L02.setVisibility(View.INVISIBLE);
                        L03.setVisibility(View.INVISIBLE);
                        L04.setVisibility(View.INVISIBLE);
                    }

                }
            };
        }

        View.OnClickListener tt02(final TextView t02, final TextView t03, final int id) {
            return new View.OnClickListener() {
                public void onClick(View v) {
                    String t = t02.getText().toString();
                    if (t.equals("+")) {
                        rightButton.Code[id] = rightButton.Code[id].replaceFirst(rightButton.Code[id].split(" ")[2], "RE");
                        t02.setText("-");
                    } else if (t.equals("-")) {
                        t02.setText("c");
                        rightButton.Code[id] = rightButton.Code[id].replaceFirst(rightButton.Code[id].split(" ")[2], "FF");
                    } else if (t.equals("c")) {
                        rightButton.Code[id] = rightButton.Code[id].replaceFirst(rightButton.Code[id].split(" ")[2], "FF");
                        t02.setText("x");
                        t03.setText("無");
                    } else if (t.equals("x")) {
                        t02.setText("+");
                        rightButton.Code[id] = rightButton.Code[id].replaceFirst(rightButton.Code[id].split(" ")[2], "RE");
                        String tt = rightButton.Label.get(a);
                        Integer Co_id = Integer.parseInt(tt.split("t")[1]) + 1;
                        t03.setText(("t" + Co_id).replace("t", "元件"));
                    }
                }
            };
        }

        int a = 0;

        View.OnClickListener tt03(final TextView t02, final TextView t03) {
            return new View.OnClickListener() {
                public void onClick(View v) {
                    if (!(t02.getText().equals("x"))) {
                        a++;
                        if (a >= rightButton.Label.size()) {
                            a = 0;
                        }
                        //  Log.d("qqqqqq",a+"   "+Label.size());
                        String t = rightButton.Label.get(a);
                        Integer Co_id = Integer.parseInt(t.split("t")[1]) + 1;
                        t03.setText(("t" + Co_id).replace("t", "元件"));
                    }


                }
            };
        }

        //判斷
        private void judge(final int id, TextView t01, TextView t02, TextView t03, RadioButton r01, EditText e02, EditText e03, EditText e04) {
            String view = "";
            String t = t01.getText().toString();
            if (t.equals("無")) {
                view = "N";
            } else if (t.equals("按鈕")) {
                view = "B";
            } else if (t.equals("標籤")) {
                view = "L";
            }

            view = view + "," + e02.getText().toString();

            if (r01.isChecked()) {
                view = view + ",M" + e03.getText().toString() + "m" + e04.getText().toString();
            } else {
                view = view + ",x";
            }
            if (t03.getText().toString().equals("無")) {
                t02.setText("x");
            }
            view = view + "," + t02.getText().toString();

            view = view + t03.getText().toString().replace("元件", "t")
                    .replace("無", "t0");
            Log.d("aaaaaaaa", view + "  ");

            if (t.equals("無")) {
                view = "N,,x,xt0";
            }


            rightButton.View_[id] = view;
            if (id >= 6) {
                rightButton.setL0X(rightButton.L03, 6, 7, 8);
            } else if (id >= 3) {
                rightButton.setL0X(rightButton.L02, 3, 4, 5);
            } else {
                rightButton.setL0X(rightButton.L01, 0, 1, 2);
            }
            Tool.save(getApplicationContext(), rightButton.View_[id], "View" + id);

        }

        private void changeCode(final int id, EditText e01) {
            String code_ = rightButton.Code[id];
            rightButton.Code[id] = code_.replaceFirst(code_.split(" ")[1], e01.getText().toString());
            Tool.save(getApplicationContext(), rightButton.Code[id], "Code" + id);
        }

        View.OnClickListener RadioButton(final RadioButton r0x) {
            return new View.OnClickListener() {
                public void onClick(View v) {
                    r0x.setChecked(false);
                }
            };
        }

        //----------------------------------------------------------------------


    }
    //-------------------------------------------------------------------
    /**
     * 設定工具參數
     */
    public class SetToolView {
        Context context;

        public SetToolView(Context context) {
            this.context = context;
        }
        EditText e01, e02;
        //--------------------------------------------------------------------
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
                    //    Log.d("aaaaaaaaaaa", "zzzzzzzzzz" + "  a  " + IP + "  a " + Port);
                    if (haveInternet()) {
                        if (readData.clientSocket == null) {
                            if (IP.length() != 0 & Port != 0) {
                                readData.start(IP, Port);
                            }

                        }

                    } else {
                        Toast.makeText(MainActivity.this, "請開啟WIFI功能", Toast.LENGTH_SHORT).show();
                    }

                }
            };
        }//連線客戶端

        View.OnClickListener t01(final TextView textView, final SeekBar seekBar, final TextView MaxText, final TextView MinText) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rightButton.SendDataMode) {
                        MaxText.setText("10");
                        MinText.setText("1");
                        seekBar.setMax(9);
                        seekBar.setProgress(rightButton.UpData);
                        textView.setText("每" + rightButton.UpData + "筆傳輸一次");
                    } else {
                        MaxText.setText("1000");
                        MinText.setText("10");
                        seekBar.setMax(99);
                        seekBar.setProgress(rightButton.UpTime);
                        textView.setText("每" + (rightButton.UpTime * 10) + "秒傳輸一次");

                    }
                    rightButton.SendDataMode = !rightButton.SendDataMode;
                    Tool.save(getApplicationContext(), String.valueOf(rightButton.SendDataMode), "SendDataMode");

                }
            };
        }

        View.OnClickListener t02(final TextView textView) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rightButton.CutInHalf) {
                        textView.setText("參數減半  關閉");

                    } else {
                        textView.setText("參數減半  開啟");
                    }
                    rightButton.CutInHalf = !rightButton.CutInHalf;
                    Tool.save(getApplicationContext(), String.valueOf(rightButton.CutInHalf), "CutInHalf");
                }
            };
        }

        String root = "OFF";

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


        View.OnClickListener ReplaceVolumeButton(final boolean ad_re) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ad_re) {
                        VolumeButtonAd();

                    } else {
                        VolumeButtonRe();
                    }

                }
            };
        }

        private void VolumeButtonAd() {
            readData.start(IP, Port);
        }

        private void VolumeButtonRe() {
            View item = LayoutInflater.from(MainActivity.this).inflate(R.layout.set_parameter, null);
            Button b01 = (Button) item.findViewById(R.id.b01);

            e01 = (EditText) item.findViewById(R.id.e01);
            e02 = (EditText) item.findViewById(R.id.e02);


            e01.setText(IP);
            e02.setText(String.valueOf(Port));

            b01.setOnClickListener(b01());

            SeekBar seekBar = (SeekBar) item.findViewById(R.id.seekBar);
            seekBar.setProgress(rightButton.UpData - 1);

            final TextView MaxText = (TextView) item.findViewById(R.id.maxText);
            final TextView MinText = (TextView) item.findViewById(R.id.minText);

            final TextView textView = (TextView) item.findViewById(R.id.t01);
            if (rightButton.SendDataMode) {
                MaxText.setText("1000");
                MinText.setText("10");
                seekBar.setMax(99);
                seekBar.setProgress(rightButton.UpTime);
                textView.setText("每" + (rightButton.UpTime * 10) + "秒傳輸一次");

            } else {
                MaxText.setText("10");
                MinText.setText("1");
                seekBar.setMax(9);
                seekBar.setProgress(rightButton.UpData);
                textView.setText("每" + rightButton.UpData + "筆傳輸一次");
            }


            final TextView textView2 = (TextView) item.findViewById(R.id.t02);
            if (rightButton.CutInHalf) {
                textView2.setText("參數減半  開啟");
            } else {
                textView2.setText("參數減半  關閉");
            }
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
                                                           if (rightButton.SendDataMode) {
                                                               textView.setText("每" + ((progress + 1) * 10) + "秒傳輸一次");
                                                               Tool.save(getApplicationContext(), (progress + 1) + "", "upTimeN");

                                                               rightButton.UpTime = (progress + 1);

                                                           } else {
                                                               textView.setText("每" + (progress + 1) + "筆傳輸一次");
                                                               Tool.save(getApplicationContext(), (progress + 1) + "", "updataN");
                                                               rightButton.UpData = (progress + 1);


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
        }

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
                    if (rightButton.SendDataMode) {
                        runOnUiThread(new Runnable() {             //將內容交給UI執行緒做顯示
                            public void run() {

                                readData.sendData(leftButton.code);
                            }
                        });

                    }
                    Thread.sleep(rightButton.UpTime * 10);
                } catch (Exception e) {

                }
            }
        }
    }

    /**
     * 關於 連線(Socket) 的程式
     */
    public class ReadData extends Thread {
        Socket clientSocket;
        String IP = "";
        int Port = 0;
        String line = "";

        public ReadData() {
        }

        public void start(String IP, int Port) {
            this.IP = IP;
            this.Port = Port;
            this.start();
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
                byte[] temp = new byte[1];
                while ((n = in.read(bis)) != -1) {
                    bis2 = new byte[n];
                    for (int a = 0; a < n; a++) {
                        bis2[a] = bis[a];
                    }
                    line = "";
                    for (int a = 0; a < n; a++) {
                        if (a == 0) {
                            line = line + Conversion(Integer.parseInt(bis2[a] + ""));
                        } else {
                            line = line + " " + Conversion(Integer.parseInt(bis2[a] + ""));
                        }
                    }
                    runOnUiThread(new Runnable() {             //將內容交給UI執行緒做顯示
                        public void run() {
                            textView2.setText("接收訊息:" + line);
                        }
                    });
                    Log.d("aaaaaa", line);
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

        /**
         * 將10進制 轉 16進制
         *
         * @param data 數字(10進制)
         * @return 數字(16進制)
         */
        public String Conversion(int data) {
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

        /**
         * 傳送資料
         *
         * @param text 傳送文字
         */
        public void sendData(final String text) {
            new Thread(new Runnable() {
                // Android 4.0 之后不能在主线程中请求HTTP请求
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