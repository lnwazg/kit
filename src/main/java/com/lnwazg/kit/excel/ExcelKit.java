package com.lnwazg.kit.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.lnwazg.kit.log.Logs;

public class ExcelKit
{
    /**
     * 从xls、xlsx文件中读取格式化的数据，自适应区分excel的版本
     * @author nan.li
     * @param fileUrl
     * @param viewColumns
     * @return
     */
    public static List<Map<String, Object>> readListMapFromExcel(String fileUrl, List<String> columns)
    {
        try
        {
            URL url = new URL(fileUrl);
            File file = new File(url.getFile());
            if (!file.exists())
            {
                Logs.e("file not exist! fileUrl=" + fileUrl);
                return null;
            }
            //根据扩展名去读取excel
            String fileName = file.getName();
            String fileExtension = FilenameUtils.getExtension(fileName);
            Workbook wb = null;
            InputStream is = new FileInputStream(file);
            if ("xls".equals(fileExtension))
            {
                wb = new HSSFWorkbook(is);
            }
            else if ("xlsx".equals(fileExtension))
            {
                wb = new XSSFWorkbook(is);
            }
            if (wb == null)
            {
                return null;
            }
            //结果对象
            List<Map<String, Object>> list = new ArrayList<>();
            
            //去除第一个sheet页面
            Sheet sheet = wb.getSheetAt(0);
            // 得到总行数  
            int lastRowNum = sheet.getLastRowNum();
            
            //第一行
            //            Row row = sheet.getRow(0);
            //第一行的列数
            //            int colNum = row.getPhysicalNumberOfCells();//用不到，以columns的列数为准
            
            // 正文内容应该从第二行开始,第一行为表头的标题  
            for (int i = 0; i <= lastRowNum; i++)
            {
                Row row = sheet.getRow(i);//取出改行
                Map<String, Object> map = new HashMap<>();
                for (int j = 0; j < columns.size(); j++)
                {
                    Object obj = ExcelKit.getCellFormatValue(row.getCell(j));
                    map.put(columns.get(j), obj);
                }
                list.add(map);
            }
            return list;
        }
        catch (MalformedURLException e1)
        {
            e1.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    /** 
     *  
     * 根据Cell类型设置数据 
     *  
     * @param cell 
     * @return 
     * @author zengwendong 
     */
    public static Object getCellFormatValue(Cell cell)
    {
        Object cellvalue = "";
        if (cell != null)
        {
            // 判断当前Cell的Type  
            switch (cell.getCellType())
            {
                case Cell.CELL_TYPE_NUMERIC:// 如果当前Cell的Type为NUMERIC  
                case Cell.CELL_TYPE_FORMULA:
                {
                    // 判断当前的cell是否为Date  
                    if (DateUtil.isCellDateFormatted(cell))
                    {
                        // 如果是Date类型则，转化为Data格式  
                        // data格式是带时分秒的：2013-7-10 0:00:00  
                        // cellvalue = cell.getDateCellValue().toLocaleString();  
                        // data格式是不带带时分秒的：2013-7-10  
                        Date date = cell.getDateCellValue();
                        cellvalue = date;
                    }
                    else
                    {
                        // 如果是纯数字  
                        // 取得当前Cell的数值  
                        cellvalue = String.valueOf((int)cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING:// 如果当前Cell的Type为STRING  
                    // 取得当前的Cell字符串  
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                default:// 默认的Cell值  
                    cellvalue = "";
            }
        }
        else
        {
            cellvalue = "";
        }
        return cellvalue;
    }
    
}
