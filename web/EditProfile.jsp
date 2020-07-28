<%-- 
    Document   : newjsp
    Created on : Feb 15, 2019, 8:08:50 PM
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
        <title>Edit your Profile !</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <link href="newcss.css" type="text/css" rel="stylesheet">
    <script>
$(document).ready(function() {

    
    var readURL = function(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();

            reader.onload = function (e) {
                $('.profile-pic').attr('src', e.target.result);
            }
    
            reader.readAsDataURL(input.files[0]);
        }
    }
    

    $(".file-upload").on('change', function(){
        readURL(this);
  });
    
    $(".upload-button").on('click', function() {
       $(".file-upload").click();
    });
}); 
    </script>
    </head>
    <body>
        
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
                         <li><a href="myrides.jsp">My Rides</a></li>
                         <li><a class="active" href="#">Profile</a></li>
                         <li><a href="Logout">Logout</a></li>
                    </ul>
                </div>      </div>
            
            </nav>
         <div class="container-fluid ">
        <div class="col-md-4" ></div>
   <div class="col-md-4 bg" >
    
       
       
         <div class="p-image ">
      
             <h3 style="color:black" align="center"> Edit Profile </h3> <br/>
            
       
        
       <form name="a" method="post" action="Ep" class="form-container">
                    <div class="form-group">
                       
                    <% 
           
            String uid = session.getAttribute("userid").toString();  
              String image =request.getParameter("path");        
              String name= request.getParameter("name");
              String mail= request.getParameter("email");
              String dob= request.getParameter("dob");
              String phno= request.getParameter("phno");
              String add= request.getParameter("addr");
              
              String gen= request.getParameter("g");
                    if( gen==null){
                  gen="0";
              }
    %>
    <div class="circle" style="color: red">
         <img class="profile-pic" src="image/<%=image%>" >  
             
        </div>
         <input type="hidden" name="uid" value="<%=uid%>"/>
                         <input class="file-upload" id="imgupload" name="photo1" type="file" accept="image/*"/><br/>
                        <label for="name">Name:</label>
                        <input type="text" name="name" value="<%=name%>" class="form-control"/><br/>
                        <label for="email">Email:</label>
                        <input type="email" name="email" value="<%=mail%>" class="form-control"/><br/>
                        <label for="dob">Date of birth:</label>
                        <input type="date" name="dob" class="form-control" value="<%=dob%>" /><br/>
                        <div class="custom-control custom-radio">
                            <label for="gen">Gender:</label>
                            <input type="radio" name="g" value="1"  <%if(Integer.parseInt(gen)==1){%> checked="checked" <%}%> class="custom-control-input"/> 
                            <label for="male">Male</label>
                            <input type="radio" name="g" value="2" <%if(Integer.parseInt(gen)==2){%> checked="checked" <%}%> class="custom-control-input"/>
                            <label for="female">Female</label><br/>   
                        </div>
                        <label for="ph num">Phone number:</label>
                        <input type="text" name="phno" value="<%=phno%>"  class="form-control" readonly=""/><br/>
                        <label for="addr">Address:</label>
                        <textarea class="form-control rounded-0" name="addr"   rows="3"><%=add%></textarea><br/>
                        
                   
    <button type="submit" class="btn form-control">Submit</button>
          </div>
                        
       </div>
   </div>
  </div>
            <div class="col-md-4">
                
            </div>
    

    </body>
</html>
