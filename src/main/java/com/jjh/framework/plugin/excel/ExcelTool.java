package com.jjh.framework.plugin.excel;

import cn.hutool.core.date.DateUtil;
import com.jjh.common.exception.BusinessException;
import com.jjh.common.util.TimeUtils;
import com.jjh.framework.properties.FileProperties;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * Excel相关处理
 * 自定义导出表头，表数据
 *
 * @author jjh
 */
public class ExcelTool
{
    private static final Logger log = LoggerFactory.getLogger(ExcelTool.class);

    /**
     * Excel sheet最大行数，默认65536
     */
    public static final int sheetSize = 65536;

    /**
     * 工作表名称
     */
    private String sheetName;

    /**
     * 工作薄对象
     */
    private Workbook wb;

    /**
     * 工作表对象
     */
    private Sheet sheet;

    /**
     * 样式列表
     */
    private Map<String, CellStyle> styles;


    public ExcelTool()
    {
    }


    /**
     * 创建表格样式
     *
     * @param wb 工作薄对象
     * @return 样式列表
     */
    private Map<String, CellStyle> createStyles(Workbook wb)
    {
        // 写入各条记录,每条记录对应excel表中的一行
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        Font dataFont = wb.createFont();
        dataFont.setFontName("Arial");
        dataFont.setFontHeightInPoints((short) 10);
        style.setFont(dataFont);
        styles.put("data", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = wb.createFont();
        headerFont.setFontName("Arial");
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(headerFont);
        styles.put("header", style);

        return styles;
    }

    /**
     * 创建单元格
     */
    private Cell createCell(String headValue, Row row, int column)
    {
        // 创建列
        Cell cell = row.createCell(column);
        // 写入列信息
        cell.setCellValue(headValue);
        setDataValidation(headValue, row, column);
        cell.setCellStyle(styles.get("header"));
        return cell;
    }


    /**
     * 创建表格样式
     */
    private void setDataValidation(String headValue, Row row, int column)
    {
        if (headValue.indexOf("注：") >= 0)
        {
            sheet.setColumnWidth(column, 6000);
        }
        else
        {
            // 设置列宽
            sheet.setColumnWidth(column, (int) ((16 + 0.72) * 256));
            row.setHeight((short) (14 * 20));
        }
    }



    /**
     * 编码文件名
     */
    public static String encodingFilename(String filename)
    {
        // 文件路径按日期归类
        String dateDir = "excel"+ File.separator
                +DateUtil.thisYear() + File.separator + DateUtil.thisMonth() + File.separator + DateUtil.thisDayOfMonth()+ File.separator ;
        filename = dateDir + filename +  "_" + TimeUtils.getDateTimeFileName() + ".xlsx";
        return filename;
    }

    /**
     * 获取下载路径
     *
     * @param filename 文件名称
     */
    public static String getAbsoluteFile(String filename)
    {
        String downloadPath = FileProperties.getResourcePath() + File.separator + filename;
        File desc = new File(downloadPath);
        if (!desc.getParentFile().exists())
        {
            desc.getParentFile().mkdirs();
        }
        return downloadPath;
    }

    /**
     * 创建一个工作簿
     */
    private void createWorkbook()
    {
        this.wb = new SXSSFWorkbook(500);
    }

    /**
     * 创建工作表
     *
     * @param sheetNo sheet数量
     * @param index 序号
     */
    private void createSheet(double sheetNo, int index)
    {
        this.sheet = wb.createSheet();
        this.styles = createStyles(wb);
        // 设置工作表的名称.
        if (sheetNo == 0)
        {
            wb.setSheetName(index, sheetName);
        }
        else
        {
            wb.setSheetName(index, sheetName + index);
        }
    }

    /**
     * 【自定义扩展方法】
     * 对list数据源将其里面的数据导入到excel表单
     *
     * @param headerRow 表格标题
     * @param dataList 表格数据
     * @author jjh
     * @return 结果
     */
    public String exportExcel(Map<String, String> headerRow, List<Map<String, Object>> dataList)
    {
        return this.exportExcel(headerRow, dataList, "sheet1");
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     *
     * @param headerRow 表格标题
     *                  key 映射字段，value 表头名称
     * @param dataList 表格数据
     *                 key 映射字段，value 表格数据
     * @author jjh
     * @return 结果
     */
    public String exportExcel(Map<String, String> headerRow, Collection<Map<String, Object>> dataList, String sheetName)
    {
        this.sheetName = sheetName;
        createWorkbook();
        OutputStream out = null;
        try
        {
            // 取出一共有多少个sheet.
            double sheetNo = Math.ceil(dataList.size() / sheetSize);
            for (int index = 0; index <= sheetNo; index++)
            {
                createSheet(sheetNo, index);

                // 产生一行
                Row row = sheet.createRow(0);
                int column = 0;
                // 写入各个字段的列头名称
                for (Map.Entry<String, String> headerColumn : headerRow.entrySet())
                {
                    this.createCell(headerColumn.getValue(), row, column++);
                }
                fillExcelData(index, row, headerRow, dataList);
            }
            String filename = encodingFilename(sheetName);
            out = new FileOutputStream(getAbsoluteFile(filename));
            wb.write(out);
            return filename;
        }
        catch (Exception e)
        {
            log.error("导出Excel异常{}", e.getMessage());
            throw new BusinessException("导出Excel失败，请联系网站管理员！");
        }
        finally
        {
            if (wb != null)
            {
                try
                {
                    wb.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 初始化excel表单
     *
     * @author jjh
     * @return 结果
     */
    public void initExcel(String sheetName)
    {
        this.sheetName = sheetName;
        createWorkbook();
    }

    /**
     * 填充excel数据
     *
     * @param index 序号
     * @param row 单元格行
     */
    private void fillExcelData(int index, Row row,Map<String, String> headerRow, Collection<Map<String, Object>> dataList)
    {
        int startNo = index * sheetSize;
        int endNo = Math.min(startNo + sheetSize, dataList.size());
        Iterator<Map<String, Object>> iterator = dataList.iterator();
        for (int i = startNo; i < endNo; i++)
        {
            row = sheet.createRow(i + 1 - startNo);
            // 得到导出对象.
            Map<String, Object> dataRow = iterator.next();
            int column = 0;
            for (Map.Entry<String, String> headerColumn : headerRow.entrySet())
            {
                this.addCell(row, headerColumn, dataRow, column++);
            }
        }
    }

    /**
     * 添加单元格
     */
    private Cell addCell(Row row, Map.Entry<String, String> headerColumn, Map<String, Object> dataRow, int column)
    {
        Cell cell = null;
        try
        {
            // 设置行高
            row.setHeight((short) (14 * 20));
            // 创建cell
            cell = row.createCell(column);
            cell.setCellStyle(styles.get("data"));

            Object value = getTargetValue(headerColumn, dataRow);
            setCellValue(cell, value);
        }
        catch (Exception e)
        {
            log.error("导出Excel失败{}", e);
        }
        return cell;
    }

    /**
     * 获取对应列中的数据
     * @param headerColumn
     * @param dataRow
     * @return
     */
    private Object getTargetValue(Map.Entry<String, String> headerColumn, Map<String, Object> dataRow) {
        for (Map.Entry<String, Object> dataColumn : dataRow.entrySet()) {
            if (dataColumn.getKey().equals(headerColumn.getKey())) {
                return dataColumn.getValue();
            }
        }

        return null;
    }

    /**
     * 设置对应列的数据
     * @param cell
     * @param value
     */
    private void setCellValue(Cell cell, Object value) {
        if (value == null) {
            return;
        }
        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        }
    }
}