package edu.weber.resptherapy.charting;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONObject;

import edu.weber.resptherapy.charting.model.Patient;

/**
 * Created by Vantrease on 3/13/2017.
 */
@WebServlet("/ServletPatient")
public class ServletPatient extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public ServletPatient() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     String type = request.getParameter("type");
        System.out.println("did the post work?");
        String firstName = request.getParameter("firstName");
        String middleName = request.getParameter("middleName");
        String lastName = request.getParameter("lastName");
        String dob = request.getParameter("dob");
        String gender = request.getParameter("gender");
        String phone = request.getParameter("phone");
        System.out.println(type + firstName + middleName + lastName + dob + gender + phone);

        DatabaseCalls dc = new DatabaseCalls();
        dc.createPatient(firstName, middleName, lastName, dob, gender, phone);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
