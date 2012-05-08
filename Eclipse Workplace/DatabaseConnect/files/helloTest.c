/*The Parallel Hello World Program*/
#include <stdio.h>
#include <mpi.h>

main(int argc, char **argv)
{
      int node;
         
         MPI_Init(&argc,&argv);
            MPI_Comm_rank(MPI_COMM_WORLD, &node);
                 
               printf("Hello World Wide Web from son number %d of Obama \n",node);
                           
                  MPI_Finalize();
}

