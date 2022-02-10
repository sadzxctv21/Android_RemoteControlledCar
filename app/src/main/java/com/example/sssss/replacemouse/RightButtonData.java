package com.example.sssss.replacemouse;


import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import static com.example.sssss.replacemouse.MainActivity.context;
import static com.example.sssss.replacemouse.MainActivity.readData;
import static com.example.sssss.replacemouse.MainActivity.setToolView;
import static com.example.sssss.replacemouse.MainActivity.rightButton;

public class RightButtonData {

    public RightButtonData(){

    }

    /**
     * 元件類型 B:按鈕 L:標籤 N:無
     */
    String data1Type = "N";
    /**
     * 按鈕
     */
    final static String data1Button = "B";
    /**
     * 標籤
     */
    final static String data1Label = "L";
    /**
     * 無
     */
    final static String data1None = "N";

    //-----------------------------------------------------------------------
    /**
     * 顯示文字
     */
    String data2Text = "";
    //-----------------------------------------------------------------------
    /**
     * 計數最大值
     */
    Integer data3Max = 10;
    /**
     * 計數最小值
     */
    Integer data3Min = 0;
    /**
     * 計數
     */
    Integer data3Count = 0;
    private Integer Calculation(boolean ad_re) {
        RightButtonData t=rightButton.ViewS[data4ChangeId-1];
        if (ad_re) {
            t.data3Count++;
        } else {
            t.data3Count--;
        }
        if (t.data3Count >t.data3Max) {
            t.data3Count = t.data3Max;
        } else if (t.data3Count <t.data3Min) {
            t.data3Count =t.data3Min;
        }
        t.View.setText(t.data3Count + "");

        return t.data3Count;

    }
    //-----------------------------------------------------------------------
    /**
     * 觸發機制 N:無 C:改變文字 +:增加計數 -:增加計數
     */

    String data4Mode = "";
    final static String data4None= "N";
    final static String data4Change = "C";
    final static String data4Add = "+";
    final static String data4Re = "-";

    /**
     * 被改變的元件ID
     */
    Integer data4ChangeId = 0;
    //-----------------------------------------------------------------------

    /**
     * 回傳指令
     */
    String data5Code = "";
    //-----------------------------------------------------------------------
    TextView View;
    public void setView(String data1Type,String data2Text
            ,Integer data3Max,Integer data3Min,Integer data3Count
            ,String data4Mode,Integer data4ChangeId
            ,String data5Code){
        this.data1Type=data1Type;
        this.data2Text=data2Text;
        this.data3Max=data3Max;
        this.data3Min=data3Min;
        this.data3Count=data3Count;
        this.data4Mode=data4Mode;
        this.data4ChangeId=data4ChangeId;
        this.data5Code=data5Code;
        View = new TextView(context);
        View.setText(data2Text);
        View.setTextSize(64);
        View.setGravity(Gravity.CENTER);
        View.setTextColor(Color.parseColor("#FFFFFF"));
        View.setBackground(context.getResources().getDrawable(R.drawable.ic_black));
        if (data1Type.equals(data1Button)) {//按鈕
            View.setBackground(context.getResources().getDrawable(R.drawable.ic_imageu1));
        } else if (data1Type.equals(data1Label)) {
            //----------
        }
        View.setOnTouchListener(textViewOnTouchListener());
    }

    View.OnTouchListener textViewOnTouchListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                String Code_t = "";
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {  //按下的時候改變背景及顏色

                    if (setToolView.root.equals("ON")) {  //開發模式:開
                        //setButtonView.setRoot(id, View_[id]);
                    } else {//開發模式:關
                        Integer Count=0;
                        if (data1Type.equals(data1Button)) {

                            View.setBackground(context.getResources().getDrawable(R.drawable.ic_imageu2));
                            if (data4Mode.equals(data4Add)) {
                                Count=Calculation( true);
                            } else if (data4Mode.equals(data4Re)) {
                                Count=Calculation( false);

                            } else if (data4Mode.equals(data4Change)) {
                                rightButton.ViewS[data4ChangeId-1].View.setText(data2Text);
                            }
                            Code_t = data5Code.replace("RE",Count + "");
                            Log.d("qqqqqqq",Code_t);
                            readData.sendData(Code_t);
                        }
                    }
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {  //按下的時候改變背景及顏色
                    if (data1Type.equals(data1Button)) {
                        View.setBackground(context.getResources().getDrawable(R.drawable.ic_imageu1));
                    } else if (data1Type.equals(data1Label)) {//計數
                        //      code = Code.replace("RE", Integer.toHexString(i01) + "");
                    }
                    return true;
                }




                return true;
            }
        };
    }
}
