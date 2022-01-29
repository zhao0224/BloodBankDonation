package view;

import entity.BloodBank;
import entity.BloodDonation;
import entity.Person;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.BloodBankLogic;
import logic.BloodDonationLogic;
import logic.LogicFactory;
import logic.PersonLogic;

/**
 *
 * @author Jing Zhao
 */

@WebServlet(name = "CreateBloodBankJSP", urlPatterns = {"/CreateBloodBankJSP"})
public class CreateBloodBankViewJSP extends HttpServlet {
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
     
    private void getTableData( HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
      String path = request.getServletPath();
      BloodBankLogic bbLogic = LogicFactory.getFor("BloodBank");
      PersonLogic psLogic = LogicFactory.getFor("Person");
              
    //  request.setAttribute("bloodbankColName", bbLogic.getColumnNames().subList(1,bbLogic.getColumnNames().size()-1));
      request.setAttribute("request", toStringMap(request.getParameterMap()));
      request.setAttribute("personList", psLogic.getAll() );
      request.setAttribute("path", path);
      request.setAttribute("title", path.substring(1));
      request.getRequestDispatcher("/jsp/CreateTable-BloodBank.jsp" ).forward(request, response);
    }
    
    private String toStringMap(Map<String, String[]> m) {
       StringBuilder builder = new StringBuilder();
       m.keySet().forEach((k)->{
           builder.append("Key=").append(k)
                  .append( ", ")
                  .append( "Value/s=").append(Arrays.toString(m.get(k)))
                  .append(System.lineSeparator());
           
       });
       return builder.toString();
       
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
        errorMessage=null;
                
        BloodBankLogic logic = LogicFactory.getFor("BloodBank");
        PersonLogic personLogic = LogicFactory.getFor("Person");
        String owner = request.getParameter(BloodBankLogic.OWNER_ID);
      
        try{
            BloodBank bloodBank = logic.createEntity(request.getParameterMap());
            if(owner.isEmpty()){
                logic.add(bloodBank);
            }else{
                //foreign key, set dependence;
                BloodBank bankExist = logic.getBloodBanksWithOwner(Integer.parseInt(owner));
                if(bankExist == null) {
                       Person person= personLogic.getWithId(Integer.parseInt(owner));
                        if(person != null){

                            bloodBank.setOwner(person);

                            logic.add(bloodBank);  
                       }else{
                          errorMessage = "Person: \"" + person + "\" does not exist";
                       }
                } else {
                    errorMessage = "Person: \"" + owner + "\" already exists in the bank";
                }

            }       
        }catch(Exception e){
            errorMessage = e.getMessage();
        };

        if (request.getParameter("add") != null) {
            getTableData(request, response);
        } else if (request.getParameter("view") != null) {
            response.sendRedirect("BloodBankTableJSP");
        }
    }

     /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Create a BloodBank Entity By JSP";
    }


    private static final boolean DEBUG = true;

    public void log(String msg) {
        if (DEBUG) {
            String message = String.format("[%s] %s", getClass().getSimpleName(), msg);
            getServletContext().log(message);
        }
    }

    public void log(String msg, Throwable t) {
        String message = String.format("[%s] %s", getClass().getSimpleName(), msg);
        getServletContext().log(message, t);
    }
    
}
