package view;

import entity.Account;
import entity.BloodBank;
import entity.Person;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import static javax.persistence.GenerationType.values;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.BloodBankLogic;
import logic.LogicFactory;
import logic.PersonLogic;

/**
 *
 * @author Jing Zhao
 */
@WebServlet(name = "CreateBloodBank", urlPatterns = {"/CreateBloodBank"})
public class CreateBloodBank extends HttpServlet {

    private String errorMessage = null;

    /**
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<title>Create Blood Bank</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div style=\"text-align: center;\">");
            out.println("<div style=\"display: inline-block; text-align: left;\">");
            out.println("<form method=\"post\">");
            out.println("Owner:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", BloodBankLogic.OWNER_ID);
            out.println("<br>");
            out.println("Name:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", BloodBankLogic.NAME);
            out.println("<br>");
            out.println("Privately_owned:<br>");
            out.printf("<select name=\"%s\" >", BloodBankLogic.PRIVATELY_OWNED);
            out.println("<option value=\"1\">True</option>");
            out.println("<option value=\"0\">False</option>");
            out.println("</select><br>");
            out.println("<br>");
            out.println("Established:<br>");
            out.printf("<input type=\"datetime-local\" step=\"1\" name=\"%s\" value=\"\"><br>", BloodBankLogic.ESTABLISHED);
            out.println("<br>");
            out.println("EmployeeCount:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", BloodBankLogic.EMPLOYEE_COUNT);
            out.println("<br>");
            out.println("<input type=\"submit\" name=\"view\" value=\"Add and View\">");
            out.println("<input type=\"submit\" name=\"add\" value=\"Add\">");
            out.println("</form>");

            if (errorMessage != null && !errorMessage.isEmpty()) {
                out.println("<p color=red>");
                out.println("<font color=red size=4px>");
                out.println(errorMessage);
                out.println("</font>");
                out.println("</p>");

            }
            out.println("<pre>");
            out.println("Submitted keys and values:");
            out.println(toStringMap(request.getParameterMap()));
            out.println("</pre>");
            out.println("</div>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    /**
     * 
     * @param values
     * @return 
     */
    private String toStringMap(Map<String, String[]> values) {
        StringBuilder builder = new StringBuilder();
        values.forEach((k, v) -> builder.append("Key=").append(k)
                .append(", ")
                .append("Value/s=").append(Arrays.toString(v))
                .append(System.lineSeparator()));
        return builder.toString();
    }

    /**
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        log("GET");
        processRequest(req, resp);
    }

    /**
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log("POST");
        errorMessage=null;
                
        BloodBankLogic logic = LogicFactory.getFor("BloodBank");
        PersonLogic personLogic = LogicFactory.getFor("Person");
        String owner = req.getParameter(BloodBankLogic.OWNER_ID);
      
        try{
            BloodBank bloodBank = logic.createEntity(req.getParameterMap());
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

        if (req.getParameter("add") != null) {
            processRequest(req, resp);
        } else if (req.getParameter("view") != null) {
            resp.sendRedirect("BloodBankTable");
        }
    }

    /**
     * 
     * @return 
     */
    @Override
    public String getServletInfo() {
        return "Create a BloodBank Entity";
    }

    private static final boolean DEBUG = true;

    /**
     * 
     * @param msg 
     */
    public void log(String msg) {
        if (DEBUG) {
            String message = String.format("[%s] %s", getClass().getSimpleName(), msg);
            getServletContext().log(message);
        }
    }

    /**
     * 
     * @param msg
     * @param t 
     */
    public void log(String msg, Throwable t) {
        String message = String.format("[%s] %s", getClass().getSimpleName(), msg);
        getServletContext().log(message, t);
    }
}
