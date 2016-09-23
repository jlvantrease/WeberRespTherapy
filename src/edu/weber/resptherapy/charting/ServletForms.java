package edu.weber.resptherapy.charting;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.html.simpleparser.StyleSheet;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.codec.Base64;

/**
 * Servlet implementation class ServletForms
 */
@WebServlet("/ServletForms")
public class ServletForms extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletForms() {
        super();
        // TODO Auto-generated constructor stub
    }

    //___________________________________________________________________________________________________________________
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	//___________________________________________________________________________________________________________________
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String type = request.getParameter("type");
		
		HttpSession session = request.getSession();
		
		if (type.equals("createForm")) {
			
			String theFormHtml = request.getParameter("formHTML");
			String userId = request.getParameter("userID");
			String theFormName = request.getParameter("formName");
			
			createForm(theFormHtml, userId, theFormName);
			
			session.setAttribute("userForms", getAllForms(userId));
		}
		else if (type.equals("getAllForms")) {
			
			String userId = request.getParameter("userID");
			
			session.setAttribute("userForms", getAllForms(userId)); 
		}
		else if (type.equals("updateForm")) {
			
			String theFormHtml = request.getParameter("formHTML");
			String userId = request.getParameter("userID");
			String theFormName = request.getParameter("formName");
			int theFormId = Integer.valueOf(request.getParameter("formID"));
			
			updateForm(theFormName, theFormHtml, theFormId, userId);
			
			session.setAttribute("userForms", getAllForms(userId));
		}
		else if (type.equals("generatePDF")) {
			
			String userID = request.getParameter("userID");
			int formID = Integer.valueOf(request.getParameter("formID"));
			
			OutputStream out = response.getOutputStream();
			
			PrintWriter pw = response.getWriter();
			
			response.setContentType("application/pdf");
			response.setHeader("content-disposition", "attachment; filename=\"myform.pdf\"");
			
			
			
			generatePDF(out, userID, formID);
		}
		else if (type.equals("getFormToEdit")) {
			
			String userID = request.getParameter("userID");
			int formID = Integer.valueOf(request.getParameter("formID"));	
			
			session.setAttribute("formToFillOut", getFormToEdit(userID, formID));
		}
	}
	
	//___________________________________________________________________________________________________________________
	
	//boolean createForm()
	public boolean createForm(String theFormHtml, String userId, String theFormName){
		
		Date todaysDate = new Date();
		
		DatabaseConnector connector = new DatabaseConnector();
		
		try {
			
			Connection conn = connector.connectDatabase();
			
			DatabaseCalls calls = new DatabaseCalls();
			
			return calls.createForm(conn, theFormHtml, userId, todaysDate, theFormName);
		
		}catch(Exception e){
			e.printStackTrace();
			
			return false; // if we get here, creating a new form didn't work
		}
		
		
	}
	
	//Map<String, Form> getAllForms()
	public Map<Integer, Form> getAllForms(String theUserId){
		
		DatabaseConnector connector = new DatabaseConnector();
		
		try {
			
			Connection conn = connector.connectDatabase();
			
			DatabaseCalls calls = new DatabaseCalls();
			
			return calls.getAllForms(conn, theUserId);
		
		}catch(Exception e){
			e.printStackTrace();
			
			return null; // if we get here, getting all forms didn't work
		}
	}
	
	//boolean updateForm()
	public boolean updateForm(String theFormName, String theFormHtml, int theFormId, String theUserId){
		
		DatabaseConnector connector = new DatabaseConnector();
		
		try {
			
			Connection conn = connector.connectDatabase();
			
			DatabaseCalls calls = new DatabaseCalls();
			
			return calls.updateForm(conn, theFormName, theFormHtml, theFormId, theUserId);
		
		}catch(Exception e){
			e.printStackTrace();
			
			return false; // if we get here, getting all forms didn't work
		}
	}
	
	//boolean updateForm()
		public Form getFormToEdit(String userId, int theFormId){
			
			DatabaseConnector connector = new DatabaseConnector();
			
			try {
				
				Connection conn = connector.connectDatabase();
				
				DatabaseCalls calls = new DatabaseCalls();
				
				return calls.getFormToEdit(conn, userId, theFormId);
			
			}catch(Exception e){
				e.printStackTrace();
				
				return null; // if we get here, getting all forms didn't work
			}
		}

	public void generatePDF(OutputStream out, String userID, int formID){
		
		try {
			
			DatabaseConnector connector = new DatabaseConnector();
			
			Connection conn = connector.connectDatabase();
			
			DatabaseCalls calls = new DatabaseCalls();
			
//			userID = "w11112222";
//			formID = 1;
			
			String html = calls.generatePDF(conn, userID, formID);
			
//			FileInputStream in = new FileInputStream(html);
//			byte[] buffer = new byte[4096];
//			int length;
//			
//			while( (length = in.read(buffer)) > 0 ){
//				
//				out.write(buffer, 0, length);
//			}
						
			
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			Document doc = new Document(PageSize.A4);
			PdfWriter.getInstance(doc, byteArrayOutputStream);
			doc.open();
			HTMLWorker worker = new HTMLWorker(doc);
			worker.parse(new StringReader(html));
			doc.close();

			byte[] pdfBytes = byteArrayOutputStream.toByteArray();
			
			
			
			int length;
			
			while( (length = pdfBytes.length) > 0 ){
				
				out.write(pdfBytes);
			}
			
//			in.close();
//			out.flush();
			
			
			
//			return doc;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			System.out.println("PDF generation failed!");
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("PDF generation failed!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("PDF generation failed!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("PDF generation failed!");
		}
		
	}
	
	
//	public void thing(){
//		
//		try{
//			
//			DatabaseConnector connector = new DatabaseConnector();
//			
//			Connection conn = connector.connectDatabase();
//			
//			DatabaseCalls calls = new DatabaseCalls();
//			
//	//		userID = "w11112222";
//	//		formID = 1;
//			
//			String html = calls.generatePDF(conn, userID, formID);
//			
//			Document pdfDoc = new Document();
//			// Buffered reader to read the test.html file
//			Reader htmlReader = new BufferedReader(new InputStreamReader(new FileInputStream(html))); //change the name accordingly
//			ByteArrayOutputStream byteAOS = new ByteArrayOutputStream();
//			// get Instance
//			PdfWriter.getInstance(pdfDoc, byteAOS);
//			// Open document
//			pdfDoc.open();
//			//Instance for stylesheet
//			StyleSheet stylez = new StyleSheet();
//			//load a basic font. can also do other functions
//			stylez.loadTagStyle(�body�, �font�, �Times New Roman�);
//			//HTML worker in simpleParser
//			ArrayList arrayElementList = HTMLWorker.parseToList(htmlReader, stylez);
//			//Add to the document
//			for (int i = 0; i < arrayElementList.size(); ++i) {
//			Element e = (Element) arrayElementList.get(i);
//				pdfDoc.add(e);
//			}
//			//close the document after adding
//			pdfDoc.close();
//			byte[] byt = byteAOS.toByteArray();
//			//encode the bytes
//			String pdfBase64 = Base64.encodeBytes(byt);
//			File pdfFile = new File(�Worked.pdf�); //target name. change accordingly
//			FileOutputStream out = new FileOutputStream(pdfFile);
//			out.write(byt);
//			out.close();
//		
//		}catch(Exception e){
//			
//			e.printStackTrace();
//		}
//	}
	
}
