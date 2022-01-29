package view;

import entity.Person;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.LogicFactory;
import logic.PersonLogic;

/**
 *
 * @author Xiaodan
 */
@WebServlet(name = "CreatePersonJSP", urlPatterns = {"/CreatePersonJSP"})
public class CreatePersonViewJSP extends HttpServlet {
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
    protected void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();
        req.setAttribute( "request", toStringMap( req.getParameterMap() ) );
        req.setAttribute( "path", path );
        req.setAttribute( "title", path.substring( 1 ) );
        req.getRequestDispatcher( "/jsp/CreateTable-Person.jsp" ).forward( req, resp );
       
    }

    private Object toStringMap(Map<String, String[]> parameterMap) {
       StringBuilder builder = new StringBuilder();
       parameterMap.keySet().forEach((k)->{
           builder.append("Key=").append(k)
                  .append( ", ")
                  .append( "Value/s=").append(Arrays.toString(parameterMap.get(k)))
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log( "POST" );
        PersonLogic aLogic = LogicFactory.getFor( "Person" );
        try {
            Person person = aLogic.createEntity( req.getParameterMap() );
            aLogic.add( person );
        } catch( Exception ex ) {
            errorMessage = ex.getMessage();
        }

        if( req.getParameter( "add" ) != null ){
            //if add button is pressed return the same page
            processRequest( req, resp );
        } else if( req.getParameter( "view" ) != null ){
            //if view button is pressed redirect to the appropriate table
            resp.sendRedirect( "PersonTableJSP" );
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Create a Person Entity JSP";
    }

}
