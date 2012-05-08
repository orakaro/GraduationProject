#include<stdio.h>
#include"mpi.h"
main(int argc, char **argv)
{
	MPI_Status status;
	int myrank,size,a;
	MPI_Init(&argc,&argv);
	MPI_Comm_rank(MPI_COMM_WORLD,&myrank);
	MPI_Comm_size(MPI_COMM_WORLD,&size);
	if(myrank==0){
	a=3;
	}
	if(myrank!=0){
		MPI_Recv(&a,1,MPI_INT,myrank-1,0,MPI_COMM_WORLD,&status);
	a*=2;
	}
	printf("(%d,%d)\n",a,myrank);
	if(myrank!=size-1){
		MPI_Send(&a,1,MPI_INT,myrank+1,0,MPI_COMM_WORLD);
	}
	MPI_Finalize();
}
