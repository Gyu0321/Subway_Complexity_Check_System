//ApiSystem
//최초 작성일: 2025/12/01(월)
//수정일: 2025/12/12(금)
//작성자: 2022243049 최연규
import java.time.LocalTime;
import java.util.ArrayList;

public class ApiSystem {	//서비스 키와 API URL을 저장하며, 역 이름, 호선,상하내외선, 현재 시간을 확인
	private static final String SERVICE_KEY = "6514169a700526d7f6f93f1ac9600e34c7f1eeb0b1b7b51207f4d73cf5e8f776"; 
	private static final String API_URL_BASE = "https://api.odcloud.kr/api/15071311/v1/uddi:daf4624e-d52d-4b09-856b-e1c13749b20e"; 
	ArrayList<ApiData> apidata = new ArrayList<ApiData>();
	
	public void start(String sn, String line, String ud) {	//입력받은 데이터를 바탕으로 Guii열기
		if(sn != null && !sn.isEmpty()) {
			ApiReader reader = new ApiReader(SERVICE_KEY, API_URL_BASE);
			reader.readApiInformation(this.apidata, sn, line, ud);
			new Guii(this.apidata);
		}
	}
	
    public static String getCurrentHourHalf() {	//현재 시간 확인
        LocalTime now = LocalTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();
        if(hour == 0 && minute > 30) {	//00:30 이후부턴 다음날 첫타임을 표시
        	hour = 5;
        	minute = 30;
        } 
        else if(hour >= 1 && hour < 5) {	//00:30 이후부턴 다음날 첫타임을 표시
        	hour = 5;
        	minute = 30;
        }
        else if(minute >= 45) { //45분 이상이면 시간추가하고 00분
            hour = (hour + 1) % 24;
            minute = 0;
        } 
        else if(minute >= 15) { //15분 이상이면 30분
            minute = 30;
        } 
        else { //15분 미만이면 00분
            minute = 0;
        }
        return String.format("%02d%02d", hour, minute); //시간 반환
    }
}