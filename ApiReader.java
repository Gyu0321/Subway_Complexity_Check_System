//ApiReader
//최초 작성일: 2025/12/01(월)
//수정일: 2025/12/12(금)
//작성자: 2022243049 최연규
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ApiReader {		//API 정보 읽기
    private final String SERVICE_KEY;
    private final String API_URL_BASE;
    
    public ApiReader(String serviceKey, String apiUrlBase) {	//API URL과 인증키 저장
        this.SERVICE_KEY = serviceKey;
        this.API_URL_BASE = apiUrlBase;
    }
    
    public void readApiInformation(ArrayList<ApiData> apidata, String sn, String line, String ud) {
    	//사용자로부터 입력받은 호선, 상하내외선, 역 이름 정보를 저장
        apidata.clear();
        String resultJson = null;
        String requiredUd = ud + "선";
        
        try {	//API 인코딩(생성형 AI 도움받았습니다.)
            String encodedStationName = URLEncoder.encode(sn, "UTF-8");
            String apiUrl = API_URL_BASE+"?page=1&perPage=1671&returnType=JSON"+"&serviceKey="+SERVICE_KEY;
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
                StringBuilder sb = new StringBuilder();
                String line1;
                while ((line1 = br.readLine()) != null) {
                    sb.append(line1);
                }
                resultJson = sb.toString();
            }
            conn.disconnect();
            if (resultJson != null && !resultJson.isEmpty()) {
                JsonParser parser = new JsonParser();
                JsonObject rootObj = parser.parse(resultJson).getAsJsonObject();
                JsonArray dataArray = rootObj.getAsJsonArray("data"); 
                if (dataArray != null && dataArray.size() > 0) {
                    for (JsonElement element : dataArray) {	//API 세부 내용 구분
                        JsonObject item = element.getAsJsonObject();
                        String foundStation = getString(item,"출발역");
                        String foundLine = getString(item, "호선");
                        String foundUd = getString(item, "상하구분");
                        if(foundStation.equals(sn) && foundLine.equals(line) && foundUd.equals(requiredUd)) {
                        	ApiData r = new ApiData();
                            r.date = getString(item, "요일구분");
                            r.line = foundLine;
                            r.name = foundStation;
                            r.ud = foundUd;
                            String[] congestionHours = {
                            	    "5시30분", "6시00분", "6시30분", "7시00분", "7시30분", "8시00분", "8시30분", 
                            	    "9시00분", "9시30분", "10시00분", "10시30분", "11시00분", "11시30분", 
                            	    "12시00분", "12시30분", "13시00분", "13시30분", "14시00분", "14시30분", 
                            	    "15시00분", "15시30분", "16시00분", "16시30분", "17시00분", "17시30분", 
                            	    "18시00분", "18시30분", "19시00분", "19시30분", "20시00분", "20시30분", 
                            	    "21시00분", "21시30분", "22시00분", "22시30분", "23시00분", "23시30분", 
                            	    "00시00분", "00시30분"};
                            for (String hourKey : congestionHours) {
                                double congestionValue = getDouble(item, hourKey);
                                r.congestionMap.put(hourKey, congestionValue);
                            }
                            apidata.add(r);
                        }
                    }
                } 
            } 
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    private String getString(JsonObject item, String key) {	//내용들 확인
        return item.has(key) && !item.get(key).isJsonNull() ? item.get(key).getAsString().trim() : "";
    }
    private double getDouble(JsonObject item, String key) {	//혼잡도 확인
    	try {
            return item.has(key) && !item.get(key).isJsonNull() ? item.get(key).getAsDouble() : 0.0;
        } catch (NumberFormatException e) {
            return 0.0;
        } catch (IllegalStateException e) {
            return 0.0;
        }
    }
}