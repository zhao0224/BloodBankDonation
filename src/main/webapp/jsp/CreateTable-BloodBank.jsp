<%-- 
    Document   : CreateTable-BloodBank
    Created on : Apr. 10, 2021, 2:24:18 a.m.
    Author     : Jing Zhao
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html><head><title>Create Blood Bank JSP</title>
    </head>
    <body>
        <div style="text-align: center;">
            <div style="display: inline-block; text-align: left;">
                <form method="post">
                    Owner:<br>
                    <select name="owner_id">
                        <c:forEach var="person" items="${personList}">
                            <option value="${person.getId()}">${person.getLastName()}</option> 
                        </c:forEach>
                    </select>
                    </br></br>
                    Name:<br>
                    <input type="text" name="name" value="">
                    <br><br>  
                    Privately_Owned:<br>
                    <select name="privately_owned" >
                        <option value="True">True</option>
                        <option value="False">False</option>
                    </select>
                    </br></br>
                    Established:<br>
                    <input type="datetime-local" step="1" name="established" value=""><br><br>
                    Employee_Count:<br>
                    <input type="text" name="employee_count" value=""><br><br>
                    <input type="submit" name="view" value="Add and View">
                    <input type="submit" name="add" value="Add">
                </form>
                <!--  <pre>
                       Submitted keys and values:
                       toStringMap( request.getParameterMap() )
                  </pre>-->

            </div>
        </div>


    </body></html>
