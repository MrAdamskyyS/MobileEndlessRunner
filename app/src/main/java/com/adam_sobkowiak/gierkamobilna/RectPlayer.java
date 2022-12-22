package com.adam_sobkowiak.gierkamobilna;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class RectPlayer implements GameObject{

    private Rect rectangle;
    private int color;

    private Animation driveFWD;
    private Animation turnLeft;
    private Animation turnRight;
    private AnimationManager animManager;

    public Rect getRectangle(){
        return rectangle;
    }

    public RectPlayer(Rect rectangle, int color){
        this.rectangle = rectangle;
        this.color = color;

        BitmapFactory bf = new BitmapFactory();
        Bitmap drive = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.carfwdidle);
        Bitmap turnL = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.carleft);
        Bitmap turnR = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.carright);

        driveFWD = new Animation(new Bitmap[]{drive}, 2);
        turnLeft = new Animation(new Bitmap[]{turnL}, 0.5f);
        turnRight = new Animation(new Bitmap[]{turnR}, 0.5f);

        animManager = new AnimationManager(new Animation[]{driveFWD, turnRight, turnLeft});
    }

    @Override
    public void draw(Canvas canvas){
        animManager.draw(canvas,rectangle);
    }

    @Override
    public void Update(){
        animManager.update();
    }

    public void Update(Point point){
        rectangle.set(point.x - rectangle.width()/2, point.y - rectangle.height()/2, point.x + rectangle.width()/2, point.y + rectangle.height()/2);

        float oldLeft = rectangle.left;
        int state = 0;
        if(rectangle.left - oldLeft > 1){
            state = 1;
        } else if (rectangle.left - oldLeft < -1){
            state = 2;
        }

        animManager.playAnim(state);
        animManager.update();
    }
}
