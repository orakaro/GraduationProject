package databaseconnect;

public class SendRecvMessage {
	public double tsStart;
	public double tsEnd;
	
	public int icomm;
	public int thd;
	
	public int senderRank;
	public int receiverRank;
	
	public int tag;
	public int sz;
	
	SendRecvMessage(int Srank, int Rrank){
		this.senderRank=Srank;
		this.receiverRank=Rrank;
		this.icomm=0;
		this.thd=0;
	}
}
