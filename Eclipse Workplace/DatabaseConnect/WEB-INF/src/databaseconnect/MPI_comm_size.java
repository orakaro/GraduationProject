package databaseconnect;

public class MPI_comm_size {
	public double tsStart;
	public double tsEnd;
	
	public int icomm;
	public int rank;
	public int thd;
	
	public String type;
	
	MPI_comm_size(int rank){
		this.rank=rank;
		this.icomm=0;
		this.thd=0;
		this.type="bare";
	}

}
