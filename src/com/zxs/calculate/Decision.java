package com.zxs.calculate;

import java.util.ArrayList;

/**
 * Created by zxs on 16/5/11.
 * 决策相同的得分使用选择那个点击
 */
public class Decision {
    private ArrayList<Integer> selectedData;
    public void decision(ArrayList<TemPoint> temPoints,int[][] data){
        int num = temPoints.size();
        for(int i=0;i<num;i++){

        }
    }

    private boolean hasDeep(){

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
    private int getScore(int y, int x, boolean calculate,int[][] data) {
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
//        if (num > 0 && lasX == y && lasY == x && doubleScore >= 0) {
//            if (doubleScore == 0) {
//                num = num + 2;
//            } else {
//                num = num + doubleScore + 1;
//            }
//        }
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
