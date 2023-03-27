package test.yezac2.global.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import test.yezac2.testExcel.TestExcelData;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
*             Reading an excel file using POI is simple if we divide this in steps.
*
*             Create workbook instance from excel sheet
*             Get to the desired sheet
*             First, Create cells for the header row
*                 then create cells for the date rows
*             Increment row number
*             iterate over all cells in a row
*             repeat step 3 and 4 until all data is read*
 */
public class UtilExcelExporter {
    //UtilExcel 에서 설정하는 값들 : (no of columns, actual data value, cell style)
    public static XSSFWorkbook writeRows(List<TestExcelData> tedList, XSSFWorkbook workbook, XSSFSheet sheet) throws NoSuchFieldException, IllegalAccessException {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("new1");

        int rowCnt = 0;
        int colCnt = 0;

        // header row
        Row headerRow = sheet.createRow(rowCnt);
        CellStyle headerStyle = getHeaderStyle(workbook);
        List<Field> fields  = Arrays.stream( tedList.getClass().getDeclaredFields() )
                                .toList();    // input class fields
        for(int i=0; i<fields.size(); i++) {
            createCell(sheet, headerRow, colCnt++, fields.get(i).getName(), headerStyle);
        }

        // data row
        CellStyle rowStyle = getRowStyle(workbook);
        for (TestExcelData ted : tedList) {
            Row row = sheet.createRow(rowCnt++);
            colCnt = 0;

            // toDo: ******************* check here ***********************
            for( Field f : fields ) {
                Field field = ted.getClass().getDeclaredField( f.getName() );
                field.setAccessible(true);
                createCell(sheet, row, colCnt++, field.get( ted ), rowStyle);
            }
            /*
            createCell(sheet, row, colCnt++, ted.getCol1() , rowStyle);
            createCell(sheet, row, colCnt++, ted.getCol2() , rowStyle);
            createCell(sheet, row, colCnt++, ted.getCol3() , rowStyle);
            createCell(sheet, row, colCnt++, ted.getCol4() , rowStyle);
            createCell(sheet, row, colCnt++, ted.getCol5() , rowStyle);
            */
        }

        System.out.println(sheet);
        return workbook;

    }

    private static CellStyle getHeaderStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        //font.setFontHeight((short) 12);
        font.setBold(true);
        style.setFont(font);

        return style;
    }

    private static CellStyle getRowStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        style.setFont(font);

        return style;
    }

    private static void createCell(XSSFSheet sheet, Row row, int colCnt, Object value, CellStyle style) {
        //각 셀에 적용되는 스타일 설정
        sheet.autoSizeColumn(colCnt);
        Cell cell = row.createCell(colCnt);

        if (value instanceof Integer) {
            cell.setCellValue( (Integer) value );
        } else if (value instanceof Long){
            cell.setCellValue( (Long) value );
        } else if (value instanceof Boolean) {
            cell.setCellValue( (Boolean) value );
        } else if (value instanceof Date) {
            cell.setCellValue( (Date) value );
        } else if (value instanceof Double) {
            cell.setCellValue( (Double) value );
        } else {
            cell.setCellValue( (String) value );
        }

        cell.setCellStyle(style);
    }
}
