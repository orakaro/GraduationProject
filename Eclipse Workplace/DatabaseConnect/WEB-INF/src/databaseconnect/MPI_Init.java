package databaseconnect;

public class MPI_Init {
	public double ts1;
	public double ts2;
	public double ts3;
	
	public int icomm;
	public int rank;
	public int thd;
	
	public String type1;
	public String type2;
	public String type3;
	
	public float shift;
	
	protected int CommWorldCreate_icomm;
	protected int CommWorldCreate_rank;
	protected int CommWorldCreate_wrank;
	protected String CommWorldCreate_gcomm;
	
	protected int CommSelfCreate_icomm;
	protected int CommSelfCreate_rank;
	protected int CommSelfCreate_wrank;
	protected String CommSelfCreate_gcomm;
	
	public String gcomm;

		MPI_Init(int rank) {
		this.rank=rank;
		this.icomm=0;
		this.thd=0;
		this.type1="shft";
		this.type2="comm";
		this.type3="comm";
		this.CommWorldCreate_icomm=0;
		this.CommWorldCreate_rank=rank;
		this.CommWorldCreate_wrank=rank;
		this.CommSelfCreate_icomm=rank+1;
		this.CommSelfCreate_rank=0;
		this.CommSelfCreate_wrank=rank;
	}

}
