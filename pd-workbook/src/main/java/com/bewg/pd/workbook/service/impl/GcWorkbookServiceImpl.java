package com.bewg.pd.workbook.service.impl;

import com.bewg.pd.common.entity.excel.*;
import com.bewg.pd.common.entity.excel.Parameter;
import com.bewg.pd.common.entity.req.WorkbookCalcReq;
import com.bewg.pd.common.entity.req.WorkbookContextReq;
import com.bewg.pd.common.entity.vo.Result;
import com.bewg.pd.common.enums.DirectionEnum;
import com.bewg.pd.common.enums.GroupTypeEnum;
import com.bewg.pd.common.enums.StageEnum;
import com.bewg.pd.common.util.ExcelUtil;
import com.bewg.pd.common.util.IdWorkerUtil;
import com.bewg.pd.workbook.cache.WorkbookCacheManager;
import com.bewg.pd.workbook.constant.WorkbookConstant;
import com.bewg.pd.workbook.service.IWorkbookService;
import com.bewg.pd.workbook.utils.FileUtil;
import com.grapecity.documents.excel.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

import static com.bewg.pd.workbook.constant.WorkbookConstant.*;

/**
 * <p>
 * excel操作gcexcel实现类
 * </p>
 *
 * @author tianzhitao
 * @since 2021-10-22
 */
@Slf4j
@Component
public class GcWorkbookServiceImpl implements IWorkbookService {

    @Value("${workbook.contextDir}")
    private String contextDir;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private String downloadPrefix = "http://10.10.41.3:8080/";

