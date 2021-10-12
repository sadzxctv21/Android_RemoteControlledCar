package com.example.sssss.replacemouse;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class ImageViewAll extends View implements Runnable {
    public ImageViewAll(Context context){
        super(context);
        init();
    }
    public ImageViewAll(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }
    Paint White,Black;
    //省略构造方法
    private void init() {
        White = new Paint();
        Black=new Paint();

        White.setColor(Color.rgb(255, 255, 255));
        White.setStrokeWidth(5);

        Black.setColor(Color.rgb(0, 0, 0));
        Black.setStrokeWidth(5);

        Colour=White;
        Colour2=Black;

        new Thread(this).start();
    }
    int height=0,width=0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        height = this.getHeight();
        width = this.getWidth();

        if (X1==0&Y1==0){
            X1=width/2;
            Y1=height/2;
        }


        canvas.drawColor(Color.BLACK);//设置画布背景颜色
        //    Log.d("xxxxxxxx",width+" "+height);
        switch (mod){
            case 0:
                if (width > height) {
                    special0(width / 2, height / 2, height / 3, canvas);
                } else {
                    special0(width / 2, height / 2, width / 3, canvas);
                }
                break;
            case 1:
                if (width > height) {
                    special1(width / 2, height / 2, height / 4 - height / 10, canvas);
                } else {
                    special1(width / 2, height / 2, width / 4 - width / 10, canvas);
                }
                break;
            case 2:
                if (width > height) {
                    special2(width / 2, height / 2, height / 4 - height / 10, canvas);
                } else {
                    special2(width / 2, height / 2, width / 4 - width / 10, canvas);
                }
                break;
            case 3:
                if (width > height) {
                    special3(width / 2, height / 2, height / 4 - height / 10, canvas);
                } else {
                    special3(width / 2, height / 2, width / 4 - width / 10, canvas);
                }
                break;
            case 4:
                if (width > height) {
                    special4(width / 2, height / 2, height / 4 - height / 10, canvas);
                } else {
                    special4(width / 2, height / 2, width / 4 - width / 10, canvas);
                }
                break;
        }

    }

    Paint Colour,Colour2;
    boolean UpdateView=false;
    boolean down=false;
    public void changeColour(boolean down){
        this.down=down;
        if (down==true){
            Colour =Black;
            Colour2 =White;
            UpdateView=true;
        }
    }
    int mod=-1;
    boolean InitialSet=false;//t初始設定
    public void setMod(int mod){
        this.mod=mod;
        UpdateView=true;
    }
    float X1=0,Y1=0;
    public void setX1Y1(float X1,float Y1){
    //    Log.d("zzzzzzzzzz","X1:"+X1+"  Y1:"+Y1);
        this.X1=X1;
        this.Y1=Y1;
    }

    private void special0(float X, float Y, int R,Canvas canvas) {
        for (float a = -6.28f; a < 6.28f; a = a + 0.01f) {
            canvas.drawPoint((float) Math.cos(a) * R + X, (float) Math.sin(a) * R + Y, White);
        }
        canvas.drawCircle(X1, Y1, R/2, White);
    }

    private void special1(float X, float Y, int a,Canvas canvas) {

        canvas.drawCircle(X,Y,a*2,Colour2);
        for (float b = -6.28f; b < 6.28f; b = b + 0.01f) {
            canvas.drawPoint((float) Math.cos(b) * a * 2 + X, (float) Math.sin(b) * a * 2 + Y, Colour);
        }
        canvas.drawLine(X - a / 2 * (float) Math.sqrt(2), Y - a / 2 * (float) Math.sqrt(2), X + a / 2 * (float) Math.sqrt(2), Y - a / 2 * (float) Math.sqrt(2), Colour);//上
        canvas.drawLine(X - a / 2 * (float) Math.sqrt(2), Y + a / 2 * (float) Math.sqrt(2), X + a / 2 * (float) Math.sqrt(2), Y + a / 2 * (float) Math.sqrt(2), Colour);//下
        canvas.drawLine(X - a / 2 * (float) Math.sqrt(2), Y - a / 2 * (float) Math.sqrt(2), X - a / 2 * (float) Math.sqrt(2), Y + a / 2 * (float) Math.sqrt(2), Colour);//左
        canvas.drawLine(X + a / 2 * (float) Math.sqrt(2), Y - a / 2 * (float) Math.sqrt(2), X + a / 2 * (float) Math.sqrt(2), Y + a / 2 * (float) Math.sqrt(2), Colour);//右
    }

    private void special2(float X, float Y, int a,Canvas canvas) {
        canvas.drawCircle(X,Y,a*2,Colour2);
        for (float b = -6.28f; b < 6.28f; b = b + 0.01f) {
            canvas.drawPoint((float) Math.cos(b) * a*2 + X, (float) Math.sin(b) * a*2 + Y, Colour);
        }
        canvas.drawLine(X, Y - a, X - a / 2 * (float) Math.sqrt(3), Y + a / 2, Colour);//左上
        canvas.drawLine(X, Y - a, X + a / 2 * (float) Math.sqrt(3), Y + a / 2, Colour);//右上
        canvas.drawLine(X - a / 2 * (float) Math.sqrt(3), Y + a / 2
                , X + a / 2 * (float) Math.sqrt(3), Y + a / 2, Colour);//下

    }

    private void special3(float X, float Y, int a,Canvas canvas) {
        canvas.drawCircle(X,Y,a*2,Colour2);
        for (float b = -6.28f; b < 6.28f; b = b + 0.01f) {
            canvas.drawPoint((float) Math.cos(b) * a*2 + X, (float) Math.sin(b) * a *2+ Y, Colour);
        }
        for (float b = -6.28f; b < 6.28f; b = b + 0.01f) {
            canvas.drawPoint((float) Math.cos(b) * a + X, (float) Math.sin(b) * a + Y, Colour);
        }
    }

    private void special4(float X, float Y, int a,Canvas canvas) {
        canvas.drawCircle(X,Y,a*2,Colour2);
        for (float b = -6.28f; b < 6.28f; b = b + 0.01f) {
            canvas.drawPoint((float) Math.cos(b) * a *2+ X, (float) Math.sin(b) * a*2 + Y,Colour);
        }
        canvas.drawLine(X - a/ 2 * (float) Math.sqrt(2) , Y - a/ 2 * (float) Math.sqrt(2) , X + a/ 2 * (float) Math.sqrt(2) , Y + a/ 2 * (float) Math.sqrt(2) ,Colour);
        canvas.drawLine(X - a/ 2 * (float) Math.sqrt(2) , Y + a / 2 * (float) Math.sqrt(2), X + a/ 2 * (float) Math.sqrt(2) , Y - a/ 2 * (float) Math.sqrt(2) , Colour);
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(10);
                if (UpdateView){
         ///         Log.d("aaaaaaa","aaaaaaaaaa");
                    postInvalidate();
                } if ( Colour ==Black&UpdateView==true&down==false){
                    Thread.sleep(10);
                    Colour =White;
                    Colour2 =Black;
                    postInvalidate();
                    UpdateView=false;
                }
                if (mod!=-1&InitialSet==false){
                    UpdateView=false;
                    InitialSet=true;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            // 使用postInvalidate可以直接在线程中更新界面
        }
    }

}
