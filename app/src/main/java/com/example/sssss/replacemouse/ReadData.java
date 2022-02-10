package com.example.sssss.replacemouse;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import static com.example.sssss.replacemouse.MainActivity.context;
import static com.example.sssss.replacemouse.MainActivity.mainActivity;
/**
 * 關於 連線(Socket) 的程式
 */
public class ReadData extends Thread {
    Socket clientSocket;
    String IP = "";
    Integer Port = 0;
    String line = "";

    public ReadData() {
        displayView = (TextView) mainActivity.findViewById(R.id.textView2);
        loadData();
    }

    //-----------------------------------------------------
    /**
     * 載入以前設定的參數
     */
    private void loadData() {
        //-----------------------------------------------------------------------
        String ip = Tool.read(context, "IP");
        String port = Tool.read(context, "Port");
        if (ip.length() != 0) {
            IP = ip;
        }
        try {
            Port = Integer.parseInt(port);
        } catch (Exception e) {
        }
        Log.i("IP", IP);
        Log.i("Port", String.valueOf(Port));
    }
    //-----------------------------------------------------

    TextView displayView;

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
            Tool.save(context.getApplicationContext(), IP, "IP");
            Tool.save(context.getApplicationContext(), Port + "", "Port");
            mainActivity.runOnUiThread(new Runnable() {             //將內容交給UI執行緒做顯示
                public void run() {
                    Toast.makeText(mainActivity, "連線成功", Toast.LENGTH_SHORT).show();
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
                mainActivity.runOnUiThread(new Runnable() {             //將內容交給UI執行緒做顯示
                    public void run() {
                        displayView.setText("接收訊息:" + line);
                    }
                });
                Log.d("aaaaaa", line);
            }
            Log.v("連線狀況", "連線結束");
            mainActivity.runOnUiThread(new Runnable() {             //將內容交給UI執行緒做顯示
                public void run() {
                    Toast.makeText(mainActivity, "連線結束", Toast.LENGTH_SHORT).show();
                }
            });
            clientSocket = null;
        } catch (IOException e) {
            e.printStackTrace();
            Log.v("連線狀況", "連線失敗");
            mainActivity.runOnUiThread(new Runnable() {             //將內容交給UI執行緒做顯示
                public void run() {
                    Toast.makeText(mainActivity, "連線失敗", Toast.LENGTH_SHORT).show();
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
