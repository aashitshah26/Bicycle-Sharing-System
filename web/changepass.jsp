<%-- 
    Document   : changepass
    Created on : Apr 18, 2019, 3:02:44 PM
    Author     : NIKHIL
--%>

<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page import="p1.C"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <link href="newcss.css" type="text/css" rel="stylesheet">
        <title>Change Password</title>
    </head>
    <body>
        <script>
         function CheckPassword() 
{ 
if(myform.pass.value == "") {
    alert("Error:Password cannot be blank!");
      myform.pass.focus();
      return false;
     
      
 }
 if(myform.pass1.value == "") {
      alert("Error:New Password cannot be blank!");
      myform.pass1.focus();
      return false;
 }
 if(myform.pass2.value == "") {
      alert("Error:Retype Password cannot be blank!");
      myform.pass2.focus();
      return false;
 }
 if(myform.pass2.value != myform.pass1.value ) 
            {
            alert("Error: Password must be same !");
            myform.pass2.focus();
            return false;
            }
            
return true;
}   
            
          
            </script>
         <nav class="navbar navbar-default navbar-fixed-top" >
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar-collapse-main">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>   
                    </button>
                    <a class="navbar-brand" href="#"><img  style="width: 139px ; margin-top: -15px;"src="images/logo.png" ></a>
                </div>
                <div class="collapse navbar-collapse" id="navbar-collapse-main">
                     <ul class="nav navbar-nav navbar-right">
                        
                         <li><a  href="index.jsp">Home</a></li>
                         <li><a href="about.jsp">About Us</a></li>
                         <li><a href="contact.jsp">Contact Us</a></li>
                         <li><a href="myrides.jsp">My Rides</a></li>
                         <li><a class="active" href="Profile.jsp">Profile</a></li>
                    </ul>
                </div>      </div>
            
            </nav>
 
        <div class="col-md-4" ></div>
   <div class="col-md-4 bg" >
       <form name="myform" method="post" action="pass" class="form-container" onsubmit="return CheckPassword()">
           <div class="form-group">
        <label for="pass">Password</label>
        <input type="password" name="pass" class="form-control" value="" placeholder="*******"/><br/>
        <label for="pass1">New Password</label>
        <input type="password" name="pass1" class="form-control" /><br/>
        <label for="pass2">Rewrite Password</label>
        <input type="password" name="pass2" class="form-control"/><br/>
           </div>
             <button type="submit" class="btn btn-success btn-block">Submit</button>
             </form>
   </div>
         <div class="col-md-4" ></div>
                   
    </body>
</html>
