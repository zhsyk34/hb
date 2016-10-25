package com.cat.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * 功能描述：EXCEL报表工具类.
 * 
 * @author : Teny_lu 刘鹰
 * @ProjectName : b2b_new
 * @FileName : ExportExcel.java
 * @E_Mail : liuying5590@163.com
 * @CreatedTime : 2015年3月31日-上午9:58:26
 */
public class ExportExcel {
	
	private Workbook	wb		= null;
	
	private Sheet		sheet	= null;
	
	/**
	 * @param wb
	 * @param sheet
	 */
	public ExportExcel(HSSFWorkbook wb, HSSFSheet sheet) {
		super();
		this.wb = wb;
		this.sheet = sheet;
	}
	
	/**
	 * @return the sheet
	 */
	public Sheet getSheet() {
		return sheet;
	}
	
	/**
	 * @param sheet
	 *            the sheet to set
	 */
	public void setSheet(Sheet sheet) {
		this.sheet = sheet;
	}
	
	/**
	 * @return the wb
	 */
	public Workbook getWb() {
		return wb;
	}
	
	/**
	 * @param wb
	 *            the wb to set
	 */
	public void setWb(Workbook wb) {
		this.wb = wb;
	}
	
	/**
	 * 功能描述： 创建通用EXCEL头部
	 * 
	 * @author : Teny_lu 刘鹰
	 * @E_Mail : liuying5590@163.com
	 * @CreatedTime : 2015年3月31日-上午11:00:15
	 * @param headString
	 *            头部显示的字符
	 * @param colSum
	 *            该报表的列数
	 * @return
	 */
	public void createNormalHead(String headString, int colSum) {
		
		Row row = sheet.createRow(0);
		
		// 设置第一行
		HSSFCell cell = (HSSFCell) row.createCell(0);
		// 设置行高
		row.setHeight((short) 1000);
		
		// 定义单元格为字符串类型
		cell.setCellType(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue(new HSSFRichTextString(headString));
		
		// 指定合并区域
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, colSum));
		
		// 创建数据单元格样式
		CellStyle cellStyle = wb.createCellStyle();
		
		// 指定单元格居中对齐
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		
		// 指定单元格垂直居中对齐
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		
		// 指定当单元格内容显示不下时自动换行
		cellStyle.setWrapText(true);
		
		// 设置单元格字体
		Font font = wb.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font.setFontHeight((short) 320);
		cellStyle.setFont(font);
		
		cell.setCellStyle(cellStyle);
	}
	
	/**
	 * 功能描述：创建通用报表第二行
	 * 
	 * @author : Teny_lu 刘鹰
	 * @E_Mail : liuying5590@163.com
	 * @CreatedTime : 2015年3月31日-上午11:00:36
	 * @param params
	 *            统计条件数组
	 * @param colSum
	 *            需要合并到的列索引
	 * @return
	 */
	public void createNormalTwoRow(String[] params, int colSum) {
		Row row1 = sheet.createRow(1);
		// 设置行高
		row1.setHeight((short) 400);
		
		Cell cell2 = row1.createCell(0);
		cell2.setCellType(Cell.CELL_TYPE_STRING);
		cell2.setCellValue(new String("查询日期从 " + params[0] + " 到 " + params[1]));
		
		// 指定合并区域
		sheet.addMergedRegion(new CellRangeAddress(1, 0, 1, colSum));
		
		// 创建数据单元格样式
		CellStyle cellStyle = wb.createCellStyle();
		
		// 指定单元格居中对齐
		cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
		
		// 指定单元格垂直居中对齐
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		
		// 指定当单元格内容显示不下时自动换行
		cellStyle.setWrapText(true);
		
		// 设置单元格字体
		Font font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeight((short) 320);
		cellStyle.setFont(font);
		
		cell2.setCellStyle(cellStyle);
		
	}
	
	/**
	 * 功能描述：创建内容单元格
	 * 
	 * @author : Teny_lu 刘鹰
	 * @E_Mail : liuying5590@163.com
	 * @CreatedTime : 2015年3月31日-下午3:28:17
	 * @param wb
	 *            HSSFWorkbook
	 * @param row
	 *            HSSFRow
	 * @param col
	 *            short型的列索引
	 * @param align
	 *            对齐方式
	 * @param val
	 *            列值
	 * @return
	 */
	public void cteateCell(Workbook wb, Row row, int col, short align, String val) {
		Cell cell = row.createCell(col);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(val));
		CellStyle cellstyle = wb.createCellStyle();
		cellstyle.setAlignment(align);
		cell.setCellStyle(cellstyle);
	}
	
	/**
	 * 设置报表标题
	 * 
	 * @param columHeader
	 *            标题字符串数组
	 */
	public void createColumHeader(String[] columHeader) {
		
		// 设置列头
		Row row2 = sheet.createRow(2);
		
		// 指定行高
		row2.setHeight((short) 600);
		
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER); // 指定单元格居中对齐
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
		cellStyle.setWrapText(true);// 指定单元格自动换行
		
		// 单元格字体
		Font font = wb.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		font.setFontHeight((short) 250);
		cellStyle.setFont(font);
		
		/*
		 * cellStyle.setBorderBottom(CellStyle.BORDER_THIN); // 设置单无格的边框为粗体 cellStyle.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色．
		 * cellStyle.setBorderLeft(CellStyle.BORDER_THIN); cellStyle.setLeftBorderColor(HSSFColor.BLACK.index); cellStyle.setBorderRight(CellStyle.BORDER_THIN);
		 * cellStyle.setRightBorderColor(HSSFColor.BLACK.index); cellStyle.setBorderTop(CellStyle.BORDER_THIN); cellStyle.setTopBorderColor(HSSFColor.BLACK.index);
		 */
		
		// 设置单元格背景色
		cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Cell cell3 = null;
		
		for (int i = 0; i < columHeader.length; i++) {
			cell3 = (Cell) row2.createCell(i);
			cell3.setCellType(Cell.CELL_TYPE_STRING);
			cell3.setCellStyle(cellStyle);
			cell3.setCellValue(new String(columHeader[i]));
		}
		
	}
	
	/**
	 * 创建合计行
	 * 
	 * @param colSum
	 *            需要合并到的列索引
	 * @param cellValue
	 */
	public void createLastSumRow(int colSum, String[] cellValue) {
		
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER); // 指定单元格居中对齐
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
		cellStyle.setWrapText(true);// 指定单元格自动换行
		
		// 单元格字体
		Font font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		font.setFontHeight((short) 250);
		cellStyle.setFont(font);
		
		Row lastRow = sheet.createRow((sheet.getLastRowNum() + 1));
		Cell sumCell = lastRow.createCell(0);
		
		sumCell.setCellValue(new HSSFRichTextString("合计"));
		sumCell.setCellStyle(cellStyle);
		sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(), (short) 0, sheet.getLastRowNum(), (short) colSum));// 指定合并区域
		
		for (int i = 2; i < (cellValue.length + 2); i++) {
			sumCell = lastRow.createCell(i);
			sumCell.setCellStyle(cellStyle);
			sumCell.setCellValue(new HSSFRichTextString(cellValue[i - 2]));
			
		}
		
	}
	
	/**
	 * 输入EXCEL文件
	 * 
	 * @param fileName
	 *            文件名
	 */
	public void outputExcel(String fileName) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(fileName));
			wb.write(fos);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
