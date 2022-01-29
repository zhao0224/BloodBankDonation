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
 * @author Gabriel Matte
 */
@WebServlet( name = "CreateDonationRecord", urlPatterns = { "/CreateDonationRecord" } )
public class CreateDonationRecord extends HttpServlet {
    
    private String errorNotice = null;
    
    /**
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    protected void processRequest( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        response.setContentType( "text/html;charset=UTF-8" );
        
        try( PrintWriter out = response.getWriter() ) {
            
            // Start of page
            out.println( "<!DOCTYPE html>" );
            out.println( "<html>" );
            out.println( "<head>" );
            out.println( "<title>Create Account</title>" );
            out.println( "<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6\" crossorigin=\"anonymous\">");
            out.println( "</head>" );
            out.println( "<body>" );
        
            // Container div and page title
            out.println( "<div class=\"container my-4\" style=\"width: 450px;\">" );
            out.println( "<h2>Add a Donation Record</h2>" );
            
            // FORM html
            out.println( "<form method=\"post\">");
            // PersonID input
            out.println( "<label class=\"mt-2\" for=\"personId\">Person ID:</label>" );
            out.printf ( "<input class=\"form-control\" type=\"text\" id=\"personId\" name=\"%s\" value=\"\">", DonationRecordLogic.PERSON_ID);
            // DonationID input
            out.printf ( "<label class=\"mt-2\" for=\"donationId\">Donation ID:</label>" );
            out.printf ( "<input class=\"form-control\" type=\"text\" id=\"donationId\" name=\"%s\" value=\"\">", DonationRecordLogic.DONATION_ID);           
            // Administrator input
            out.println( "<label class=\"mt-2\" for=\"admin\">Administrator:</label>" );
            out.printf ( "<input class=\"form-control\" type=\"text\" id=\"admin\" name=\"%s\" value=\"\">", DonationRecordLogic.ADMINISTRATOR);           
            // Hospital input
            out.println( "<label class=\"mt-2\" for=\"hospital\">Hospital:</label>" );
            out.printf ( "<input class=\"form-control\" type=\"text\" id=\"hospital\" name=\"%s\" value=\"\">", DonationRecordLogic.HOSPITAL);
            // Tested radio input
            out.printf ( "<label class=\"mt-2\" for=\"\">Tested:</label>" );
            out.println( "<div class=\"form-check ml-2\">");
            out.printf ( "<input class=\"form-check-input\" type=\"radio\" id=\"testedTrue\" name=\"%s\" value=\"1\">", DonationRecordLogic.TESTED);
            out.println( "<label class=\"form-check-label\" for=\"testedTrue\">True</label>");
            out.println( "</div>" );
            out.println( "<div class=\"form-check ml-2\">");
            out.printf ( "<input class=\"form-check-input\" type=\"radio\" id=\"testedFalse\" name=\"%s\" value=\"0\">", DonationRecordLogic.TESTED);
            out.println( "<label class=\"form-check-label\" for=\"testedFalse\">False</label>");
            out.println( "</div>");
            //CreatedDate input
            out.println( "<label class=\"mt-2\" for=\"created\">Created Date Time:</label>" );
            out.printf ( "<input class=\"form-control\" type=\"datetime-local\" step=\"1\" id=\"created\" name=\"%s\" value=\"\">", DonationRecordLogic.CREATED);  
           
//Submit buttons
            out.println( "<div class=\"row\">");
            out.println( "<div class=\"col-1\"></div>");
            out.println( "<input class=\"btn btn-primary my-2 col-4\" type=\"submit\" name=\"add\" value=\"Add\">");
            out.println( "<div class=\"col-2\"></div>");
            out.println( "<input class=\"btn btn-primary my-2 col-4\" type=\"submit\" name=\"view\" value=\"Add & View\">");
            out.println( "<div class=\"col-1\"></div>");
            out.println( "</div>" );      
            
            // End of form
            out.println( "</form>" );
            
            if (errorNotice != null) {
                out.println( "<pre>");
                out.println( errorNotice);
                out.println( "</pre>");
            }
            
            out.println( "<pre>" );
            out.println( "Submitted keys and values:" );
            out.println( toStringMap( request.getParameterMap() ) );
            out.println( "</pre>" );
            
            out.println( "</div>" );
            
            // End of page
            out.println( "</body>" );
            out.println( "</html>" );
        }
    }
    
    /**
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log( "GET");
        processRequest( request, response);
    }
    
    /**
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            processRequest( request, response);            
        } else if ( request.getParameter( "view") != null) {
            response.sendRedirect( "DonationRecordTable");
        }
  
    }
    
    /**
     * 
     * @param values
     * @return 
     */
    private String toStringMap( Map<String, String[]> values ) {
        StringBuilder builder = new StringBuilder();
        values.forEach( ( k, v ) -> builder.append( "Key=" ).append( k )
                .append( ", " )
                .append( "Value/s=" ).append( Arrays.toString( v ) )
                .append( System.lineSeparator() ) );
        return builder.toString();
    }
    
}
