package com.example.sssss.replacemouse;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import static com.example.sssss.replacemouse.MainActivity.context;
import static com.example.sssss.replacemouse.MainActivity.mainActivity;
import static com.example.sssss.replacemouse.MainActivity.readData;
import static com.example.sssss.replacemouse.MainActivity.setToolView;
import static com.example.sssss.replacemouse.MainActivity.rightButton;

/**
 * 設定左區域按鈕
 */
public class LeftButton {
    Context context;
    ImageViewAll imageView0;
    TextView textView;

    public LeftButton() {

        imageView0 = (ImageViewAll)mainActivity.findViewById(R.id.image0);
        imageView0.setOnTouchListener(ButImage(imageView0));
        imageView0.setMod(0);
        textView = (TextView) mainActivity.findViewById(R.id.textView);
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
                        if (!setToolView.SendDataMode) {
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
                    if (count % setToolView.UpData == 0) {
                        count = 0;
                        code = "A5 20 " + readData.Conversion(bX) + " 30 " + readData.Conversion(bY) + " FA";
                        if (!setToolView.SendDataMode) {
                            readData.sendData(code);
                        }

                    }
                    textView.setText("搖桿紀錄:" + "向右" + bX + "%  " + "向上" + bY + "% ");
                } else {
                    if (bY < -100) {
                        bY = -100;
                    }
                    if (count % setToolView.UpData == 0) {
                        count = 0;
                        code = "A5 20 " + readData.Conversion(bX) + " 40 " + readData.Conversion(-bY) + " FA";
                        if (!setToolView.SendDataMode) {
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
                    if (count % setToolView.UpData == 0) {
                        count = 0;
                        code = "A5 10 " + readData.Conversion(-bX) + " 30 " + readData.Conversion(bY) + " FA";
                        if (!setToolView.SendDataMode) {
                            readData.sendData(code);
                        }

                    }
                    textView.setText("搖桿紀錄:" + "向左" + -bX + "%  " + "向上" + bY + "% ");
                } else {
                    if (bY < -100) {
                        bY = -100;
                    }
                    if (count % setToolView.UpData == 0) {
                        count = 0;
                        code = "A5 10 " + readData.Conversion(-bX) + " 40 " + readData.Conversion(-bY) + " FA";
                        if (!setToolView.SendDataMode) {
                            readData.sendData(code);
                        }

                    }
                    textView.setText("搖桿紀錄:" + "向左" + -bX + "%  " + "向下" + -bY + "% ");
                }
            }
        } else {
            bX = (int) (eventX - ViewW);
            bY = -(int) (eventY - ViewH);
            if (setToolView.CutInHalf) {
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
                    if (count % setToolView.UpData == 0) {
                        count = 0;
                        code = "A5 20 " + readData.Conversion(bX) + " 30 " + readData.Conversion(bY) + " FA";
                        if (!setToolView.SendDataMode) {
                            readData.sendData(code);
                        }

                    }
                    textView.setText("搖桿紀錄:" + "向右" + bX + "(" + (int) (MaxX - ViewW) + ")" + "向上" + bY + "(" + (int) -(MaxY - ViewH) + ")");
                } else {
                    if (bY < -(int) MaxY) {
                        bY = -(int) MaxY;
                    }
                    if (count % setToolView.UpData == 0) {
                        count = 0;
                        code = "A5 20 " + readData.Conversion(bX) + " 40 " + readData.Conversion(-bY) + " FA";
                        if (!setToolView.SendDataMode) {
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
                    if (count % setToolView.UpData == 0) {
                        count = 0;
                        code = "A5 10 " + readData.Conversion(-bX) + " 30 " + readData.Conversion(bY) + " FA";
                        if (!setToolView.SendDataMode) {
                            readData.sendData(code);
                        }

                    }
                    textView.setText("搖桿紀錄:" + "向左" + -bX + "(" + (int) -(MaxX - ViewW) + ")" + "向上" + bY + "(" + (int) -(MaxY - ViewH) + ")");
                } else {
                    if (bY < -(int) MaxY) {
                        bY = -(int) MaxY;
                    }
                    if (count % setToolView.UpData == 0) {
                        count = 0;
                        code = "A5 10 " + readData.Conversion(-bX) + " 40 " + readData.Conversion(-bY) + " FA";
                        if (!setToolView.SendDataMode) {
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
