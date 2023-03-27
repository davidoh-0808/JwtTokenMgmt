/* Java 1.8 샘플 코드 */


import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;

public class ApiExplorer {
    public static void main(String[] args) throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B551182/mdfeeCrtrInfoService/getDiagnossMdfeeList"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=서비스키"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*10*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
        urlBuilder.append("&" + URLEncoder.encode("mdfeeCd","UTF-8") + "=" + URLEncoder.encode("NA242W14", "UTF-8")); /*수가코드(검색 유형 : A%)*/
        urlBuilder.append("&" + URLEncoder.encode("mdfeeDivNo","UTF-8") + "=" + URLEncoder.encode("자24-1나(2)(라)", "UTF-8")); /*수가코드에 대한 분류번호 (검색 유형 : A%)*/
        urlBuilder.append("&" + URLEncoder.encode("korNmkorNmkorNmkorNmkorNmkorNmkorNmkorNmkorNmkorNmkorNmkorNmkorNmkorNmkorNmkorNmkorNmkorNm","UTF-8") + "=" + URLEncoder.encode("신생아", "UTF-8")); /*수가 한글명 (검색 유형 : %A%)*/
        urlBuilder.append("&" + URLEncoder.encode("unprc3","UTF-8") + "=" + URLEncoder.encode("0", "UTF-8")); /*치과병·의원단가(단위:원)*/
        urlBuilder.append("&" + URLEncoder.encode("unprc4","UTF-8") + "=" + URLEncoder.encode("0", "UTF-8")); /*보건기관단가(단위:원)*/
        urlBuilder.append("&" + URLEncoder.encode("unprc5","UTF-8") + "=" + URLEncoder.encode("0", "UTF-8")); /*조산원단가(단위:원)*/
        urlBuilder.append("&" + URLEncoder.encode("unprc6","UTF-8") + "=" + URLEncoder.encode("0", "UTF-8")); /*한방병원단가(단위:원)*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        System.out.println(sb.toString());
    }
}