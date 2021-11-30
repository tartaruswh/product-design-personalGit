package com.bewg.pd.workbook.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFColor;

/**
 * <p>
 * excel操作代理类
 * </p>
 *
 * @author tianzhitao
 * @since 2021-10-22
 */
public class ColorUtil {
    /**
     * 颜色的alpha值，此值控制了颜色的透明度
     */
    public int A;
    /**
     * 颜色的红分量值，Red
     */
    public int R;
    /**
     * 颜色的绿分量值，Green
     */
    public int G;
    /**
     * 颜色的蓝分量值，Blue
     */
    public int B;

    public int toRGB() {
        return this.R << 16 | this.G << 8 | this.B;
    }

    public java.awt.Color toAWTColor() {
        return new java.awt.Color(this.R, this.G, this.B, this.A);
    }

    public static ColorUtil fromARGB(int red, int green, int blue) {
        return new ColorUtil((int)0xff, (int)red, (int)green, (int)blue);
    }

    public static ColorUtil fromARGB(int alpha, int red, int green, int blue) {
        return new ColorUtil(alpha, red, green, blue);
    }

    public ColorUtil(int a, int r, int g, int b) {
        this.A = a;
        this.B = b;
        this.R = r;
        this.G = g;
    }

    /**
     * excel97中颜色转化为uof颜色
     *
     * @param color
     *            颜色序号
     * @return 颜色或者null
     */
    public static ColorUtil Excel97Color2UOF(Workbook book, short color) {
        if (book instanceof HSSFWorkbook) {
            HSSFWorkbook hb = (HSSFWorkbook)book;
            HSSFColor hc = hb.getCustomPalette().getColor(color);
            ColorUtil ci = ExcelColor2UOF(hc);
            return ci;
        }
        return null;
    }

    /**
     * excel(包含97和2007)中颜色转化为uof颜色
     *
     * @param color
     *            颜色序号
     * @return 颜色或者null
     */
    public static ColorUtil ExcelColor2UOF(Color color) {
        if (color == null) {
            return null;
        }
        ColorUtil ci = null;
        if (color instanceof XSSFColor) {
            // .xlsx
            XSSFColor xc = (XSSFColor)color;
            byte[] b = xc.getRGB();
            if (b != null) {
                if (b.length == 3) {
                    ci = ColorUtil.fromARGB(b[0] & 0xFF, b[1] & 0xFF, b[2] & 0xFF);
                } else if (b.length == 4) {
                    ci = ColorUtil.fromARGB(b[0] & 0xFF, b[1] & 0xFF, b[2] & 0xFF, b[3] & 0xFF);
                }
            }
        } else if (color instanceof HSSFColor) {
            // .xls
            HSSFColor hc = (HSSFColor)color;
            short[] s = hc.getTriplet();
            if (s != null) {
                ci = ColorUtil.fromARGB(s[0], s[1], s[2]);
            }
        }
        return ci;
    }

    @Override
    public String toString() {
        return "ColorInfo{" + "A=" + A + ", R=" + R + ", G=" + G + ", B=" + B + '}';
    }
}
