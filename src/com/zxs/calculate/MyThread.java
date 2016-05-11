package com.zxs.calculate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by zxs on 16/5/9.
 */
public class MyThread extends Thread{
    String data = "";
    @Override
    public void run() {
        super.run();
        for(int i=0;i<99;i++){
            System.out.println("计算数据文件  "+i);
            calculate(i);
        }

    }

    private void calculate(int fileName){
        data = "";
        File file = new File("data/data/key_"+fileName+".txt");
        ArrayList<String> dataList = new ArrayList<String>();
        try{
            String line = "";
            FileInputStream fis = new FileInputStream(file.getPath());
            InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
            BufferedReader br = new BufferedReader(isr);
            int num = 0;

            while((line=br.readLine())!=null){
                if(num != 0){
                    data = data+"\n"+line;
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

        int data[][] = new int[dataList.size()][dataList.get(0).length()];
        for(int i =0;i<dataList.size();i++){
            char[] datas = dataList.get(i).replace(" ","").toCharArray();
            for(int j =0;j<dataList.get(0).length();j++){
                data[i][j] = Integer.parseInt(datas[j]+"");
            }
        }
        Calculate calculate = new Calculate(data,0);
        calculate.begin();
    }
}