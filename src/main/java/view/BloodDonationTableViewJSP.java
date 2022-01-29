package view;

import entity.BloodDonation;
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
import logic.BloodDonationLogic;
import logic.LogicFactory;

/**
 *
 * @author Danping Tang
 */
@WebServlet(name = "BloodDonationTableJSP", urlPatterns = {"/BloodDonationTableJSP"})
public class BloodDonationTableViewJSP extends HttpServlet {

    /**
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void fillTableData(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();
        req.setAttribute("entities", extractTableData(req));
        req.setAttribute("request", toStringMap(req.getParameterMap()));
        req.setAttribute("path", path);
        req.setAttribute("title", path.substring(1));
        req.getRequestDispatcher("/jsp/ShowTable-BloodDonation.jsp").forward(req, resp);
    }

    /**
     *
     * @param req
     * @return
     */
    private List<?> extractTableData(HttpServletRequest req) {
        String search = req.getParameter("searchText");
        BloodDonationLogic logic = LogicFactory.getFor("BloodDonation");
        req.setAttribute("columnName", logic.getColumnNames());
        req.setAttribute("columnCode", logic.getColumnCodes());
        List<BloodDonation> list;
        if (search != null) {
            list = logic.search(search);
        } else {
            list = logic.getAll();
        }
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        return appendDataToNewList(list, logic::extractDataAsList);
    }

    /**
     *
     * @param <T>
     * @param list
     * @param toArray
     * @return
     */
    private <T> List<?> appendDataToNewList(List<T> list, Function<T, List<?>> toArray) {
        List<List<?>> newlist = new ArrayList<>(list.size());
        list.forEach(i -> newlist.add(toArray.apply(i)));
        return newlist;
    }

    /**
     *
     * @param parameterMap
     * @return
     */
    private Object toStringMap(Map<String, String[]> parameterMap) {
        StringBuilder builder = new StringBuilder();
        parameterMap.keySet().forEach((k) -> {
            builder.append("Key=").append(k)
                    .append(", ")
                    .append("Value/s=").append(Arrays.toString(parameterMap.get(k)))
                    .append(System.lineSeparator());
        });
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
        fillTableData(req, resp);
    }

    /**
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log("PUT");
        doPost(req, resp);
    }

    /**
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log("DELETE");
        doPost(req, resp);
    }

    /**
     *
     * @return
     */
    @Override
    public String getServletInfo() {
        return "BloodDonation Table using JSP";
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
