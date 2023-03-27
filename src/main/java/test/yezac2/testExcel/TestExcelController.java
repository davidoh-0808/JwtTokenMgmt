package test.yezac2.testExcel;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import test.yezac2.global.common.ApiResponse;
import test.yezac2.global.util.UtilExcelExporter;
import test.yezac2.global.util.UtilExcelUploader;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/test/excel")
public class TestExcelController {

    private final TestExcelMapper testExcelMapper;

    // for excel
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;



    /**
     *  1. fetch repo data
     *  2. call util excel
     *  3. set excel filename
     *  4. set excel header
     * @param req
     * @param resp
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/download")
    public ResponseEntity<byte[]> download(TestExcelReq req, HttpServletResponse resp) throws IOException, NoSuchFieldException, IllegalAccessException {

        List<TestExcelData> tedList = testExcelMapper.getTestExcelData( req );

        workbook = UtilExcelExporter.writeRows(tedList, workbook, sheet);

        String name = "TestExcelExport_" + getTimeNow();
        String fileExt = ".xlsx";
        String filename = name + fileExt;

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + filename;
        resp.setContentType("application/octet-stream");
        resp.setHeader(headerKey, headerValue);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        workbook.write( os );

        return ResponseEntity.ok().body( os.toByteArray() );
    }

    private LocalDateTime getTimeNow() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        dtf.format( now );

        return now;
    }


    /**
     * 1. Retrieve the client's original filename from MultipartFile request
     * 2. Create a xls file object with specific file path to read
     * 3. Create input stream
     * 4. Invoke UtilExcel with params, the file input stream & the class object to convert to (ie. TestExcelData), to convert excel file into a list of class object
     * 5. Actually save the converted list of class object into repo
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    @PostMapping(value = "/upload")
    public ResponseEntity<ApiResponse> upload(
            /*@RequestPart TestExcelFile tef*/
            @RequestParam(value = "TestExcelFile") MultipartFile file
            ) throws Exception {

        String filePath = file.getOriginalFilename();
        File xlsFile = new File(filePath);
        FileInputStream fis = new FileInputStream(xlsFile);

        List<Object> convertedFile = UtilExcelUploader.convertFileToClassObjs(fis, TestExcelData.class);

        testExcelMapper.saveConvertedList( convertedFile );

        return ResponseEntity.ok(
                ApiResponse.builder().resultMessage("Excel File Uploaded Successfully").build()
        );
    }


}
