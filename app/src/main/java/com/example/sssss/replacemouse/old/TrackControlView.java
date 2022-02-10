package com.example.sssss.replacemouse.old;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.sql.Array;
import java.util.ArrayList;

public class TrackControlView extends View implements Runnable {
    public TrackControlView(Context context) {
        super(context);
        init();
    }


    public TrackControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }
    Paint mLine,mRed;//粗線
    int WidthO=4958;
    int HeightO=1796;
    float proportion=0;//比例
    int x01=216,x02=216,x03=1857,x04=1857,x05=2235,x06=2235,x07=4759,x08=4759,x09=216,x10=216,x11=4759,x12=4759;
    int y01=0,y02=685,y03=685,y04=0,y05=0,y06=685,y07=685,y08=0,y09=1796,y10=1071,y11=1071,y12=1796;
    int x[]={216,216,1857,1857,2235,2235,4759,4759,216,216,4759,4759};
    int y[]={0,685,685,0,0,685,685,0,1796,1071,1071,1796};
    private void init() {
        mLine = new Paint();
        mLine.setColor(Color.rgb(0, 0, 0));
        mLine.setStrokeWidth(10);
        mRed = new Paint();
        mRed.setColor(Color.rgb(168, 0, 0));
        mRed.setStrokeWidth(10);
        new Thread(this).start();

    }
    int W=0,H=0;
    private void BackGround(Canvas canvas) {
        int h=getHeight();
        W=getWidth();
        H=getHeight();
        if (proportion==0){

        proportion=(float)h/(float) HeightO;
        Log.d("aaaaazz",proportion+"   aaaa"+W+"  "+H);


    }
        BasicX=BasicX+mobileX;
        BasicY=BasicY+mobileY;
    //    Log.d("aaaaa",BasicX+"   "+BasicY);
        mobileX=0;
        mobileY=0;

        if (!(BasicX>-1)){
            BasicX=0;
        }
        if (!(BasicX+W<(WidthO*proportion))){
            BasicX=WidthO*proportion-W;
        }
        if (!(BasicY>-1)){
            BasicY=0;
        }
        if (!(BasicY+H<(HeightO*proportion))){
            BasicY=HeightO*proportion-H;
        }
            JudgeS(canvas,BasicX,BasicY);
        ForbidTrackS(canvas,BasicX,BasicY);
        for (int a=0;a<CarX.size();a++){
            track(CarX.get(a)-BasicX,CarY.get(a)-BasicY,112*proportion,canvas);
        }

        outset(canvas);



    }

    private void outset(Canvas canvas){
        canvas.drawLine((2046-50)*proportion-BasicX,(100-50)*proportion-BasicY
                ,(2046+50)*proportion-BasicX,(100+50)*proportion-BasicY,mLine);
        canvas.drawLine((2046+50)*proportion-BasicX,(100-50)*proportion-BasicY
                ,(2046-50)*proportion-BasicX,(100+50)*proportion-BasicY,mLine);

    }

    //-------------------------------------------------------------------------
    int ForbidDistance=70;//禁止距離
    private void  ForbidCoordinate(){

    }//禁止座標

    private void ForbidTrackS(Canvas canvas,float X,float Y) {
        for (int a = 0; a < 8; a = a + 4) {
            ForbidTrack(canvas, (x[a + 0] - ForbidDistance) * proportion, (y[a + 0] - ForbidDistance) * proportion, (x[a + 1] - ForbidDistance) * proportion, (y[a + 1] + ForbidDistance) * proportion, X, Y);//1
            ForbidTrack(canvas, (x[a + 1] - ForbidDistance) * proportion, (y[a + 1] + ForbidDistance) * proportion, (x[a + 2] + ForbidDistance) * proportion, (y[a + 2] + ForbidDistance) * proportion, X, Y);//1
            ForbidTrack(canvas, (x[a + 2] + ForbidDistance) * proportion, (y[a + 2] + ForbidDistance) * proportion, (x[a + 3] + ForbidDistance) * proportion, (y[a + 3] - ForbidDistance) * proportion, X, Y);//1
        }
        for (int a = 8; a < 12; a = a + 4) {
            ForbidTrack(canvas, (x[a + 0] - ForbidDistance) * proportion, (y[a + 0] + ForbidDistance) * proportion, (x[a + 1] - ForbidDistance) * proportion, (y[a + 1] - ForbidDistance) * proportion, X, Y);//1
            ForbidTrack(canvas, (x[a + 1] - ForbidDistance) * proportion, (y[a + 1] - ForbidDistance) * proportion, (x[a + 2] + ForbidDistance) * proportion, (y[a + 2] - ForbidDistance) * proportion, X, Y);//1
            ForbidTrack(canvas, (x[a + 2] + ForbidDistance) * proportion, (y[a + 2] - ForbidDistance) * proportion, (x[a + 3] + ForbidDistance) * proportion, (y[a + 3] + ForbidDistance) * proportion, X, Y);//1
        }
    }

    private void ForbidTrack(Canvas canvas,float x1,float y1,float x2,float y2,float X,float Y){
        canvas.drawLine(x1-X,y1-Y,x2-X,y2-Y,mRed);
    }
    //-------------------------------------------------------------------------

    float BasicX=0,BasicY=0;
    private void JudgeS(Canvas canvas,float X,float Y){
        for (int a=0;a<12;a=a+4) {
            Judge(canvas, x[a + 0] * proportion, y[a + 0] * proportion, x[a + 1] * proportion, y[a + 1] * proportion, X, Y);//1
            Judge(canvas, x[a + 1] * proportion, y[a + 1] * proportion, x[a + 2] * proportion, y[a + 2] * proportion, X, Y);//2
            Judge(canvas, x[a + 2] * proportion, y[a + 2] * proportion, x[a + 3] * proportion, y[a + 3] * proportion, X, Y);//3
        }
    }
    private void Judge(Canvas canvas,float x1,float y1,float x2,float y2,float X,float Y){

        if (W<x2){
     //      x2= W;
        }
        if (H<y2){
      //      y2= H;
        }
        if (W>x1){
      //      x1=0;
        }
        if (W>y1){
       //     y1= 0;
        }
        canvas.drawLine(x1-X,y1-Y,x2-X,y2-Y,mLine);
    }
    public void track(float X, float Y, float R,Canvas canvas) {
        canvas.drawCircle(X, Y, R/2, mLine);
     //   for (float a = -6.28f; a < 6.28f; a = a + 0.01f) {
         //   canvas.drawPoint((float) Math.cos(a) * R + X, (float) Math.sin(a) * R + Y, mLine);
     //   }
    }

    float mobileX,mobileY;
    public void  enter(float mobileX,float mobileY){
        this.mobileX=mobileX;
        this.mobileY=mobileY;
        Log.d("aaaaaa",mobileX+"   "+mobileY);
    }
    //----------------------------------------------
    ArrayList<Float> CarX=new ArrayList<Float>();
    ArrayList<Float> CarY=new ArrayList<Float>();
    public void  enter2(float X,float Y){
        CarX.add(X+BasicX);
        CarY.add(Y+BasicY);
        Log.d("CarTrack",(X+BasicX)/proportion+"   "+(Y+BasicY)/proportion);
    }
    public void  enter3(float proportion){
        this.proportion=  this.proportion+proportion;
        Log.d("CarTrack",proportion+"");
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(100);
                postInvalidate();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            // 使用postInvalidate可以直接在线程中更新界面
        }
    }
    Canvas canvas;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        BackGround(canvas);

    }

}
