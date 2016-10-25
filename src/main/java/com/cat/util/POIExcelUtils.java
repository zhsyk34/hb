package com.cat.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zuipin.util.AnnotationUtils;
import com.zuipin.util.ConvertUtils;
import com.zuipin.util.ExcelError;
import com.zuipin.util.ExcelFirstCallBack;
import com.zuipin.util.HibernateValidateUtils;
import com.zuipin.util.ReflectionUtils;

/**
 * 功能描述：POI表格导入工具类
 * 
 * @author : Teny_lu 刘鹰
 * @ProjectName : zuipinerp
 * @FileName : POIExcelUtils.java
 * @E_Mail : liuying5590@163.com
 * @CreatedTime : 2014年10月24日-下午2:55:55
 */
public class POIExcelUtils {
	
	private static Logger	logger	= LoggerFactory.getLogger(POIExcelUtils.class);
	
	private POIExcelUtils() {
		throw new AssertionError();
	}
	
	public static Workbook createImportWorkBook(String filePath) {
		InputStream is = null;
		try {
			is = new FileInputStream(new File(filePath));
			Object localObject2;
			if (filePath.toLowerCase().endsWith("xls")) {
				localObject2 = new HSSFWorkbook(is);
				return (Workbook) localObject2;
			}
			if (filePath.toLowerCase().endsWith("xlsx")) {
				localObject2 = new XSSFWorkbook(is);
				return (Workbook) localObject2;
			}
		} catch (FileNotFoundException e) {
			logger.error("文件：" + filePath + "没有找到");
		} catch (IOException e) {
			logger.error("文件IO错误");
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(is);
		}
		IOUtils.closeQuietly(is);
		
		return (Workbook) null;
	}
	
	public static Workbook createExportWorkBook(String version) {
		Workbook workbook = null;
		if ("2003".equals(version) || "xls".equals(version)) {
			workbook = new HSSFWorkbook();
		} else if ("2007".equals(version) || "xlsx".equals(version)) {
			workbook = new XSSFWorkbook();
		}
		return workbook;
	}
	
	public static Object getCellValue(Row row, int index) {
		if (row == null) {
			return null;
		}
		Object value = null;
		Cell cell = row.getCell(index);
		if (cell != null) {
			switch (cell.getCellType()) {
				case 1:
					value = cell.getRichStringCellValue().getString();
					break;
				case 0:
					if (DateUtil.isCellDateFormatted(cell)) {
						try {
							value = DateFormatUtils.format(cell.getDateCellValue(), "yyyy-MM-dd HH:mm:ss");
						} catch (Exception e) {
							value = null;
						}
					} else {
						value = new BigDecimal(cell.getNumericCellValue()).toString();
					}
					break;
				case 4:
					value = Boolean.valueOf(cell.getBooleanCellValue());
					break;
				case 2:
					value = cell.getStringCellValue();
					break;
				case 5:
					value = String.valueOf(cell.getErrorCellValue());
					break;
				case 3:
					break;
			}
			
		}
		
		return StringUtils.trim(ObjectUtils.toString(value));
	}
	
