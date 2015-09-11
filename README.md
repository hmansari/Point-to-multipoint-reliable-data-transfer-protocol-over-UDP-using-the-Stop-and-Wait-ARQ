# Point-to-multipoint-reliable-data-transfer-protocol-over-UDP-using-the-Stop-and-Wait-ARQ
Source code

Implemented point-to-multipoint reliable data transfer protocol using the Stop-and-Wait automatic repeat request (ARQ) scheme, and carry out 
a number of experiments to evaluate its performance by building various fundamental skills related to transport layer services.


Editor Used : NetBeans
JDK Version : java version "1.8.0_20"

Compilation Of the Projects.

1. Files in the projects are dependent on each other as we use the reference of one file into another.
2. For compilation use the "Makefile" file that will automatically make the classes of the java files. 
3. It is must for all the files to get compiled before running this project.

Compilation and Running If using terminal:

For Server:

make                 					---compiling file

java Server           				---executing 

For Client:

make          	          			       	---compiling file (Don't write, if already done at server terminal)

java Client      	                     	---executing file

Exceuting:

1. First run Server.java. The syntax of the input is as follows:

p2mpserver port# file-name p

Invoking Server

Enter the port number

<input>

The destination file name is: 

<input>

Enter the probability 

<input>


2. After that, run Client.java. The syntax is as follows:

p2mpclient server-1 server-2 ...-n server-port# file-name MSS

Invoking Client

Enter the number of receivers: 

<input>

Enter the IP Address of 1 receivers: 

<input>

Enter the Maximum Segment Size(MSS): 

<input>

Enter the file name to be sent

<input>

Enter the port number

<input>
