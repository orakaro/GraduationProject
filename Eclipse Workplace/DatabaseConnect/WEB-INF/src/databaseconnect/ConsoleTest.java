package databaseconnect;

import java.io.*;

public class ConsoleTest {
	public static void main(String[] args) {

        //Create a console object.
        Console objConsole = System.console();
        if (objConsole != null) {

            // readLine method call.
            String userName = objConsole.readLine("Enter  username  : ");
            System.out.println("You have entered : " + userName);
        }
    }
}
