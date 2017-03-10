package edu.weber.resptherapy.charting;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.weber.resptherapy.charting.model.Userform;

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
		System.out.print("Debug: doPost");
		String type = request.getParameter("type");
		
		HttpSession session = request.getSession();
		
		if (type.equals("createForm")) {
			
			String theFormHtml = request.getParameter("formHTML");
			String userId = request.getParameter("userID");
			String theFormName = request.getParameter("formName");
			
			createForm(theFormHtml, userId, theFormName);
			session.setAttribute("userForms", getAllForms(userId));

		}	else if (type.equals("getAllForms")) {
			
			String userId = request.getParameter("userID");
			session.setAttribute("userForms", getAllForms(userId)); 
		}
		else if (type.equals("updateForm")) {
			String theFormHtml = request.getParameter("theFormHTML");
			String userId = request.getParameter("userId");
			String theFormName = request.getParameter("theFormName");
			int theFormId = Integer.valueOf(request.getParameter("theFormId"));
			
			updateForm(theFormName, theFormHtml, theFormId, userId);
			session.setAttribute("userForms", getAllForms(userId));

		} else if (type.equals("getFormToEdit")) {
			
			String userID = request.getParameter("userID");
			int formID = Integer.valueOf(request.getParameter("formID"));	
			
			session.setAttribute("formToFillOut", getFormToEdit(userID, formID));
		}
	}
	
	//___________________________________________________________________________________________________________________
	
	//boolean createForm()
	private boolean createForm(String theFormHtml, String userId, String theFormName){
		
		Date todaysDate = new Date();
		
//		DatabaseConnector connector = new DatabaseConnector();
//		
//		try {
//			
//			Connection conn = connector.connectDatabase();
			
			DatabaseCalls calls = new DatabaseCalls();
			
			return calls.createForm(theFormHtml, userId, todaysDate, theFormName);
		
//		}catch(Exception e){
//			e.printStackTrace();
//			
//			return false; // if we get here, creating a new Formtemplate didn't work
//		}
		
		
	}
	
	//Map<String, Formtemplate> getAllForms()
	public Map<Integer, Userform> getAllForms(String theUserId){
		
		
		try {
			DatabaseCalls calls = new DatabaseCalls();
			
			return calls.getAllForms(theUserId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null; // if we get here, getting all forms didn't work
		}

	}
	
	//boolean updateForm()
	private boolean updateForm(String theFormName, String theFormHtml, int theFormId, String theUserId){
		
//		DatabaseConnector connector = new DatabaseConnector();
//		
		try {
//			
//			Connection conn = connector.connectDatabase();
			
			DatabaseCalls calls = new DatabaseCalls();
			
			return calls.updateForm( theFormName, theFormHtml, theFormId, theUserId);
		
		}catch(Exception e){
			e.printStackTrace();
			
			return false; // if we get here, getting all forms didn't work
		}
	}
	
	//boolean updateForm()
		private Userform getFormToEdit(String userId, int theFormId){
			
//			DatabaseConnector connector = new DatabaseConnector();
//			
//			try {
//				
//				Connection conn = connector.connectDatabase();
				
				DatabaseCalls calls = new DatabaseCalls();
				
				return calls.getFormToEdit( userId, theFormId);
			
//			}catch(Exception e){
//				e.printStackTrace();
//				
//				return null; // if we get here, getting all forms didn't work
//			}
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
