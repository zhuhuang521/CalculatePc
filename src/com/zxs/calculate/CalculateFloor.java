package com.zxs.calculate;


import java.util.ArrayList;

/**
 * Created by zxs on 16/4/25.
 * 每一层的运算
 */
public class CalculateFloor {
    private int data[][];
    private int score;
    private int floor;
    //上一次触摸点,二维数组，表示上一次点击的次数 x,y
    private int[] lastPoint;
    //连续点击次数
    private int doubleScore, beginDoubleScore;
    private ArrayList<int[]> clickPoints;
    private ArrayList<Integer> selectedData;
    private int beginClickPointsNum;

    public CalculateFloor(int data[][], int score, int floor, int lastPoint[], int doubleTime, ArrayList<int[]> clickPoints) {
        this.data = data;
        this.score = score;
        this.floor = floor;
        this.lastPoint = new int[2];
        this.lastPoint[0] = lastPoint[0];
        this.lastPoint[1] = lastPoint[1];
        this.doubleScore = doubleTime;
        this.beginDoubleScore = doubleTime;
        this.clickPoints = clickPoints;
        selectedData = new ArrayList<Integer>();
        beginClickPointsNum = clickPoints.size();
    }

    /**
     * 返回从当前层数往下到最后一层中计算的最大分数
     */
    public int calculate() {
        int maxData = score;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 25; j++) {
                if (data[i][j] == 0) {
                    doubleScore = beginDoubleScore;
                    int floorNum = score;
                    int num = getScore(i, j);
                    Calculate.CTimes++;
                    //System.out.println("运算次数"+Calculate.CTimes);
                    if (num > 0) {
                        //如果大于0，表示这次计算有数据，把数据传递进去进行，进行下一层运算
                        if (lastPoint[0] == i && lastPoint[1] == j) {
                            doubleScore = doubleScore + 1;
                        } else {
                            lastPoint[0] = -1;
                            lastPoint[1] = -1;
                            doubleScore = 0;
                        }
                        floorNum = floorNum + num;
//                        if(floor == 1 && !hasNexPoint(pointList)){
//                            return num;
//                        }

                        int clickP[] = new int[]{i, j};
                        ArrayList<int[]> pointList = new ArrayList<int[]>();
                        pointList.add(clickP);
                        getScore(i, j);
                        int nextData[][] = clearPoint(i, j, copyData(data));
                        if (doubleClickEnable(i, j, nextData)) {
                            CalculateFloor calculateFloor = new CalculateFloor(nextData, floorNum, 0, lastPoint, doubleScore, pointList);
                            floorNum = calculateFloor.calculate();
                        }
                        if (floorNum > maxData) {
                            maxData = floorNum;
                            if (clickPoints.size() > beginClickPointsNum) {
                                for (int clear = clickPoints.size() - 1; clear >= beginClickPointsNum; clear--) {
                                    clickPoints.remove(clear);
                                }
                            }
                            clickPoints.addAll(pointList);
                        }
                    }

                }
            }
        }
        return maxData;
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

    private int[][] copyData(int[][] data) {
        int copyData[][] = new int[16][25];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 25; j++) {
                copyData[i][j] = data[i][j];
            }
        }
        return copyData;
    }

    /**
     * 获取点击某个点的数据
     */
    private int getScore(int y, int x) {
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
        if (num > 0 && lastPoint[1] == y && lastPoint[0] == x && doubleScore > 0) {
            num = num + doubleScore;
        }
        return num;
    }

    /***
     * 清除点击某个点后的数据
     */
    private int[][] clearPoint(int y, int x, int[][] data) {
        int num = selectedData.size();
        for (int k = 0; k < selectedData.size(); k++) {
            int select = selectedData.get(k);
            //left
            for (int i = x - 1; i >= 0; i--) {
                if (data[y][i] == 0) {
                    continue;
                }
                if (data[y][i] != 0 && data[y][i] == select) {
                    data[y][i] = 0;
                    break;
                } else {
                    break;
                }
            }
            //top
            for (int i = y - 1; i >= 0; i--) {
                if (data[i][x] == 0) {
                    continue;
                }
                if (data[i][x] != 0 && data[i][x] == select) {
                    data[i][x] = 0;
                    break;
                } else {
                    break;
                }
            }
            //right
            for (int i = x + 1; i < 25; i++) {
                if (data[y][i] == 0) {
                    continue;
                }
                if (data[y][i] != 0 && data[y][i] == select) {
                    data[y][i] = 0;
                    break;
                } else {
                    break;
                }
            }
            //bottom
            for (int i = y + 1; i < 16; i++) {
                if (data[i][x] == 0) {
                    continue;
                }
                if (data[i][x] != 0 && data[i][x] == select) {
                    data[i][x] = 0;
                    break;
                } else {
                    break;
                }
            }
        }

        return data;
    }
}
