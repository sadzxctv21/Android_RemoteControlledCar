package com.example.sssss.replacemouse;

import static com.example.sssss.replacemouse.MainActivity.context;
import static com.example.sssss.replacemouse.MainActivity.mainActivity;
import static com.example.sssss.replacemouse.MainActivity.readData;

import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;

public class RightButton {
    public RightButton() {
        if(loadData()==false){
            setinitializationView();
        }


        LinearLayout L01 = (LinearLayout) mainActivity.findViewById(R.id.L01);
        LinearLayout L02 = (LinearLayout) mainActivity.findViewById(R.id.L02);
        LinearLayout L03 = (LinearLayout) mainActivity.findViewById(R.id.L03);
        setL0X(L01, 0, 1, 2);
        setL0X(L02, 3, 4, 5);
        setL0X(L03, 6, 7, 8);
    }

    //設定初始化元件
    private void setinitializationView() {
        String JsonS = "[";
        //-------1------------------------------
        JsonS += setJson(0, RightButtonData.data1None, ""
                , 0, 0, 0
                , RightButtonData.data4None, 0
                , "A5 50 FF FF FF FA") + ",";
        //-------2------------------------------
        JsonS += setJson(1, RightButtonData.data1Button, "△"
                , 0, 0, 0
                , RightButtonData.data4Add, 5
                , "A5 70 RE FF FF FA") + ",";
        //-------3------------------------------
        JsonS += setJson(2, RightButtonData.data1Button, "U"
                , 0, 0, 0
                , RightButtonData.data4Change, 6
                , "A5 81 FF FF FF FA") + ",";
        //-------4------------------------------
        JsonS += setJson(3, RightButtonData.data1Button, "☐"
                , 0, 0, 0
                , RightButtonData.data4None, 0
                , "A5 60 FF FF FF FA") + ",";
        //-------5------------------------------
        JsonS += setJson(4, RightButtonData.data1Label, "1"
                , 10, 0, 1
                , RightButtonData.data4None, 0
                , "A5 50 FF FF FF FA") + ",";
        //-------6------------------------------
        JsonS += setJson(5, RightButtonData.data1Label, "U"
                , 0, 0, 0
                , RightButtonData.data4None, 0
                , "A5 50 FF FF FF FA") + ",";
        //-------7------------------------------
        JsonS += setJson(6, RightButtonData.data1None, ""
                , 0, 0, 0
                , RightButtonData.data4None, 0
                , "A5 50 FF FF FF FA") + ",";
        //-------8------------------------------
        JsonS += setJson(7, RightButtonData.data1Button, "▽"
                , 0, 0, 0
                , RightButtonData.data4Re, 5
                , "A5 90 RE FF FF FA") + ",";
        //-------9------------------------------
        JsonS += setJson(8, RightButtonData.data1Button, "W"
                , 0, 0, 0
                , RightButtonData.data4Change, 6
                , "A5 80 FF FF FF FA");
        JsonS += "]";

        View = JsonS;
        Log.d("xxxxx", JsonS);

    }

    private String setJson(Integer id, String data1Type, String data2Text
            , Integer data3Max, Integer data3Min, Integer data3Count
            , String data4Mode, Integer data4ChangeId
            , String data5Code) {
        String json = "{";
        json += "\"data1Type\":" + "\"" + data1Type + "\",";
        json += "\"data2Text\":" + "\"" + data2Text + "\",";
        json += "\"data3Max\":" + data3Max + ",";
        json += "\"data3Min\":" + data3Min + ",";
        json += "\"data3Count\":" + data3Count + ",";
        json += "\"data4Mode\":" + "\"" + data4Mode + "\",";
        json += "\"data4ChangeId\":" + data4ChangeId + ",";
        json += "\"data5Code\":" + "\"" + data5Code + "\"";
        json += "}";
        View = json;
        Tool.save(context, json, "View" + id);

        ViewS[id] = new RightButtonData();
        ViewS[id].setView(data1Type, data2Text
                , data3Max, data3Min, data3Count
                , data4Mode, data4ChangeId, data5Code);
        return json;

    }

    String View = "";

    private Boolean loadData() {
        try {
            for (int a = 0; a < 9; a++) {
                String json = Tool.read(context, "View" + a);
                if(json.length()==0){
                    return false;
                }
                JSONObject jsonObject = new JSONObject(json);
                String data1Type = jsonObject.getString("data1Type");
                String data2Text = jsonObject.getString("data2Text");
                Integer data3Max = jsonObject.getInt("data3Max");
                Integer data3Min = jsonObject.getInt("data3Min");
                Integer data3Count = jsonObject.getInt("data3Count");
                String data4Mode = jsonObject.getString("data4Mode");
                Integer data4ChangeId = jsonObject.getInt("data4ChangeId");
                String data5Code = jsonObject.getString("data5Code");
                ViewS[a] = new RightButtonData();
                ViewS[a].setView(data1Type, data2Text
                        , data3Max, data3Min, data3Count
                        , data4Mode, data4ChangeId, data5Code);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    RightButtonData[] ViewS = new RightButtonData[9];

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
        L0x.addView(ViewS[a].View, param1);
        L0x.addView(ViewS[b].View, param1);
        L0x.addView(ViewS[c].View, param1);
    }

}
