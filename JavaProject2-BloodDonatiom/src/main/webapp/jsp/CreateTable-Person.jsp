<%-- 
    Document   : CreateTable-Person
    Created on : Apr. 13, 2021, 12:55:11 a.m.
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
        <title>Create Person JSP Page</title>
    </head>
    <body>
        <div style="text-align: center;">
        <div style="display: inline-block; text-align: left;">
         <form method="post">
         First Name:<br>
         <input type="text" name="first_name" value=""><br>
         <br>
         Last Name:<br>
         <input type="text" name="last_name" value=""><br>
         <br>
         Phone:<br>
         <input type="text" name="phone" value=""><br>
         <br>
         Address:<br>
         <input type="text" name="address" value=""><br>
         <br>
        Birthday:<br>
        <input type="datetime-local" step="1" name="birth" value=""><br>
        <br>
        <input type="submit" name="view" value="Add and View">
        <input type="submit" name="add" value="Add">
        </form>
    </body>

</html>
