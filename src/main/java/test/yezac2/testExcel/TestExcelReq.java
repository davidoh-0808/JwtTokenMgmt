package test.yezac2.testExcel;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TestExcelReq {

    private List<Long> idsToDownload;

}
