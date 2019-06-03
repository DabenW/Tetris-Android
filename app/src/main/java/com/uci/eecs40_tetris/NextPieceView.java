package com.uci.eecs40_tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import java.util.ArrayList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class NextPieceView extends View {
    private Board board;
    private ArrayList<PuzzlePiece> puzzlePieceList;
    private Bitmap I = BitmapFactory.decodeResource(getResources(),R.drawable.i);
    private Bitmap J = BitmapFactory.decodeResource(getResources(),R.drawable.j);
    private Bitmap L = BitmapFactory.decodeResource(getResources(),R.drawable.l);
    private Bitmap O = BitmapFactory.decodeResource(getResources(),R.drawable.o);
    private Bitmap S = BitmapFactory.decodeResource(getResources(),R.drawable.s);
    private Bitmap T = BitmapFactory.decodeResource(getResources(),R.drawable.t);
    private Bitmap Z = BitmapFactory.decodeResource(getResources(),R.drawable.z);


    public NextPieceView(Context context, Board board) {

        super(context);
        this.board = board;
        puzzlePieceList = board.getPuzzlePieceList();
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        Paint paint = new Paint();
        boolean isEmpty = puzzlePieceList.isEmpty();

        if(!isEmpty){
            PuzzlePiece nextone = board.getNextPuzzlePiece();

            if(nextone.getColorIndex()==1){
                canvas.drawBitmap(I,12,120,paint);
            }else if (nextone.getColorIndex()==2){
                canvas.drawBitmap(J,80,50,paint);
            }else if (nextone.getColorIndex()==3){
                canvas.drawBitmap(L,80,50,paint);
            }else if (nextone.getColorIndex()==4){
                canvas.drawBitmap(O,80,80,paint);
            }else if (nextone.getColorIndex()==5){
                canvas.drawBitmap(S,50,80,paint);
            }else if (nextone.getColorIndex()==6){
                canvas.drawBitmap(T,50,80,paint);
            }else if (nextone.getColorIndex()==7){
                canvas.drawBitmap(Z,50,80,paint);
            }
        }
    }
}
