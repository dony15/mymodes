package com.test03.utils;

import com.test03.pojo.TradeRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author DonY15
 * @description (报表工具类)读取数据库数据生成excel表格,发送到前端/本地
 * @create 2018\7\21 0021
 * ==========================================================
 * POI jar包
 * <!-- xls 2003版-->
 * <dependency>
 *     <groupId>org.apache.poi</groupId>
 *     <artifactId>poi</artifactId>
 *     <version>3.17</version>
 * </dependency>
 * <!-- xlsx 2007+版 推荐-->
 * <dependency>
 *      <groupId>org.apache.poi</groupId>
 *      <artifactId>poi-ooxml</artifactId>
 *      <version>3.17</version>
 * </dependency>
 * <!-- xlsx必备解析xmlbeans -->
 * <dependency>
 *      <groupId>org.apache.xmlbeans</groupId>
 *      <artifactId>xmlbeans</artifactId>
 *      <version>3.0.0</version>
 * </dependency>
 * ==========================================================
 *
 * 使用实例
 * ajax无法激活使用(机制限制),请使用a标签/location.href/表单等
 * ExcelUtils.export(response, tradeRecords, "tradeRecord.xlsx",100000);
 *
 * ==========================================================
 *
 */
public class ExcelUtils {
    /**
     *
     * @param response 固定响应输出流使用
     * @param tradeRecords 数据集合
     * @param fileName excel名 需要加.xlsx后缀
     * @param sheetLength 每一页(sheet)的长度,xls最长约6W+ xlsx最长约100W+ 根据需要和性能选择即可
     * @return
     */
    public static String export(HttpServletResponse response, List<TradeRecord> tradeRecords, String fileName,Integer sheetLength){


            // 第一步，创建一个webbook，对应一个Excel文件,
            //Workbook wb = new HSSFWorkbook(); //xls
            Workbook wb = new SXSSFWorkbook(); //xlsx

        /**
         * @listNum 控制总记录数量衔接
         * @innerRow 控制每个sheet行数的叠加和重置
         * @i 控制sheet页数
         */
            int listNum=0;
            // 第三步，因为sheet的容量有限制,一般使用xlsx可以容纳百万条数据,当数据超出时增加sheet组
            for (int i=0;tradeRecords!=null&&i<=tradeRecords.size()/sheetLength;i++){
                // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
                Sheet sheet = wb.createSheet(fileName+i);
                Row row = sheet.createRow(0);
                // 第四步，创建单元格，并设置值表头 设置表头居中
                CellStyle style = wb.createCellStyle();
                style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式


                row.createCell(0).setCellValue("交易ID");
                row.createCell(1).setCellValue("交易时间");
                row.createCell(2).setCellValue("支出");
                row.createCell(3).setCellValue("存入");
                row.createCell(4).setCellValue("余额");
                row.createCell(5).setCellValue("交易类型");
                row.createCell(6).setCellValue("交易备注");

                int innerRow=1;
                for (;listNum<tradeRecords.size();) {
                   TradeRecord tradeRecord=tradeRecords.get(listNum);
                    // System.out.println(tradeRecord.getId()+"--->测试成功");
                    Row row1 = sheet.createRow(innerRow);
                    row1.createCell(0).setCellValue(tradeRecord.getId());
                    row1.createCell(1).setCellValue(tradeRecord.getTradeDate());
                    row1.createCell(2).setCellValue(ExcelUtils.getDoubleVal(tradeRecord.getDisburse(),wb,style));
                    row1.createCell(3).setCellValue(ExcelUtils.getDoubleVal( tradeRecord.getWrite(),wb,style));
                    row1.createCell(4).setCellValue(ExcelUtils.getDoubleVal( tradeRecord.getBalance(),wb,style));
                    row1.createCell(5).setCellValue(tradeRecord.getTradeType());
                    row1.createCell(6).setCellValue(tradeRecord.getTradeRemark());
                    listNum++;
                    innerRow++;
                    if (listNum%sheetLength==0){
                        // System.err.println("这里改换sheet了");
                        break;
                    }
                }
            }
            // 第六步，直接导出到前端(方案一)
            try {

                response.setContentType("application/excel;charset=utf=-8");
                response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
                response.setHeader("Cache-Control", "no-cache");
                response.setHeader("Pragma", "no-cache");
                response.setDateHeader("Expires", -1);

                ServletOutputStream os = response.getOutputStream();
                BufferedOutputStream osB = new BufferedOutputStream(os);

                wb.write(osB);
                wb.close();
                os.flush();
                os.close();
                return fileName;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        /**
         * ==========================================================================
         *  也可以将文件存到指定位置(方案二)
         *                      (≖ᴗ≖)✧
         * ==========================================================================
         */
        /**
         try {
         File excel = new File("student1." + excelExtName);
         FileOutputStream fout = new FileOutputStream(excel);
         workbook.write(fout);
         workbook.close();
         fout.close();
         return excel;
         } catch (Exception e) {
         e.printStackTrace();
         }
         return null;
         */
    }




    /**
     * ==========================================================================
     *
     * 各种格式转换工具(持续更新)
     * 1.货币: BigDecimal转换为double使用(否则excel不识别)
     *
     *                                              _(´ཀ`」∠)_加班...
     * ==========================================================================
     */

    //BigDecimal转化double导出货币
    public static double getDoubleVal(BigDecimal value, Workbook wb, CellStyle style) {
        if (value==null){return 0;}
        double doubleVal = (value.doubleValue());
        DataFormat format = wb.createDataFormat();
        //此格式是货币格式
        style.setDataFormat(format.getFormat("￥#,##0.00"));
        //最终必须接收一个double类型的数据
        return doubleVal;
    }

}
