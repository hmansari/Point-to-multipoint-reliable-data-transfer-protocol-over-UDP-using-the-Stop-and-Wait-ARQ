
package ip_newprj2.src.ip_newprj2;



import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
 

public class Client  {

   
    public static int mss_value,port_value;
   
    public static String x,file_path,port;
    String data_identifier="0101010101010101";
    String data = null,thread_name=null;
    public static String[] ip_address,thread_num;
    boolean[] serv=new boolean[number_servers+1];
    boolean all=false;
    boolean[] send=new boolean[number_servers];
    boolean sendall=false,flag=false;
    Checksum checks = new CRC32();
    
    DatagramPacket sendPacket=null;

    int n,thread_number;
    public static int number_servers;
    public static byte b[]=new byte[100];
    int[] server_port=new int[10];
    int[] threading=new int[number_servers];
    static DatagramSocket client;

           
    public static void main(String[] args) throws Exception {
        
           System.out.println("p2mpclient server-1 server-2 ...-n server-port# file-name MSS");
           BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
           System.out.println("Invoking Client");
                         
           System.out.println("Enter the number of receivers: ");
           number_servers=Integer.parseInt(br.readLine());
           ip_address=new String[number_servers];
           System.out.println("Enter the IP Address of "+number_servers+" receivers: ");
           for(int l=0;l<number_servers;l++){
               ip_address[l]=br.readLine();
           }
           System.out.println("Enter the Maximum Segment Size(MSS): ");
           mss_value=Integer.parseInt(br.readLine());
           
           System.out.println("Enter the file name to be sent");
           file_path = br.readLine()+".txt";
                        
           System.out.println("Enter the port number");
           port_value=Integer.parseInt(br.readLine());
           client=new DatagramSocket();
           client.setSoTimeout(50);
           new Client().start_fun();  
    }
        public synchronized boolean[] recv_pkt (String data,String[] ip_address,byte[] sendData,Thread t,String final_sequence_number) throws Exception
    {
        byte[] receiveData = new byte[1024];
        DatagramPacket sendPacket1=null;
        String thread_name = t.currentThread().getName();

        String[] thread_num = thread_name.split("_");
        InetAddress server_IPAddress=null;
        int thread_number = Integer.parseInt(thread_num[1]);
             try{ 
                 
                		 while(sendall==false)
                		 {
                		 System.out.println("waiting.. segment not sent to all servers");
                		 Thread.sleep(200);
                		 }
                	 
                 DatagramPacket receivePacket = null;

             receivePacket = new DatagramPacket(receiveData, receiveData.length);
            
             client.receive(receivePacket);
         
             String received_data = new String(receivePacket.getData());
             System.out.println("received ACK is "+ received_data);
             String[] ack_recvd=received_data.split(" EOF ");
               server_IPAddress=receivePacket.getAddress();
               String ipaddr=server_IPAddress.toString();
       
               String ack_data = String.valueOf(Long.parseLong(ack_recvd[1].trim()));
              String ack_data_1 = "1010101010101010";
              int len=ip_address.length;
          	if(ack_data.equals(ack_data_1))
              {
                 
                  for(int i=0;i<len;i++)
              		{
                        if(ipaddr.equals("/"+ip_address[i]))
                        { serv[i+1]=true;
                     
                        }
                    
              		}
                  
              } 
              serv[0]=false; 
             
              return serv; 
    }
     catch (SocketTimeoutException s) {
    	
    	 	if(sendall==true)
    	 	{
        	 	
        	 	for(int c = 0;c<number_servers;c++)
        	 	{
        	 		if(serv[c+1]==false)
        	 		{
                    sendPacket1 = new DatagramPacket(sendData, sendData.length,InetAddress.getByName(ip_address[c]),port_value);
            	 	client.send(sendPacket1);
            	 	System.out.println("Timeout"+final_sequence_number);
        	 		}
        	 	}
            	serv[0]=true;
            	System.out.println("Segment Retransmitted");
    	 	}
           
        	return serv;
     }
     catch (Exception e) {
        System.out.println(e);
        return serv;
     }
    }

   public void start_fun(){
                     try{
    
                   Receivers client_obj=new Receivers();

                       }
                        catch(Exception e)
                {
                    System.out.println(e);
                }

                   }     

