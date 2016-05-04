import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.fireofandroid.crashhandler.CrashMsg;

public class Server {
 
   public static void main(String args[]) throws IOException {
     int port = 8899;
     
     //����һ��ServerSocket�����ڶ˿�8899��
     ServerSocket server = new ServerSocket(port);
     System.out.println("���������˿ڳɹ�");
     while (true) {
    	 System.out.println("�ȴ�����...");
         //server���Խ�������Socket����������server��accept����������ʽ��
         Socket socket = server.accept();
         System.out.println("������������: " + socket.getInetAddress().getHostAddress());
         
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
      
      @Override
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
      private void handleSocket() {
    	 try {
	    	 ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
	    	 CrashMsg msg = (CrashMsg) ois.readObject();
	    	 ois.close();
	    	 String traceup = msg.getStackTraceUp();
	         System.out.println("From IP:" + socket.getInetAddress().getHostName() + 
	        		 "---" 
	        		 + msg.getDate() + "-"
	        		 + msg.getType() + "-"
	        		 + msg.getThread() + "-"
	        		 + msg.getBody() + "-"
	        		 + traceup);
	         socket.close();
    	 } catch (IOException e) {
    		 e.printStackTrace();
    		 System.out.println(e.getMessage());
    	 } catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
      }
   }
}
