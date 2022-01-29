package view;

import java.io.PrintWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.LogicFactory;
import entity.BloodDonation;
import logic.BloodDonationLogic;

/**
 * Processes requests
 *
 * @author Danping Tang
 */
@WebServlet(name = "BloodDonationTable", urlPatterns = {"/BloodDonationTable"})
public class BloodDonationTableView extends HttpServlet {

    /**
     *
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>BloodDonationView</title>");
            out.println("</head>");
            out.println("<body>");

            out.println("<table style=\"margin-left: auto; margin-right: auto;\" border=\"1\">");
            out.println("<caption>BloodDonation</caption>");

            BloodDonationLogic bdLogic = LogicFactory.getFor("BloodDonation");
            out.println("<tr>");
            bdLogic.getColumnNames().forEach(c -> out.printf("<th>%s</th>", c));
            out.println("</tr>");

            bdLogic.getAll().forEach(e -> out.printf("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>",
                    bdLogic.extractDataAsList(e).toArray()));
            out.println("<tr>");

            bdLogic.getColumnNames().forEach(c -> out.printf("<th>%s</th>", c));
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
        BloodDonation bloodDonation = bdLogic.updateEntity(request.getParameterMap());
        bdLogic.update(bloodDonation);
        processRequest(request, response);
    }

    /**
     *
     * @return
     */
    @Override
    public String getServletInfo() {
        return "Sample of BloodDonation View Normal";
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
