/*Profiler created here!
	* Read the txt log file 
*/

package databaseconnect;

import java.io.*;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.Integer;

public class Profiler extends HttpServlet {
    /**
	 * 
	 * 
	 */
	//DEFINE Classes HERE
	public MPI_Init MPI_Init_Array[]= new MPI_Init[FileExec.NodeNum];
	public MPI_comm_rank MPI_comm_rank_Array[]= new MPI_comm_rank[FileExec.NodeNum];
	public MPI_comm_size MPI_comm_size_Array[]= new MPI_comm_size[FileExec.NodeNum];
	public MPI_Finallize MPI_Finallize_Array[]= new MPI_Finallize[FileExec.NodeNum];
	public MPI_Send MPI_Send_Array[]= new MPI_Send[4000000];
	public MPI_Recv MPI_Recv_Array[]= new MPI_Recv[4000000];
	public SendRecvMessage SendRecvMessage_Array[]= new SendRecvMessage[4000000];
	public Send Send_Array[] = new Send[FileExec.NodeNum];
	public Recv Recv_Array[] = new Recv[FileExec.NodeNum];
	public SendRecv SendRecv_Array[] = new SendRecv[FileExec.NodeNum];
	public Rank Rank_Array[] = new Rank[FileExec.NodeNum];
	
	public int mesCount=0;
	public double MaxTime;
	public String[] rbuf=new String[100];
	public static int lineCount=0;
	private static final long serialVersionUID = 1L;

	//@SuppressWarnings("deprecation")
	
