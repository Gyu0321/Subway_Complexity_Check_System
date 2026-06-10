//ApiData
//최초 작성일: 2025/12/01(월)
//수정일: 2025/12/12(금)
//작성자: 2022243049 최연규
import java.util.ArrayList;
import java.util.HashMap;

public class ApiData {		//OPEN API에서 받은 정보를 나누기 위해 존재하는 클래스
	public String date;	    // 평일과 주말
	public String line;	    // 호선
	public String name;	    // 역 이름
	public String ud;		// 상선, 하선, 내선, 외선
	public HashMap<String, Double> congestionMap = new java.util.HashMap<>();	//05시30분부터 00시30분까지 30분 단위
	
	public String getDate() {
		return date;
	}
	
	public String getLine() {
		return line;
	}
	
	public String getName() {
		return name;
	}
	
	public String getUd() {
		return ud;
	}
	
	public java.util.HashMap<String, Double> getCongestionMap(){
		return congestionMap;
	}
}