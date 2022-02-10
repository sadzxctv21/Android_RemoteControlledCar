package com.example.sssss.replacemouse;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.sssss.replacemouse.MainActivity.context;
import static com.example.sssss.replacemouse.MainActivity.mainActivity;
import static com.example.sssss.replacemouse.MainActivity.readData;
/**
 * 設定工具參數
 */
public class SetToolView {


    /**
     * 每N筆傳輸一次
     */
    Integer UpData = 5;
    /**
     * 每N秒傳輸一次
     */
    Integer UpTime = 10;
    /**
     * 參數減半
     * T:開啟
     * F:關閉
     */
    Boolean CutInHalf = false;//參數減半  T:開啟 F:關閉
    /**
     * 傳輸模式 T:每秒傳輸  F:每筆傳輸
     */
    Boolean SendDataMode = false;//傳輸模式 T:每秒傳輸  F:每筆傳輸


    public SetToolView() {
        loadData();
    }
    //-------------------------------------------------------------
    /**
     * 載入以前設定的參數
     */
    private void loadData() {
        String updata = "";//每N筆傳輸一次
        String upTime = "";//每N秒傳輸一次
        updata = Tool.read(context, "Updata");
        upTime = Tool.read(context, "UpTime");
        if (updata.length() != 0) {
            UpData = Integer.parseInt(updata);
        } else {//預設 每5筆傳輸一次
            Tool.save(context, 5 + "", "updataN");
        }
        Log.i("UpData(每N筆傳輸一次)", String.valueOf(UpData));
        if (upTime.length() != 0) {
            UpTime = Integer.parseInt(upTime);
        } else {//預設 每5秒傳輸一次
            Tool.save(context, 5 + "", "upTimeN");
        }
        Log.i("UpTime(每N秒傳輸一次)", String.valueOf(UpTime));
        //-----------------------------------------------------------------------
        String cutInHalf = "";//參數減半  T:開啟 F:關閉
        cutInHalf = Tool.read(context, "CutInHalf");
        String sendDataMode = "";//傳輸模式 T:每秒傳輸  F:每筆傳輸
        sendDataMode = Tool.read(context, "SendDataMode");

        if (cutInHalf.length() != 0) {
            CutInHalf = Boolean.parseBoolean(cutInHalf);
        } else {//預設 關閉
            Tool.save(context, "false", "CutInHalf");
        }
        Log.i("CutInHalf", String.valueOf(CutInHalf));
        if (sendDataMode.length() != 0) {
            SendDataMode = Boolean.parseBoolean(sendDataMode);
        } else {//預設 關閉
            Tool.save(context, "false", "SendDataMode");
        }
        Log.i("SendDataMode(傳輸模式)", String.valueOf(SendDataMode));

        //-----------------------------------------------------------------------
    }
    //-------------------------------------------------------------

    EditText e01, e02;
    //--------------------------------------------------------------------
    View.OnClickListener b01() {
        return new View.OnClickListener() {
            public void onClick(View v) {
                readData.IP = e01.getText().toString();
                int Port = 0;
                try {
                    readData.Port = Integer.parseInt(e02.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(mainActivity, "請輸入正確Port", Toast.LENGTH_SHORT).show();
                }

                if (Tool.haveInternet(context)) {
                    if (readData.clientSocket == null) {
                        if (readData.IP.length() != 0 & readData.Port  != 0) {
                            readData.start(readData.IP, readData.Port );
                        }

                    }
                } else {
                    Toast.makeText(mainActivity, "請開啟WIFI功能", Toast.LENGTH_SHORT).show();
                }

            }
        };
    }//連線客戶端

    //設定傳輸方式
    View.OnClickListener t01(final TextView textView,
                             final SeekBar seekBar,
                             final TextView MaxText,
                             final TextView MinText) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SendDataMode) {
                    MaxText.setText("10");
                    MinText.setText("1");
                    seekBar.setMax(9);
                    seekBar.setProgress(UpData);
                    textView.setText("每" + UpData + "筆傳輸一次");
                } else {
                    MaxText.setText("1000");
                    MinText.setText("10");
                    seekBar.setMax(99);
                    seekBar.setProgress(UpTime);
                    textView.setText("每" + (UpTime * 10) + "秒傳輸一次");

                }
                SendDataMode = !SendDataMode;
                Tool.save(context, String.valueOf(SendDataMode), "SendDataMode");

            }
        };
    }
    //設定是否數值減半
    View.OnClickListener t02(final TextView textView) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CutInHalf) {
                    textView.setText("參數減半  關閉");

                } else {
                    textView.setText("參數減半  開啟");
                }
                CutInHalf = !CutInHalf;
                Tool.save(context, String.valueOf(CutInHalf), "CutInHalf");
            }
        };
    }

    String root = "OFF";
    //設定是否啟動開發者模式
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


    //------------------------------------------------------------------

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

    public void VolumeButtonAd() {
        readData.start(readData.IP, readData.Port);
    }

    public void VolumeButtonRe() {
        View item = LayoutInflater.from(mainActivity).inflate(R.layout.set_parameter, null);
        Button b01 = (Button) item.findViewById(R.id.b01);

        e01 = (EditText) item.findViewById(R.id.e01);
        e02 = (EditText) item.findViewById(R.id.e02);


        e01.setText(readData.IP);
        e02.setText(String.valueOf(readData.Port));

        b01.setOnClickListener(b01());

        SeekBar seekBar = (SeekBar) item.findViewById(R.id.seekBar);
        seekBar.setProgress(UpData - 1);

        final TextView MaxText = (TextView) item.findViewById(R.id.maxText);
        final TextView MinText = (TextView) item.findViewById(R.id.minText);

        final TextView textView = (TextView) item.findViewById(R.id.t01);
        if (SendDataMode) {
            MaxText.setText("1000");
            MinText.setText("10");
            seekBar.setMax(99);
            seekBar.setProgress(UpTime);
            textView.setText("每" + (UpTime * 10) + "秒傳輸一次");

        } else {
            MaxText.setText("10");
            MinText.setText("1");
            seekBar.setMax(9);
            seekBar.setProgress(UpData);
            textView.setText("每" + UpData + "筆傳輸一次");
        }


        final TextView textView2 = (TextView) item.findViewById(R.id.t02);
        if (CutInHalf) {
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
                                                       if (SendDataMode) {
                                                           textView.setText("每" + ((progress + 1) * 10) + "秒傳輸一次");
                                                           Tool.save(context, (progress + 1) + "", "upTimeN");

                                                           UpTime = (progress + 1);

                                                       } else {
                                                           textView.setText("每" + (progress + 1) + "筆傳輸一次");
                                                           Tool.save(context, (progress + 1) + "", "updataN");
                                                           UpData = (progress + 1);


                                                       }

                                                   }

                                                   //拖曳途中觸發事件，回傳參數 progress 告知目前拖曳數值
                                               }
                                           }
        );
        textView.setOnClickListener(t01(textView, seekBar, MaxText, MinText));


        new AlertDialog.Builder(context)
                .setView(item)
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

}