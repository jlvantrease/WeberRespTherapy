package edu.weber.resptherapy.charting;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;

@WebServlet("/ServletFormDownload")
public class ServletFormDownload extends HttpServlet {

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String errMsg = "POST Method not allowed for 'ServletFormDownload' endpoint.";
    response.sendError(405, errMsg);
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String userID = request.getParameter("userID");
    int formID = Integer.valueOf(request.getParameter("formID"));

    OutputStream out = response.getOutputStream();

    response.setContentType("application/pdf");
    response.setHeader("content-disposition", "attachment; filename=\"myform_" + formID + ".pdf\"");
    generatePDF(out, userID, formID);
  }

  private void generatePDF(OutputStream out, String userID, int formID) {

    try {

      DatabaseConnector connector = new DatabaseConnector();  // This will setup hibernate, but not really needed

      DatabaseCalls calls = new DatabaseCalls();
      String html = calls.generatePDF(userID, formID);

      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      Document doc = new Document(PageSize.A4);
      PdfWriter.getInstance(doc, byteArrayOutputStream);
      doc.open();
      HTMLWorker worker = new HTMLWorker(doc);
      worker.parse(new StringReader(html));
      doc.close();

      byte[] pdfBytes = byteArrayOutputStream.toByteArray();
      out.write(pdfBytes);
      out.flush();

    } catch (DocumentException | IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      System.out.println("PDF generation failed!");
    }
  }
}