    /**
     * 解析excel
     *
     * @param workbook
     * @return
     */
    @Override
    public Result parseExcel(Workbook workbook) {

        // sheet页
        Sheet sheet = new Sheet();

        // 业务组
        List<Group> groups = new ArrayList<>();
        sheet.setGroups(groups);

        // 图例
        Legend legend = new Legend();
        sheet.setLegend(legend);

        // 处理阶段
        StageEnum currentStage = StageEnum.UNKNOWN;

        int rowNum = 0;
        Group group = null;
        Table table = null;
        boolean inTable = false;

        IWorksheet worksheet = workbook.getWorksheets().get(0);

        while (true) {
            try {

                boolean isBlankRow = isRowEmpty(worksheet, rowNum);

                // 空行
                if (isBlankRow) {
                    log.debug("文件读取结束, 行号{}", rowNum);
                    break;
                }

                // 用于处理配置清单内容(配置清单中, 第一列合并单元格后, 非第一行值为null)
                Object firstCellValue = worksheet.getRange(rowNum, 0).getValue();

                // 获取阶段信息
                if (firstCellValue != null && firstCellValue.toString().indexOf("设计输入") > 0) {
                    currentStage = StageEnum.DESIGN_INPUT;
                    rowNum++;
                    continue;
                } else if (firstCellValue != null && firstCellValue.toString().indexOf("设计输出") > 0) {
                    currentStage = StageEnum.DESIGN_OUTPUT;
                    rowNum++;
                    continue;
                }

                // 读取参数分组
                if (firstCellValue != null && firstCellValue.toString().startsWith(WorkbookConstant.WELL_SYMBOL)) {

                    // 新建业务分组
                    group = new Group();

                    // 设置分组业务阶段
                    group.setStage(currentStage);
                    groups.add(group);
                    // 设置分组名称
                    group.setName(getFilteredGroupName(firstCellValue.toString()));

                    // 配置清单组
                    if (firstCellValue.toString().startsWith((WorkbookConstant.TABLE_START_SYMBOL))) {

                        // 记录配置清单开始位置
                        if (sheet.getTableStartIndex() == 0) {
                            sheet.setTableStartIndex(rowNum);
                        }

                        group.setType(GroupTypeEnum.CONFIG_LIST);
                        table = new Table();
                        // 表名
                        table.setName(group.getName());
                        // 表头
                        table.setColumns(parseHead(++rowNum, worksheet));
                        // 表格内容
                        group.setTable(table);
                        inTable = true;
                        // 普通参数组, 跳过分组头
                    } else {
                        group.setType(GroupTypeEnum.PARAMETER);
                        rowNum++;
                        inTable = false;
                    }

                    rowNum++;
                    continue;
                }

                // 配置清单组的行
                if (inTable) {
                    table.getRows().add(parseRow(table, rowNum, worksheet));
                    // 普通参数组的行
                } else {

                    // 处理普通参数----开始
                    ParameterC parameterC = new ParameterC();

                    // 参数名
                    IRange nameCell = worksheet.getRange(rowNum, COMMON_CELL_NAME_INDEX);
                    parameterC.setParamName(getCellValue(nameCell));

                    // 参数值
                    IRange valueCell = worksheet.getRange(rowNum, COMMON_CELL_VALUE_INDEX);
                    parameterC.setValue(getCellValue(valueCell));

                    // 是否为公式项
                    parameterC.setFormula(valueCell.getHasFormula());

                    // 公式项内容
                    if (parameterC.isFormula()) {
                        parameterC.setFormulaValue(valueCell.getFormula());
                    }

                    // 坐标
                    if (valueCell != null) {
                        parameterC.setCoordinate(valueCell.getAddress().replaceAll("\\$", ""));
                    }

                    // 根据单元格底色判断输入参数是否为谨慎调整项
                    Color colorInfo = valueCell.getDisplayFormat().getInterior().getColor();

                    parameterC.setDirection(DirectionEnum.OUT);
                    // 普通入参
                    if (colorInfo.getR() == COMMON_IN_R && colorInfo.getG() == COMMON_IN_G && colorInfo.getB() == COMMON_IN_B) {
                        parameterC.setRequired(true);
                        parameterC.setDirection(DirectionEnum.IN);
                        // 谨慎调整项入参
                    } else if (colorInfo.getR() == CAUTIOUS_IN_R && colorInfo.getG() == CAUTIOUS_IN_G && colorInfo.getB() == CAUTIOUS_IN_B) {
                        parameterC.setCautious(true);
                        parameterC.setDirection(DirectionEnum.IN);
                    }

                    // 单位
                    IRange unitCell = worksheet.getRange(rowNum, COMMON_CELL_UNIT_INDEX);
                    parameterC.setUnit(getCellValue(unitCell));

                    // 注释
                    IRange commentCell = worksheet.getRange(rowNum, COMMON_CELL_COMMENT_INDEX);
                    parameterC.setComment(getCellValue(commentCell));

                    // 参数放置到组中
                    if (group != null) {
                        group.getParameter().getParameterC().add(parameterC);
                    }
                    // 处理普通参数----结束


                    // 处理判断性参数----开始
                    if (group != null && hasJudgeParameter(rowNum, worksheet)) {
                        ParameterJ parameterJ = readJudgeParaInfo(rowNum, worksheet);
                        group.getParameter().getParameterJ().add(parameterJ);
                    }
                    // 处理判断性参数----结束
                }

            } catch (Exception e) {
                log.error("解析excel出错, 第{}行数据异常! ", rowNum, e);
            }
            rowNum++;

        }
        return Result.ok(sheet);
    }

    /**
     * 判断行是否为空, 逻辑: 是否所有列都为空
     *
     * @param worksheet
     * @param rowNum
     * @return
     */
    private static boolean isRowEmpty(IWorksheet worksheet, int rowNum) {

        int columnCount = worksheet.getColumns().getColumnCount();

        // 空列数量
        int nullCellNum = 0;

        for (int i = 0; i < columnCount; i++) {
            Object value = worksheet.getRange(rowNum, i).getValue();
            if (value == null || "".equals(value.toString().trim())) {
                nullCellNum++;
            }
        }

        return nullCellNum == columnCount;
    }

