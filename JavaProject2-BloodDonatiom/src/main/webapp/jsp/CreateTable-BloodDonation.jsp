<%-- 
    Document   : CreateTable-BloodDonation.jsp
    Created on : Apr. 13, 2021, 12:54:08 a.m.
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create BloodDonation JSP Page</title>
    </head>
    <body>
        <div style="text-align: center;">
        <div style="display: inline-block; text-align: left;">
        <form method="post">
        BloodBankID:<br>
        <select name="bank_id">
             <c:forEach var="bank" items="${bankList}">
             <option value="${bank.getId()}">${bank.getName()}</option> 
             </c:forEach>
        </select>
        <br><br>
        Milliliters:<br>
        <input type="text" name="milliliters" value=""><br><br>
        Blood Group:<br>
        <input type="text" name="blood_group" value=""><br><br>
        Rh Factor:<br>
        <input type="text" name="rhesus_factor" value=""><br><br>
        Created Date Time:<br>
        <input type="datetime-local" step="1" name="created" value=""><br><br>
        <input type="submit" name="view" value="Add and View">
        <input type="submit" name="add" value="Add">
        </form>
    </body>
</html>
