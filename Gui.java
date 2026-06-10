//Gui
//최초 작성일: 2025/12/01(월)
//수정일: 2025/12/12(금)
//작성자: 2022243049 최연규
import javax.swing.*;
import java.awt.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

public class Gui extends JFrame {	//사용자가 설정할 GUI
JTextField jtf = new JTextField(10);
private final String SETTINGS_FILE_NAME = "Settings.txt";	//최근 사용 저장할 파일
JComboBox jc2 = new JComboBox();
JComboBox jc3 = new JComboBox();
	public Gui() {	//GUI구성
		Container ct = getContentPane();
		JPanel jp1 = new JPanel();
		JLabel jl1 = new JLabel("호선과 상하선, 출발역을 입력하시오.");
		JPanel jp2 = new JPanel();
		String jc22[] = {"1호선","2호선","3호선","4호선","5호선","6호선","7호선","8호선"};
		for(int i=0;i<8;i++) {
			jc2.addItem(jc22[i]);
		}
		jc3.addItem("상");
		jc3.addItem("하");
		jc3.addItem("내");
		jc3.addItem("외");
		jc2.setPreferredSize(new Dimension(60,30));
		jc3.setPreferredSize(new Dimension(60,30));
		jtf.setPreferredSize(new Dimension(150,30));
		JPanel jp3 = new JPanel();
		JButton jb1 = new JButton("저장");
		JButton jb2 = new JButton("불러오기");
		JButton jb3 = new JButton("확인");
		ct.setLayout(new GridLayout(3,1));
		jp1.setLayout(new FlowLayout(FlowLayout.CENTER));
		jp1.add(jl1);
		jp2.setLayout(new FlowLayout(FlowLayout.CENTER));
		jp2.add(jc2);
		jp2.add(jc3);
		jp2.add(jtf);
		jp3.setLayout(new FlowLayout(FlowLayout.RIGHT));
		jp3.add(jb1);
		jp3.add(jb2);
		jp3.add(jb3);
		ct.add(jp1);
		ct.add(jp2);
		ct.add(jp3);
		setTitle("지하철 복잡도 확인 시스템");
		setSize(280,350);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		jb1.addActionListener(e -> {	//저장 버튼
			ss();
		});
		jb2.addActionListener(e -> {	//불러오기 버튼
			ls();
		});
		jb3.addActionListener(e -> {	//확인 버튼
			String sl = (String) jc2.getSelectedItem(); // 선택된 호선 (예: "1호선")
		    String su = (String) jc3.getSelectedItem();     // 선택된 상하선 (예: "상")
		    String sn = jtf.getText().trim();
		    if(!sn.isEmpty()) {	//역 이름을 입력했을 경우 정보 전달
		        ApiSystem system = new ApiSystem();
		        system.start(sn, sl, su); 
		    }
		});
	}
	public static void main(String []args) {
		int port = 1273; // 사용할 포트 번호
		
		try (ServerSocket serverSocket = new ServerSocket(port)) {	//서버 소켓
			System.out.println("서버 소켓 시작.");
			
			Socket clientSocket = serverSocket.accept();	//클라이언트 접속 허용
			System.out.println("클라이언트 접속.");
	
			SwingUtilities.invokeLater(() -> {	//사용자에게 Gui실행
				new Gui(); 
			});
		} catch (IOException e) {
			System.err.println("오류 발생.");
		}
	}
	
	private void ss() {	//저장
	    String sl = (String)jc2.getSelectedItem(); 
	    String su = (String)jc3.getSelectedItem();     
	    String sn = jtf.getText().trim();            
	    File settingsFile = new File(SETTINGS_FILE_NAME);
	    String Save = String.format("%s,%s,%s", sl, su, sn);
	    try (PrintWriter writer = new PrintWriter(settingsFile)) {	//파일입력 사용
	        writer.println(Save);
	        JOptionPane.showMessageDialog(this, "설정이 " + SETTINGS_FILE_NAME + "에 저장되었습니다.");
	    } catch (IOException e) {
	        System.err.println("오류 발생.");
	    }
	}
	private void ls() {	//불러오기
	    File settingsFile = new File(SETTINGS_FILE_NAME);
	    
	    try (BufferedReader reader = new BufferedReader(new FileReader(settingsFile))) { //파일출력 사용
	        String line = reader.readLine();	//읽기
	        String[] parts = line.split(",");	//,단위로 나누기
	        jc2.setSelectedItem(parts[0].trim()); // 호선
	        jc3.setSelectedItem(parts[1].trim()); // 상하선
	        jtf.setText(parts[2].trim());        // 역 이름
	        
	        JOptionPane.showMessageDialog(this,"불러오기 완료.");
	    } catch (IOException e) {
	    	System.err.println("오류 발생.");
	    }
	}
}