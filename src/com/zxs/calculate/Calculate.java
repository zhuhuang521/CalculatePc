package com.zxs.calculate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.rmi.server.ExportException;
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
    int doubleScore = 0;
    private final int floor = 3;
    private ArrayList<Integer> selectedData;
    public static int calculateTimes = 1;
    public static long CTimes = 0;
    private File outFile;
    private FileWriter fileWriter;
    private int lasX, lasY;

    public Calculate(int[][] data, int score) {
        this.data = data;
        this.score = score;
        selectedData = new ArrayList<Integer>();
    }

    public void begin() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss sss");
        System.out.println(df.format(new Date()));
        outFile = new File("data/out.txt");
        outFile.delete();
        if (!outFile.exists()) {
            try {
                outFile.createNewFile();
                fileWriter = new FileWriter(outFile.getPath());
            } catch (Exception e) {
            }
        }
        start();

        System.out.println(df.format(new Date()) + "最后得分 score   " + score);
        try {
            fileWriter.close();
        } catch (Exception e) {
        }
    }

    //新的算法,如果有深度,一直遍历下去
    public void start() {

        int maxScore = 0;
        ArrayList<int[]> clearPoint = new ArrayList<int[]>();
        //如果没有深度,并且有相同的数据,对相同数据进行深度N层次,看运行结果
        boolean hasDeep = false;
        ArrayList<TemPoint> temPointsList = new ArrayList<TemPoint>();
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 25; j++) {
                if (data[i][j] == 0) {
                    int floorTime = floor;
                    int num = getScore(i, j, false);
                    ArrayList<int[]> clickPoints = new ArrayList<int[]>();
                    CTimes++;
                    //System.out.println("运算次数"+CTimes);
                    if (num > 0) {
                        int clickP[] = new int[]{i, j};
                        clickPoints.add(clickP);
                        int nextData[][] = clearPoint(i, j, copyData(data));
                        if (doubleClickEnable(i, j, nextData)) {
                            //点击再次点击可以double
                            hasDeep = true;
                            CalculateNextStep calculateNextStep = new CalculateNextStep(nextData, num, 0, clickP, 2, clickPoints);
                            num = calculateNextStep.calculate();
                        }
                    }
                    if (num >= maxScore || (num == 0 && maxScore == 0)) {
                        if (num > 0) {
                            if (!hasDeep) {
                                TemPoint temPoint = new TemPoint(i, j, num);
                                if (num > maxScore) {
                                    temPointsList.clear();
                                    temPointsList.add(temPoint);
                                } else if (num == maxScore) {
                                    temPointsList.add(temPoint);
                                }
                            } else {
                                temPointsList.clear();
                            }
                        }
                        maxScore = num;
                        clearPoint = clickPoints;
                    }
                }
            }
        }
        if (!hasDeep && temPointsList.size() >= 2) {
            System.out.println("需要比较不同数据 "+temPointsList.get(0).score+"  "+temPointsList.size());
            //从这里进行暴力的列举计算没一个节点得到的分数最后进行计算
//            clearPoint.clear();
//            int clickP[] = new int[]{temPointsList.get(temPointsList.size()-1).i,temPointsList.get(temPointsList.size()-1).j};
//            clearPoint.add(clickP);
//            maxScore = temPointsList.get(temPointsList.size()-1).score;

            Decision decision = new Decision();
            decision.decision(temPointsList,copyData(data));
        }
        temPointsList.clear();
        if (maxScore != 0 || hasNexPoint(clearPoint)) {
            int thisDoubleScore = 0;
            int thisCalculateScore = 0;
            int clearNum = clearPoint.size();
            for (int i = 0; i < clearNum; i++) {
                int finalNum = getScore(clearPoint.get(i)[0], clearPoint.get(i)[1], true);
                //System.out.println("第 "+calculateTimes+"  运算 点击 "+clearPoint.get(i)[0]+","+clearPoint.get(i)[1]+"  得分"+(finalNum+thisDoubleScore));
                thisCalculateScore = finalNum + thisDoubleScore + thisCalculateScore;
                calculateTimes++;
                data = clearPoint(clearPoint.get(i)[0], clearPoint.get(i)[1], data);
                if (thisDoubleScore == 0) {
                    thisDoubleScore = 2;
                } else {
                    thisDoubleScore++;
                }
                try {
                    fileWriter.write(clearPoint.get(i)[1] + "," + clearPoint.get(i)[0] + "\r\n");
                } catch (Exception e) {

                }
            }

            //System.out.println("两次数据结果是  "+maxScore+"   "+thisCalculateScore+"  "+(thisCalculateScore == maxScore?" 结果相同 ":"  不一样"));
            //System.out.println("运算补数  "+clearNum +"   得分 "+maxScore);
            score = score + maxScore;
            start();
        }

    }

    private void sysOut() {
        System.out.println("   0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 ");
        System.out.println();
        for (int i = 0; i < 16; i++) {
            System.out.print("" + (i % 10) + " ");
            for (int j = 0; j < 25; j++) {
                System.out.print(" " + data[i][j]);
            }
            System.out.println();
        }
    }

    private int[][] copyData(int[][] data) {
        int copyData[][] = new int[16][25];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 25; j++) {
                copyData[i][j] = data[i][j];
            }
        }
        return copyData;
    }

    /***
     * 再次点击看能不能消除
     */
    private boolean doubleClickEnable(int y, int x, int[][] data) {
        int num = 0;
        int point[] = new int[4];
        //计算四个方向的数据
        //left
        for (int i = x - 1; i >= 0; i--) {
            if (data[y][i] != 0) {
                point[0] = data[y][i];
                break;
            }
        }
        //top
        for (int i = y - 1; i >= 0; i--) {
            if (data[i][x] != 0) {
                point[1] = data[i][x];
                break;
            }
        }
        //right
        for (int i = x + 1; i < 25; i++) {
            if (data[y][i] != 0) {
                point[2] = data[y][i];
                break;
            }
        }
        //bottom
        for (int i = y + 1; i < 16; i++) {
            if (data[i][x] != 0) {
                point[3] = data[i][x];
                break;
            }
        }
        //计算四个数据的数值
        int clearData = -1;
        for (int i = 0; i < 4; i++) {
            for (int j = i + 1; j < 4; j++) {
                if (point[i] == point[j] && point[i] != 0) {
                    if (clearData == -1 || clearData == point[i]) {
                        num = (num == 0 ? 2 : 2 * num);
                        point[j] = 0;

                    } else {
                        num = 8;
                    }
                    clearData = point[i];

                }
            }
        }
        if (num > 0) {
            return true;
        }
        return false;
    }

    /**
     * 是否还有下一个可点击的点，从左到右，从上倒下
     */
    private boolean hasNexPoint(ArrayList<int[]> points) {
        if (points.size() == 0) {
            return false;
        }
        int[] point = points.get(points.size() - 1);
        for (int j = point[1] + 1; j < 25; j++) {
            if (data[point[0]][j] == 0) {
                return true;
            }
        }
        for (int i = point[0] + 1; i < 16; i++) {
            for (int j = 0; j < 25; j++) {
                if (data[i][j] == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取点击某个点的数据
     */
    private int getScore(int y, int x, boolean calculate) {
        int num = 0;
        int point[] = new int[4];
        //计算四个方向的数据
        //left
        for (int i = x - 1; i >= 0; i--) {
            if (data[y][i] != 0) {
                point[0] = data[y][i];
                break;
            }
        }
        //top
        for (int i = y - 1; i >= 0; i--) {
            if (data[i][x] != 0) {
                point[1] = data[i][x];
                break;
            }
        }
        //right
        for (int i = x + 1; i < 25; i++) {
            if (data[y][i] != 0) {
                point[2] = data[y][i];
                break;
            }
        }
        //bottom
        for (int i = y + 1; i < 16; i++) {
            if (data[i][x] != 0) {
                point[3] = data[i][x];
                break;
            }
        }
        //计算四个数据的数值
        selectedData = new ArrayList<Integer>();
        int clearData = -1;
        for (int i = 0; i < 4; i++) {
            for (int j = i + 1; j < 4; j++) {
                if (point[i] == point[j] && point[i] != 0) {
                    if (clearData == -1 || clearData == point[i]) {
                        num = (num == 0 ? 2 : 2 * num);
                        selectedData.add(point[j]);
                        point[j] = 0;

                    } else {
                        selectedData.add(point[j]);
                        num = 8;
                    }
                    clearData = point[i];

                }
            }
        }
        if (num > 0 && lasX == y && lasY == x && doubleScore >= 0) {
            if (doubleScore == 0) {
                num = num + 2;
            } else {
                num = num + doubleScore + 1;
            }
        }
        return num;
    }

    /***
     * 清除点击某个点后的数据
     */
    private int[][] clearPoint(int y, int x, int[][] data) {
        boolean left = false, top = false, right = false, bottom = false;
        for (int k = 0; k < selectedData.size(); k++) {
            int select = selectedData.get(k);
            //left
            if (!left) {
                for (int i = x - 1; i >= 0; i--) {
                    if (data[y][i] == 0) {
                        continue;
                    }
                    //这里有问题
                    if (data[y][i] != 0) {
                        if (data[y][i] == select) {
                            data[y][i] = 0;
                            left = true;
                        }
                        break;
                    }
                }
            }

            //top
            if (!top) {
                for (int i = y - 1; i >= 0; i--) {
                    if (data[i][x] == 0) {
                        continue;
                    }
                    if (data[i][x] != 0) {
                        if (data[i][x] == select) {
                            data[i][x] = 0;
                            top = true;
                        }

                        break;
                    }
                }
            }

            //right
            if (!right) {
                for (int i = x + 1; i < 25; i++) {
                    if (data[y][i] == 0) {
                        continue;
                    }
                    if (data[y][i] != 0) {

                        if (data[y][i] == select) {
                            right = true;
                            data[y][i] = 0;
                        }
                        break;
                    }
                }
            }

            //bottom
            if (!bottom) {
                for (int i = y + 1; i < 16; i++) {
                    if (data[i][x] == 0) {
                        continue;
                    }
                    if (data[i][x] != 0) {
                        if (data[i][x] == select) {
                            data[i][x] = 0;
                            bottom = true;
                        }

                        break;
                    }
                }
            }

        }

        return data;
    }


}
