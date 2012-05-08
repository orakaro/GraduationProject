#include <stdio.h>
#include "mpi.h"
int w,m,n,p,myrank,size;
MPI_Status status;

//input 
void input()
{
printf("Input number of rows matrix a\n");
scanf("%d",&m);
printf("Input number of rows matrix b\n");
scanf("%d",&n);
printf("Input number of columms of matrix b\n");
scanf("%d",&p);
}


//Main function
void main (int argc, char **argv)
{
int i,j,k;
float s;
float a[m][n],b[n][p],c[m][p];

MPI_Init(&argc, &argv);
MPI_Comm_rank(MPI_COMM_WORLD, &myrank);
MPI_Comm_size(MPI_COMM_WORLD, &size);


//Create size of 2 matrix
if(myrank==0){
//input();
m=3;n=5;p=7;
	MPI_Send(&m,1,MPI_INT,myrank+1,0,MPI_COMM_WORLD);
	MPI_Send(&n,1,MPI_INT,myrank+1,1,MPI_COMM_WORLD);
	MPI_Send(&p,1,MPI_INT,myrank+1,2,MPI_COMM_WORLD);
	
}
else{
	MPI_Recv(&m,1,MPI_INT,myrank-1,0,MPI_COMM_WORLD,&status);
	MPI_Recv(&n,1,MPI_INT,myrank-1,1,MPI_COMM_WORLD,&status);
	MPI_Recv(&p,1,MPI_INT,myrank-1,2,MPI_COMM_WORLD,&status);
}

//printf("Myrank is %d\n",myrank);
//Create matrix
//Create a
for(i=0;i<m/size;i++)
	for(j=0;j<n;j++) a[i][j]=1.0;

//Create b	
for(i=0;i<n;i++)
	for(j=0;j<p;j++) b[i][j]=1.0;

//Create c
for(i=0;i<m/size;i++)
	for(j=0;j<p;j++) {
		c[i][j]=0;
		for(k=0;k<n;k++) c[i][j]+=a[i][k]*b[k][j];
	}


//Print Multiplied Matrix
int u,v;
	if(myrank!=0)
		MPI_Recv(&w,1,MPI_INT,myrank-1,20,MPI_COMM_WORLD,&status);
	else printf("The multiplied matrix !! \n");
	
	for(u=0;u<m/size;u++){
		for(v=0;v<p;v++) printf("%7.3f",c[u][v]);
		printf("\n");
	}
	if(myrank!=size-1)
		MPI_Send(&myrank,1,MPI_INT,myrank+1,20,MPI_COMM_WORLD);


MPI_Finalize();
}



