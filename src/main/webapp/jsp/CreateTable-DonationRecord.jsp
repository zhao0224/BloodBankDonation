<%-- 
    Document   : CreateTable-DonationRecord
    Created on : Apr. 13, 2021, 12:54:49 a.m.
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
        <title>Create Donation Record JSP</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6" crossorigin="anonymous">
    </head>
    <body>
<!--      Container div and page title-->
        <div class="container my-4" style="width: 450px;">
        <h2>Add a Donation Record</h2>
<!--     FORM html-->
        <form method="post">
<!--      PersonID input-->
        <label class="mt-2" for="personId">Person ID:</label>
        <select name="person_id">
            <c:forEach var="person" items="${personList}">
                <option value="${person.getId()}">${person.getLastName()}</option> 
            </c:forEach>
        </select>
        </br></br>
<!--        <input class="form-control" type="text" id="personId" name="person_id" value="">-->
        
<!--         DonationID input-->
        <label class="mt-2" for="donationId">Donation ID:</label>
        <input class="form-control" type="text" id="donationId" name="donation_id" value="">        
<!--        Administrator input-->
        <label class="mt-2" for="admin">Administrator:</label>
        <input class="form-control" type="text" id="admin" name="administrator" value="">          
<!--          Hospital input-->
        <label class="mt-2" for="hospital">Hospital:</label>
        <input class="form-control" type="text" id="hospital" name="hospital" value="">
<!--            Tested radio input-->
        <label class="mt-2" for="tested">Tested:</label>
        <div class="form-check ml-2">
          <input class="form-check-input" type="radio" id="testedTrue" name="tested" value="1">
          <label class="form-check-label" for="testedTrue">True</label>
        </div>
        <div class="form-check ml-2">
        <input class="form-check-input" type="radio" id="testedFalse" name="tested" value="0">
        <label class="form-check-label" for="testedFalse">False</label>
        </div>
<!--        Created Date-->
       <label class="mt-2" for="created">Created Date Time:</label>
       <input class="form-control" type="datetime-local" step="1" id="created" name="%s" value=""> 
           
<!--                  Submit buttons-->
        <div class="row">
        <div class="col-1"></div>
        <input class="btn btn-primary my-2 col-4" type="submit" name="add" value="Add">
        <div class="col-2"></div>
        <input class="btn btn-primary my-2 col-4" type="submit" name="view" value="Add & View">
        <div class="col-1"></div>
        </div>      
            
<!--         End of form-->
        </form> 

    </body>
    

</html>
