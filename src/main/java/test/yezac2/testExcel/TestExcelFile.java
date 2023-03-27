package test.yezac2.testExcel;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TestExcelFile {

    /*private CommonsMultipart fileData;*/
    private MultipartFile file;

}
