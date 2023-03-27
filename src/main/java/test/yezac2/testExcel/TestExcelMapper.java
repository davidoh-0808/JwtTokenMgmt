package test.yezac2.testExcel;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TestExcelMapper {

    List<TestExcelData> getTestExcelData(TestExcelReq req);

    int saveConvertedList(List<Object> convertedFile);

}
