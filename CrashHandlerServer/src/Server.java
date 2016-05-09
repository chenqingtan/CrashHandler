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
		 System.out.println("参数非法, 使用示例(java Server port):" + 
				 "\n\t\\java Server 8899");
		 
		 return;
	 }
		 
     int port = Integer.parseInt(args[0]);
     
     //定义一个ServerSocket监听在端口8899上
     ServerSocket server = new ServerSocket(port);
     System.out.println("创建监听端口:" + port + "成功");
     while (true) {
    	 System.out.println("等待连接...");
         //server尝试接收其他Socket的连接请求，server的accept方法是阻塞式的
         Socket socket = server.accept();
         System.out.println("连接请求来自: " + socket.getInetAddress().getHostAddress());
         
         //每接收到一个Socket就建立一个新的线程来处理它
         new Thread(new Task(socket)).start();
      }
   }
   
   /**
    * 用来处理Socket请求的
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
       * 跟客户端Socket进行通信
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
