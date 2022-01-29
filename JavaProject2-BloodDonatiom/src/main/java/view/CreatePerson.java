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
 * @author Xiaodan Chen
 */
@WebServlet(name = "CreatePerson", urlPatterns = {"/CreatePerson"})
public class CreatePerson extends HttpServlet {

    private String errorMessage = null;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Create Person</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div style=\"text-align: center;\">");
            out.println("<div style=\"display: inline-block; text-align: left;\">");
            out.println("<form method=\"post\">");
            out.println("First Name:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", PersonLogic.FIRST_NAME);
            out.println("<br>");
            out.println("Last Name:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", PersonLogic.LAST_NAME);
            out.println("<br>");
            out.println("Phone:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", PersonLogic.PHONE);
            out.println("<br>");
            out.println("Address:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", PersonLogic.ADDRESS);
            out.println("<br>");
            out.println("Birthday:<br>");
            out.printf("<input type=\"datetime-local\" step=\"1\" name=\"%s\" value=\"\"><br>", PersonLogic.BIRTH);
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
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log("POST");
        PersonLogic aLogic = LogicFactory.getFor("Person");
        try {
            Person person = aLogic.createEntity(req.getParameterMap());
            aLogic.add(person);
        } catch (Exception ex) {
            errorMessage = ex.getMessage();
        }

        if (req.getParameter("add") != null) {
            //if add button is pressed return the same page
            processRequest(req, resp);
        } else if (req.getParameter("view") != null) {
            //if view button is pressed redirect to the appropriate table
            resp.sendRedirect("PersonTable");
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
     * @return
     */
    @Override
    public String getServletInfo() {
        return "Create a Person Entity";
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
