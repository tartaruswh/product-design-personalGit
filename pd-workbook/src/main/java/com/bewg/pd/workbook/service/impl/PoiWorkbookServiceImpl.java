//package com.bewg.pd.workbook.service.impl;
//
//import com.bewg.pd.common.entity.excel.*;
//import com.bewg.pd.common.entity.req.WorkbookCalcReq;
//import com.bewg.pd.common.enums.DirectionEnum;
//import com.bewg.pd.common.enums.GroupTypeEnum;
//import com.bewg.pd.common.enums.StageEnum;
//import com.bewg.pd.common.util.ExcelUtil;
//import com.bewg.pd.workbook.constant.WorkbookConstant;
//import com.bewg.pd.workbook.service.IWorkbookService;
//import com.bewg.pd.workbook.utils.ColorUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.poi.ss.usermodel.CellType;
//import org.apache.poi.xssf.usermodel.*;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.bewg.pd.workbook.constant.WorkbookConstant.*;
//
///**
// * <p>
// * excel操作POI实现类
// * </p>
// *
// * @author tianzhitao
// * @since 2021-10-22
// */
//@Slf4j
//@Component
//public class PoiWorkbookServiceImpl implements IWorkbookService {
//
//    /**
//     * 解析excel
//     *
//     * @param fis
//     * @return
//     */
//    @Override
//    public Sheet parseExcel(InputStream fis) {
//
//        // sheet页
//        Sheet sheet = new Sheet();
//
//        // 业务组
//        List<Group> groups = new ArrayList<>();
//        sheet.setGroups(groups);
//
//        // 参数列表
//        List<Parameter> allParameters = new ArrayList<>();
//        sheet.setAllParameters(allParameters);
//
//        // 处理阶段
//        StageEnum currentStage = StageEnum.UNKNOWN;
//
//        int rowNum = 0;
//        Group group = null;
//        Table table = null;
//        Parameter parameter;
//        boolean inTable = false;
//
//        XSSFWorkbook workbook;
//        try {
//            workbook = new XSSFWorkbook(fis);
//        } catch (IOException e) {
//            log.error("无法获取到计算书流!", e);
//            return sheet;
//        }
//
//        // 默认第一个sheet页为数据模型页
//        XSSFSheet targetSheet = workbook.getSheetAt(0);
//
//        while (true) {
//            // 当前行
//            XSSFRow currentRow = targetSheet.getRow(rowNum++);
//            try {
//
//                // 文件结束
//                if (currentRow == null) {
//                    break;
//                    // 出现空行
//                } else if (isRowEmpty(currentRow)) {
//                    continue;
//                }
//
//                Object firstCellValue = getCellValue(currentRow.getCell(0));
//
//                // 异常行
//                if (firstCellValue == null) {
//                    continue;
//                }
//
//                // 获取阶段信息
//                if (firstCellValue.toString().indexOf(StageEnum.DESIGN_INPUT.getDesc()) > 0) {
//                    currentStage = StageEnum.DESIGN_INPUT;
//                    continue;
//                } else if (firstCellValue.toString().indexOf(StageEnum.DESIGN_OUTPUT.getDesc()) > 0) {
//                    currentStage = StageEnum.DESIGN_OUTPUT;
//                    continue;
//                }
//
//                // 读取参数分组
//                if (firstCellValue.toString().startsWith(WorkbookConstant.WELL_SYMBOL)) {
//
//                    // 新建业务分组
//                    group = new Group();
//
//                    // 设置分组名称
//                    group.setName(firstCellValue.toString());
//
//                    // 设置分组业务阶段
//                    group.setStage(currentStage);
//                    groups.add(group);
//
//                    // 配置清单组
//                    if (group.getName().startsWith((WorkbookConstant.TABLE_START_SYMBOL))) {
//                        group.setType(GroupTypeEnum.CONFIG_LIST);
//                        // 创建表格
//                        table = new Table();
//                        // 设置表格名称
//                        table.setName(group.getName().replace(WorkbookConstant.TABLE_START_SYMBOL, ""));
//                        // 解析表头
//                        table.setColumns(parseHead(targetSheet.getRow(rowNum++)));
//                        group.setTable(table);
//                        inTable = true;
//                        // 普通参数组, 跳过分组头
//                    } else {
//                        group.setType(GroupTypeEnum.PARAMETER);
//                        rowNum++;
//                        inTable = false;
//                    }
//                    continue;
//                }
//
//                // 配置清单组的行
//                if (inTable) {
//                    table.getRows().add(parseRow(table, currentRow));
//                    // 普通参数组的行
//                } else {
//                    // 读取参数
//                    parameter = new Parameter();
//
//                    // 参数名
//                    XSSFCell nameCell = currentRow.getCell(COMMON_CELL_NAME_INDEX);
//                    parameter.setParamName(nameCell.getStringCellValue().trim());
//
//                    // 参数值
//                    XSSFCell valueCell = currentRow.getCell(COMMON_CELL_VALUE_INDEX);
//                    parameter.setValue(getCellValue(valueCell));
//
//                    // 是否为公式项
//                    parameter.setFormula(valueCell.getCellType() == CellType.FORMULA);
//
//                    // 坐标
//                    parameter.setCoordinate(ExcelUtil.fromIndexToAlpha(valueCell.getColumnIndex()) + (valueCell.getRowIndex() + 1));
//
//                    // 根据单元格底色判断输入参数是否为谨慎调整项
//                    XSSFColor cellBackgroundColor = valueCell.getCellStyle().getFillBackgroundXSSFColor();
//
//                    // 存在颜色即为输入项
//                    if (cellBackgroundColor != null) {
//                        parameter.setDirection(DirectionEnum.IN);
//                        XSSFColor cellForegroundColor = valueCell.getCellStyle().getFillForegroundXSSFColor();
//                        ColorUtil colorInfo = ColorUtil.ExcelColor2UOF(cellForegroundColor);
//                        // 普通入参
//                        if (colorInfo.R == COMMON_IN_R && colorInfo.G == COMMON_IN_G && colorInfo.B == COMMON_IN_B) {
//                            parameter.setCautious(true);
//                            // 谨慎调整项入参
//                        } else if (colorInfo.R == CAUTIOUS_IN_R && colorInfo.G == CAUTIOUS_IN_G && colorInfo.B == CAUTIOUS_IN_B) {
//                            parameter.setCautious(false);
//                        }
//                    } else {
//                        parameter.setDirection(DirectionEnum.OUT);
//                    }
//
//                    // 单位
//                    XSSFCell unitCell = currentRow.getCell(COMMON_CELL_UNIT_INDEX);
//                    if (unitCell != null) {
//                        parameter.setUnit(unitCell.getStringCellValue());
//                    }
//
//                    // 注释
//                    XSSFCell commentCell = currentRow.getCell(COMMON_CELL_COMMENT_INDEX);
//                    if (commentCell != null) {
//                        parameter.setComment(commentCell.getStringCellValue());
//                    }
//
//                    // 将读取到的参数对象添加至集合
//                    if (group != null) {
//                        group.getParameters().add(parameter);
//                    }
//                    allParameters.add(parameter);
//
//                    // 读取判断性参数
//                    if (hasJudgeParameter(currentRow)) {
//                        Parameter judgeParameter = readJudgeParaInfo(currentRow);
//                        if (group != null) {
//                            group.getParameters().add(judgeParameter);
//                        }
//                        allParameters.add(judgeParameter);
//                    }
//                }
//            } catch (Exception e) {
//                log.error("解析excel出错, 第{}行数据异常! currentRow={}", rowNum, currentRow, e);
//            }
//        }
//
//        return sheet;
//    }
//
//    /**
//     * 判断该行是否有判断性参数
//     *
//     * @param row
//     * @return
//     */
//    private boolean hasJudgeParameter(XSSFRow row) {
//
//        XSSFCell nameCell = row.getCell(JUDGE_CELL_NAME_INDEX);
//
//        XSSFCell valueCell = row.getCell(JUDGE_CELL_VALUE_INDEX);
//
//        if (nameCell == null || valueCell == null) {
//            return false;
//        }
//
//        try {
//            String name = "", value = "";
//
//            if (nameCell.getCellType() == CellType.STRING) {
//                name = nameCell.getStringCellValue();
//            }
//
//            if (valueCell.getCellType() == CellType.NUMERIC) {
//                value = String.valueOf(valueCell.getNumericCellValue());
//            } else if (valueCell.getCellType() == CellType.FORMULA) {
//                value = valueCell.getRawValue();
//            }
//
//            if (!"".equals(name.trim()) && !"".equals(value.trim())) {
//                return true;
//            }
//        } catch (Exception e) {
//            return false;
//        }
//
//        return false;
//    }
//
//    /**
//     * 读取判断性参数信息
//     *
//     * @param row
//     * @return
//     */
//    private Parameter readJudgeParaInfo(XSSFRow row) {
//
//        XSSFCell judgeNameCell = row.getCell(JUDGE_CELL_NAME_INDEX);
//
//        XSSFCell judgeValueCell = row.getCell(JUDGE_CELL_VALUE_INDEX);
//
//        XSSFCell judgeUnitCell = row.getCell(JUDGE_CELL_UNIT_INDEX);
//
//        XSSFCell judgeCommentCell = row.getCell(JUDGE_CELL_COMMENT_INDEX);
//
//        XSSFCell judgeMinCell = row.getCell(JUDGE_CELL_MIN_INDEX);
//
//        XSSFCell judgeMaxCell = row.getCell(JUDGE_CELL_MAX_INDEX);
//
//        Parameter parameter = new Parameter();
//
//        // 名称
//        if (judgeNameCell.getCellType() == CellType.STRING) {
//            parameter.setParamName(judgeNameCell.getStringCellValue());
//        }
//
//        // 值
//        if (judgeValueCell.getCellType() == CellType.NUMERIC) {
//            parameter.setValue(judgeValueCell.getNumericCellValue());
//        } else if (judgeValueCell.getCellType() == CellType.FORMULA) {
//            parameter.setValue(judgeValueCell.getRawValue());
//        }
//
//        // 单位
//        if (judgeUnitCell.getCellType() == CellType.STRING) {
//            parameter.setUnit(judgeUnitCell.getStringCellValue());
//        }
//
//        // 注释
//        if (judgeCommentCell.getCellType() == CellType.STRING) {
//            parameter.setComment(judgeCommentCell.getStringCellValue());
//        }
//
//        // 最小值
//        if (judgeMinCell.getCellType() == CellType.NUMERIC) {
//            parameter.setMinimum(new BigDecimal(Double.toString(judgeMinCell.getNumericCellValue())));
//        } else if (judgeMinCell.getCellType() == CellType.FORMULA) {
//            String rawValue = judgeMinCell.getRawValue();
//            if (!"".equals(rawValue.trim())) {
//                parameter.setMinimum(new BigDecimal(rawValue));
//            } else {
//                parameter.setMinimum(new BigDecimal(Double.toString(Double.MIN_VALUE)));
//            }
//        } else {
//            parameter.setMinimum(new BigDecimal(Double.toString(Double.MIN_VALUE)));
//        }
//
//        // 最大值
//        if (judgeMaxCell.getCellType() == CellType.NUMERIC) {
//            parameter.setMaximum(new BigDecimal(Double.toString(judgeMaxCell.getNumericCellValue())));
//        } else if (judgeMaxCell.getCellType() == CellType.FORMULA) {
//            String rawValue = judgeMaxCell.getRawValue();
//            if (!"".equals(rawValue.trim())) {
//                parameter.setMaximum(new BigDecimal(rawValue));
//            } else {
//                parameter.setMaximum(new BigDecimal(Double.toString(Double.MAX_VALUE)));
//            }
//        } else {
//            parameter.setMaximum(new BigDecimal(Double.toString(Double.MAX_VALUE)));
//        }
//
//        parameter.setDirection(DirectionEnum.OUT);
//        parameter.setJudge(true);
//
//        return parameter;
//    }
//
//
//    /**
//     * 获取单元格值
//     *
//     * @param cell
//     * @return
//     */
//    private static Object getCellValue(XSSFCell cell) {
//
//        if (cell == null) {
//            return null;
//        }
//
//        if (cell.getCellType() == CellType.NUMERIC) {
//            return cell.getNumericCellValue();
//        } else if (cell.getCellType() == CellType.STRING) {
//            return cell.getStringCellValue();
//        } else if (cell.getCellType() == CellType.FORMULA) {
//            String formula = cell.getCellFormula();
//            if (formula.contains("INDIRECT")) {
//                log.info("===> Formula_INDIRECT = " + formula);
//            }
//            return cell.getRawValue();
//        } else if (cell.getCellType() == CellType.BLANK) {
//            return "";
//        } else {
//            return null;
//        }
//    }
//
//    /**
//     * 判断行是否为空, 逻辑: 是否所有列都为空
//     *
//     * @param row
//     * @return
//     */
//    private static boolean isRowEmpty(XSSFRow row) {
//
//        if (row == null) {
//            return true;
//        }
//
//        // 第一个列位置
//        int firstCellIndex = row.getFirstCellNum();
//
//        // 最后一个列位置
//        int lastCellIndex = row.getLastCellNum();
//
//        // 空列数量
//        int nullCellNum = 0;
//
//        for (int i = firstCellIndex; i < lastCellIndex; i++) {
//            Object value = getCellValue(row.getCell(i));
//            if (value == null || "".equals(value.toString().trim())) {
//                nullCellNum++;
//            }
//        }
//
//        return nullCellNum == (lastCellIndex - firstCellIndex);
//    }
//
//    /**
//     * 解析表头
//     *
//     * @param currentRow
//     */
//    private List<Column> parseHead(XSSFRow currentRow) {
//
//        List<Column> columns = new ArrayList<>();
//
//        int columnStart = currentRow.getFirstCellNum();
//
//        int columnEnd = currentRow.getLastCellNum();
//
//        for (int index = columnStart; index < columnEnd; index++) {
//            Object columnName = getCellValue(currentRow.getCell(index));
//            Column column = new Column();
//            column.setIndex(index);
//            // 空列
//            if (columnName == null || "".equals(columnName.toString())) {
//                continue;
//            } else {
//                column.setName(columnName.toString());
//            }
//            columns.add(column);
//        }
//        return columns;
//    }
//
//    /**
//     * 解析数据行
//     *
//     * @param currentRow
//     */
//    private Row parseRow(Table table, XSSFRow currentRow) {
//
//        Row row = new Row(table.getColumns().size());
//
//        for (int i = 0; i < table.getColumns().size(); i++) {
//            int columnIndex = table.getColumns().get(i).getIndex();
//            Object value = getCellValue(currentRow.getCell(columnIndex));
//            if (value == null) {
//                value = "";
//            }
//            row.getValues()[i] = value.toString();
//        }
//        return row;
//    }
//
//    /**
//     * 获取excel外部引用文件列表
//     *
//     * @param fis
//     * @return
//     */
//    @Override
//    public List<String> getRefFileNames(InputStream fis) {
//
//
//        return null;
//    }
//
//    /**
//     * 创建计算书上下文实例 todo 后期参数需要改成filepath
//     *
//     * @param fis
//     * @return
//     */
//    @Override
//    public String createContext(InputStream fis) {
//
//
//        return null;
//    }
//
//    /**
//     * 根据入参驱动excel计算
//     *
//     * @param workbookCalcReq
//     * @return
//     */
//    @Override
//    public Sheet calculate(WorkbookCalcReq workbookCalcReq) {
//
//
//        return null;
//    }
//}
