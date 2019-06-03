package com.uci.eecs40_tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;

public class MainGame extends View {
    private MainActivity mainActivity;
    private NextPieceView nextPieceView;
    private TextView scoreView;
    private TextView gameOverText;
    private Board board;
    private Timer gameTimer = new Timer();
    private int timerPeriod =700;
    private ArrayList<PuzzlePiece> puzzlePieceList;
    private Random randomGenerator = new Random();
    private GestureDetector gestureDetector;
    private int score = 0;


    public MainGame(Context context, NextPieceView nextPieceView, Board board) {
        super(context);

        this.mainActivity = (MainActivity) context;
        this.nextPieceView = nextPieceView;
        this.board = board;
        puzzlePieceList = board.getPuzzlePieceList();
        gestureDetector = new GestureDetector(getContext(),new MyGestureListener());
        scoreView = mainActivity.getScoreText();
        scoreView.append(String.valueOf(score));
        gameOverText = mainActivity.getGameOverText();

        StartGame();
    }

    //start game with a timer
    public void StartGame(){
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mainActivity.runOnUiThread(new TimerTask() {
                    @Override
                    public void run() {
                        if(mainActivity.getGameState() && !isOver()){
                            board.FallDown(board.getCurrentPuzzlePiece());

                            if(!board.checkFallDown(board.getCurrentPuzzlePiece())){// check if need next piece
                                int disapperRow = board.DisappearRows();
                                puzzlePieceList.remove(board.getCurrentPuzzlePiece());
                                puzzlePieceList.add(new PuzzlePiece(randomGenerator.nextInt(board.getPieceNum())+1));
                                nextPieceView.invalidate();

                                if(disapperRow>0){
                                    score = score+ disapperRow*10;
                                    scoreView.setText("Score: "+"  "+String.valueOf(score));
                                }

                            }
                            invalidate();
                        }
                    }
                });
            }
        },0,timerPeriod);
    }

    public void Restart(){ //rest all the value and object
        gameTimer.cancel();
        gameTimer = null;
        puzzlePieceList.clear();
        board.clearBoard();
        board.initialPiece();
        gameOverText.setText("");
        score = 0;
        scoreView.setText("Score: ");
        scoreView.append(String.valueOf(score));
        mainActivity.onRestart();
        gameTimer = new Timer();
        StartGame();
        invalidate();

    }

    public boolean isOver(){
        boolean gameState = board.checkGameState(board.getCurrentPuzzlePiece());
        if(!gameState){
            invalidate();
            gameOverText.setText("LOSE");
            gameOverText.append(" CONTINUE?");
            gameTimer.cancel();

//            board.clearBoard();
            return true;
        }
        return false;
    }

    //draw the game part on board
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        Paint background = new Paint();
        Paint paint = new Paint();
        Paint strokePaint = new Paint();
        Paint strokePaint2 = new Paint();
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint2.setStyle(Paint.Style.STROKE);
        float offsetx = (mainActivity.getBoardViewHeight()/board.getHeight());
        float offsety = (mainActivity.getBoardViewWidth()/board.getWidth());
        int height = board.getHeight();
        int width = board.getWidth();
        for(int i = 0; i<height;i++){
            for(int j = 0; j <width;j++){
                int color = board.getColor(i,j);
                paint.setColor(color);
                background.setColor(Color.parseColor("#042c34"));
                paint.setAntiAlias(true);
                strokePaint2.setAntiAlias(true);
                strokePaint.setAntiAlias(true);
                strokePaint2.setColor(Color.parseColor("#FFFFFF"));
                strokePaint.setStrokeWidth(2);
                strokePaint2.setStrokeWidth(5);
                RectF rect1 = new RectF(j*offsety, i*offsetx,j*offsety+offsety,i*offsetx+offsetx);
                RectF rect2 = new RectF(j*offsety+3, i*offsetx+3,j*offsety+offsety-3,i*offsetx+offsetx-3);
                canvas.drawRect(rect1,background);
                if(board.getBoard()[i][j]>0) {
                    canvas.drawRoundRect(rect1,3,3, paint);
                    canvas.drawRoundRect(rect1,3,3, strokePaint);
                    canvas.drawRoundRect(rect2,5,5,strokePaint2);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        gestureDetector.onTouchEvent(event);
        return true;
    }

    //deal with all the on touch event
    class MyGestureListener implements GestureDetector.OnGestureListener{
        private static final int FLING_MIN_DISTANCE = 50;
        private static final int FLING_MIN_VELOCITY = 0;
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            boolean gameState = mainActivity.getGameState();
            if(gameState){//if the game is on
                board.Rotate(board.getCurrentPuzzlePiece());
                invalidate();
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            board.FastFallDown(board.getCurrentPuzzlePiece());
            invalidate();
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean gameState = mainActivity.getGameState();
            if(gameState) {
                if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                    board.SlideLeft(board.getCurrentPuzzlePiece());
                    invalidate();
                } else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                    board.SlideRight(board.getCurrentPuzzlePiece());
                    invalidate();
                }
            }
            return false;
        }

    }

    public void ButtonDown(){
        if(mainActivity.getGameState()) {
            board.FastFallDown(board.getCurrentPuzzlePiece());
            invalidate();
        }
    }

    public void changeSpeed(){//get slower
        this.timerPeriod = 100;
        gameTimer.cancel();
        gameTimer = new Timer();
        StartGame();
        invalidate();
    }

    public void changeBackSpeed(){//return back
        this.timerPeriod = 700;
        gameTimer.cancel();
        gameTimer = new Timer();
        StartGame();
        invalidate();
    }



}
