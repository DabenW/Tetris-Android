package com.uci.eecs40_tetris;

import android.graphics.Color;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private Button Start;
    private Button Restart;
    private ImageButton downBotton;
    private TextView scoreText;
    private TextView gameOverText;
    private NextPieceView nextPiece;
    private MainGame tetrisView;
    private Board board;
    private boolean gameState = false; //if ture game start, false game not start
    private int boardViewHeight;
    private int boardViewWidth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        board = new Board();


        Start = findViewById(R.id.buttonstart);
        Restart = findViewById(R.id.buttonrestart);
        downBotton = findViewById(R.id.buttonDown);
        scoreText = findViewById(R.id.textScore);
        gameOverText = findViewById(R.id.GameOver);

        nextPiece = new NextPieceView(this, board);
        RelativeLayout.LayoutParams nextParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        nextPiece.setLayoutParams(nextParams);
        RelativeLayout nextLayout = findViewById(R.id.nextPiece);
        nextLayout.addView(nextPiece);

        final RelativeLayout gameBoard = findViewById(R.id.gameBoard);

        gameBoard.post(new Runnable() { // get width and height of board view dynamically
            @Override
            public void run() {
                boardViewHeight = gameBoard.getHeight();
                boardViewWidth = gameBoard.getWidth();
            }
        });

        tetrisView = new MainGame(this, nextPiece, board);
        RelativeLayout.LayoutParams tetrisParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        tetrisView.setBackgroundColor(Color.parseColor("#66CDAA"));
        tetrisView.setLayoutParams(tetrisParams);

        gameBoard.addView(tetrisView);

        Start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                gameState = true;
            }
        });

        ButtonListener b = new ButtonListener();
        downBotton.setOnTouchListener(b);

        Restart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                tetrisView.Restart();
            }
        });

    }

    class ButtonListener implements View.OnTouchListener{
        public boolean onTouch(View v, MotionEvent event) {
            if(v.getId() == R.id.buttonDown){
                if(event.getAction() == MotionEvent.ACTION_UP){
                    tetrisView.changeBackSpeed();
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    tetrisView.changeSpeed();
                }
            }
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_1:
                Toast toast = Toast.makeText(this,"\nTouch the screen!\nLeft Slide:  move left\nRight Slide:  move right\nTap once:  rotate 90 degrees\nLong Press: place piece instantly\nDown Button: accelerate the fall",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                return true;
            case R.id.action_2:
                Toast.makeText(this,"Design By Daben Wang and Yuting Jiang",Toast.LENGTH_SHORT).show();
                return true;
            default:
                //do nothing
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRestart(){
        super.onRestart();
        gameState = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        finish();
    }

    public boolean getGameState(){
        return this.gameState;
    }

    public int getBoardViewHeight(){
        return this.boardViewHeight;
    }

    public int getBoardViewWidth(){
        return this.boardViewWidth;
    }

    public TextView getScoreText(){
        return this.scoreText;
    }

    public TextView getGameOverText(){
        return this.gameOverText;
    }
}