     class Receivers implements Runnable
   {
            Thread t;
 
            public Receivers()
            {   
                 for(int i=0;i<number_servers;i++)
                 {  
                     t=new Thread(this,"T_"+i);
                  t.start();
                 }
                 
            }
          
	public void run()
	{ 
	    
            String thread_name = Thread.currentThread().getName();
            try {
//                        
                        
                         ByteArrayInputStream read_file = null;
                         FileInputStream f = null;
                               
                                File file=new File(file_path);
                                f = new FileInputStream(file);
                                 byte filebytearray[] = new byte[(int)file.length()];
                               int z =0;
		    while(f.available()!=0)
		    {
		    	 filebytearray[z] = (byte)f.read();
		    	z++;
		    }
       
         read_file=new ByteArrayInputStream(filebytearray);
        
         int a=read_file.available();
       //  int sequence_number =0;
           for(n=0;n<=(a/mss_value);n++)
           {
               byte[] cbuf = new byte[mss_value];	
        for(int fl=0;fl<=number_servers;fl++)
        {
        	serv[fl]=false;
        	
        }
          if(n==(a/mss_value))
          {
         
         read_file.read(cbuf, 0,a%mss_value);
                  }
    
          else
          {
                      read_file.read(cbuf, 0, mss_value);
                      int j=0;
                      j+=mss_value;
                      read_file.mark(j);
   

                  }
         
        String str = new String(cbuf);

                        String sequence_number3 = Integer.toBinaryString(n);
                        String final_sequence_number =  sequence_number3;
                           System.out.println("Sequence No: "+final_sequence_number);
                     
//          CHECKSUM
                        checks.update(cbuf,0,cbuf.length);
		long checks1 = checks.getValue();
                System.out.println("calculated checksum: "+checks1);
     Arrays.fill(cbuf,(byte)0);
     
//          SEQUENCE NO
        data=final_sequence_number+"<sp>"+checks1+"<sp>"+data_identifier+"<sp>"+str+"<sp>"; 
     
           byte[] sendData = data.getBytes();
      
            if(flag==false)
            {
                while(sendall==false)
                {flag=true;
                 for(int b=0;b<number_servers;b++)
                 	{
                    sendPacket = new DatagramPacket(sendData, sendData.length,InetAddress.getByName(ip_address[b]),port_value);
                    if(b==number_servers-1)
                 	   sendall=true;
          
                    client.send(sendPacket);
                
             		}
                }

            }
         
             serv=recv_pkt(data,ip_address,sendData,t,final_sequence_number);
             
             while(serv[0]==true)   
             {
                 System.out.println("Timeout"+final_sequence_number);
             serv=recv_pkt(data,ip_address,sendData,t,final_sequence_number);
             }
     
             while(all==false)
             { 
             thread_name = t.currentThread().getName();
      
             thread_num = thread_name.split("_");
             thread_number = Integer.parseInt(thread_num[1]);

                       for(int p=1;p<=ip_address.length;p++)
                       { 
                            if( serv[p] == true)
                            { all = true;
                             
                            }
                       else
                            { all = false;
                           
                            threading[thread_number]=n;
                          
                            Thread.sleep(400);
                       
                            p=0;
                            thread_name = t.currentThread().getName();
                           
                            thread_num = thread_name.split("_");
                            thread_number = Integer.parseInt(thread_num[1]);
                            if(n!=threading[thread_number])
                            	{
                            
                            	if(n>(a/mss_value))
                            	{
                                	all=true;
                                	System.out.println("Entire file sent to all servers");
                            		break;
                            	}
                            	serv=recv_pkt(data,ip_address,sendData,t,final_sequence_number);
                                while(serv[0]==true)   
                                {System.out.println("Timeout"+final_sequence_number);
                                serv=recv_pkt(data,ip_address,sendData,t,final_sequence_number);
                                }

                            	}
                            else
                            {
                            	if(all==false)
                                	serv=recv_pkt(data,ip_address,sendData,t,final_sequence_number);
                            }
                            }
            } 
                       
             }
             sendall=false;
             flag=false;

    } 
          
            }
           catch(Exception e)
        	{
                	System.out.println(e);	 
        	} 

}
}
}
   
    

        
   

   
    
          
            
            
            
            
                         
      
          
        
             

              
                            