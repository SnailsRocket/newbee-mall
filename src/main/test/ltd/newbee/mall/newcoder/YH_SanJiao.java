package ltd.newbee.mall.newcoder;

import java.util.Scanner;

/**
 * @Author: Xubo
 * @Date: 2020/9/3 22:25
 * 给定一个非负整数 numRows，
 * 生成杨辉三角的前 numRows 行(杨辉三角即每个数是它左上方和右上方的数的和。)
 */
public class YH_SanJiao {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入行高：");
        int row = sc.nextInt();
        YH_SanJiao yh_sanJiao = new YH_SanJiao();
        yh_sanJiao.showSanjiao(row);

    }

    private void showSanjiao(int row) {
        for (int j = 0; j < row; j++) {
            for (int i = 0; i < row-j; i++) {
                System.out.print(" ");
            }
            for (int i = 0; i < j+1; i++) {
                System.out.print(1);
                for (int k = 0; k < j; k++) {
                    System.out.print(",");
                }
            }


        }

    }

}
