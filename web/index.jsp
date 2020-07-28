<%-- 
    Document   : newjsp
    Created on : Dec 20, 2018, 11:29:55 PM
    Author     : NIKHIL
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
      
        <title>HOME</title>
   <link href="css/bootstrap.min.css" rel="stylesheet">
      
       <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>

 <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link href="index.css" type="text/css" rel="stylesheet">
    </head>
    <body>
       <nav class="navbar navbar-default navbar-fixed-top bg-dark navbar-dark" >
            
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar-collapse-main">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>   
                    </button>
                    <a class="navbar-brand" href="#"><img style="width: 139px ; margin-top: -10px;" src="images/logo.png" ></a>
                </div>
                <div class="collapse navbar-collapse" id="navbar-collapse-main">
                     <ul class="nav navbar-nav navbar-right">
                        <%
                            if(session.getAttribute("login")!=null)
                            {
                        %>
                         <li><a class="active" href="index.jsp">Home</a></li>
                         <li><a href="about.jsp">About Us</a></li>
                         
                         <li><a href="myrides.jsp">My Rides</a></li>
                         <li><a href="Profile.jsp">Profile</a></li>
                         <li><a href="Logout">Logout</a></li>
                        <%   
                            }
                        else
                            {      
                        %>
                         <li><a class="active" href="index.jsp">Home</a></li>
                         <li><a href="about.jsp">About Us</a></li>
                         <li><a href="login.jsp">Login</a></li>
                         <li><a href="reg1.jsp">Sign Up</a></li>
                         <%}%>
                    </ul>
                  </div>
            </div>
        </nav>
        
    <div id="home">
        <div class="landing-text">
            <h1 style="color:#18188f">PSYCLEPATH</h1>
            <h3 style="color:#517913">Go Eco-Friendly</h3>
            
        </div>
    </div>
        
        <div class="padding" style="background-color:#d9e2fa">
        <div class="container">
            <div class="row">
                <div class="col-sm-6">
                    <img src="images/cycling1.jpg">
                </div>
                <div class="col-sm-6 text-center" >
                    <h2>All about Riding a Bicycle</h2>
                    <p class="lead">Cycling is widely regarded as a very effective and efficient mode of transportation optimal for short to moderate distances.</p>
                     
                    <p class="lead">Bicycles provide numerous benefits in comparison with motor vehicles, including the sustained physical exercise involved in cycling, easier parking, and access to roads, bike paths and rural trails.</p>
                     
                   
                </div>
            </div>
        </div>
    </div>
              
            <div id="fixed">
         <div class="landing-text">
             <h3>So here is our product to encourage cycling among people ,</h3>
             <h3>prevent environment from being polluted , </h3>
             <h3> and help people stay healthy . </h3>
             <h1 style="color:#18188f">PSYCLEPATH</h1>
            <h3 style="color:#517913">"Go Eco-Friendly"</h3>
            
        </div>
    </div>
    <div class="padding"  style="background-color:#d9e2fa">
        <div class="container">
            <div class="row">
                
                <div class="col-sm-6 text-center" >
                    <p class="lead">'PSYCLEPATH' will be a bicycle sharing system to promote environment friendly and affordable transport system. </p>
                <p class="lead"> It covers a part of city with a number of automated bicycle stations
from which people can rent a digitally locked bicycle for certain amount of time frame from one
station and return it to a different station near their destination location.</p>
                    
                </div>
                <div class="col-sm-6">
                    
                    <img src="images/3.jpg">
                    
                </div>
            </div>
        </div>
    </div>
        
        <footer class="container-fluid text-center">
        <div class="row">
            
            <div class="col-sm-4">
                
                <h3>CONTACT US</h3><br>
                <h4>Our contact info here</h4>
                
            </div>
            <div class="col-sm-4">
                <h3>
                    Connect
                </h3>
                <a href="#" class="fa fa-facebook"></a>
                <a href="#" class="fa fa-twitter"></a>
                <a href="#" class="fa fa-google"></a>
                <a href="#" class="fa fa-instagram"></a>
                <a href="#" class="fa fa-youtube"></a>
            </div>
            <div class="col-sm-4">
                <img src="images/logo.png" class="icon">
                
            </div>
        </div>
        
    </footer>
    </body>
</html>
