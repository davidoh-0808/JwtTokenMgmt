package test.yezac2.global.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


/**
 * Reading the first sheet of the excel file
 * Prep the class obj to insert the row data
 * Iterating all the rows
 * Iterating all the columns in a row
 *      and insert the column(cell) values into class obj (via reflection)
 */
@Slf4j
public class UtilExcelUploader {

    public static List<Object> convertFileToClassObjs(FileInputStream fis, Object classToConvertTo) throws Exception {

        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);

        List<Object> convertedFile = new ArrayList<>();                                         // *
        Constructor<?> constructor = classToConvertTo.getClass().getConstructor();              // **
        Iterator<Row> rowIter = sheet.iterator();
        while (rowIter.hasNext()) {
            List<Object> rowValList = new ArrayList<>();                                        // ***

            Row nextRow = rowIter.next();
            Iterator<Cell> cellIter = nextRow.cellIterator();
            while (cellIter.hasNext()) {
                Cell cell = cellIter.next();
                switch (cell.getCellType()) {
                    case STRING:
                        rowValList.add( cell.getStringCellValue() );
                        break;
                    case BOOLEAN:
                        rowValList.add( cell.getBooleanCellValue() );
                        break;
                    case NUMERIC:
                        rowValList.add( cell.getNumericCellValue() );
                        break;
                    default:
                        break;
                }

                Object classObjWithData = constructor.newInstance( rowValList.toArray() );     // ** , ***
                convertedFile.add( classObjWithData );                                         // *
            }
        }

        return convertedFile;
    }

}
