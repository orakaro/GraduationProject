package databaseconnect;

import java.util.ArrayList;

public class Recv {
	public ArrayList<Double> tsStart = new ArrayList<Double>();
	public ArrayList<Double> tsEnd = new ArrayList<Double>();
	public int currentProcess;
	
	public int icomm;
	public int rank;
	public int thd;
	
	public String type;
	
	Recv(int rank){
		currentProcess=0;
		this.rank=rank;
		this.icomm=0;
		this.thd=0;
		this.type="bare";
	}
	public void putTsStart(double ts){
		this.tsStart.add(ts);
	}
	public void putTsEnd(double ts){
		this.tsEnd.add(ts);
	}
}
