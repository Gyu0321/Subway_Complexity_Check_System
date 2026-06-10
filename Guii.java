//Guii
//최초 작성일: 2025/12/01(월)
//수정일: 2025/12/12(금)
//작성자: 2022243049 최연규
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Guii extends JFrame {
	private ArrayList<ApiData> resultData;
	JLabel jl1 = new JLabel();
	JLabel jl2 = new JLabel();
	JLabel jl3 = new JLabel();
	public Guii(ArrayList<ApiData> apidataList) {	//GUI
		this.resultData = apidataList;
		Container ct = getContentPane();
		JPanel jp = new JPanel();
		ct.setLayout(new FlowLayout(FlowLayout.CENTER));
		jp.setLayout(new GridLayout(3,1));
		jp.add(jl1);
		jp.add(jl2);
		jp.add(jl3);
		ct.add(jp);
		setTitle("지하철 복잡도");
		setSize(300,150);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		displayData();
	}
	private void displayData() {	//GUI 디스플레이 변경
	    if(resultData == null || resultData.isEmpty()) {	//입력된 데이터가 검색 결과에 없을 경우
	        jl1.setText("<html><b>검색 결과 없음</b></html>");
	        jl2.setText("API 통신 실패 또는 해당 역의 데이터가 없습니다.");
	        jl3.setText("");
	        return;
	    }
	    ApiData fi = resultData.get(0);	//API 호출
	    String ct = ApiSystem.getCurrentHourHalf();	//현재 시간
	    double cc = 0.0;	//복잡도
	    Map<String, Double> cm = fi.getCongestionMap();	//시간대별 데이터
	    String clt = findClosestTimeKey(cm.keySet(), ct);	//가까운 시간 저장
	    cc = cm.getOrDefault(clt, 0.0);
	    String cl = getLevelText(cc);	//복잡도 수준 저장
	    
	    jl1.setText("<html><b>역 정보: [" + fi.getName() + "]	/ 호선: " + fi.getLine() + "</b></html>");
	    jl2.setText("<html>요일: " + fi.getDate() + " / 방향: " + fi.getUd() + "</html>");
	    jl3.setText("<html>시간: "+formatTimeKey(clt)+" / 혼잡도: <b>" + String.format("%.1f", cc) + "%("+cl+")</b></html>");
	}
	private String getLevelText(double c) {	//혼잡도 레벨 표시
	    if (c >= 75.0) return "매우 혼잡";
	    else if (c >= 60.0) return "혼잡";
	    else if (c >= 40.0) return "보통";
	    else return "원활";
	}
	private String formatTimeKey(String jsonTimeKey) {	//시간 형식
	    return jsonTimeKey.replace("시", ":").replace("분", "");
	}

	private String findClosestTimeKey(java.util.Set<String> jsonKeys, String targetTimeKey) {
		//현재 시간에 맞는 복잡도 도출(최대 시간인 00:30을 넘어가면 10시로 고정되는 오류를 수정하기 위해 생성형 AI를 사용했습니다.)
	    String closestKey = null;
	    int minDiffMinutes = Integer.MAX_VALUE;
	    if (targetTimeKey.length() != 4) return jsonKeys.iterator().next(); 
	    int targetMinutes = (Integer.parseInt(targetTimeKey.substring(0, 2)) * 60) + 
	                        Integer.parseInt(targetTimeKey.substring(2, 4));
	    for (String key : jsonKeys) {
	        String timePart = key.replace("시", "").replace("분", "");
	        if (timePart.length() != 4) continue;
	        int keyMinutes = (Integer.parseInt(timePart.substring(0, 2)) * 60) + 
	                         Integer.parseInt(timePart.substring(2, 4));
	        int diff = keyMinutes - targetMinutes;
	        if (diff >= 0 && diff < minDiffMinutes) {
	            minDiffMinutes = diff;
	            closestKey = key;
	        }
	    }
	    if (closestKey == null) {
	        for (String key : jsonKeys) {
	            if (key.contains("00시00분")) return key;
	        }
	    }
	    return closestKey != null ? closestKey : jsonKeys.iterator().next(); 
	}
}