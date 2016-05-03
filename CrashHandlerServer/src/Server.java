import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
 
   public static void main(String args[]) throws IOException {
     int port = 8899;
     
     //����һ��ServerSocket�����ڶ˿�8899��
     ServerSocket server = new ServerSocket(port);
      while (true) {
         //server���Խ�������Socket����������server��accept����������ʽ��
         Socket socket = server.accept();
         //ÿ���յ�һ��Socket�ͽ���һ���µ��߳���������
         new Thread(new Task(socket)).start();
      }
   }
   
   /**
    * ��������Socket�����
   */
   static class Task implements Runnable {
 
      private Socket socket;
      
      public Task(Socket socket) {
         this.socket = socket;
      }
      
      public void run() {
         try {
            handleSocket();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
      
      /**
       * ���ͻ���Socket����ͨ��
      * @throws Exception
       */
      private void handleSocket() throws Exception {
         BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         StringBuilder sb = new StringBuilder();
         String temp;
         while ((temp=br.readLine()) != null) {
            sb.append(temp);
         }
         
         System.out.println("From IP:" + socket.getInetAddress().getHostName() + "---" + sb);
      }
   }
}