	public static CellStyle setDateStyle(Workbook workbook, String format) {
		CellStyle cellStyle = workbook.createCellStyle();
		CreationHelper creationHelper = workbook.getCreationHelper();
		cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat(format));
		return cellStyle;
	}
	
	public static CellStyle getTitleStyle(Workbook workbook) {
		CellStyle cellStyle = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 20);
		font.setFontName("黑体");
		font.setBoldweight((short) 700);
		cellStyle.setFont(font);
		
		cellStyle.setAlignment((short) 2);
		
		cellStyle.setVerticalAlignment((short) 1);
		
		cellStyle.setBorderBottom((short) 1);
		cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		cellStyle.setBorderLeft((short) 1);
		cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		cellStyle.setBorderRight((short) 1);
		cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		cellStyle.setBorderTop((short) 1);
		cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
		return cellStyle;
	}
	
	public static CellStyle getTailStyle(Workbook workbook) {
		CellStyle cellStyle = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setBoldweight((short) 700);
		cellStyle.setFont(font);
		cellStyle.setAlignment((short) 2);
		cellStyle.setVerticalAlignment((short) 1);
		return cellStyle;
	}
	
	public static CellStyle getHeaderCenterStyle(Workbook workbook) {
		CellStyle cellStyle = getTitleStyle(workbook);
		cellStyle.setFillForegroundColor(IndexedColors.BLACK.getIndex());
		cellStyle.setFillPattern((short) 1);
		return cellStyle;
	}
	
	public static CellStyle getContentStyle(Workbook workbook) {
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setWrapText(false);
		cellStyle.setAlignment((short) 2);
		return cellStyle;
	}
	
	public static <T> List<T> readExcel(String fileName, Class<T> t, ExcelFirstCallBack excelFirstCallBack, String[] keyValues, List<T> errorsList) {
		List<T> list = new ArrayList<T>();
		
		Sheet sheet = getSheet(fileName);
		
		int count = sheet.getLastRowNum();
		
		if (count > 0) {
			Row row = sheet.getRow(0);
			
			if (excelFirstCallBack.paseFirstSheet(row)) {
				return getResultPageSile(list, sheet, 1, count, keyValues, t, errorsList);
			}
			logger.info("回调函数返回的值为false");
			return null;
		}
		
		logger.info("没有行数");
		return null;
	}
	
	public static <T> List<T> readExcel(String fileName, Class<T> t, ExcelFirstCallBack excelFirstCallBack, String[] keyValues) {
		return readExcel(fileName, t, excelFirstCallBack, keyValues, null);
	}
	
	public static Sheet getSheet(String fileName) {
		Workbook workbook = createImportWorkBook(fileName);
		
		Sheet sheet = workbook.getSheetAt(0);
		
		if (sheet.getLastRowNum() < 1) {
			sheet = workbook.getSheetAt(1);
		}
		return sheet;
	}
	
	public static <T> List<T> readExcel(String fileName, Class<T> t, ExcelFirstCallBack excelFirstCallBack, String[] keyValues, int firstRoot, int getsize, List<T> errorsList) {
		List<T> list = new ArrayList<T>();
		
		Sheet sheet = getSheet(fileName);
		
		int count = sheet.getLastRowNum();
		
		if (count > 0) {
			Row row = sheet.getRow(0);
			
			if (excelFirstCallBack.paseFirstSheet(row)) {
				return getResultPageSile(list, sheet, firstRoot, getsize, keyValues, t, errorsList);
			}
			logger.info("回调函数返回的值为false");
			return null;
		}
		
		logger.info("没有行数");
		return null;
	}
	
	public static <T> List<T> readExcel(String fileName, Class<T> t, ExcelFirstCallBack excelFirstCallBack, String[] keyValues, int firstRoot, int getsize) {
		return readExcel(fileName, t, excelFirstCallBack, keyValues, firstRoot, getsize, null);
	}
	
	public static <T> List<T> readExcelByList(String fileName, Class<T> t, ExcelFirstCallBack excelFirstCallBack, String[] keyValues) {
		return readExcel(fileName, t, excelFirstCallBack, keyValues);
	}
	
	public static <T> List<T> readExcelByList(String fileName, Class<T> t, ExcelFirstCallBack excelFirstCallBack, int firstRoot, int getsize, String[] keyValues) {
		return readExcel(fileName, t, excelFirstCallBack, keyValues, firstRoot, getsize);
	}
	
	public static <T> List<T> readExcelByList(String fileName, Class<T> t, ExcelFirstCallBack excelFirstCallBack, String[] keyValues, List<T> errorsList) {
		return readExcel(fileName, t, excelFirstCallBack, keyValues, errorsList);
	}
	
	public static <T> List<T> readExcelByList(String fileName, Class<T> t, ExcelFirstCallBack excelFirstCallBack, int firstRoot, int getsize, String[] keyValues, List<T> errorsList) {
		return readExcel(fileName, t, excelFirstCallBack, keyValues, firstRoot, getsize, errorsList);
	}
	
	private static <T> List<T> getResultPageSile(List<T> list, Sheet sheet, int firstRoot, int getsize, String[] keyValues, Class<T> t, List<T> errorsList) {
		int count = sheet.getLastRowNum();
		
		if (count < firstRoot) {
			logger.info("指定的开始行数比excel的总行数还大");
			return null;
		}
		
		int getLastCount = firstRoot + getsize - 1;
		int pageSize;
		if (count < getLastCount)
			pageSize = count;
		else {
			pageSize = getLastCount;
		}
		
		if (t == Map.class) {
			if (keyValues != null) {
				for (int i = firstRoot; i <= pageSize; i++) {
					Map<String, Object> map = new HashMap<String, Object>();
					
					Row row = sheet.getRow(i);
					int j = 0;
					for (String string : keyValues) {
						map.put(string, getCellValue(row, j));
						j++;
					}
					list.add((T) map);
				}
			} else {
				logger.info("keyValues的值不能为空");
				return null;
			}
		} else {
			for (int i = firstRoot; i <= pageSize; i++) {
				try {
					Object t2 = t.newInstance();
					
					Row row = sheet.getRow(i);
					int j = 0;
					int m = 0;
					for (String string : keyValues) {
						ReflectionUtils.invokeSetterMethod(t2, string,
								ConvertUtils.convertStringToObject((String) getCellValue(row, j), t2.getClass().getDeclaredField(string).getType()));
						Object object = ReflectionUtils.invokeGetterMethod(t2, string);
						if (object == null)
							m++;
						else if (StringUtils.isBlank(ObjectUtils.toString(object))) {
							m++;
						}
						
						j++;
					}
					if (j == m) {
						break;
					}
					if (errorsList == null) {
						list.add((T) t2);
					} else {
						Set constraintViolations = HibernateValidateUtils.getValidator(t2);
						
						String error = ((ConstraintViolation) constraintViolations.iterator().next()).getMessage();
						if (constraintViolations.size() < 1) {
							list.add((T) t2);
						} else {
							List<Field> fields = AnnotationUtils.getHaveAnnotationByField(t2.getClass(), ExcelError.class);
							Method method;
							if (fields.size() < 1) {
								List methods = AnnotationUtils.getHaveAnnotationByMethod(t2.getClass(), ExcelError.class);
								if (methods.size() > 0)
									for (Iterator localIterator = methods.iterator(); localIterator.hasNext();) {
										method = (Method) localIterator.next();
										method.invoke(t2, new Object[] { error });
									}
							} else {
								for (Field field : fields) {
									ReflectionUtils.invokeSetterMethod(t2, field.getName(), error);
								}
							}
							errorsList.add((T) t2);
						}
					}
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
}