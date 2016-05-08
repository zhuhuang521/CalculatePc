package com.zxs.calculate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zxs on 16/4/25.
 */
public class Calculate {

    private int[][] data;
    int score;
    public static long calculateNum = 0;
    int lastPointX =-1;
    int lastPointY = -1;
    private int finalLastX = -1,finalLastY = -1;
    int doubleTime = 0;
    private final int floor = 4;
    private ArrayList<Integer> selectedData;
    private int calculateTimes = 0;
    public static long CTimes = 0;
    public Calculate(int [][] data,int score){
        this.data = data;
        this.score = score;
        selectedData = new ArrayList<Integer>();
    }
    public void start(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int maxScore = 0;
        ArrayList<int[]> clearPoint = new ArrayList<int[]>();
        lastPointX = finalLastX;
        lastPointY = finalLastY;
        System.out.println(df.format(new Date())+"第"+calculateTimes+"次运算开始,得分 score   "+score);
        for(int i=0;i<16;i++){
            for(int j=0;j<25;j++){
                if(data[i][j] == 0){
                    int floorTime = floor;
                    int num = getScore(i,j);
                    ArrayList<int[]> clickPoints = new ArrayList<int[]>();
                    CTimes++;
                    //System.out.println("运算次数"+CTimes);
                    if(num > 0){
                        if(lastPointX == -1){
                            lastPointX = i;
                            lastPointY = j;
                            doubleTime = 2;
                        }else if(lastPointX != i || lastPointY != j){
                            lastPointX = -1;
                            lastPointY = -1;
                            doubleTime = 0;
                        }

                        int clickP[] = new int[]{i,j};
                        clickPoints.add(clickP);
                        int lastF = floorTime-1;
                        int nextData[][] = clearPoint(i,j,copyData(data));
                        CalculateFloor calculateFloor = new CalculateFloor(nextData,num,lastF,clickP,doubleTime,clickPoints);
                        num = calculateFloor.calculate();
                    }
                   if(num >= maxScore || (num == 0 && maxScore ==0)){
                       maxScore = num;
                       clearPoint = clickPoints;
                   }
                }
            }
        }
        calculateTimes++;
        System.out.println(df.format(new Date())+"第"+calculateTimes+"次运算结束,得分 score   "+score);
        if(maxScore != 0 || hasNexPoint(clearPoint)){
            int clearNum = clearPoint.size();
            for(int i =0;i<clearNum;i++){

                getScore(clearPoint.get(i)[0],clearPoint.get(i)[1]);
                data = clearPoint(clearPoint.get(i)[0],clearPoint.get(i)[1],data);
            }
            score = score + maxScore;
            start();
        }
        System.out.println(df.format(new Date())+"最后得分 score   "+score);
    }

    private int[][] copyData(int[][] data){
        int copyData[][] = new int[16][25];
        for(int i =0;i<16;i++){
            for(int j=0;j<25;j++){
                copyData[i][j] = data[i][j];
            }
        }
        return copyData;
    }
    /**
     * 是否还有下一个可点击的点，从左到右，从上倒下
     * */
    private boolean hasNexPoint(ArrayList<int[]> points){
        if(points.size() == 0){
            return false;
        }
        int[] point = points.get(points.size()-1);
        for(int j = point[1]+1 ;j<25;j++){
            if(data[point[0]][j] == 0){
                return true;
            }
        }
        for(int i = point[0]+1;i<16;i++){
            for(int j=0;j<25;j++){
                if(data[i][j] == 0){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取点击某个点的数据
     * */
    private int getScore(int y,int x){
        int num = 0;
        int point[] = new int[4];
        //计算四个方向的数据
        //left
        for(int i = x-1;i>=0;i--){
            if(data[y][i] != 0){
                point[0] = data[y][i];
                break;
            }
        }
        //top
        for(int i = y-1;i>=0;i--){
            if(data[i][x] != 0){
                point[1] = data[i][x];
                break;
            }
        }
        //right
        for(int i = x+1;i<25;i++){
            if(data[y][i] != 0){
                point[2] = data[y][i];
                break;
            }
        }
        //bottom
        for(int i = y+1;i<16;i++){
            if(data[i][x] != 0){
                point[3] = data[i][x];
                break;
            }
        }
        //计算四个数据的数值
        selectedData = new ArrayList<Integer>();
        int clearData = -1;
        for(int i=0;i<4;i++){
            for(int j=i+1;j<4;j++){
                if(point[i] == point[j] && point[i] != 0){
                    if(clearData == -1 || clearData == point[i]){
                        num = (num==0?2:2*num);
                        selectedData.add(point[j]);
                        point[j] = 0;

                    }else{
                        selectedData.add(point[j]);
                        num = 8;
                    }
                    clearData = point[i];

                }
            }
        }
        if(num>0 && lastPointX == y && lastPointY == x && doubleTime > 0){
            num = num +doubleTime;
            doubleTime = doubleTime + 1;
        }
        return num;
    }

    /***
     * 清除点击某个点后的数据
     */
    private int[][] clearPoint(int y,int x,int[][] data){
        for(int k = 0;k<selectedData.size();k++){
            int select = selectedData.get(k);
            //left
            for(int i = x-1;i>=0;i--){
                if(data[y][i] == 0){
                    continue;
                }
                if(data[y][i] != 0 && data[y][i] == select){
                    data[y][i]=0;
                    break;
                }else{
                    break;
                }
            }
            //top
            for(int i = y-1;i>=0;i--){
                if(data[i][x] == 0){
                    continue;
                }
                if(data[i][x] != 0 && data[i][x] == select){
                    data[i][x]=0;
                    break;
                }else{
                    break;
                }
            }
            //right
            for(int i = x+1;i<25;i++){
                if(data[y][i] == 0) {
                    continue;
                }
                if(data[y][i] != 0 && data[y][i] == select){
                    data[y][i]=0;
                    break;
                }else{
                    break;
                }
            }
            //bottom
            for(int i = y+1;i<16;i++){
                if(data[i][x] == 0){
                    continue;
                }
                if(data[i][x] != 0 && data[i][x] == select){
                    data[i][x]=0;
                    break;
                }else{
                    break;
                }
            }
        }

        return data;
    }


}
