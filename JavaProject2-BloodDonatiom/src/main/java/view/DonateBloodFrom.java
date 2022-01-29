package view;

import entity.BloodBank;
import entity.BloodDonation;
import entity.BloodGroup;
import entity.DonationRecord;
import entity.Person;
import entity.RhesusFactor;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.BloodBankLogic;
import logic.BloodDonationLogic;
import logic.DonationRecordLogic;
import logic.LogicFactory;
import logic.PersonLogic;

/**
 *
 * @author Gabriel Matte，Danping Tang
 */
@WebServlet(name = "DonateBloodFrom", urlPatterns = {"/DonateBloodFrom"})
public class DonateBloodFrom extends HttpServlet {
        private String errorMessage = null;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            //HTML Doctype declaration and Head
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Donate Blood Form</title>");
            out.println("<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6\" crossorigin=\"anonymous\">");
            out.println("</head>");

            //HTML Body
            out.println("<body>");

            out.println("<div class=\"container my-4\" style=\"background-color: #e6f2ff; border-radius: 10px; box-shadow: 3px 3px 8px #cccccc;\">");
            out.println("<h1 class=\"text-center pt-4\" style=\"text-decoration: underline;\">Donate Blood Information Form</h1>");
            out.println("<form method=\"post\">");

            //Section - PERSON
            out.println("<div class=\"row pt-3 pb-4 px-4\">");
            out.println("<h3>Personal Information</h3>");
            out.println("<div class=\"col-6\">");
            out.println("<label for=\"lastname\">Last Name</label>");
            out.printf("<input class=\"form-control\" type=\"text\" id=\"lastname\" name=\"%s\" value=\"\">", PersonLogic.LAST_NAME);
            out.println("<label for=\"address\">Address</label>");
            out.printf("<input class=\"form-control\" type=\"text\" id=\"address\" name=\"%s\" value=\"\">", PersonLogic.ADDRESS);
            out.println("<label for\"dob\">Date of Birth</label>");
            out.printf("<input class=\"form-control\" type=\"datetime-local\" step=\"1\" id=\"dob\" name=\"%s\" value=\"\">", PersonLogic.BIRTH);
            out.println("</div>");

            out.println("<div class=\"col-6\">");
            out.println("<label for=\"firstname\">First Name</label>");
            out.printf("<input class=\"form-control\" type=\"text\" id=\"firstname\" name=\"%s\" value=\"\">", PersonLogic.FIRST_NAME);
            out.println("<label for\"phone\">Phone</label>");
            out.printf("<input class=\"form-control\" type=\"text\" id=\"phone\" name=\"%s\" value=\"\">", PersonLogic.PHONE);
            out.println("</div>");

            //End of Row - PERSON
            out.println("</div>");

            //Section - BLOOD DONATION
            BloodBankLogic bbLogic = LogicFactory.getFor("BloodBank");
            out.println("<div class=\"row pt-3 pb-4 px-4\">");
            out.println("<h3>Blood Donation Information</h3>");
            out.println("<div class=\"col-6\">");
            out.println("<label for=\"bloodbank\">Blood Bank</label>");
            out.printf("<select class=\"form-select\" id=\"bloodbank\" name=\"%s\">", BloodDonationLogic.BANK_ID);
            bbLogic.getAll().forEach(e -> out.printf("<option value=\"%s\">%s</option>",
                    bbLogic.extractDataAsList(e).toArray()[0], bbLogic.extractDataAsList(e).toArray()[2]));
            out.println("</select>");
            out.println("<label for=\"bloodgroup\">Blood Group</label>");
            out.printf("<select class=\"form-select\" id=\"bloodgroup\" name=\"%s\">", BloodDonationLogic.BLOOD_GROUP);
            //add for loop to get and dispaly BLOOD_GROUP value 
            for (BloodGroup bdgroup : BloodGroup.values()) {
                out.printf("<option value=\"%s\">", bdgroup);
                out.print(bdgroup);
                out.println("</option>");
            }
            out.println("</select>");
            out.println("</div>");
            out.println("<div class=\"col-6\">");
            out.println("<label for\"\">Mililitres Collected</label>");
            out.printf("<input class=\"form-control\" type=\"text\" id=\"ml\" name=\"%s\">", BloodDonationLogic.MILLILITERS);
            out.println("<label for=\"\">Rhesus Factor</label>");
            out.printf("<select class=\"form-select\" id=\"rhesusfactor\" name=\"%s\">", BloodDonationLogic.RHESUS_FACTOR);
            //add for loop to get and display RhesusFactor value 
             for (RhesusFactor rhfactor : RhesusFactor.values()) {
                out.printf("<option value=\"%s\">", rhfactor);
                out.print(rhfactor);
                out.println("</option>");
            }
            out.println("</select>");
            out.println("</div>");

