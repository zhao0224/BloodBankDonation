package view;

import entity.BloodDonation;
import entity.DonationRecord;
import entity.Person;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.BloodDonationLogic;
import logic.DonationRecordLogic;
import logic.LogicFactory;
import logic.PersonLogic;

/**
 *
 * @author Admin
 */
@WebServlet(name = "CreateDonationRecordJSP", urlPatterns = {"/CreateDonationRecordJSP"})
public class CreateDonationRecordViewJSP extends HttpServlet {
   private String errorNotice = null;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    private void getTableData( HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException{
      DonationRecordLogic drLogic = LogicFactory.getFor("DonationRecord");
      PersonLogic pLogic = LogicFactory.getFor("Person");
      BloodDonationLogic bdLogic = LogicFactory.getFor("BloodDonation");
      String path = req.getServletPath();
      
      req.setAttribute("request", toStringMap(req.getParameterMap()));
      req.setAttribute("personList", pLogic.getAll() );
      req.setAttribute("path", path);
      req.setAttribute("title", path.substring(1));
      req.getRequestDispatcher("/jsp/CreateTable-DonationRecord.jsp" ).forward(req, resp);
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
        log( "GET");
        getTableData(request, response);
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
        log( "POST");
        DonationRecordLogic logic = LogicFactory.getFor( "DonationRecord");
        
        // ADDS THE TIMESTAMP THAT THE FORM WAS SUBMITTED TO THE PARAMETER MAP
        // Format the date to be consistent in the DB
        Map map = new HashMap(request.getParameterMap());
        String date = logic.convertDateToString(new Date());
        map.put(DonationRecordLogic.CREATED, new String [] { date });
        
        String bloodDonationId = request.getParameter(DonationRecordLogic.DONATION_ID);
        String personId = request.getParameter(DonationRecordLogic.PERSON_ID);
        
        try {
            DonationRecord record = logic.createEntity( map );
            
            BloodDonationLogic bdLogic = LogicFactory.getFor("BloodDonation");
            BloodDonation bloodDonation = bdLogic.getWithId(Integer.parseInt(bloodDonationId));
            if (bloodDonation == null) {
                errorNotice = "Blood Donation ID entered does not exist.";
            }
            record.setBloodDonation(bloodDonation);
            
            PersonLogic pLogic = LogicFactory.getFor("Person");
            Person person = pLogic.getWithId(Integer.parseInt(personId));
            if (person == null) {
                errorNotice = "Person ID entered does not exist.";
            }
            record.setPerson(person);

            logic.add(record);
            if (errorNotice != null) {               
            } else {
                errorNotice = "No Exception Occurred";
            }

        } catch( Exception e) {
            errorNotice = e.getMessage();
            e.printStackTrace();
        }
        
        if( request.getParameter( "add") != null) {
            getTableData(request, response);            
        } else if ( request.getParameter( "view") != null) {
            response.sendRedirect( "DonationRecordTableJSP");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Create a DonationRecord Entity By JSP";
    }// </editor-fold>

    private String toStringMap( Map<String, String[]> values ) {
        StringBuilder builder = new StringBuilder();
        values.forEach( ( k, v ) -> builder.append( "Key=" ).append( k )
                .append( ", " )
                .append( "Value/s=" ).append( Arrays.toString( v ) )
                .append( System.lineSeparator() ) );
        return builder.toString();
    }
}
