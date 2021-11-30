package com.bewg.pd.common.util;


import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * excel操作工具类
 * </p>
 *
 * @author tianzhitao
 * @since 2021-10-22
 */
public class ExcelUtil {

    /**
     * 列转数字下标
     *
     * @param col
     * @return
     */
    public static int fromAlphaToIndex(String col) {
        //  "AAA"
        if (col == null) {
            return -1;
        }
        // 转为大写字母组成的 char数组
        char[] chrs = col.toUpperCase().toCharArray();
        int length = chrs.length;
        int ret = -1;
        for (int i = 0; i < length; i++) {
            // 当做26进制来算 AAA=111 26^2+26^1+26^0
            ret += (chrs[i] - 'A' + 1) * Math.pow(26, length - i - 1);
        }
        // 702; 从0开始的下标
        return ret;
    }

    /**
     * 数字下标转列
     *
     * @param index
     * @return
     */
    public static String fromIndexToAlpha(int index) {
        int shang = index;
        int yu;
        // 10进制转26进制 倒序
        List<Integer> list = new ArrayList<>();
        do {
            yu = shang % 26;
            shang = shang / 26;
            list.add(yu);
        }
        while (shang != 0);
        StringBuilder sb = new StringBuilder();
        for (int j = list.size() - 1; j >= 0; j--) {
            //倒序拼接  序号转字符 非末位 序号减去 1
            sb.append((char) (list.get(j) + 'A' - (j > 1 ? 1 : j)));
        }
        return sb.toString();
    }

    /**
     * 是否为数值类型单元格
     *
     * @param cellValue
     * @return
     */
    public static boolean isNumericCell(String cellValue) {

        if (cellValue == null) {
            return false;
        }

        cellValue = cellValue.replaceAll("\\.", "").trim();
        return cellValue.matches("^[0-9]*$");

    }

}
