package com.example.sssss.replacemouse;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import static com.example.sssss.replacemouse.MainActivity.rightButton;
import static com.example.sssss.replacemouse.MainActivity.context;

/**
 * 設定按鈕參數
 */
public class SetButtonView {
    public SetButtonView() {

    }
/*
    private void setRoot(final int id, final String Temp) {

        //------------------------------------------------------------------------
        View item = LayoutInflater.from(context).inflate(R.layout.set_root, null);
        final LinearLayout L01 = (LinearLayout) item.findViewById(R.id.L01);
        final EditText e01 = (EditText) item.findViewById(R.id.e01);
        e01.setText(rightButton.ViewS[id].data5Code.split(" ")[1]);
        //------------------------------------------------------------------------
        LinearLayout L02 = (LinearLayout) item.findViewById(R.id.L02);
        final EditText e02 = (EditText) item.findViewById(R.id.e02);
        e02.setText(rightButton.ViewS[id].data2Text);
        //------------------------------------------------------------------------
        LinearLayout L03 = (LinearLayout) item.findViewById(R.id.L03);
        final EditText e03 = (EditText) item.findViewById(R.id.e03);
        final EditText e04 = (EditText) item.findViewById(R.id.e04);
        final RadioButton r01 = (RadioButton) item.findViewById(R.id.r01);
        RadioButton r02 = (RadioButton) item.findViewById(R.id.r02);
        r01.setOnClickListener(RadioButton(r02));
        r02.setOnClickListener(RadioButton(r01));
        if (rightButton.ViewS[id].data3Max>0) {
            e03.setText(rightButton.ViewS[id].data3Max);
            e04.setText(rightButton.ViewS[id].data3Min);
            r01.setChecked(true);
        } else {
            r02.setChecked(true);
        }

        //------------------------------------------------------------------------
        LinearLayout L04 = (LinearLayout) item.findViewById(R.id.L04);

        final TextView t01 = (TextView) item.findViewById(R.id.t01);
        final TextView t02 = (TextView) item.findViewById(R.id.t02);
        final TextView t03 = (TextView) item.findViewById(R.id.t03);


        t02.setText(rightButton.ViewS[id].data4Mode);
        if (rightButton.ViewS[id].data4Mode.equals("N")) {
            t03.setText("無");
        } else {
            t03.setText("元件" + rightButton.ViewS[id].data4ChangeId);
        }
        //------------------------------------------------------------------------
        if (rightButton.ViewS[id].data1Type.equals("N")) {
            t01.setText("無");
            L01.setVisibility(View.INVISIBLE);
            L02.setVisibility(View.INVISIBLE);
            L03.setVisibility(View.INVISIBLE);
            L04.setVisibility(View.INVISIBLE);

        } else if (rightButton.ViewS[id].equals("B")) {
            t01.setText("按鈕");
            L04.setVisibility(View.VISIBLE);
            L03.setVisibility(View.INVISIBLE);
        } else if (rightButton.ViewS[id].equals("L")) {
            t01.setText("標籤");
            L03.setVisibility(View.VISIBLE);
            L04.setVisibility(View.INVISIBLE);
        }


        t01.setOnClickListener(tt01(t01, L01, L02, L03, L04));
        t02.setOnClickListener(tt02(t02, t03, id));
        t03.setOnClickListener(tt03(t02, t03));

        new AlertDialog.Builder(context)
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
                    rightButton.ViewS[id].data5Code = rightButton.ViewS[id].data5Code.replaceFirst(rightButton.Code[id].split(" ")[2], "RE");
                    t02.setText("-");
                } else if (t.equals("-")) {
                    t02.setText("c");
                    rightButton.ViewS[id].data5Code= rightButton.ViewS[id].data5Code.replaceFirst(rightButton.Code[id].split(" ")[2], "FF");
                } else if (t.equals("c")) {
                    rightButton.ViewS[id].data5Code= rightButton.ViewS[id].data5Code.replaceFirst(rightButton.Code[id].split(" ")[2], "FF");
                    t02.setText("x");
                    t03.setText("無");
                } else if (t.equals("x")) {
                    t02.setText("+");
                    rightButton.ViewS[id].data5Code= rightButton.ViewS[id].data5Code.replaceFirst(rightButton.Code[id].split(" ")[2], "RE");
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
        Tool.save(context, rightButton.View_[id], "View" + id);

    }

    private void changeCode(final int id, EditText e01) {
        String code_ = rightButton.Code[id];
        rightButton.Code[id] = code_.replaceFirst(code_.split(" ")[1], e01.getText().toString());
        Tool.save(context, rightButton.Code[id], "Code" + id);
    }

    View.OnClickListener RadioButton(final RadioButton r0x) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                r0x.setChecked(false);
            }
        };
    }

    //----------------------------------------------------------------------

*/
}
