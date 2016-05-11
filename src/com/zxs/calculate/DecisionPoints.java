package com.zxs.calculate;

import java.util.ArrayList;

/**
 * Created by zxs on 16/5/12.
 * 决策数据
 */
public class DecisionPoints {
    public ArrayList<TemPoint> decisionList;
    public int maxScore = 0;
    public  int step = 0;
    boolean hasDeep = false;
    private int maxSetp = 4;
    public DecisionPoints(){
        decisionList = new ArrayList<TemPoint>();
    }
    public void addPoint(int i,int j,int score){
        TemPoint temPoint = new TemPoint(i,j,0);
        decisionList.add(temPoint);
        maxScore = maxScore + score;
        step ++;
    }
    public void addPoint(TemPoint temPoint){
        decisionList.add(temPoint);
        maxScore = maxScore + temPoint.score;
        step++;
    }

    public ArrayList<TemPoint> copyList(){
        ArrayList<TemPoint> copyList = new  ArrayList<TemPoint>();
        for(int i = 0;i<decisionList.size();i++){
            TemPoint temPoint = decisionList.get(i).copy();
            copyList.add(temPoint);
        }
        return copyList;
    }

    public DecisionPoints copy(){
        DecisionPoints decisionPoints = new DecisionPoints();
        decisionPoints.maxScore = maxScore;
        decisionPoints.step = step;
        decisionPoints.decisionList = copyList();
        decisionPoints.hasDeep = hasDeep;
        return decisionPoints;
    }
    public boolean couldDeep(){
        return maxSetp > step;
    }
}
