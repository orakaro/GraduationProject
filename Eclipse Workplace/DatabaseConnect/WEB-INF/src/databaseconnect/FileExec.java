/*File execution page
	* Overwrite after check if file is changed or not
	* SCP file to cluster   
	* Let cluster exe and get result line by line back to browser 
*/

package databaseconnect;

import ch.ethz.ssh2.*;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
//import java.sql.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileExec extends HttpServlet {
	public static int NodeNum;
	public static String UFileNameWithoutExtension;
    public static String[] rbuf=new String[100];
    public static int lineCount;//use to read output line by line 
	private static final String hostname = "192.168.100.30";
	private static final String userid = "root";
	private static final String password = "h!ked@";
	final int BUFFER_SIZE = 4096;
//	public static String getFileName;
	
//Copy file to cluster
public void doSCP() throws IOException {
		Connection conn = new Connection(hostname);
		conn.connect();
		boolean result = conn.authenticateWithPassword(userid, password);
		if (result) {
			String BasicPATH="C://Research corner/Eclipse Workplace/DatabaseConnect/files/";
			String PATH =BasicPATH.concat(FileUploaded.UFileName);//path to the uploaded file
	//		getFileName=PATH;
			SCPClient scp = conn.createSCPClient();
			scp.put(PATH, "/opt/css/v-minh");
		}
		conn.close();
}

//Run file
public void doExe(int nodeNum) throws IOException {
			lineCount=0;
// connect & login
			Connection conn = new Connection(hostname);
			conn.connect();
			boolean result = conn.authenticateWithPassword(userid, password);
			if (result) {
				Session session = conn.openSession();
				//session.execCommand("ls -l");
				int Ulength = FileUploaded.UFileName.length();
				UFileNameWithoutExtension=FileUploaded.UFileName.substring(0, Ulength-2);//get the file name without extension ".c"
				session.execCommand(
						"mpicc -mpe=mpilog -o /opt/css/v-minh/"+UFileNameWithoutExtension+" /opt/css/v-minh/"+UFileNameWithoutExtension+".c" +
						"&& mpdboot" +
						"&& mpiexec -n "+nodeNum+" /opt/css/v-minh/"+UFileNameWithoutExtension +
						"&& clog2_print /opt/css/v-minh/"+UFileNameWithoutExtension+".clog2 > /opt/css/v-minh/"+UFileNameWithoutExtension+".txt" 
						);
				streamToString(session.getStdout());
//Printout to Java console result of execution file
				for(int i=0;i<lineCount;i++)
						System.out.println(rbuf[i]);
				session.close();
			}
			conn.close();
	}


//Copy log file to WebServer
public void doLogSCP() throws IOException {
	Connection conn = new Connection(hostname);
	conn.connect();
	boolean result = conn.authenticateWithPassword(userid, password);
	if (result) {
		String TXTPATH="/opt/css/v-minh/"+UFileNameWithoutExtension+".txt";
		String CLOGPATH="/opt/css/v-minh/"+UFileNameWithoutExtension+".clog2";
		SCPClient clogScp = conn.createSCPClient();
		clogScp.get(TXTPATH, "C://Research corner/Eclipse Workplace/DatabaseConnect/Logfiles/");
		SCPClient txtScp = conn.createSCPClient();
		txtScp.get(CLOGPATH, "C://Research corner/Eclipse Workplace/DatabaseConnect/Logfiles/");
	}
	conn.close();
}


// read InputStream and return output as String
	private void streamToString(InputStream in) throws IOException {
		if (in != null) {           
			                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			                while ( (rbuf[lineCount]=reader.readLine()) != null) {
			                    lineCount++;
			                }
		} else {       
		}
	}
	
	private static final long serialVersionUID = 1L;
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		req.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter writer = resp.getWriter();
		String w = req.getParameter("nodeNum");
		int n = Integer.parseInt(w);
		NodeNum = n;
		String v = req.getParameter("dataTrick");//get the newest TextArea content
		String chkb = req.getParameter("SaveCheck");//get the check value to know if file is changed of not
		int changeFlag;  
		if(chkb == null)  
			changeFlag = 0;  
		else  
		    changeFlag = 1; 

//Modify file if changed 
		if ( changeFlag==1){
		String sourceName="C://Research corner/Eclipse Workplace/DatabaseConnect/files/"+FileUploaded.UFileName;
	    try{
	    	File source = new File(sourceName);
	    	File tmp = new File("tmp.c");
	    	if(!tmp.exists()) tmp.createNewFile();
	    	
	    	FileWriter fw = new FileWriter(tmp);
	    	fw.write(v);
	    	fw.close();
	    	boolean deleted = source.delete();
	    	if (deleted) 
	    		tmp.renameTo(source);
	    }
		catch(Exception se){
			System.out.println("Error - "+se.toString());
		}
		}
		
		writer.println("<html>");
		writer.println("<body>");
			doSCP();
			doExe(n);
			doLogSCP();
		writer.println("<p>**********************************************</p>");
		for(int i=0;i<lineCount;i++)
			writer.println("<p>" + rbuf[i] + "</p>");
		writer.println("<p>**********************************************</p>");
		
		writer.println("<form name=\"form1\" action=\"http://localhost:8080/DatabaseConnect/servlets/Profiler\" method=\"POST\" >");
		writer.println("<input type=\"submit\" value=\"View Profiler\" >");
		writer.println("</form>"); 

		writer.println("</body>");
		writer.println("</html>");
	}
}