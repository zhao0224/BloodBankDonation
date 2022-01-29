package view;

import entity.BloodBank;
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
import logic.LogicFactory;

/**
 *
 * @author Jing Zhao
 */
@WebServlet(name = "BloodBankTableJSP", urlPatterns = {"/BloodBankTableJSP"})
public class BloodBankTableViewJSP extends HttpServlet {
    
    /**
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException 
     */
    private void fillTableData( HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException{
      String path = req.getServletPath();
      req.setAttribute("entities", extractTableData(req));
      req.setAttribute("request", toStringMap(req.getParameterMap()));
      req.setAttribute("path", path);
      req.setAttribute("title", path.substring(1));
      req.getRequestDispatcher("/jsp/ShowTable-BloodBank.jsp" ).forward(req, resp);
    }

    /**
     * 
     * @param req
     * @return 
     */
    private List<?> extractTableData(HttpServletRequest req) {
        String search = req.getParameter("searchText");
        BloodBankLogic logic = LogicFactory.getFor("BloodBank");
        req.setAttribute("columnName", logic.getColumnNames());
        req.setAttribute("columnCode", logic.getColumnCodes());
        List<BloodBank> list;
        if(search != null){
            list = logic.search(search);
        }else{
            list = logic.getAll();
        }
        if(list == null || list.isEmpty()){
            return Collections.emptyList();
        }
        return appendDataToNewList( list, logic::extractDataAsList );
       
    }
    
    /**
     * 
     * @param <T>
     * @param list
     * @param toArray
     * @return 
     */
    private <T> List<?> appendDataToNewList(List<T> list, Function<T, List<?>> toArray){
        List<List<?>> newlist = new ArrayList<>(list.size());
        list.forEach(i -> newlist.add(toArray.apply(i)));
        return newlist;
    }

    /**
     * 
     * @param m
     * @return 
     */
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
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException{
        log("POST");
        BloodBankLogic logic = LogicFactory.getFor("BloodBank");
        if(req.getParameter("edit")!=null){
            BloodBank bloodBank = logic.updateEntity(req.getParameterMap());
            logic.update(bloodBank);
        }else if(req.getParameter("delete")!= null){
            String[] ids = req.getParameterMap().get("deleteMark");
            for(String id: ids){
                logic.delete(logic.getWithId(Integer.valueOf(id)));
            }
        }
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
        return "BloodBank Table using JSP";
    }
    
    
    private static final boolean DEBUG = true;
    
    /**
     * 
     * @param msg 
     */
    public void log( String msg ) {
        if(DEBUG){
            String message = String.format("[%s] %s", getClass().getSimpleName(), msg);
            getServletContext().log(message);
        }
    }
    
    /**
     * 
     * @param msg
     * @param t 
     */
    public void log( String msg, Throwable t ) {
        String message = String.format( "[%s] %s", getClass().getSimpleName(), msg );
        getServletContext().log( message, t );
    }
    
}