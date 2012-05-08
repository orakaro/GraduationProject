/*File uploaded page
	* Show the file detail
	* Provide a simple code editor with TAB key enable
	* Get the uploaded content to the editor
	* Enable Save function and save the default content by two button Save and Default
	* Can get the number of node
	* Run button will jump to FileExec file and show up the result of execution
This file also show how to include CSS and JavaScript, check the code to see!
*/

package databaseconnect;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
 
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
 
 
public class FileUploaded extends HttpServlet {
	/**
	 * 
	 */
	public static String UFileName;//uploaded file name
	private static final long serialVersionUID = 1L;
	private static final String TMP_DIR_PATH = "c:\\tmp";
	private File tmpDir;
	public static final String DESTINATION_DIR_PATH ="/files";
	public static String FCon = null;//use to read uploaded file content
	private File destinationDir;
 
//File upload handler
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		tmpDir = new File(TMP_DIR_PATH);
		if(!tmpDir.isDirectory()) {
			throw new ServletException(TMP_DIR_PATH + " is not a directory");
		}
		String realPath = getServletContext().getRealPath(DESTINATION_DIR_PATH);
		destinationDir = new File(realPath);
		if(!destinationDir.isDirectory()) {
			throw new ServletException(DESTINATION_DIR_PATH+" is not a directory");
		}
 
	}
 
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

//Java Script import
		out.println("<script type=\"text/javascript\" src=\""+request.getContextPath()+"/javascript/jquery-1.4.2.js\"></script>");
		out.println("<script type=\"text/javascript\" src=\""+request.getContextPath()+"/javascript/EnableTab.js\"></script>");
		out.println("<script type=\"text/javascript\" src=\""+request.getContextPath()+"/javascript/ButtonPushed.js\"></script>");

//Demo about how to embedded external file content to result HTML
/*		
		//Load "EnableTab JavaScript" to HTML
		//If want to use JavaScript DO it in the file EnableTab.txt!!!! 
				String filename = "/javascript/EnableTab.txt";
				ServletContext context = getServletContext();
				InputStream inp = context.getResourceAsStream(filename);
			    if (inp != null) {
			        InputStreamReader isr = new InputStreamReader(inp);
			        BufferedReader reader = new BufferedReader(isr);
			        String text = "";
			        while ((text = reader.readLine()) != null) {
			        out.println(text);
			        }
			    }
*/   

		out.println("<html>");
		out.println("<head>");
//CSS import
		String CSSpathVar = request.getContextPath() + "/css/DStyle.css";  
		out.println("<link rel='stylesheet' type='text/css' href='" +CSSpathVar+ "' >");
		out.println("</head>");
		out.println("<body>");
//File handler
		out.println("<h2>File detail</h2>");
	    out.println();
 		DiskFileItemFactory  fileItemFactory = new DiskFileItemFactory ();
		/*
		 *Set the size threshold, above which content will be stored on disk.
		 */
		fileItemFactory.setSizeThreshold(1*1024*1024); //1 MB
		/*
		 * Set the temporary directory to store the uploaded files of size above threshold.
		 */
		fileItemFactory.setRepository(tmpDir);
 
		ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);
		try {
			/*
			 * Parse the request
			 */
			List items = uploadHandler.parseRequest(request);
			Iterator itr = items.iterator();
			while(itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				UFileName=item.getName();
				/*
				 * Handle Form Fields.
				 */
				if(item.isFormField()) {
					out.println("ファイル名 = "+item.getFieldName()+", 値 = "+item.getString());
					
				} else {
					//Handle Uploaded files.
					out.println("ファイル名 = "+item.getName()+", サイズ = "+item.getSize());
					/*
					 * Write file to the ultimate location.
					 */
					File file = new File(destinationDir,item.getName());
					item.write(file);
					FCon=item.getString();//get the content
				}
				
//Main form		
				out.println("<form name=\"form1\" action=\"http://localhost:8080/DatabaseConnect/servlets/FileExec\" method=\"POST\">");
				out.println("<br><br><br>");

//TextArea: Enable Code Editor for user to edit their code
				out.println("<textarea name=\"data\" id=\"styled\" rows=\"10\" columns=\"50\" wrap=\"off\" onkeydown=\"return catchTab(this,event)\" >" 
						+ FCon +
						"</textarea>");
//The trick TextArea: hidden and consist real content which is passed to FileExec, 
//This TextArea get content from tmp variable in javaScript
				out.println("<textarea style=\"display:none;\" name=\"dataTrick\" id=\"styledTrick\" rows=\"10\" columns=\"50\" wrap=\"off\" onkeydown=\"return catchTab(this,event)\" >" 
						+ FCon +
						"</textarea>");
				
//"SaveCheck": check if file content changed or not
				out.println("<input type=checkbox name=\"SaveCheck\" style=\"display:none;\"  >");
//Save button
				out.println("<br><input type=\"button\" class=\"class1\" onclick=\"show_confirm()\" value=\"保存\" >");
//Turn TextArea content back to default code				
				out.println("<input type=\"button\" class=\"class1\" onclick=\"turn_back()\" value=\"既定コード\" >");
//Jump to run file				
				out.println("<br><br><br>実行したいノード数：");
				out.println("<input type=\"text\" name=\"nodeNum\"><br>");
				out.println("実行なう!　　");
				out.println("<input type=\"submit\" onclick=\"getRightContent()\" value=\"Yes We GO!\">");
				out.println("</form>");
				
				out.println("</body>");
				out.println("</html>");
				
						
				out.close();
			}
		}catch(FileUploadException ex) {
			log("Error encountered while parsing the request",ex);
		} catch(Exception ex) {
			log("Error encountered while uploading file",ex);
		}
 	}
 
}