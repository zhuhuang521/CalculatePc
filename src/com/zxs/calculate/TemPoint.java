package com.zxs.calculate;

/**
 * Created by zxs on 16/5/10.
 * 临时数据,如果存在相同分数,要进行深度
 */
public class TemPoint {
    public int score;
    public int i;
    public int j;
    public TemPoint(int i,int j,int score){
        this.i = i;
        this.j = j;
        this.score = score;
    }

    public TemPoint copy(){
        return new TemPoint(i,j,score);
    }
}
