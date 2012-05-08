#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "mpi.h"

#define MSG_LEN 100

int main(int argc, char* argv[]){
	int my_rank;
	int tag = 0;
	char message[MSG_LEN];
MPI_Status status;
	MPI_Init(&argc, &argv);
	MPI_Comm_rank(MPI_COMM_WORLD, &my_rank);

	double starttime,endtime;/* 時間格納用変数 */
	starttime = MPI_Wtime();/* 時間計測開始 */
	
	if(my_rank != 0){
		int dest = 0,count;
		int senddata = 1;
		int recvdata = 0;
		
		printf("start\n");

		for(count==0;count<100000;count++){
			if(my_rank == 1){
		//		printf("my rank=%d：count=%d\n",my_rank,count);
				MPI_Send(&senddata, 1, MPI_INT, 2, 0, MPI_COMM_WORLD);
				MPI_Recv(&recvdata, 1, MPI_INT, 3, MPI_ANY_TAG, MPI_COMM_WORLD, &status);
			}else if(my_rank == 2){
		//		printf("my rank=%d：count=%d\n",my_rank,count);
				MPI_Send(&senddata, 1, MPI_INT, 3, 0, MPI_COMM_WORLD);
				MPI_Recv(&recvdata, 1, MPI_INT, 1, MPI_ANY_TAG, MPI_COMM_WORLD, &status);
			}else if(my_rank == 3){
		//		printf("my rank=%d：count=%d\n",my_rank,count);
				MPI_Send(&senddata, 1, MPI_INT, 1, 0, MPI_COMM_WORLD);
				MPI_Recv(&recvdata, 1, MPI_INT, 2, MPI_ANY_TAG, MPI_COMM_WORLD, &status);
			}
		}
		
		sprintf(message,"Hello, my process rank is %d", my_rank);
		MPI_Send(message, strlen(message)+1, MPI_CHAR, dest, tag, MPI_COMM_WORLD);
	}
	
	

	else{
		int process_num;
		int source;
		MPI_Status recv_status;

		MPI_Comm_size(MPI_COMM_WORLD, &process_num);

		for(source = 1; source < process_num; source++){

			MPI_Recv(message, MSG_LEN, MPI_CHAR, source, tag, MPI_COMM_WORLD,&recv_status);

			fprintf(stdout, "%s\n", message);
		}
		}
	
		if(my_rank==0){
		endtime = MPI_Wtime();
		printf("time:%f(s)\n",endtime-starttime);
	}

	MPI_Finalize();
	exit(EXIT_SUCCESS);
}
