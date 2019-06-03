package com.uci.eecs40_tetris;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Board {
    private final int height = 24;
    private final int width = 10;
    private int Board[][] = new int [height][width];
    private ArrayList<PuzzlePiece> puzzlePieceList = new ArrayList<PuzzlePiece>();
    private final int pieceNum = 7;
    private final int puzzleNum = 4;
    private int topIndex = 23; //log the higgest piece fall

    public Board() {
        Random random = new Random();
        int index = random.nextInt(pieceNum)+1;
        puzzlePieceList.add(new PuzzlePiece(index)); //add current
        int nextIndex = random.nextInt(pieceNum)+1;
        puzzlePieceList.add(new PuzzlePiece(nextIndex)); // add next piece to list
    }

    public void initialPiece(){
        Random random = new Random();
        int index = random.nextInt(pieceNum)+1;
        puzzlePieceList.add(new PuzzlePiece(index)); //add current
        int nextIndex = random.nextInt(pieceNum)+1;
        puzzlePieceList.add(new PuzzlePiece(nextIndex)); // add next piece to list
    }


    public void initialPuzzlePiece(PuzzlePiece currentPiece){ // initial the first piece with 4 pzzules
        Board[currentPiece.getPx()[0]][currentPiece.getPy()[0]] = currentPiece.getColorIndex();
        Board[currentPiece.getPx()[1]][currentPiece.getPy()[1]] = currentPiece.getColorIndex();
        Board[currentPiece.getPx()[2]][currentPiece.getPy()[2]] = currentPiece.getColorIndex();
        Board[currentPiece.getPx()[3]][currentPiece.getPy()[3]] = currentPiece.getColorIndex();
    }

    public void clearPuzzlePiece(PuzzlePiece currentPiece){
        Board[currentPiece.getPx()[0]][currentPiece.getPy()[0]] = 0;
        Board[currentPiece.getPx()[1]][currentPiece.getPy()[1]] = 0;
        Board[currentPiece.getPx()[2]][currentPiece.getPy()[2]] = 0;
        Board[currentPiece.getPx()[3]][currentPiece.getPy()[3]] = 0;
    }

    public void FallDown(PuzzlePiece currentPiece){
        boolean canFalldown = checkFallDown(currentPiece);
        if(canFalldown && currentPiece.Isfirst()){
            clearPuzzlePiece(currentPiece);
            currentPiece.move(1,0);
            initialPuzzlePiece(currentPiece);
        }else if(canFalldown && !currentPiece.Isfirst()){
            initialPuzzlePiece(currentPiece);
            currentPiece.startMove();
        }
    }

    public void FastFallDown(PuzzlePiece currentPiece){
        boolean canFallDown = checkFallDown(currentPiece);
        while(canFallDown){
            clearPuzzlePiece(currentPiece);
            FallDown(currentPiece);
            initialPuzzlePiece(currentPiece);
        }
    }

    public void SlideRight(PuzzlePiece currentPiece){
        boolean canSlideRight = checkSlideRight(currentPiece);
        if(canSlideRight){
            clearPuzzlePiece(currentPiece);
            currentPiece.move(0,1);
            initialPuzzlePiece(currentPiece);
        }
    }
    public void SlideLeft(PuzzlePiece currentPiece){
        boolean chanSlideRight = checkSlideLeft(currentPiece);
        if(chanSlideRight){
            clearPuzzlePiece(currentPiece);
            currentPiece.move(0,-1);
            initialPuzzlePiece(currentPiece);
        }
    }

    public void Rotate(PuzzlePiece currentPiece){
        boolean canRoate = checkRotateState(currentPiece);
        if(canRoate){
            clearPuzzlePiece(currentPiece);
            currentPiece.Rotate();
            initialPuzzlePiece(currentPiece);
        }
    }

    public int DisappearRows(){
        ArrayList<Integer> deletedList = new ArrayList<>();
        int count = 0;
        for(int i = 0; i< height; i++){
            for(int j = 0; j<width; j++){
                if(Board[i][j]==0){
                    break;
                }
                boolean isEnd = (j == width-1);
                if(isEnd){
                    deletedList.add(i);
                    for (int m = 0; m < width; m++) {// delete the row is full
                        Board[i][m] =0;
                    }
                    count++;
                }
            }
        }
        if(count>0){//there is row disappear
            for(int i = 0;i<count;i++){
                int[][] beforeDelete = new int[deletedList.get(i)][width];

                //copy out the matrix above the disappear row
                for(int m = 0; m< deletedList.get(i);m++){
                    for(int n = 0; n<width;n++){
                        beforeDelete[m][n] = Board[m][n];
                    }
                }
                // move the above matrix to the next level
                for(int m = 0; m< deletedList.get(i);m++){
                    for(int n = 0; n<width;n++){
                        Board[m+1][n] = beforeDelete[m][n];
                    }
                }
                for (int m = 0; m < width; m++) {
                    Board[0][m] =0;
                }
            }
        }
        return count;
    }

    public PuzzlePiece getNextPuzzlePiece(){
        return puzzlePieceList.get(puzzlePieceList.size()-1);
    }

    public PuzzlePiece getCurrentPuzzlePiece(){
        return puzzlePieceList.get(puzzlePieceList.size()-2);
    }


    public void clearBoard() { //clean the board to initial
        for(int i=0; i<height; i++) {
            for(int j=0; j<width; j++) {
                Board[i][j]= 0;
            }
        }
    }

    // to see if the current piece can move to the desitination
    public boolean checkMoveState(PuzzlePiece currentPiece, int offsetx, int offsety){
        Integer[] X = currentPiece.getPx();
        Integer[] Y = currentPiece.getPy();
        int[] newX = {currentPiece.getPx()[0]+offsetx,currentPiece.getPx()[1]+offsetx,currentPiece.getPx()[2]+offsetx,currentPiece.getPx()[3]+offsetx};
        int[] newY = {currentPiece.getPy()[0]+offsety,currentPiece.getPy()[1]+offsety,currentPiece.getPy()[2]+offsety,currentPiece.getPy()[3]+offsety};

        int puzzleCount = 0;
        int puzzleNum = 4;
        for (int i=0; i<puzzleNum;i++){
            if(newX[i]<height && newY[i]>=0 && newY[i]<width && Board[newX[i]][newY[i]]==0){
                puzzleCount++;
            }else if(((newX[i]==X[0]&&newY[i]==Y[0])||(newX[i]==X[1]&&newY[i]==Y[1])||(newX[i]==X[2]&&newY[i]==Y[2])||(newX[i]==X[3]&&newY[i]==Y[3]))&&currentPiece.Isfirst()){
                puzzleCount++;
            }
        }

        if(puzzleCount==puzzleNum){
            return true;
        }

        return false;
    }

    // to see if the current piece can move to the desitination
    public boolean checkRotateState(PuzzlePiece currentPiece){
        Integer[] X = currentPiece.getPx();
        Integer[] Y = currentPiece.getPy();

        PuzzlePiece preRotatePiece = new PuzzlePiece(currentPiece.getColor(),currentPiece.getPx().clone(),currentPiece.getPy().clone(),currentPiece.getRotateTimes()); //store a current piece to operate
        preRotatePiece.Rotate();

        Integer[] newX = preRotatePiece.getPx();
        Integer[] newY = preRotatePiece.getPy();

        int puzzleCount = 0;
        int puzzleNum = 4;
        for(int i=0;i<puzzleNum;i++){
            if(newX[i]<height &&  newX[i]>=0 && newY[i]<width && newY[i]>=0 && Board[newX[i]][newY[i]]==0){
                puzzleCount++;
            }else if((newX[i].equals(X[0]) && newY[i].equals(Y[0]))||(newX[i].equals(X[1]) && newY[i].equals(Y[1]))||(newX[i].equals(X[2]) && newY[i].equals(Y[2]))||(newX[i].equals(X[3]) && newY[i].equals(Y[3]))){
                puzzleCount++;
            }
        }
        if(puzzleCount==puzzleNum){
            return true;
        }
        return false;
    }


    public boolean checkFallDown(PuzzlePiece currentPiece){
        if(currentPiece.Isfirst()){
            return checkMoveState(currentPiece, 1, 0);
        }else{
            return checkMoveState(currentPiece, 0, 0);
        }
    }

    public boolean checkSlideRight(PuzzlePiece currentPiece){
        return checkMoveState(currentPiece ,0,1);
    }

    public boolean checkSlideLeft(PuzzlePiece currentPiece){
        return checkMoveState(currentPiece ,0,-1);
    }

    //if game still going on return true, else return false
    public boolean checkGameState(PuzzlePiece currentPiece){
        if(!checkFallDown(currentPiece)&&currentPiece.getPieceTop()<=0){
            int blankrows = 0;
            int botttom = currentPiece.getPx()[3]; //get bottom of piece
            for(int i = 0; i<3;i++) {
                for(int j = (int)Collections.min(Arrays.asList(currentPiece.getPy()));j<= Collections.max(Arrays.asList(currentPiece.getPy()));j++){
                    if(Board[i][j]!=0){
                        break;
                    }
                    if(j==Collections.max(Arrays.asList(currentPiece.getPy()))){
                        blankrows++;
                    }
                }
            }
            if(blankrows>0){
                int rowIndex = blankrows-1;
                int puzzleNum = 4;
                for(int m = rowIndex;m>=0;m--){
                    for(int n= 0;n<puzzleNum;n++){
                        if(currentPiece.getPx()[n]==botttom){
                            Board[m][currentPiece.getPy()[n]]=currentPiece.getColor();
                        }
                    }
                    botttom--;
                }
            }

            return false;
        }
        return true;
    }

    public ArrayList<PuzzlePiece> getPuzzlePieceList(){
        return puzzlePieceList;
    }

    public int getHeight(){
        return height;
    }

    public int getWidth(){
        return width;
    }

    public int[][] getBoard(){
        return Board;
    }

    public int getPieceNum(){
        return this.pieceNum;
    }

    public int getColor(int x, int y){
        int index = Board[x][y];
        switch(index){
            case 0:
                return Color.parseColor("#042c34");
            case 1:   //i
                return Color.parseColor("#7fc5fe");
            case 2: //j
                return Color.parseColor("#0000dd");
            case 3: //l
                return Color.parseColor("#f9955a");
            case 4: //o
                return Color.parseColor("#f3ac2a");
            case 5: //s
                return Color.parseColor("#e42d2d");
            case 6: //t
                return Color.parseColor("#8c4da6");
            case 7: //z
                return Color.parseColor("#44d083");
            default:
                return Color.parseColor("#042c34");
        }
    }
}
