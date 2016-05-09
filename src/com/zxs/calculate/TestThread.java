package com.zxs.calculate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by zxs on 16/5/9.
 */
public class TestThread extends Thread{
    String dataStr = "";
    int data[][];
    int testdata[][];
    private ArrayList<Integer> selectedData;
    int lastPointX =-1;
    int lastPointY = -1;
    int doubleScore = 0;

    int score = 0;
    public int step = 0;
    boolean show = true;
    @Override
    public void run() {
        super.run();
        //String path = Environment.getExternalStorageDirectory().getPath();
        selectedData = new ArrayList<Integer>();
        String path = "";
        File file = new File("data/locs.txt");
        ArrayList<String> dataList = new ArrayList<String>();
        try{
            String line = "";
            FileInputStream fis = new FileInputStream(file.getPath());
            InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
            BufferedReader br = new BufferedReader(isr);
            int num = 0;

            while((line=br.readLine())!=null){
                if(num != 0){
                    dataStr = dataStr+"\n"+line;
                    dataList.add(line.replace(" ",""));
                }
                num ++;
            }
            br.close();
            isr.close();
            fis.close();
        }catch (Exception e){
            System.out.println("error "+e.toString());
        }

        data = new int[dataList.size()][dataList.get(0).length()];
        for(int i =0;i<dataList.size();i++){
            char[] datas = dataList.get(i).replace(" ","").toCharArray();
            for(int j =0;j<dataList.get(0).length();j++){
                data[i][j] = Integer.parseInt(datas[j]+"");
            }
        }

        dataStr = "";
        File testFile = new File("data/out.txt");
        ArrayList<String> testList = new ArrayList<String>();
        try{
            String line = "";
            FileInputStream fis = new FileInputStream(testFile.getPath());
            InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
            BufferedReader br = new BufferedReader(isr);
            int num = 0;

            while((line=br.readLine())!=null){

                dataStr = dataStr+"\n"+line;
                testList.add(line.replace(" ",""));

                num ++;
            }
            br.close();
            isr.close();
            fis.close();
        }catch (Exception e){
            System.out.println("error "+e.toString());
        }
        testdata = new int[testList.size()][2];
        for(int i =0;i<testList.size();i++){
            String[] datas = testList.get(i).replace(" ","").split(",");
            for(int j =0;j<2;j++){
                testdata[i][j] = Integer.parseInt(datas[(j)%2]+"");
            }
        }
        //计算得分
        int num = testdata.length;
        for(int i =0;i<num;i++){
            sysOut();
            score = score + getScore(testdata[i][0],testdata[i][1],false);
            System.out.println("  总分 "+score);
            clearPoint(testdata[i][0],testdata[i][1],data);
            lastPointX = testdata[i][0];
            lastPointY = testdata[i][1];
        }
        System.out.println("最后得分"+score);


    }

    private void sysOut(){
        System.out.println("   0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 ");
        System.out.println();
        for(int i=0;i<16;i++){
            System.out.print(""+(i%10)+" ");
            for(int j=0;j<25;j++){
                System.out.print(" "+data[i][j]);
            }
            System.out.println();
        }
    }
    /**
     * 获取点击某个点的数据
     * */
    public int getScore(int y,int x,boolean calculate){
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
                        boolean hasData =false;
                        for(int k=0;k<selectedData.size();k++){
                            if(selectedData.get(k) == point[j]){
                                hasData = true;
                            }
                        }
                        if(!hasData){
                            selectedData.add(point[j]);
                        }

                        point[j] = 0;

                    }else{
                        boolean hasData =false;
                        for(int k=0;k<selectedData.size();k++){
                            if(selectedData.get(k) == point[j]){
                                hasData = true;
                            }
                        }
                        if(!hasData){
                            selectedData.add(point[j]);
                        }
                        num = 8;
                    }
                    clearData = point[i];

                }
            }
        }
        if(num>0 && lastPointX == y && lastPointY == x && doubleScore >= 0){
            if(doubleScore == 0){
                num = num +2;
                doubleScore = 2;
            }else{
                doubleScore = doubleScore + 1;
                num = num +doubleScore;
            }
        }else{
            doubleScore = 0;
        }
        step++;
        System.out.println("第"+step+"步得分 "+""+num+"   点击 "+y+","+x+"  消除颜色"+selectedData.toString());

        return num;
    }

    /***
     * 清除点击某个点后的数据
     */
    private int[][] clearPoint(int y,int x,int[][] data){
        boolean left = false,top=false,right=false,bottom=false;
        for(int k = 0;k<selectedData.size();k++){
            int select = selectedData.get(k);
            //left
            if(!left){
                for(int i = x-1;i>=0;i--){
                    if(data[y][i] == 0){
                        continue;
                    }
                    //这里有问题
                    if(data[y][i] != 0){
                        if(data[y][i] == select){
                            data[y][i]=0;
                            left = true;
                            if(show){
                                System.out.println("第"+step+"步消除左面  "+y+"   "+i+"  消除颜色"+select);
                            }
                        }
                        break;
                    }
                }
            }

            //top
            if(!top){
                for(int i = y-1;i>=0;i--){
                    if(data[i][x] == 0){
                        continue;
                    }
                    if(data[i][x] != 0){
                        if(data[i][x] == select){
                            data[i][x]=0;
                            top = true;
                            if(show){
                                System.out.println("第"+step+"步消除上面  "+i+"   "+x+"  消除颜色"+select);
                            }
                        }

                        break;
                    }
                }
            }

            //right
            if(!right){
                for(int i = x+1;i<25;i++){
                    if(data[y][i] == 0) {
                        continue;
                    }
                    if(data[y][i] != 0){

                        if(data[y][i] == select){
                            right = true;
                            data[y][i]=0;
                            if(show){
                                System.out.println("第"+step+"步消除右面  "+y+"   "+i+"  消除颜色"+select);
                            }
                        }

                        break;
                    }
                }
            }

            //bottom
            if(!bottom){
                for(int i = y+1;i<16;i++){
                    if(data[i][x] == 0){
                        continue;
                    }
                    if(data[i][x] != 0 ){

                        if(data[i][x] == select){
                            data[i][x]=0;
                            bottom = true;
                            if(show){
                                System.out.println("第"+step+"步消除下面  "+i+"   "+x+"  消除颜色"+select);
                            }
                        }

                        break;
                    }
                }
            }

        }

        return data;
    }
}
