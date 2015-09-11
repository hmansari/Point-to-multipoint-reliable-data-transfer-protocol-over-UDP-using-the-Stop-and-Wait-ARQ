package ip_newprj2.src.ip_newprj2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;
import java.util.zip.CRC32;
import java.util.zip.Checksum;


public class Server 
{
	public Server()
	{

	}

   public static void main(String args[]) throws Exception
      {
	Checksum checks = new CRC32();   
        int port_no;
     
       System.out.println("p2mpserver port# file-name p");
 
	    	DatagramSocket serverSocket;
            byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024];
         //   String user_input=null;
            String file_name;
            boolean flag=true;
            //System.out.println("true: " );

    	    Random generator = new Random();
    		int seqnum = 0;
                System.out.println("Invoking Server");
                BufferedReader input =new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Enter the port number");
                port_no=Integer.parseInt(input.readLine());
              
                serverSocket = new DatagramSocket(port_no);
    			//file_name=argz[2]+".txt";
                System.out.print("The destination file name is: ");
                file_name=input.readLine()+".txt";
                File file=new File(file_name);
                
                System.out.print("Enter the probability ");
                double p_value = Double.parseDouble(input.readLine());
//                
//                double p_value = Double.parseDouble(argz[3]);

    			while(flag==true)//?
    			{

                double r_value = generator.nextDouble();
                System.out.println("Random value generated is "+r_value);
                if (r_value <= p_value)
                {
					System.out.println("Packet loss, sequence number = "+seqnum);
					continue;
                }
           	
            	DatagramPacket rcv_Packet = new DatagramPacket(receiveData, receiveData.length);
                  serverSocket.receive(rcv_Packet);
                  //System.out.println("packet: " );
                  String sentence = new String(rcv_Packet.getData());
                //  System.out.println("RECEIVED segment: " + sentence);
                  InetAddress IPAddress = rcv_Packet.getAddress();
                  int port = rcv_Packet.getPort();
                 
                  String[] header=sentence.split("<sp>");
                  //System.out.println("header length"+header.length);
                  if (header[2].equals("0101010101010101")==false)
                  {
                	  System.out.println("wrong data identifier"+header[2]);
                	  continue;
                  }
                  
                  byte[] cbuf = new byte[9];
                  cbuf = header[3].getBytes();
                  //checksum
                  checks.update(cbuf,0,cbuf.length);
		long checks1 = checks.getValue();
//                  String checksum=sunCRC16( cbuf ); 
                  System.out.println("calculated checksum: "+checks1);
              

                  String s2 = Integer.toBinaryString(170);
                  String ack_ident=s2+s2;
                  String ack = header[0] + " EOF " + ack_ident; 
                  sendData = ack.getBytes();
                  //*ack

                  
                  DatagramPacket sendPacket =
                  new DatagramPacket(sendData, sendData.length, IPAddress, port);
                  serverSocket.send(sendPacket);
                  System.out.println("Acknowledgement sent: "+header[0]);
            
                  try {
                	    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file_name, true)));
                	    out.append(header[3]);
                	    out.flush();
                	    System.out.println("Segment sent");
                	    out.close();
                	} catch (IOException e) {
                		System.out.println(e);
                	}

                 

    			}
      }
}
