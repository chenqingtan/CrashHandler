import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.fireofandroid.crashhandler.CrashMsg;

public class Server {
 
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	
   public static void main(String args[]) throws IOException {
	 if (args.length != 1) {
		 System.out.println("�����Ƿ�, ʹ��ʾ��(java Server port):" + 
				 "\n\t\\java Server 8899");
		 
		 return;
	 }
		 
     int port = Integer.parseInt(args[0]);
     
     //����һ��ServerSocket�����ڶ˿�8899��
     ServerSocket server = new ServerSocket(port);
     System.out.println("���������˿�:" + port + "�ɹ�");
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
	    	 String color = ANSI_WHITE;
	    	 switch (msg.getType()) {
	    	 case 'E':
	    		 color = ANSI_RED;
	    		 break;
	    	 case 'M':
	    		 color = ANSI_WHITE;
	    		 break;
	    	 default:
	    			 break; 			
	    	 }
	    	 
	         System.out.println(color + "From IP:" + socket.getInetAddress().getHostName() + 
	        		 "---" 
	        		 + msg.getDate() + "-"
	        		 + msg.getType() + "-"
	        		 + msg.getThread() + "-"
	        		 + msg.getBody() + "-"
	        		 + msg.getStackTraceUp() + ANSI_RESET);
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