            //End of Row - BLOOD DONATION
            out.println("</div>");

            //Section - DONATION RECORD
            out.println("<div class=\"row pt-3 pb-4 px-4 mb-2\">");
            out.println("<h3>Donation Record Information</h3>");
            out.println("<div class=\"col-6\">");
            out.println("<label for\"admin\">Administrator</label>");
            out.printf("<input class=\"form-control\" type=\"text\" id=\"admin\" name=\"%s\" value=\"\">", DonationRecordLogic.ADMINISTRATOR);
            out.println("<label for\"hospital\">Hospital</label>");
            out.printf("<input class=\"form-control\" type=\"text\" id=\"hospital\" name=\"%s\" value=\"\">", DonationRecordLogic.HOSPITAL);
            out.println("</div>");

            out.println("<div class=\"col-6\">");
            out.println("<label for\"tested\">Tested</label>");
            out.printf("<select class=\"form-select\" id=\"tested\" name=\"%s\">", DonationRecordLogic.TESTED);
            out.println("<option value=\"1\">True</option>");
            out.println("<option value=\"0\">False</option>");
            out.println("</select>");
            out.println("<label for\"date\">Date</label>");
            out.printf("<input class=\"form-control\" type=\"datetime-local\" step=\"1\" id=\"date\" name=\"%s\" value=\"\">", BloodDonationLogic.CREATED);
            out.println("</div>");

            //End of Row - DONATION RECORD
            out.println("</div>");

            //Section SUBMIT
            out.println("<div class=\"text-center mb-2\">");
            out.println("<input class=\"btn btn-primary mb-4\" type=\"submit\" name=\"add\" value=\"Add\" style=\"width: 150px;\">");
            out.println("</div>");

            //End of Container - Form
            out.println("</form>");
            out.println("</div>");

            //End of HTML
            out.println("</body>");
            out.println("</html>");
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log("POST");
  
        PersonLogic aLogic = LogicFactory.getFor("Person");
        BloodDonationLogic bdLogic = LogicFactory.getFor("BloodDonation");
        BloodBankLogic bkLogic = LogicFactory.getFor("BloodBank");
        DonationRecordLogic logic = LogicFactory.getFor("DonationRecord");
      

        try {
            //Here we will add the persFon to DB first, then get the ID and store it.
            Person person = aLogic.createEntity(request.getParameterMap());
            aLogic.add(person);
            
            String bankID = request.getParameter(BloodDonationLogic.BANK_ID);
            BloodDonation bDonation = bdLogic.createEntity(request.getParameterMap());
            //add depedencies
           
            BloodBank bank = bkLogic.getWithId(Integer.parseInt(bankID));
            bDonation.setBloodBank(bank);
            bdLogic.add(bDonation);

            //Finally, we will add the DonationRecord using the personID and the BloodDonationID
            DonationRecord dRecord = logic.createEntity(request.getParameterMap());
            //add person and donation depedencies 
            dRecord.setPerson(person);
            dRecord.setBloodDonation(bDonation);
            logic.add(dRecord);

        } catch (Exception ex) {
            log("",ex);
            errorMessage = ex.getMessage();
        }

        if (request.getParameter("add") != null) {
            //if add button is pressed return the same page
            processRequest(request, response);
        }

    }
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
     private static final boolean DEBUG = true;

    public void log( String msg ) {
        if( DEBUG ){
            String message = String.format( "[%s] %s", getClass().getSimpleName(), msg );
            getServletContext().log( message );
        }
    }

    public void log( String msg, Throwable t ) {
        String message = String.format( "[%s] %s", getClass().getSimpleName(), msg );
        getServletContext().log( message, t );
    }

}
