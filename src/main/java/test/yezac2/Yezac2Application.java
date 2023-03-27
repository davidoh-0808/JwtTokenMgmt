package test.yezac2;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import test.yezac2.testExcel.TestExcelData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class Yezac2Application {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Yezac2Application.class, args);

		/*
		TestExcelData ted = TestExcelData.builder()
				.id(1).col1("column1").col2("column2").col3("column3").col4("column4").col5("column5")
				.build();

		List<Field> fields  = Arrays.stream( ted.getClass().getDeclaredFields() )
								.toList();
		for( Field f : fields) {
			Field field = (Field) ted.getClass().getDeclaredField( f.getName() );
			field.setAccessible(true);
			Object fieldData = field.get(ted);

			System.out.println( fieldData );
		}
		 */


	}
}