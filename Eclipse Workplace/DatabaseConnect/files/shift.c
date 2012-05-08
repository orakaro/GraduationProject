#include<stdio.h>
#include"mpi.h"
main(int argc, char **argv){
	MPI_Status status;
	int myrank,size,dest,source,a;

	MPI_Init(&argc,&argv);
	MPI_Comm_rank(MPI_COMM_WORLD,&myrank);	
	MPI_Comm_size(MPI_COMM_WORLD,&size);
	dest=(myrank+1)%size;
	source=(size+myrank-1)%size;
	MPI_Sendrecv(&myrank,1,MPI_INT,dest,0,&a,1,MPI_INT,source,0,MPI_COMM_WORLD,&status);

	printf("Old rank %d  New rank %d\n",a,myrank);
	MPI_Finalize();
}