    /**
     * 判断该行是否有判断性参数
     *
     * @param rowNum
     * @param worksheet
     * @return
     */
    private boolean hasJudgeParameter(int rowNum, IWorksheet worksheet) {

        IRange nameCell = worksheet.getRange(rowNum, JUDGE_CELL_NAME_INDEX);

        IRange valueCell = worksheet.getRange(rowNum, JUDGE_CELL_VALUE_INDEX);

        if (nameCell.getValue() == null || valueCell.getValue() == null) {
            return false;
        }

        try {
            String name = nameCell.getValue().toString(), value = valueCell.getValue().toString();
            if (!"".equals(name.trim()) && !"".equals(value.trim()) && ExcelUtil.isNumericCell(value.trim())) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }

    /**
     * 读取判断性参数信息
     *
     * @param rowNum
     * @param worksheet
     * @return
     */
    private ParameterJ readJudgeParaInfo(int rowNum, IWorksheet worksheet) {

        IRange judgeNameCell = worksheet.getRange(rowNum, JUDGE_CELL_NAME_INDEX);

        IRange judgeValueCell = worksheet.getRange(rowNum, JUDGE_CELL_VALUE_INDEX);

        IRange judgeUnitCell = worksheet.getRange(rowNum, JUDGE_CELL_UNIT_INDEX);

        IRange judgeCommentCell = worksheet.getRange(rowNum, JUDGE_CELL_COMMENT_INDEX);

        IRange judgeMinCell = worksheet.getRange(rowNum, JUDGE_CELL_MIN_INDEX);

        IRange judgeMaxCell = worksheet.getRange(rowNum, JUDGE_CELL_MAX_INDEX);

        ParameterJ parameterJ = new ParameterJ();

        // 名称
        parameterJ.setParamName(getCellValue(judgeNameCell));

        // 值
        parameterJ.setValue(getCellValue(judgeValueCell));

        // 单位
        parameterJ.setUnit(getCellValue(judgeUnitCell));

        // 注释
        parameterJ.setComment(getCellValue(judgeCommentCell));

        // 坐标
        parameterJ.setCoordinate(judgeValueCell.getAddress().replaceAll("\\$", ""));

        // 最小值
        if (judgeMinCell == null || judgeMinCell.getValue() == null || "".equals(judgeMinCell.getValue().toString().trim())) {
            parameterJ.setMinimum(new BigDecimal(Double.toString(Double.MIN_VALUE)));
        } else {
            parameterJ.setMinimum(new BigDecimal(judgeMinCell.getValue().toString().trim()));
        }

        // 最大值
        if (judgeMaxCell == null || judgeMaxCell.getValue() == null || "".equals(judgeMaxCell.getValue().toString().trim())) {
            parameterJ.setMaximum(new BigDecimal(Double.toString(Double.MAX_VALUE)));
        } else {
            parameterJ.setMaximum(new BigDecimal(judgeMaxCell.getValue().toString().trim()));
        }

        // todo 数值是否产生报警
        parameterJ.setAlarm(false);

        return parameterJ;
    }

    /**
     * 获取单元格值
     *
     * @param range
     * @return
     */
    private static String getCellValue(IRange range) {

        if (range == null) {
            return null;
        }

        if (range.getValue() == null) {
            return null;
        }

        return range.getValue().toString().trim();
    }

    /**
     * 解析表头
     *
     * @param rowNum
     * @param worksheet
     * @return
     */
    private List<Column> parseHead(int rowNum, IWorksheet worksheet) {

        List<Column> head = new ArrayList<>();
        int columnCount = worksheet.getColumnCount();
        for (int index = 0; index < columnCount; index++) {
            Object columnName = worksheet.getRange(rowNum, index).getValue();
            Column column = new Column();
            // 过滤掉空列
            if (columnName == null || "".equals(columnName.toString())) {
                continue;
            }
            column.setName(columnName.toString());
            column.setIndex(index);
            head.add(column);
        }
        return head;
    }

    /**
     * 解析数据行
     *
     * @param table
     * @param rowNum
     * @param worksheet
     * @return
     */
    private Row parseRow(Table table, int rowNum, IWorksheet worksheet) {

        Row row = new Row();
        for (int i = 0; i < table.getColumns().size(); i++) {
            int columnIndex = table.getColumns().get(i).getIndex();
            IRange range = worksheet.getRange(rowNum, columnIndex);
            String value = "";
            if (range != null && range.getValue() != null && !"".equals(range.getValue().toString())) {
                value = range.getValue().toString();
            }
            row.getValues().add(value);
        }
        return row;
    }

    /**
     * 获取过滤后的组名
     *
     * @param cellValue
     * @return
     */
    private String getFilteredGroupName(String cellValue) {

        if (StringUtils.isEmpty(cellValue)) {
            return "";
        }

        cellValue = cellValue.replaceAll(WorkbookConstant.TABLE_START_SYMBOL, "");
        cellValue = cellValue.replaceAll(WorkbookConstant.WELL_SYMBOL, "");
        cellValue = cellValue.replaceAll(WorkbookConstant.CHINESE_COLON, "");
        cellValue = cellValue.replaceAll(WorkbookConstant.ENGLISH_COLON, "");
        cellValue = cellValue.replaceAll(" ", "");

        return cellValue;
    }

    /**
     * 获取excel外部引用文件列表
     *
     * @param fis
     * @return
     */
    @Override
    public Result getRefFileNames(InputStream fis) {
        Workbook workbook = new Workbook();
        workbook.open(fis);
        return Result.ok(workbook.getExcelLinkSources());
    }

    /**
     * 创建计算书上下文实例
     *
     * @param workbookContextReq
     * @return
     */
    @Override
    public synchronized Result createContext(WorkbookContextReq workbookContextReq) {

        // 1、生成上下文编号并创建目录
        IdWorkerUtil idWorkerUtil = new IdWorkerUtil();
        String contextId = String.valueOf(idWorkerUtil.nextId());
        String workDir = this.contextDir + File.separator + contextId;

        boolean isCreated = FileUtil.createDir(workDir);
        if (!isCreated) {
            log.error("计算书上下文目录创建失败! contextId={}", contextId);
            return Result.error("计算书上下文目录创建失败!");
        }

        // 2、将计算书文件及依赖文件拷贝到上下文目录
        List<Map<String, String>> localDocLinkPath = new ArrayList<>();
        String localDocPath = workDir + File.separator + workbookContextReq.getDocName() + "." + FileUtil.getFileSuffix(workbookContextReq.getDocPath());
        try {
            FileUtils.copyURLToFile(new URL(downloadPrefix + workbookContextReq.getDocPath()), new File(localDocPath));
            for (WorkbookContextReq.ParamPair paramPair : workbookContextReq.getAssociatedDocPath()) {
                FileUtils.copyURLToFile(new URL(downloadPrefix + paramPair.getDocPath()), new File(workDir + File.separator + paramPair.getDocName() + "." + FileUtil.getFileSuffix(paramPair.getDocPath())));

                Map<String, String> docNameToPath = new HashMap<>();
                String fileName = paramPair.getDocName() + "." + FileUtil.getFileSuffix(paramPair.getDocPath());
                docNameToPath.put(fileName, workDir + File.separator + fileName);
                localDocLinkPath.add(docNameToPath);
            }
        } catch (IOException e) {
            log.error("复制计算书相关资源失败! docPath={} associatedDocPath={}", workbookContextReq.getDocPath(), workbookContextReq.getAssociatedDocPath(), e);
            return Result.error("复制计算书相关资源失败!");
        }

        // 3、封装上下文对象
        WorkbookContext context = new WorkbookContext();
        context.setContextId(contextId);
        context.setProductMemberId(workbookContextReq.getProductMemberId());
        context.setWorkDir(workDir);
        context.setDocPath(workbookContextReq.getDocPath());
        context.setDocName(workbookContextReq.getDocName());
        context.setLocalDocPath(localDocPath);
        context.setAssociatedDocs(workbookContextReq.getAssociatedDocPath());

        // 4、缓存计算书及其外部引用文件
        Workbook workbook = new Workbook();
        workbook.open(context.getLocalDocPath());

        for (Map<String, String> linkDocMap : localDocLinkPath) {
            Set<String> nameSet = linkDocMap.keySet();
            for (String name : nameSet) {
                Workbook temp = new Workbook();
                temp.open(linkDocMap.get(name));
                workbook.updateExcelLink(name, temp);
            }
        }

        WorkbookCacheManager.cacheWorkbook(contextId, workbook);
        context.setCalcResult((Sheet) this.parseExcel(workbook).getResult());

        // 5、持久化上下文对象到redis中
        redisTemplate.opsForValue().set(WORKBOOK_CONTEXT_PREFIX + contextId, context);

        return Result.ok(context.getContextId());
    }

    /**
     * 根据入参驱动excel计算
     *
     * @param workbookCalcReq
     * @return
     */
    @Override
    public synchronized Result calculate(WorkbookCalcReq workbookCalcReq) {


        // 1、加载计算上下文
        WorkbookContext workbookContext = (WorkbookContext) redisTemplate.opsForValue().get(WORKBOOK_CONTEXT_PREFIX + workbookCalcReq.getContextId());

        if (workbookContext == null) {
            return Result.error("无效上下文编号!");
        }

        // 2、加载计算书
        Workbook workbook = WorkbookCacheManager.getWorkbookCache(workbookCalcReq.getContextId());
        if (workbook == null) {
            return Result.error("无效上下文编号, 请求服务节点不正确!");
        }

        IWorksheet worksheet = workbook.getWorksheets().get(0);

        // 3、已调整过谨慎调整参数后, 再次调整必须输入参数, 清空谨慎调整参数
        if (workbookCalcReq.getReqParam().isRequired() && workbookContext.getCautiousParams() != null) {

            if (workbookContext.getRequiredParams() == null) {
                workbookContext.setRequiredParams(new HashMap<>());
            }

            // 记录必须输入参数
            workbookContext.getRequiredParams().put(workbookCalcReq.getReqParam().getCoordinate(), workbookCalcReq.getReqParam().getVal());

            // 清空谨慎调整参数
            workbookContext.setCautiousParams(null);

            String localDocPath = workbookContext.getWorkDir() + File.separator + workbookContext.getDocName() + "." + FileUtil.getFileSuffix(workbookContext.getDocPath());
            Workbook newWorkbook = new Workbook();
            newWorkbook.open(localDocPath);

            // 3、驱动计算
            this.driveCalc(newWorkbook, workbookContext.getRequiredParams());

            // 4、更新计算书及其解析结果
            WorkbookCacheManager.cacheWorkbook(workbookContext.getContextId(), newWorkbook);
            workbookContext.setCalcResult((Sheet) this.parseExcel(newWorkbook).getResult());

            // 5、更新计算结果
            this.refreshSheet(newWorkbook, workbookContext.getCalcResult());

            // 6、更新上下文
            redisTemplate.opsForValue().set(WORKBOOK_CONTEXT_PREFIX + workbookCalcReq.getContextId(), workbookContext);

            return Result.ok(workbookContext.getCalcResult());
        }

        // 3、驱动计算
        Map<String, Object> params = new HashMap<>();
        params.put(workbookCalcReq.getReqParam().getCoordinate(), workbookCalcReq.getReqParam().getVal());

        this.driveCalc(workbook, params);

        // 4、更新计算结果
        if (workbookCalcReq.getReqParam().isRequired()) {
            if (workbookContext.getRequiredParams() == null) {
                workbookContext.setRequiredParams(new HashMap<>());
            }
            workbookContext.getRequiredParams().put(workbookCalcReq.getReqParam().getCoordinate(), workbookCalcReq.getReqParam().getVal());
        } else {
            if (workbookContext.getCautiousParams() == null) {
                workbookContext.setCautiousParams(new HashMap<>());
            }
            workbookContext.getCautiousParams().put(workbookCalcReq.getReqParam().getCoordinate(), workbookCalcReq.getReqParam().getVal());
        }
        this.refreshSheet(workbook, workbookContext.getCalcResult());

        // 5、更新上下文
        redisTemplate.opsForValue().set(WORKBOOK_CONTEXT_PREFIX + workbookCalcReq.getContextId(), workbookContext);

        log.info("B6  :" + worksheet.getRange("B6").getValue());
        log.info("B7  :" + worksheet.getRange("B7").getValue());
        log.info("B8  :" + worksheet.getRange("B8").getValue());
        log.info("B45 :" + worksheet.getRange("B45").getValue());
        log.info("B78 :" + worksheet.getRange("B78").getValue());
        log.info("B106:" + worksheet.getRange("B106").getValue());
        log.info("-------------------------------------------------");

        return Result.ok(workbookContext.getCalcResult());
    }

    /**
     * 驱动计算
     *
     * @param workbook 计算书
     * @param params   参数
     */
    private void driveCalc(IWorkbook workbook, Map<String, Object> params) {
        IWorksheet worksheet = workbook.getWorksheets().get(0);
        // dirty() method will clear the cached value of the workbook.
        workbook.dirty();
        // enable calc engine.
        workbook.setEnableCalculation(true);
        workbook.calculate();
        Set<String> coordinateSet = params.keySet();
        for (String coordinate : coordinateSet) {
            worksheet.getRange(coordinate).setValue(params.get(coordinate));
        }
    }

    /**
     * 缓存计算结果(输入和输出参数)
     *
     * @param workbook
     * @param sheet
     */
    private void refreshSheet(Workbook workbook, Sheet sheet) {
        IWorksheet worksheet = workbook.getWorksheets().get(0);
        List<Group> groups = sheet.getGroups();
        for (Group group : groups) {
            // 跳过配置清单
            if (!group.getType().name().equals(GroupTypeEnum.PARAMETER.name())) {
                continue;
            }
            Parameter parameter = group.getParameter();
            for (ParameterC parameterC : parameter.getParameterC()) {
                parameterC.setValue(worksheet.getRange(parameterC.getCoordinate()).getValue());
            }
            for (ParameterJ parameterJ : parameter.getParameterJ()) {
                parameterJ.setValue(worksheet.getRange(parameterJ.getCoordinate()).getValue());
            }
        }
        parseConfigTable(workbook, sheet);
    }

    /**
     * 解析配置清单表(驱动计算时使用)
     *
     * @param workbook
     * @param sheet
     */
    private void parseConfigTable(Workbook workbook, Sheet sheet) {

        int rowNum = sheet.getTableStartIndex();
        IWorksheet worksheet = workbook.getWorksheets().get(0);

        Group group;
        Table table = null;
        boolean inTable = false;

        // 用于替换计算结果sheet中的配置清单
        Map<String, Group> groupMap = new HashMap<>();

        while (true) {

            boolean isBlankRow = isRowEmpty(worksheet, rowNum);

            // 空行
            if (isBlankRow) {
                log.debug("文件读取结束, 行号{}", rowNum);
                break;
            }

            Object firstCellValue = worksheet.getRange(rowNum, 0).getValue();

            // 检测到表头, 创建表结构
            if (firstCellValue != null && firstCellValue.toString().startsWith((WorkbookConstant.TABLE_START_SYMBOL))) {
                // 新建业务分组
                group = new Group();
                // 设置分组业务阶段
                group.setStage(StageEnum.DESIGN_OUTPUT);
                // 设置分组名称
                group.setName(getFilteredGroupName(firstCellValue.toString()));
                group.setType(GroupTypeEnum.CONFIG_LIST);
                groupMap.put(group.getName(), group);

                table = new Table();
                // 表名
                table.setName(group.getName());
                // 表头
                table.setColumns(parseHead(++rowNum, worksheet));
                // 表格内容
                group.setTable(table);
                inTable = true;
                rowNum++;
                continue;
            }

            // 读取表格内容
            if (inTable) {
                table.getRows().add(parseRow(table, rowNum, worksheet));
            }
            rowNum++;
        }

        // 替换原有配置清单表
        List<Group> groupList = sheet.getGroups();
        for (int i = 0; i < groupList.size(); i++) {
            Group temp = groupList.get(i);
            if (groupMap.containsKey(temp.getName())) {
                groupList.set(i, groupMap.get(temp.getName()));
            }
        }
    }
}
