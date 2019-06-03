package com.uci.eecs40_tetris;

import android.graphics.Color;

import java.util.ArrayList;

public class PuzzlePiece {
    private int color;
    private int rotateTimes=0; //this only for i piece, to record home times it rotate
    private Integer px[] = new Integer[4];
    private Integer py[] = new Integer[4];
    private boolean isFirst = false;

    public PuzzlePiece(int color, Integer[] px, Integer[] py, int rotateTimes) {
        this.px = px;
        this.py = py;
        this.color = color;
        this.rotateTimes = rotateTimes;
    }

    public PuzzlePiece(int index){
        switch(index){
            case 1:  //i piece
                px[0] = 0; py[0] = 3;
                px[1] = 0; py[1] = 4;
                px[2] = 0; py[2] = 5;
                px[3] = 0; py[3] = 6;
                color = index;
                rotateTimes = 0;
                break;
            case 2: //j piece
                px[0] = 0; py[0] = 5;
                px[1] = 1; py[1] = 5;
                px[2] = 2; py[2] = 4;
                px[3] = 2; py[3] = 5;
                color = index;
                break;
            case 3: //l piece
                px[0] = 0; py[0] = 4;
                px[1] = 1; py[1] = 4;
                px[2] = 2; py[2] = 4;
                px[3] = 2; py[3] = 5;
                color = index;
                break;
            case 4: //o piece
                px[0]= 0; py[0]= 4;
                px[1] = 0; py[1] = 5;
                px[2] = 1; py[2] = 4;
                px[3] = 1; py[3] = 5;
                color = index;
                break;
            case 5: //s piece
                px[0] = 0; py[0] = 4;
                px[1] = 0; py[1] = 5;
                px[2] = 1; py[2] = 3;
                px[3] = 1; py[3] = 4;
                color = index;
                break;
            case 6: //t piece
                px[0] = 0; py[0] = 3;
                px[1] = 0; py[1] = 4;
                px[2] = 0; py[2] = 5;
                px[3] = 1; py[3] = 4;
                color = index;
                break;
            case 7: //z piece
                px[0] = 0; py[0] = 3;
                px[1] = 0; py[1] = 4;
                px[2] = 1; py[2] = 4;
                px[3] = 1; py[3] = 5;
                color = index;
                break;
        }
    }

    public void move(int offsetx, int offsety){
        px[0] +=offsetx;
        px[1] +=offsetx;
        px[2] +=offsetx;
        px[3] +=offsetx;
        py[0] +=offsety;
        py[1] +=offsety;
        py[2] +=offsety;
        py[3] +=offsety;
    }

    public void Rotate(){
        switch(this.color){
            case 1: //
                if((rotateTimes % 4)==0){
                    this.px[0]-=1; this.py[0]+=2;
                    this.py[1]+=1;
                    this.px[2]+=1;
                    this.px[3]+=2; this.py[3]-=1;
                }else if((rotateTimes%4)==1){
                    this.px[0]+=2; this.py[0]+=1;
                    this.px[1]+=1;
                    this.py[2]-=1;
                    this.px[3]-=1; this.py[3]-=2;
                }else if((rotateTimes%4)==2){
                    this.px[0]+=1; py[0]-=2;
                    this.py[1]-=1;
                    this.px[2]-=1;
                    this.px[3]-=2; this.py[3]+=1;
                }else if((rotateTimes%4)==3){
                    this.px[0]-=2; py[0]-=1;
                    this.px[1]-=1;
                    this.py[2]+=1;
                    this.px[3]+=1; this.py[3]+=2;
                }
                rotateTimes++;
                break;
            case 2:
                int[] center2 = {px[1],py[1]};
                for(int i=0;i<4;i++){
                    int[] temp = {px[i],py[i]};
                    int[] rotated = rotateAroundCenter(temp,center2);
                    this.px[i] = rotated[0];
                    this.py[i] = rotated[1];
                }
                break;
            case 3:
                int[] center3 = {px[1],py[1]};
                for(int i=0;i<4;i++){
                    int[] temp = {px[i],py[i]};
                    int[] rotated = rotateAroundCenter(temp,center3);
                    this.px[i] = rotated[0];
                    this.py[i] = rotated[1];
                }
                break;
            case 5:
                int[] center5 = {px[3],py[3]};
                for(int i=0;i<4;i++){
                    int[] temp = {px[i],py[i]};
                    int[] rotated = rotateAroundCenter(temp,center5);
                    this.px[i] = rotated[0];
                    this.py[i] = rotated[1];
                }
                break;
            case 6:
                int[] center6 = {px[1],py[1]};
                for(int i=0;i<4;i++){
                    int[] temp = {px[i],py[i]};
                    int[] rotated = rotateAroundCenter(temp,center6);
                    this.px[i] = rotated[0];
                    this.py[i] = rotated[1];
                }
                break;
            case 7:
                int[] center7 = {px[2],py[2]};
                for(int i=0;i<4;i++){
                    int[] temp = {px[i],py[i]};
                    int[] rotated = rotateAroundCenter(temp,center7);
                    this.px[i] = rotated[0];
                    this.py[i] = rotated[1];
                }
                break;



        }
    }

    public int[]  rotateAroundCenter(int[] temp, int[] center){
        if(temp[0]<center[0]){
            if(temp[1]<center[1]){//left up coner
                int[] rotated = {temp[0],temp[1]+2};
                return rotated;
            }else if (temp[1] == center[1]){//up
                int[] rotated = {temp[0]+1,temp[1]+1};
                return rotated;
            }else{//right up coner
                int[] rotated = {temp[0]+2, temp[1]};
                return rotated;
            }
        }else if(temp[0] == center[0]){
            if(temp[1]<center[1]){//left
                int[] rotated = {temp[0]-1,temp[1]+1};
                return rotated;
            }else if (temp[1] == center[1]){//center
                int[] rotated = {temp[0],temp[1]};
                return rotated;
            }else{//right
                int[] rotated = {temp[0]+1, temp[1]-1};
                return rotated;
            }
        }else{
            if(temp[1]<center[1]){//bottom left coner
                int[] rotated = {temp[0]-2,temp[1]};
                return rotated;
            }else if (temp[1] == center[1]){//down
                int[] rotated = {temp[0]-1,temp[1]-1};
                return rotated;
            }else{//right bottom coner
                int[] rotated = {temp[0], temp[1]-2};
                return rotated;
            }
        }
    }


    public int getPieceColor(){
        switch(this.color){
            case 1:   //i
                return Color.parseColor("#0000FF");
            case 2: //j
                return Color.parseColor("#FF0000");
            case 3: //l
                return Color.parseColor("#0000FF");
            case 4: //o
                return Color.parseColor("#00FF00");
            case 5: //s
                return Color.parseColor("#ffbf00");
            case 6: //t
                return Color.parseColor("#00FF00");
            case 7: //z
                return Color.parseColor("#FF00FF");
            default:
                return Color.parseColor("#FFFF00");
        }
    }

    public int getColorIndex(){
        return this.color;
    }

    public int getPieceTop() {
        if(Math.min(px[0],px[1])<Math.min(px[2],px[3])){
            return Math.min(px[0],px[1]);
        }else{
            return Math.min(px[2],px[3]);
        }
    }

    public int getRotateTimes(){
        return this.rotateTimes;
    }

    public int getColor(){
        return this.color;
    }
    public Integer[] getPx(){
        return this.px;
    }

    public Integer[] getPy(){
        return this.py;
    }

    public boolean Isfirst(){
        return this.isFirst;
    }

    public void startMove(){
        this.isFirst = true;
    }


}