	public void getToInstance() throws IOException {
/*	
 	Read & Get Log Info into Class Architecture START HERE!!!!!!
	We have created classes as MPI_Init, MPI_Comm_rank, MPI_Finallize, MPI_Send, MPI_Recv, Send, Recv,
	So have to create GID references array here. 
*/		
		String[] GID=new String[100];//Get GIDs
		int GIDcount=0;
		
		String LogFilePATH="C://Research corner/Eclipse Workplace/DatabaseConnect/Logfiles/";
		String PATH =LogFilePATH.concat(FileExec.UFileNameWithoutExtension);//path to log file
		PATH =PATH.concat(".txt");
		BufferedReader inBuff=new BufferedReader(new FileReader(PATH));

//Init the Arrays HERE
		for (int InitCount=0;InitCount<FileExec.NodeNum;InitCount++){
			MPI_Init_Array[InitCount] = new MPI_Init(InitCount);
			MPI_comm_rank_Array[InitCount] = new MPI_comm_rank(InitCount);
			MPI_comm_size_Array[InitCount] = new MPI_comm_size(InitCount);
			MPI_Finallize_Array[InitCount] = new MPI_Finallize(InitCount);
			MPI_Send_Array[InitCount] = new MPI_Send(InitCount);
			MPI_Recv_Array[InitCount] = new MPI_Recv(InitCount);
			Send_Array[InitCount] = new Send(InitCount);
			Recv_Array[InitCount] = new Recv(InitCount);
			SendRecv_Array[InitCount] = new SendRecv(InitCount);
			Rank_Array[InitCount] = new Rank(InitCount);
		}

		String temp = null;
		while ((temp = inBuff.readLine())!=null){
			
//FROM HERE WE READ THE LOG FILE LINE BY LINE
//*****************************************************************************************

//First get the GID value

			if (temp.substring(0,3).equals("GID")) {
					//System.out.println(temp.substring(0,3));
					int LIDIndex = temp.indexOf("LID");
					GID[GIDcount++] = temp.substring(4,LIDIndex);
					//System.out.println(GID[count-1]);
			}
	
//Now the MPI_*** class !!
			else if (temp.substring(0,2).equals("ts") && temp.indexOf("type=sdef")==-1 && temp.indexOf("type=edef")==-1){

//Define used variable
				int preRecvRankIndex,preRankIndex, rankIndex, rankValue, tsIndex, shiftIndex, tagIndex, sizeIndex, sizeValue, tagValue;
				float shiftValue;
				double tsValue;
				
//Search for MPI_Init
					if (temp.indexOf("type=shft")!=-1){
						System.out.println("shift");
						preRankIndex = temp.indexOf("rank");
						rankIndex = temp.indexOf("thd");//get the rank
						rankValue = Integer.parseInt(temp.substring(preRankIndex+5,rankIndex-1));
						tsIndex = temp.indexOf("icomm");//get the ts
						tsValue = Double.parseDouble(temp.substring(3,tsIndex-2));

						MPI_Init_Array[rankValue].ts1=tsValue;
						shiftIndex = temp.indexOf("shift");//get the shift
						shiftValue = Float.parseFloat(temp.substring(shiftIndex+6,temp.length()));
						MPI_Init_Array[rankValue].shift =shiftValue;
					}
					
					if (temp.indexOf("et=CommWorldCreate")!=-1){
						System.out.println("CommWorldCreate");
						preRankIndex = temp.indexOf("rank");
						rankIndex = temp.indexOf("thd");//get the rank
						rankValue = Integer.parseInt(temp.substring(preRankIndex+5,rankIndex-1));
						tsIndex = temp.indexOf("icomm");//get the ts
						tsValue = Double.parseDouble(temp.substring(3,tsIndex-2));
						MPI_Init_Array[rankValue].ts2 = tsValue;
						MPI_Init_Array[rankValue].gcomm = GID[MPI_Init_Array[rankValue].CommWorldCreate_icomm];
						
					}
					
					if (temp.indexOf("et=CommSelfCreate")!=-1){
						System.out.println("CommSelfCreate");
						preRankIndex = temp.indexOf("rank");
						rankIndex = temp.indexOf("thd");//get the rank
						rankValue = Integer.parseInt(temp.substring(preRankIndex+5,rankIndex-1));
						tsIndex = temp.indexOf("icomm");//get the ts
						tsValue = Double.parseDouble(temp.substring(3,tsIndex-2));
						MPI_Init_Array[rankValue].ts3 = tsValue;
						MPI_Init_Array[rankValue].gcomm = GID[MPI_Init_Array[rankValue].CommWorldCreate_icomm];
					}
					
//Search for MPI_Comm_rank
					if (temp.indexOf("et=48")!=-1){
						System.out.println("et=48");
						preRankIndex = temp.indexOf("rank");
						rankIndex = temp.indexOf("thd");//get the rank
						rankValue = Integer.parseInt(temp.substring(preRankIndex+5,rankIndex-1));
						tsIndex = temp.indexOf("icomm");//get the ts
						tsValue = Double.parseDouble(temp.substring(3,tsIndex-2));
						MPI_comm_rank_Array[rankValue].tsStart = tsValue;
					}
					
					if (temp.indexOf("et=49")!=-1){
						System.out.println("et=49");
						preRankIndex = temp.indexOf("rank");
						rankIndex = temp.indexOf("thd");//get the rank
						rankValue = Integer.parseInt(temp.substring(preRankIndex+5,rankIndex-1));
						tsIndex = temp.indexOf("icomm");//get the ts
						tsValue = Double.parseDouble(temp.substring(3,tsIndex-2));
						MPI_comm_rank_Array[rankValue].tsEnd = tsValue;
					}
//Search for MPI_Comm_size
					if (temp.indexOf("et=54")!=-1){
						System.out.println("et=54");
						preRankIndex = temp.indexOf("rank");
						rankIndex = temp.indexOf("thd");//get the rank
						rankValue = Integer.parseInt(temp.substring(preRankIndex+5,rankIndex-1));
						tsIndex = temp.indexOf("icomm");//get the ts
						tsValue = Double.parseDouble(temp.substring(3,tsIndex-2));
						MPI_comm_size_Array[rankValue].tsStart = tsValue;
					}
					
					if (temp.indexOf("et=55")!=-1){
						System.out.println("et=55");
						preRankIndex = temp.indexOf("rank");
						rankIndex = temp.indexOf("thd");//get the rank
						rankValue = Integer.parseInt(temp.substring(preRankIndex+5,rankIndex-1));
						tsIndex = temp.indexOf("icomm");//get the ts
						tsValue = Double.parseDouble(temp.substring(3,tsIndex-2));
						MPI_comm_size_Array[rankValue].tsEnd = tsValue;
					}			
//Search for MPI_Finallize
					if (temp.indexOf("et=-9")!=-1){
						System.out.println("et=-9");
						preRankIndex = temp.indexOf("rank");
						rankIndex = temp.indexOf("thd");//get the rank
						rankValue = Integer.parseInt(temp.substring(preRankIndex+5,rankIndex-1));
						tsIndex = temp.indexOf("icomm");//get the ts
						tsValue = Double.parseDouble(temp.substring(3,tsIndex-2));
						MPI_Finallize_Array[rankValue].ts = tsValue;
					}
					
//Search for MPI_Send
					if (temp.indexOf("et=send")!=-1){
						System.out.println("et=send");
						preRankIndex = temp.indexOf("rank");
						rankIndex = temp.indexOf("thd");//get the rank
						rankValue = Integer.parseInt(temp.substring(preRankIndex+5,rankIndex-1));
						tsIndex = temp.indexOf("icomm");//get the ts
						tsValue = Double.parseDouble(temp.substring(3,tsIndex-2));
						preRecvRankIndex = temp.lastIndexOf("rank");
						int recvRankIndex = temp.indexOf("tag");//get the Recv rank
						int recvRankValue = Integer.parseInt(temp.substring(preRecvRankIndex+5,recvRankIndex-1));
						sizeIndex = temp.indexOf("sz");//get the Recv stuff
						tagValue = Integer.parseInt(temp.substring(recvRankIndex+4,sizeIndex-1));
						sizeValue = Integer.parseInt(temp.substring(sizeIndex+3,sizeIndex+4));
						MPI_Send_Array[rankValue].putTs(tsValue);
						MPI_Send_Array[rankValue].putRecvRank(recvRankValue);			
						MPI_Send_Array[rankValue].putTag(tagValue);
						MPI_Send_Array[rankValue].putSz(sizeValue);
					}
			
//Search for MPI_Recv
					if (temp.indexOf("et=recv")!=-1){
						System.out.println("et=recv");
						preRankIndex = temp.indexOf("rank");
						rankIndex = temp.indexOf("thd");//get the rank
						rankValue = Integer.parseInt(temp.substring(preRankIndex+5,rankIndex-1));
						tsIndex = temp.indexOf("icomm");//get the ts
						tsValue = Double.parseDouble(temp.substring(3,tsIndex-2));
						preRecvRankIndex = temp.lastIndexOf("rank");
						int sendRankIndex = temp.indexOf("tag");//get Send rank
						int sendRankValue = Integer.parseInt(temp.substring(preRecvRankIndex+5,sendRankIndex-1));
						sizeIndex = temp.indexOf("sz");//get the Send stuff
						tagValue = Integer.parseInt(temp.substring(sendRankIndex+4,sizeIndex-1));
						sizeValue = Integer.parseInt(temp.substring(sizeIndex+3,sizeIndex+4));;
						MPI_Recv_Array[rankValue].putTs(tsValue);
						MPI_Recv_Array[rankValue].putSendRank(sendRankValue);				               
						MPI_Recv_Array[rankValue].putTag(tagValue);
						MPI_Recv_Array[rankValue].putSz(sizeValue);
					}
					

//Search for Send Process					
					if (temp.indexOf("et=160")!=-1){
						System.out.println("et=160");
						preRankIndex = temp.indexOf("rank");
						rankIndex = temp.indexOf("thd");//get the rank
						rankValue = Integer.parseInt(temp.substring(preRankIndex+5,rankIndex-1));
						tsIndex = temp.indexOf("icomm");//get the ts
						tsValue = Double.parseDouble(temp.substring(3,tsIndex-2));
						Send_Array[rankValue].currentProcess++;
						Send_Array[rankValue].putTsStart(tsValue);
					}
					
					if (temp.indexOf("et=161")!=-1){
						System.out.println("et=161");
						preRankIndex = temp.indexOf("rank");
						rankIndex = temp.indexOf("thd");//get the rank
						rankValue = Integer.parseInt(temp.substring(preRankIndex+5,rankIndex-1));
						tsIndex = temp.indexOf("icomm");//get the ts
						tsValue = Double.parseDouble(temp.substring(3,tsIndex-2));
						Send_Array[rankValue].putTsEnd(tsValue);
					}
			
//Search for Recv Process	
					if (temp.indexOf("et=154")!=-1){
						System.out.println("et=154");
						preRankIndex = temp.indexOf("rank");
						rankIndex = temp.indexOf("thd");//get the rank
						rankValue = Integer.parseInt(temp.substring(preRankIndex+5,rankIndex-1));
						tsIndex = temp.indexOf("icomm");//get the ts
						tsValue = Double.parseDouble(temp.substring(3,tsIndex-2));
						Recv_Array[rankValue].currentProcess++;
						Recv_Array[rankValue].putTsStart(tsValue);
					}
					
					if (temp.indexOf("et=155")!=-1){
						System.out.println("et=155");
						preRankIndex = temp.indexOf("rank");
						rankIndex = temp.indexOf("thd");//get the rank
						rankValue = Integer.parseInt(temp.substring(preRankIndex+5,rankIndex-1));
						tsIndex = temp.indexOf("icomm");//get the ts
						tsValue = Double.parseDouble(temp.substring(3,tsIndex-2));
						Recv_Array[rankValue].putTsEnd(tsValue);
					}
					
					
//Search for SendRecv Process
					//Search for Recv Process	
					if (temp.indexOf("et=162")!=-1){
						System.out.println("et=162");
						preRankIndex = temp.indexOf("rank");
						rankIndex = temp.indexOf("thd");//get the rank
						rankValue = Integer.parseInt(temp.substring(preRankIndex+5,rankIndex-1));
						tsIndex = temp.indexOf("icomm");//get the ts
						tsValue = Double.parseDouble(temp.substring(3,tsIndex-2));
						SendRecv_Array[rankValue].currentProcess++;
						SendRecv_Array[rankValue].putTsStart(tsValue);
					}
					
					if (temp.indexOf("et=163")!=-1){
						System.out.println("et=163");
						preRankIndex = temp.indexOf("rank");
						rankIndex = temp.indexOf("thd");//get the rank
						rankValue = Integer.parseInt(temp.substring(preRankIndex+5,rankIndex-1));
						tsIndex = temp.indexOf("icomm");//get the ts
						tsValue = Double.parseDouble(temp.substring(3,tsIndex-2));
						SendRecv_Array[rankValue].putTsEnd(tsValue);
					}
			}
				
//*****************************************************************************************

		}

//Create the SendRecvMessage instance from MPI_Send and MPI_Recv
	
		
		for (int j=0;j<FileExec.NodeNum;j++){
			if (MPI_Send_Array[j].flag!=-1)
				for (int k=0;k<MPI_Send_Array[j].recv_rank.size();k++){
					SendRecvMessage_Array[mesCount] = new SendRecvMessage(j,MPI_Send_Array[j].recv_rank.get(k));
					SendRecvMessage_Array[mesCount].tsStart=MPI_Send_Array[j].ts.get(k);
					SendRecvMessage_Array[mesCount].tag=MPI_Send_Array[j].tag.get(k);
					SendRecvMessage_Array[mesCount].sz=MPI_Send_Array[j].sz.get(k);
					int receiverRank = MPI_Send_Array[j].recv_rank.get(k);
					int receiverSize = MPI_Recv_Array[receiverRank].send_rank.size();
					for (int v=0;v<receiverSize;v++){
						if (MPI_Recv_Array[receiverRank].send_rank.get(v)==j)
							SendRecvMessage_Array[mesCount].tsEnd = MPI_Recv_Array[receiverRank].ts.get(v);
					}
					//SendRecvMessage_Array[mesCount].tsEnd=MPI_Recv_Array[MPI_Send_Array[j].recv_rank.get(k)].ts.get(k);
					mesCount++;
				}
		}

//Create Rank instance from all info
		for(int k=0;k<FileExec.NodeNum;k++){
			Rank_Array[k]._Init = MPI_Init_Array[k];
			Rank_Array[k]._comm_rank = MPI_comm_rank_Array[k];
			Rank_Array[k]._comm_size = MPI_comm_size_Array[k];
			Rank_Array[k]._Finallize = MPI_Finallize_Array[k];

			if (SendRecv_Array[k].currentProcess>0) {
				Rank_Array[k]._SendRecv = SendRecv_Array[k];
				}
			if (  MPI_Send_Array[k].flag !=-1){
				Rank_Array[k]._MPI_Send = MPI_Send_Array[k];
				Rank_Array[k]._Send = Send_Array[k];
				}
			if (  MPI_Recv_Array[k].flag !=-1){
				Rank_Array[k]._MPI_Recv = MPI_Recv_Array[k];
				Rank_Array[k]._Recv = Recv_Array[k];
				}
		}
		
//Get the MaxTime
		for(int MT=0;MT<FileExec.NodeNum;MT++){
			if (MPI_Finallize_Array[MT].ts>MaxTime) MaxTime=MPI_Finallize_Array[MT].ts;
		}
		MaxTime*=1000000;		
		
//======================================================================
//THESE CODE USED FOR TESTING !!!!!!

		for(int i=0;i<FileExec.NodeNum;i++){
			System.out.println("MPI_Init_Array["+i+"] 's gcomm: "+MPI_Init_Array[i].gcomm);
			}
		System.out.println("\n");
		
		
		System.out.println("Send Detail****************************");
		for(int w=0;w<FileExec.NodeNum;w++){
			if (MPI_Send_Array[w].flag!=-1){
				System.out.println("Send["+w+"]      "+Send_Array[w].tsStart+"     "+Send_Array[w].tsEnd);
				}
			}
		System.out.println();
		
		System.out.println("Recv Detail****************************");
		for(int w=0;w<FileExec.NodeNum;w++){
			if (MPI_Recv_Array[w].flag!=-1){
				System.out.println("Recv["+w+"]      "+Recv_Array[w].tsStart+"     "+Recv_Array[w].tsEnd);
				}
			}
		System.out.println();
		
		
		System.out.println("MPI_Send Detail****************************");
		for(int w=0;w<FileExec.NodeNum;w++){
			if (MPI_Send_Array[w].flag!=-1){
				System.out.println("MPI_Send["+w+"]     ArrayList of ts: "+MPI_Send_Array[w].ts);
				for (int v=0;v<MPI_Send_Array[w].recv_rank.size();v++){
					System.out.println("Receiver's rank: "+ MPI_Send_Array[w].recv_rank.get(v));
					}
				}
			}
		System.out.println();
		System.out.println("MPI_Recv Detail****************************");
		
		for(int w=0;w<FileExec.NodeNum;w++){
			if (MPI_Recv_Array[w].flag!=-1){
				System.out.println("MPI_Recv["+w+"]     ArrayList of ts: "+MPI_Recv_Array[w].ts);
				for (int v=0;v<MPI_Recv_Array[w].send_rank.size();v++){
					System.out.println("Sender's rank: "+ MPI_Recv_Array[w].send_rank.get(v));
					}
				}
			}
		System.out.println("\n");

		System.out.println("SendRecvMessage Detail****************************");
		for (int j=0;j<mesCount;j++){
					double x= SendRecvMessage_Array[j].tsStart;
					int y = SendRecvMessage_Array[j].tag;
					int z = SendRecvMessage_Array[j].sz;
					double t =SendRecvMessage_Array[j].tsEnd;
					System.out.println("Message No "+j);
					System.out.format("Sender's rank:   %d     Receiver's rank:   %d\n",SendRecvMessage_Array[j].senderRank,SendRecvMessage_Array[j].receiverRank);
					System.out.format("tsStart,tsEnd,tag,sz:          %.0f     %.0f     %d     %d\n",x*1000000,t*1000000,y,z);
		}
		
		System.out.println("\n\n");
//Print Rank 's info
		
		for(int k=0;k<FileExec.NodeNum;k++){
			System.out.format(" Rank "+k+" 's infomation: \n"
					+"MPI_comm_rank: %.0f  %.0f\n"
					+"MPI_comm_size: %.0f  %.0f\n"
					+"MPI_Finallize:  %.0f\n",
					Rank_Array[k]._comm_rank.tsStart*1000000,Rank_Array[k]._comm_rank.tsEnd*1000000,
					Rank_Array[k]._comm_size.tsStart*1000000,Rank_Array[k]._comm_size.tsEnd*1000000,
					Rank_Array[k]._Finallize.ts*1000000
					);
			
			if (SendRecv_Array[k].currentProcess>0){//Have SendRecv
				for (int q=0;q<SendRecv_Array[k].currentProcess;q++){
					System.out.format("SendRecv Process:  %.0f  %.0f\n",
							Rank_Array[k]._SendRecv.tsStart.get(q)*1000000,Rank_Array[k]._SendRecv.tsEnd.get(q)*1000000
							);
				}
			}
			
			if (MPI_Send_Array[k].flag !=-1){
				if (MPI_Recv_Array[k].flag !=-1){//Have Send and Recv also
					
					for (int q=0;q<Send_Array[k].currentProcess;q++){
						System.out.format("Send Process:  %.0f  %.0f\n",
											Rank_Array[k]._Send.tsStart.get(q)*1000000,Rank_Array[k]._Send.tsEnd.get(q)*1000000
											);
					}
					for (int u=0;u<Recv_Array[k].currentProcess;u++){
						System.out.format("Recv Process:  %.0f  %.0f\n",
											Rank_Array[k]._Recv.tsStart.get(u)*1000000,Rank_Array[k]._Recv.tsEnd.get(u)*1000000
											);	
					}
				}
				else {//Have Send and doesn't have Recv
					
					for (int q=0;q<Send_Array[k].currentProcess;q++){
						System.out.format("Send Process:  %.0f  %.0f\n",
											Rank_Array[k]._Send.tsStart.get(q)*1000000,Rank_Array[k]._Send.tsEnd.get(q)*1000000
											);
					}
				}
			}
			
			else if (MPI_Recv_Array[k].flag !=-1){//doesn't have Send and have Recv 
				
				for (int u=0;u<Recv_Array[k].currentProcess;u++){
					System.out.format("Recv Process:  %.0f  %.0f\n",
										Rank_Array[k]._Recv.tsStart.get(u)*1000000,Rank_Array[k]._Recv.tsEnd.get(u)*1000000
										);	
				}
			}
			//Here is doesn't have both Send and Recv
			System.out.println();
		}
		
		
//Print MaxTime
		System.out.format("\nMaxTime:  %.0f\n",MaxTime);
		
//Test if ts-variable really can be compared or not
		/*
		if (MPI_Init_Array[0].ts1 > MPI_Init_Array[1].ts1) System.out.println("rank 0 ts > rank 1 ts");
		else System.out.println("rank 0 ts < rank 1 ts");
		
		if (MPI_Init_Array[1].ts1 > MPI_Init_Array[2].ts1) System.out.println("rank 1 ts > rank 2 ts");
		else System.out.println("rank 1 ts < rank 2 ts");
		
		if (MPI_Init_Array[0].ts1 > MPI_Init_Array[2].ts1) System.out.println("rank 0 ts > rank 2 ts");
		else System.out.println("rank 0 ts < rank 2 ts");
		*/
		inBuff.close();  
}
//END TESTING CODE HERE!!!!
	
	
//======================================================================
	//NOW this is the function that will create the "MPIInfo.xml" XML file
	public void generateXML() throws IOException {
				//File f = new File("C://Research corner/Eclipse Workplace/DatabaseConnect/Flash Builder/MPILogMainProject/src/data/MPIInfo.xml");
				File f = new File("C://Research corner/Eclipse Workplace/DatabaseConnect/XML file/MPIInfo.xml");
				if(f.exists()){
					f.delete();
				}
				f.createNewFile();
			
				try{
					//FileWriter fstream = new FileWriter("C://Research corner/Eclipse Workplace/DatabaseConnect/Flash Builder/MPILogMainProject/src/data/MPIInfo.xml");
					FileWriter fstream = new FileWriter("C://Research corner/Eclipse Workplace/DatabaseConnect/XML file/MPIInfo.xml");
					PrintWriter out = new PrintWriter(new BufferedWriter (fstream));
					out.println("<Cluster>");//Cluster
					out.println("<MaxTime>"+MaxTime+"</MaxTime>\r");
					for(int rankCount=0;rankCount<FileExec.NodeNum;rankCount++){
						//Rank
						out.println("<Rank value=\"Rank "+rankCount+"\">");
							out.println("<MPI_comm_rank label=\"MPI_comm_rank\">");//MPI_comm_rank
							out.println("<tsStart>"+Rank_Array[rankCount]._comm_rank.tsStart*1000000+"</tsStart>");
							out.println("<tsEnd>"+Rank_Array[rankCount]._comm_rank.tsEnd*1000000+"</tsEnd>");
							out.println("</MPI_comm_rank>");
							
							out.println("<MPI_comm_size label=\"MPI_comm_size\">");//MPI_comm_size
							out.println("<tsStart>"+Rank_Array[rankCount]._comm_size.tsStart*1000000+"</tsStart>");
							out.println("<tsEnd>"+Rank_Array[rankCount]._comm_size.tsEnd*1000000+"</tsEnd>");
							out.println("</MPI_comm_size>");
						
							out.println("<MPI_Finallize label=\"MPI_Finallize\">");//MPI_Finallize
							out.println("<ts>"+Rank_Array[rankCount]._Finallize.ts*1000000+"</ts>");
							out.println("</MPI_Finallize>");
							
							if(SendRecv_Array[rankCount].currentProcess>0){//SendRecv Process
								for(int l=0;l<Rank_Array[rankCount]._SendRecv.currentProcess;l++){
									out.println("<SendRecv label=\"SendRecv\">");
									out.println("<tsStart>"+Rank_Array[rankCount]._SendRecv.tsStart.get(l)*1000000+"</tsStart>");
									out.println("<tsEnd>"+Rank_Array[rankCount]._SendRecv.tsEnd.get(l)*1000000+"</tsEnd>");
									out.println("</SendRecv>");
								}
							}
							if(MPI_Send_Array[rankCount].flag!=-1){//Send Process
								for(int m=0;m<Rank_Array[rankCount]._Send.currentProcess;m++){
									out.println("<Send label=\"Send\">");
									out.println("<tsStart>"+Rank_Array[rankCount]._Send.tsStart.get(m)*1000000+"</tsStart>");
									out.println("<tsEnd>"+Rank_Array[rankCount]._Send.tsEnd.get(m)*1000000+"</tsEnd>");
									out.println("</Send>");
								}
							}
							if(MPI_Recv_Array[rankCount].flag!=-1){//Recv Process
								for(int n=0;n<Rank_Array[rankCount]._Recv.currentProcess;n++){
									out.println("<Recv label=\"Recv\">");
									out.println("<tsStart>"+Rank_Array[rankCount]._Recv.tsStart.get(n)*1000000+"</tsStart>");
									out.println("<tsEnd>"+Rank_Array[rankCount]._Recv.tsEnd.get(n)*1000000+"</tsEnd>");
									out.println("</Recv>");
								}
							}						
						out.println("</Rank>");
					}
					//Message
					for (int messageCount=0;messageCount<mesCount;messageCount++){
						out.println("<Message senderRank=\""+SendRecvMessage_Array[messageCount].senderRank+"\" receiverRank=\""+SendRecvMessage_Array[messageCount].receiverRank+"\">");
						out.println("<tsStart>"+SendRecvMessage_Array[messageCount].tsStart*1000000+"</tsStart>");
						out.println("<tsEnd>"+SendRecvMessage_Array[messageCount].tsEnd*1000000+"</tsEnd>");
						out.println("<tag>"+SendRecvMessage_Array[messageCount].tag+"</tag>");
						out.println("<sz>"+SendRecvMessage_Array[messageCount].sz+"</sz>");
						out.println("</Message>");
						}
					out.println("</Cluster>");
					out.close();
				}catch (Exception e){
					System.err.println("Error: "+e.getMessage());
				}
	}
	
	
	public void compileFlexApp() throws IOException {
		 try {  
			             String compileCommand ="mxmlc -source-path+=\"C:\\Research corner\\Eclipse Workplace\\DatabaseConnect\\Flash Builder\\MPILogMainProject\\src\" " +
			             		"-library-path+=\"C:\\Research corner\\Eclipse Workplace\\DatabaseConnect\\Flash Builder\\MPILogMainProject\\libs\\DegrafaLibrary.swc\"," +
			             		"\"C:\\Research corner\\Eclipse Workplace\\DatabaseConnect\\Flash Builder\\MPILogMainProject\\libs\\AxiisLibrary.swc\" " +
			             		"\"C:\\Research corner\\Eclipse Workplace\\DatabaseConnect\\Flash Builder\\MPILogMainProject\\src\\MPILogMainProject.mxml\"";
			 				//String compileCommand ="cp \"C:\\Research corner\\copyCommand.txt\" \"C:\\Research corner\\dir.txt\"";
			             Process compileProcess = Runtime.getRuntime().exec(compileCommand);
			             System.out.println("Executing command: " + compileCommand);
			             String line;
			             BufferedReader input = new BufferedReader (new InputStreamReader(compileProcess.getInputStream()));
			               while ((line = input.readLine()) != null) {
			                 System.out.println(line);
			               }
			               input.close();

			         } catch (IOException e) {  
			             System.err.println(e);
			         }
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		req.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter writer = resp.getWriter();
		getToInstance();
		generateXML();
		//copyXML();
		//compileFlexApp();

		writer.println("<html>");
		writer.println("<body>");
		writer.println("</body>");
		writer.println("</html>");
	}
}