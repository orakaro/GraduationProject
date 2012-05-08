#include <stdio.h>
#include "mpi.h"
void main (int argc, char **argv)
{
int a,myrank, error, buffer, er, er2;
MPI_Status status;

MPI_Init(&argc, &argv);
MPI_Comm_rank(MPI_COMM_WORLD, &myrank);

if(myrank==0) {
	printf("From rank %d\n a=10\n",myrank);
	a=10;
	error = MPI_Send(&a, 1, MPI_INT, 1, 0, MPI_COMM_WORLD); 
	er = MPI_Send(&a, 1, MPI_INT, 2, 0, MPI_COMM_WORLD); 
}
else if (myrank==1) {
	error = MPI_Recv(&a, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, &status); 
	printf("\nFrom rank %d\n a=%d\n",myrank,a*(myrank+1));
}
else if (myrank==2) {
	er = MPI_Recv(&a, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, &status); 	
	er2 = MPI_Recv(&a, 1, MPI_INT, 3, 0, MPI_COMM_WORLD, &status); 		
	printf("\nFrom rank %d\n a=%d\n",myrank,a*(myrank+1));
}
else if (myrank==3) {
	a=10;
	er2 = MPI_Send(&a, 1, MPI_INT, 2, 0, MPI_COMM_WORLD); 
	printf("\nFrom rank %d\n a=%d\n",myrank,a*(myrank+1));
}


MPI_Finalize();
}
