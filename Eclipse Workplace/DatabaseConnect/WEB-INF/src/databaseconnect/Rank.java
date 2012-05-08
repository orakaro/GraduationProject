package databaseconnect;

public class Rank {
	public int rank;
	public MPI_Init _Init;
	public MPI_comm_rank _comm_rank;
	public MPI_comm_size _comm_size;
	public MPI_Finallize _Finallize;
	public MPI_Send _MPI_Send;
	public MPI_Recv _MPI_Recv;
	public Send _Send;
	public Recv _Recv;
	public SendRecv _SendRecv;
	Rank(int rank){
		this.rank=rank;
	}
}
