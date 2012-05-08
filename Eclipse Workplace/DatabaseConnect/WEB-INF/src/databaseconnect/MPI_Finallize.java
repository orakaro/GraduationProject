package databaseconnect;

public class MPI_Finallize {
	public double ts;
	
	public int icomm;
	public int rank;
	public int thd;
	
	public String type;
	
	MPI_Finallize(int rank){
		this.rank=rank;
		this.icomm=0;
		this.thd=0;
		this.type="bare";
	}
}
