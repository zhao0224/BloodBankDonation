package view;

import entity.BloodBank;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.BloodBankLogic;
import logic.Logic;
import logic.LogicFactory;

/**
 *
 * @author Jing Zhao
 */
@WebServlet(name = "BloodBankTable", urlPatterns = {"/BloodBankTable"})
public class BloodBankTableView extends HttpServlet {

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
            out.println("<title>BloodBankViewNormal</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<table style=\"margin-left: auto; margin-right: auto;\" border=\"1\">");
            out.println("<caption>BloodBank</caption>");

            Logic<BloodBank> logic = LogicFactory.getFor("BloodBank");
            out.println("<tr>");
            logic.getColumnNames().forEach(c -> out.printf("<th>%s</th>", c));

            out.println("</tr>");

            logic.getAll().forEach(e -> out.printf("<tr><td>%s</td><td>%s</td>"
                    + "<td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>",
                    logic.extractDataAsList(e).toArray()));
            out.println("<tr>");
            logic.getColumnNames().forEach(c -> out.printf("<th>%s</th>", c));
            out.println("</tr>");
            out.println("</table>");
            out.printf("<div style=\"text-align: center;\"><pre>%s</pre></div>", toStringMap(request.getParameterMap()));
            out.println("</body>");
            out.println("</html>");

        }

    }

    /**
     * 
     * @param m
     * @return 
     */
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

    /**
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        log("POST");
        BloodBankLogic logic = LogicFactory.getFor("BloodBank");
        BloodBank bloodBank = logic.updateEntity(req.getParameterMap());
        logic.update(bloodBank);
        processRequest(req, resp);
    }

    /**
     * 
     * @return 
     */
    @Override
    public String getServletInfo() {
        return "Sample of BloodBank View Normal"; //To change body of generated methods, choose Tools | Templates.
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
