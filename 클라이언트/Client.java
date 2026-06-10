import java.io.IOException;
import java.net.Socket;

public class Client {	//클라이언트
    public static void main(String[] args) {
        String serverAddress = "localhost"; // 서버가 현재 실행 중인 컴퓨터의 주소 (로컬 호스트)
        int port = 1273; 
        try (Socket socket = new Socket(serverAddress, port)) {
            System.out.println("서버 접속 성공.");
        } catch (IOException e) {
            System.err.println("서버 접속 실패.");
        }
    }
}