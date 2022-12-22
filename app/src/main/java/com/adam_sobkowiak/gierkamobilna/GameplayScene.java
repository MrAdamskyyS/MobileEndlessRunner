package com.adam_sobkowiak.gierkamobilna;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

public class GameplayScene implements Scene{

    private Rect r = new Rect();
    private RectPlayer player;
    private Point playerPoint;

    private ObstacleManager obstacleManager;

    private boolean movingPLayer = false;
    private boolean gameOver = false;
    private long gameOverTime;

    private OrientationData orientationData;
    private long frameTime;

    public GameplayScene(){
        player = new RectPlayer(new Rect(100,100,200,300), Color.rgb(255,0,0));
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, (int)(3*Constants.SCREEN_HEIGHT/3.5));
        player.Update(playerPoint);

        obstacleManager = new ObstacleManager(350, 700, 75, Color.WHITE);
        orientationData = new OrientationData();
        orientationData.register();
        frameTime = System.currentTimeMillis();
    }

    public void reset(){
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, (int)(3*Constants.SCREEN_HEIGHT/3.5));
        player.Update(playerPoint);
        obstacleManager = new ObstacleManager(350, 700, 75, Color.WHITE);
        movingPLayer = false;
    }

    @Override
    public void terminate() {
        SceneManager.ACTIVE_SCENE = 0;
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(!gameOver && player.getRectangle().contains((int)event.getX(), (int)(3*Constants.SCREEN_HEIGHT/3.5)));
                movingPLayer = true;
                if(gameOver && System.currentTimeMillis() - gameOverTime >= 2000){
                    reset();
                    gameOver = false;
                    orientationData.newGame();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(!gameOver && movingPLayer) {
                    playerPoint.set((int) event.getX(), (int)(3*Constants.SCREEN_HEIGHT/3.5));
                }
                break;
            case MotionEvent.ACTION_UP:
                movingPLayer = false;
                break;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.GRAY);
        player.draw(canvas);
        obstacleManager.draw(canvas);

        if(gameOver){
            Paint paint = new Paint();
            paint.setTextSize(200);
            paint.setColor(Color.RED);
            drawCenterText(canvas, paint, "Przegrałeś!");
        }
    }

    @Override
    public void Update() {
        if(!gameOver) {
            if(frameTime < Constants.INIT_TIME){
                frameTime = Constants.INIT_TIME;
            }
            int elapsedTime = (int)(System.currentTimeMillis() - frameTime);
            frameTime = System.currentTimeMillis();
            if(orientationData.getOrientation() != null && orientationData.getStartOrientation() != null){
                float pitch = orientationData.getOrientation()[1] - orientationData.getStartOrientation()[1];
                float roll = orientationData.getOrientation()[2] - orientationData.getStartOrientation()[2];

                float xSpeed = 2*roll*Constants.SCREEN_WIDTH/1000f;

                playerPoint.x += Math.abs(xSpeed*elapsedTime) > 5 ? xSpeed*elapsedTime : 0;
            }

            if(playerPoint.x < 0){
                playerPoint.x = 0;
            } else if(playerPoint.x > Constants.SCREEN_WIDTH){
                playerPoint.x = Constants.SCREEN_WIDTH;
            }

            player.Update(playerPoint);
            obstacleManager.Update();
            if(obstacleManager.playerCollide(player)){
                gameOver = true;
                gameOverTime = System.currentTimeMillis();
            }
        }
    }

    private void drawCenterText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }
}
