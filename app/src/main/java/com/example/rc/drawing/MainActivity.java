package com.example.rc.drawing;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends Activity {

    DrawingView dv ;
    private Paint mPaint;
   // private DrawingManager mDrawingManager=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dv = new DrawingView(this);
        setContentView(dv);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);
    }

    public class DrawingView extends View {

        public int width;
        public  int height;
        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Path mPath;
        private Paint   mBitmapPaint;
        Context context;
        private Paint circlePaint;
        private Path circlePath;
        int count=0;

        public DrawingView(Context c) {
            super(c);
            context=c;
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
            circlePaint = new Paint();
            circlePath = new Path();
            circlePaint.setAntiAlias(true);
            circlePaint.setColor(Color.BLUE);
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setStrokeJoin(Paint.Join.MITER);
            circlePaint.setStrokeWidth(4f);


        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);

        }
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);

            canvas.drawPath( mPath,  mPaint);

            canvas.drawPath( circlePath,  circlePaint);
        }

        private float mX, mY, botX, botY;
        private static final float TOUCH_TOLERANCE = 4;
        private final float length_box=30, height_box=30;
        private String msg="";

        private void touch_start(float x, float y) {
            msg="";
            mCanvas.drawColor(Color.YELLOW);
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
            botX=x;
            botY=y;

        }
        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);

            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);

                Log.d("mX & mY: ", mX + "&" + mY);
                Log.d("x & y: ", x + "&" + y);

                mX = x;
                mY = y;

                circlePath.reset();
                circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
            }

            float bot_dx = x-botX;
            float bot_dy = botY-y;

            if (Math.abs(bot_dx) >= length_box && Math.abs(bot_dy) < height_box) {


                if(bot_dx>0) {
                if (Math.abs(bot_dy) >= height_box / 2) {
                    if (bot_dy > 0) {
                        msg = msg + " 2";
                    botX=x;  botY=y;
                    }
                    else {
                        msg = msg + " 4";
                        botX=x;  botY=y;
                    }
                } else {
                    msg = msg + " 3";
                    botX=x;
                }
                }
                else {
                    if (Math.abs(bot_dy) >= height_box / 2) {
                        if (bot_dy > 0) {
                            msg = msg + " 8";
                            botX=x;  botY=y;
                        }
                        else {
                            msg = msg + " 6";
                            botX=x;  botY=y;
                        }
                    } else {
                        msg = msg + " 7";
                        botX=x;
                    }

                }
            }

            else if (Math.abs(bot_dy) >= height_box && Math.abs(bot_dx) < length_box ) {

                botY = y;
                if (bot_dy > 0) {
                    if (Math.abs(bot_dx) >= length_box / 2) {
                        if (bot_dx > 0) {
                            msg = msg + " 2";
                            botX=x;  botY=y;
                        }
                        else {
                            msg = msg + " 8";
                            botX=x;  botY=y;
                        }
                    } else {
                        msg = msg + " 1";
                      botY=y;
                    }
                }
                else {
                    if (Math.abs(bot_dx) >= length_box / 2) {
                        if (bot_dx > 0) {
                            msg = msg + " 4";
                            botX=x;  botY=y;
                        }
                        else {
                            msg = msg + " 6";
                            botX=x;  botY=y;
                        }
                    } else {
                        msg = msg + " 5";
                          botY=y;
                    }
                }
            }
            else if (Math.abs(bot_dy) >= height_box && Math.abs(bot_dx) >= length_box){
                if(bot_dy>0 && bot_dx>0) msg = msg + " 2";
                if(bot_dy>0 && bot_dx<0) msg = msg + " 8";
                if(bot_dy<0 && bot_dx>0) msg = msg + " 4";
                if(bot_dy<0 && bot_dx<0) msg = msg + " 6";
            }

        }
        private void touch_up() {
            mPath.lineTo(mX, mY);
            circlePath.reset();
            // commit the path to our offscreen
            mCanvas.drawPath(mPath,  mPaint);
            // kill this so we don't double draw
            mPath.reset();
           // mCanvas.drawColor(Color.BLACK);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            //count++;

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    count++;
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    Log.i("Action_UP ", count + "");
                    Log.i("Message ",msg);
                    count=0;
                    break;
            }
            return true;
        }
    }
}