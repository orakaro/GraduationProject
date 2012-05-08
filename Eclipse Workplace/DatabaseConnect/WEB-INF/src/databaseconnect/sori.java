package databaseconnect;

import ch.ethz.ssh2.*;
import ch.ethz.ssh2.Connection;

import java.io.*;
//import java.sql.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class sori extends HttpServlet {
    public static String[] rbuf=new String[100];
    public static int lineCount=0;
	private static final String hostname = "192.168.100.30";
	private static final String userid = "root";
	private static final String password = "h!ked@";
	final int BUFFER_SIZE = 4096;
/*
	public static void main(String[] arg) {
		try {
			sori test = new sori();
			
			test.doProc(3);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
*/
	public void doProc(int nodeNum) throws IOException {
	
			lineCount=0;
			// connect & login
			Connection conn = new Connection(hostname);
			conn.connect();
			boolean result = conn.authenticateWithPassword(userid, password);

			if (result) {
				Session session = conn.openSession();
				session.execCommand("mpicc -o /root/v-minh/hello /root/v-minh/hello.c && mpdboot && mpiexec -n "+nodeNum+" /root/v-minh/hello && mpdallexit");
				//session.execCommand("mpicc -o /root/v-minh/message /root/v-minh/message.c && mpdboot && mpiexe" +"c -n "+nodeNum+" /root/v-minh/message && mpdallexit");
				//session.execCommand("ls -l");

				//System.out.println(streamToString(session.getStdout()));
				streamToString(session.getStdout());
				for(int i=0;i<lineCount;i++)
						System.out.println(rbuf[i]);
				session.close();
			}
			conn.close();
	}


	// read InputStream and return output as String
	private void streamToString(InputStream is) throws IOException {
		
		if (is != null) {
			            //StringBuilder sb = new StringBuilder();
			            
			
			                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			                while ( (rbuf[lineCount]=reader.readLine()) != null) {
			                	//rbuf[ lineCount ] = reader.readLine();
			                    lineCount++;
			                	//sb.append(line).append("\n");
			                }
			            //return rbuf;
		} else {       
			            //return null ;
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		req.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter writer = resp.getWriter();
		String w = req.getParameter("nodeNum");
		int n = Integer.parseInt(w);
		//String v = req.getParameter("password");

		writer.println("<html>");
		writer.println("<body>");
//From here is JDBC code - use it while work with database
		/*
		  try { Class.forName("org.gjt.mm.mysql.Driver"); 
		  String url ="jdbc:mysql://localhost/lab3?characterEncoding=utf8"; 
		  java.sql.Connection con = DriverManager.getConnection(url,"cs0000ie","w1158538");
		  
		  Statement stmt = con.createStatement(); //
		  String checkSql = "SELECT * FROM USERDB WHERE NAME = 'DTVD' AND PASSWORD = 'google' ORDER BY MEMBERID"; 
		  //String sql = "SELECT * FROM USERDB WHERE NAME = '"+w+"' AND PASSWORD = '"+v+"' ORDER BY MEMBERID"; 
		  ResultSet rs = stmt.executeQuery(checkSql);
		  
		  
		  
		  rs.last(); int numberOfRows = rs.getRow(); if (numberOfRows == 0) {
		  writer.println("<form name=\"form3\" >"); writer.println(
		  "<h3>Wrong password or user name! Click Back button to go back and try again!</h3> "
		  ); writer.println(
		  "<input type=\"button\" value =\"Back\"  onClick = \"history.go(-1)\" >"
		  ); writer.println("</form>"); } rs.beforeFirst();
		  
		  
		  
		  while (rs.next()){ 
			  String type = rs.getString("TYPE"); 
			  String memberid = rs.getString("MEMBERID"); 
			  if (type.equals("librarian")) {
				  writer.println("<h3>You logged as a librarian, You can go to the main page or re-loggin </h3>"); 
				  writer.println("<form name=\"form1\" action=\"http://localhost:8080/DatabaseConnect/servlets/TestJDBC\" method=\"POST\" >"); 
				  writer.println("<input type=\"submit\" value=\"Main page\" >");
				  writer.println("<input type=\"button\" value =\"Re-loggin\"  onClick = \"history.go(-1)\" >"); 
				  writer.println("</form>"); } 
			  else if (type.equals("student")) {
				  writer.println("<h3>You logged as a student, You can go to your page or re-loggin</h3>"); 
				  writer.println("<form name=\"form2\" action=\"http://localhost:8080/DatabaseConnect/servlets/User\" method=\"POST\" >");
				  writer.println("<input type=\"hidden\" name = \"random\" value = \""  +memberid+"\" >");
				  writer.println("<input type=\"submit\" value=\"Your page\" >");
				  writer.println("<input type=\"button\" value =\"Re-loggin\"  onClick = \"history.go(-1)\" >"); 
				  writer.println("</form>"); } 
		  }
		  
		  
		  
		 } catch (ClassNotFoundException e) { e.printStackTrace(); } catch
		  (SQLException e) { e.printStackTrace(); }
		 */
		// String Karo = SSHTest.DTVD;
		// System.out.println(Karo);
			doProc(n);
		writer.println("<p>**********************************************</p>");
		for(int i=0;i<lineCount;i++)
			writer.println("<p>" + rbuf[i] + "</p>");
		writer.println("<p>**********************************************</p>");
		writer.println("</body>");
		writer.println("</html>");
	}
}