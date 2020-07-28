<%-- 
    Document   : login
    Created on : Dec 22, 2018, 11:51:08 AM
    Author     : NIKHIL
--%>

<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="p1.C"%>
<%@page import="java.sql.Connection"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        
     <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
   
     <link href="css/bootstrap.min.css" rel="stylesheet">
     <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
     <script src="js/bootstrap.min.js"></script>
     <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
     <link href="login1.css" type="text/css" rel="stylesheet">
     
       <title>Login</title>
                <script type="text/javascript">
               function onlyNumbers(event) {
                   
                   var charCode = event.which || event.keyCode;
                   if (charCode > 31 && (charCode < 48 || charCode > 57))
                       return false;

                   return true;
               }
               
           </script>
           <script>
 $(document).ready(function()
 {
    $("select option[value="+$('select option:selected').val()+"]").css({"background-color":"#0000ff","color":"#fff"});
    $('select').change(function() {
     $('select option')[0].value=$('select option:selected').val();
     $('select option')[0].innerHTML=$('select option:selected').val();
     $("select").val($('select option:selected').val());
     $("select option").css({"background-color":"","color":""});
     $("select option[value="+$('select option:selected').val()+"]").css({"background-color":"#0000ff","color":"#fff"});
    });
  });
</script>
    </head>
    
    <body>
       <script>
function CheckPassword(pswd) 
{ 
 if(myform.uid.value == "") {
    alert("Error: Username cannot be blank!");
      myform.uid.focus();
      return false;
     
      
 }
 if(myform.pswd.value == "") {
      alert("Error:Password cannot be blank!");
      myform.pswd.focus();
      return false;
 }

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
                        
                         <li><a href="index.jsp">Home</a></li>
                         <li><a href="about.jsp">About Us</a></li>
                          <li><a href="login.jsp" class="active">Login</a></li>
                         <li><a href="reg1.jsp">Sign Up</a></li>
                    </ul>
                </div>      </div>
            
            </nav>
        <div class="container-fluid bg">
            <div  class="row">
                <div class="col-md-4 col-sm-4 col-xs-12"></div>
                <div class="col-md-4 col-sm-4 col-xs-12" >
                    
                    <form class="form-container" style="background-color: #ececec;" action="Login" name="myform"  onsubmit="return CheckPassword(document.myform.pswd)">
                        <h1 id="log">Login</h1>

                        <div class="form-group ">
                            Phone Number<br> 
                            <input type="text" name="uid" class="form-control input-group" id="Phone number" placeholder="Phone number" maxlength="10" onkeypress="return onlyNumbers(event);" >
                            <br>
                             
                          Password<br>
                        <input type="password" name="pswd" class="form-control" id="Password1" placeholder="Password">
                        <br>
                        <%
                               if(request.getParameter("msg")!=null)
                                     {
                                        String value = request.getParameter("msg").toString();
                               %>
                               <p style="color: red;"><%=value%></p>
                               <%
                                   }
                               %>
                      </div>

                      <button type="submit" class="btn btn-success btn-block" >Submit</button>
                 
                    </form>
                    
                    
                </div>
                <div class="col-md-4 col-sm-4 col-xs-12"></div>
            
            </div>
        </div>
        
    </body>
    
</html>
