package view;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.DonationRecordLogic;
import logic.LogicFactory;
import java.util.Map;

/**
 *
 * @author Gabriel Matte
 */
@WebServlet(name = "DonationRecordTable", urlPatterns = {"/DonationRecordTable"})
public class DonationRecordTableView extends HttpServlet {

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            // TABLE HEADER
            out.println("<head>");
            out.println("<title>DonationRecordView</title>");
            out.println("</head>");
            out.println("<body>");

            out.println("<table style=\"margin-left: auto; margin-right: auto;\" border=\"1\">");
            out.println("<caption>DonationRecord</caption>");

            DonationRecordLogic logic = LogicFactory.getFor("DonationRecord");
            out.println("<tr>");
            logic.getColumnNames().forEach(c -> out.printf("<th>%s</th>", c));
            out.println("</tr>");
            //TABLE DATA
            logic.getAll().forEach(e -> out.printf("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>",
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
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log("GET");
        processRequest(request, response);
    }

}
