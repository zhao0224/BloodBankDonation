package view;

import entity.BloodBank;
import entity.BloodDonation;
import entity.BloodGroup;
import entity.RhesusFactor;
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
 * @author danping tang
 */
@WebServlet(name = "CreateBloodDonation", urlPatterns = {"/CreateBloodDonation"})
public class CreateBloodDonation extends HttpServlet {

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
            out.println("<head>");
            out.println("<title>Create BloodDonation</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div style=\"text-align: center;\">");
            out.println("<div style=\"display: inline-block; text-align: left;\">");
            out.println("<form method=\"post\">");
            out.println("BloodBankID:<br>");
            //instead of typing the name of column manualy use the static vraiable in logic
            //use the same name as column id of the table. will use this name to get date
            //from parameter map.
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", BloodDonationLogic.BANK_ID);
            out.println("<br>");
            out.println("Milliliters:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", BloodDonationLogic.MILLILITERS);
            out.println("<br>");
            out.println("Blood Group:<br>");
            //add for loop to get and dispaly BLOOD_GROUP value 
            out.printf("<select name=\"%s\" >", BloodDonationLogic.BLOOD_GROUP);
            for (BloodGroup bdgroup : BloodGroup.values()) {
                out.printf("<option value=\"%s\">", bdgroup);
                out.print(bdgroup);
                out.println("</option>");
            }
            out.println("</select><br>");
            out.println("<br>");
            out.println("Rhesus Factor:<br>");
            out.printf("<select name=\"%s\" >", BloodDonationLogic.RHESUS_FACTOR);
            //add for loop to get and display RhesusFactor value 
            for (RhesusFactor rhfactor : RhesusFactor.values()) {
                out.printf("<option value=\"%s\">", rhfactor);
                out.print(rhfactor);
                out.println("</option>");
            }
            out.println("</select><br>");
            out.println("<br>");
            out.println("Created DateTime:<br>");
            out.printf("<input type=\"datetime-local\" step=\"1\" name=\"%s\" value=\"\"><br>", BloodDonationLogic.CREATED);
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
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log("GET");
        processRequest(request, response);
    }

    /**
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
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
            if (bankID.isEmpty()) {
                bdLogic.add(bloodDonation);
            } else {
                //foreign key ,set dependence
                BloodBankLogic bkLogic = LogicFactory.getFor("BloodBank");
                BloodBank bank = bkLogic.getWithId(Integer.parseInt(bankID));
                //Check user input bankID if is exsit in bloodbank
                if (bank != null) {
                    bloodDonation.setBloodBank(bank);
                    bdLogic.add(bloodDonation);

                } else {
                    //if duplicate print the error message
                    errorMessage = "Blood Bank: \"" + bankID + "\" does not exist";
                }
            }
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }

        if (request.getParameter("add") != null) {
            //if add button is pressed return the same page
            processRequest(request, response);
        } else if (request.getParameter("view") != null) {
            //if view button is pressed redirect to the appropriate table
            response.sendRedirect("BloodDonationTable");
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
