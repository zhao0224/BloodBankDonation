package view;

import entity.BloodBank;
import entity.BloodDonation;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.BloodBankLogic;
import logic.BloodDonationLogic;
import logic.LogicFactory;

/**
 *
 * @author Admin
 */
@WebServlet(name = "CreateBloodDonationJSP", urlPatterns = {"/CreateBloodDonationJSP"})
public class CreateBloodDonationViewJSP extends HttpServlet {
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
     private void getTableData( HttpServletRequest req, HttpServletResponse resp )
            throws ServletException, IOException {
        String path = req.getServletPath();
        BloodDonationLogic bdLogic = LogicFactory.getFor("BloodDonation");
        BloodBankLogic bbankLogic = LogicFactory.getFor("BloodBank");
        req.setAttribute( "bankList", bbankLogic.getAll() );
        req.setAttribute( "request", toStringMap( req.getParameterMap() ) );
        req.setAttribute( "path", path );
        req.setAttribute( "title", path.substring( 1 ) );
        req.getRequestDispatcher( "/jsp/CreateTable-BloodDonation.jsp" ).forward( req, resp );
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
        log("GET");
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
        log("POST");
        BloodDonationLogic bdLogic = LogicFactory.getFor("BloodDonation");
        String bankID = request.getParameter(BloodDonationLogic.BANK_ID);
        
        try {
            BloodDonation bloodDonation = bdLogic.createEntity(request.getParameterMap());
            // first check user input is empty
            if ( bankID.isEmpty()) {
                   bdLogic.add(bloodDonation);
            } else {
                //foreign key ,set dependence
                BloodBankLogic bkLogic = LogicFactory.getFor("BloodBank");
                BloodBank bank = bkLogic.getWithId(Integer.parseInt(bankID));
                //Check user input bankID if is exsit in bloodbank
                if (bank !=null ) {
                        bloodDonation.setBloodBank(bank);
                        bdLogic.add(bloodDonation);

                }else{
                //if duplicate print the error message
                errorMessage = "Blood Bank: \"" + bankID + "\" does not exist";
                }
            }
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }

        if (request.getParameter("add") != null) {
            //if add button is pressed return the same page
            getTableData(request, response);
        } else if (request.getParameter("view") != null) {
            //if view button is pressed redirect to the appropriate table
            response.sendRedirect("BloodDonationTableJSP");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Create a BloodDonation Entity";
    }

     private String toStringMap(Map<String, String[]> m) {
        StringBuilder builder = new StringBuilder();
        for (String k : m.keySet()) {
            builder.append("Key=").append(k)
                    .append(", ")
                    .append("Value/s=").append(Arrays.toString(m.get(k)))
                    .append(System.lineSeparator());
        }
        return builder.toString();

    }
     
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
