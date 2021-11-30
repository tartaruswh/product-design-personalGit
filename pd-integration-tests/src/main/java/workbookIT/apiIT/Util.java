package workbookIT.apiIT;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 *
 * @Title: DateTimeUtil.java
 * @Description: 获取时间方法
 * @author: maxiaokui
 * @date: 2021年5月5日 下午3:56:11
 * @version V1.0
 *
 */
public class Util {
    /**
     *
     * @Title: getTimeTmp1 @Description: 获取当前的时间戳 @param: @return @return: long @throws
     */
    public static long getTimeTmp1() {
        Date date = new Date();
        return date.getTime();
    }

    /**
     *
     * @Title: getTime @Description: 获取当前的时间 @param: @return @return: String @throws
     */
    public static String getTime() {
        return DateFormat.getInstance().format(new Date());
    }

    /**
     *
     * @Title: getDate @Description: 获取当前日期 @param: @return @return: String @throws
     */
    public static String getDate() {
        return DateFormat.getDateInstance().format(new Date());
    }

    /**
     *
     * @Title: getDateTime @Description: 获取当前的日期时间 @param: @return @return: String @throws
     */
    public static String getDateTime() {
        return DateFormat.getDateTimeInstance().format(new Date());
    }

    /**
     *
     * @Title: getPatternDateTime @Description: 获取当前的定制日期时间格式 @param: @param pattern @param: @return @return: String @throws
     */
    public static String getPatternDateTime(String pattern) {
        return new SimpleDateFormat(pattern).format(new Date());
    }

    public static void main(String[] args) {
        System.out.println(new SimpleDateFormat("yyMMddhhmmssSS").format(new Date()));
    }

}
