package databaseconnect;
import java.util.*;
import java.lang.Integer;

public class MPI_Send {
	public ArrayList<Double> ts = new ArrayList<Double>();
	
	public int icomm;
	public int rank;
	public int thd;
	
	public String type;
	
	public int recv_icomm;
	public ArrayList<Integer> recv_rank = new ArrayList<Integer>();
	public ArrayList<Integer> tag = new ArrayList<Integer>();
	public ArrayList<Integer> sz = new ArrayList<Integer>();
	
	public int flag;
	
	MPI_Send(int rank){
		this.rank=rank;
		this.icomm=0;
		this.thd=0;
		this.recv_icomm=0;
		this.type="msg";
		this.flag=-1;//not have any recvRank
	}
	
	public void putRecvRank(int rank){
		this.flag=0;
		this.recv_rank.add(rank);
	}
	
	public void putTs(double ts){
		this.flag=0;
		this.ts.add(ts);
	}
	
	public void putTag(int tag){
		this.flag=0;
		this.tag.add(tag);
	}
	
	public void putSz(int sz){
		this.flag=0;
		this.sz.add(sz);
	}
}
