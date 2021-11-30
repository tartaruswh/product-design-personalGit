package com.bewg.pd.workbook.constant;

/**
 * <p>
 * 计算书模型常量
 * </p>
 *
 * @author tianzhitao
 * @since 2021-10-22
 */
public interface WorkbookConstant {

    /**
     * 计算书上下文前缀
     */
    String WORKBOOK_CONTEXT_PREFIX = "WORKBOOK:CALCULATION:";

    /**
     * 井号
     */
    String WELL_SYMBOL = "#";

    /**
     * 表格开始符号
     */
    String TABLE_START_SYMBOL = "#TABLE#";

    /**
     * 中文冒号
     */
    String CHINESE_COLON = "：";

    /**
     * 英文冒号
     */
    String ENGLISH_COLON = ":";


    /**
     * 谨慎调整项入参RGB的R值
     */
    int CAUTIOUS_IN_R = 226;
    /**
     * 谨慎调整项入参RGB的G值
     */
    int CAUTIOUS_IN_G = 238;
    /**
     * 谨慎调整项入参RGB的B值
     */
    int CAUTIOUS_IN_B = 192;


    /**
     * 普通入参RGB的R值
     */
    int COMMON_IN_R = 120;
    /**
     * 普通入参RGB的G值
     */
    int COMMON_IN_G = 189;
    /**
     * 普通入参RGB的B值
     */
    int COMMON_IN_B = 155;


    /**
     * 普通参数名单元格下标
     */
    int COMMON_CELL_NAME_INDEX = 0;

    /**
     * 普通参数值单元格下标
     */
    int COMMON_CELL_VALUE_INDEX = 1;

    /**
     * 普通参数单位单元格下标
     */
    int COMMON_CELL_UNIT_INDEX = 3;

    /**
     * 普通参数注释单元格下标
     */
    int COMMON_CELL_COMMENT_INDEX = 4;


    /**
     * 判断参数名单元格下标
     */
    int JUDGE_CELL_NAME_INDEX = 5;

    /**
     * 判断参数值单元格下标
     */
    int JUDGE_CELL_VALUE_INDEX = 6;

    /**
     * 判断参数单位单元格下标
     */
    int JUDGE_CELL_UNIT_INDEX = 7;

    /**
     * 判断参数备注单元格下标
     */
    int JUDGE_CELL_COMMENT_INDEX = 8;

    /**
     * 判断参数最小值单元格下标
     */
    int JUDGE_CELL_MIN_INDEX = 9;

    /**
     * 判断参数最大值单元格下标
     */
    int JUDGE_CELL_MAX_INDEX = 10;
}
