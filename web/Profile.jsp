<%-- 
    Document   : Profile
    Created on : Apr 6, 2019, 12:22:53 PM
    Author     : NIKHIL
--%>

<%@page import="java.io.OutputStream"%>
<%@page import="java.sql.Blob"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="p1.C"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
         <link href="css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <link href="newcss.css" type="text/css" rel="stylesheet">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>PROFILE </title>
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
                        
                         <li><a href="index.jsp">Home</a></li>
                         <li><a href="about.jsp">About Us</a></li>
                         
                         <li><a href="myrides.jsp">My Rides</a></li>
                         <li><a class="active" href="Profile.jsp">Profile</a></li>
                         <li><a href="Logout">Logout</a></li>
                    </ul>
                </div>      </div>
            
            </nav>
        
        <div class="container-fluid ">
         <div class="row">
        <div class="col-md-4" ></div>
   <div class="col-md-4 bg" >
      
       
       
         <div class="p-image ">
        
       <form name="a" method="post" action="EditProfile.jsp" class="form-container">
           <div class="form-group">
               <h3 style="color:black" align="center">Profile </h3>
    <%
        
          String uid = session.getAttribute("userid").toString();
             Connection cn = C.getConnected();
                 Statement s = cn.createStatement();
                 String query = "SELECT propic,user_name,email,dob,password,phone_num,gender,address from user where phone_num="+uid;
                 ResultSet r = s.executeQuery(query);
                 if (r.next()) {
                  
                     String path = r.getString("propic");
                     String name = r.getString("user_name");
                     String mail = r.getString("email");
                     String dob = r.getString("dob");

                     String pass = r.getString("password");
                     String phno = r.getString("phone_num");
                     String add = r.getString("address");

                     String gen = r.getString("gender");
                     if (gen == null) {
                         gen = "0";
                     }
                    
                 %>
                  <div class="circle">
                      <%if(!path.trim().equals("")){%>
                     <img  src="image/<%=path%>" alt="not null"  border="10">  <br/>
                      
          <%}else{%>
          <img  src="images/avatar.png" alt="null">  <br/>
          <%}%>
        </div><br/>
    
   
        <input type="hidden" name="path" value="<%=path%>"/>
               <label for="name">Name:</label>
               <input type="text" name="name" value="<%=name%>" class="form-control " readonly=""/><br/>
               <label for="email">Email:</label>
               <input type="email" name="email" value="<%=mail%>" readonly="" class="form-control " readonly=""/><br/>
               <label for="dob">Date of birth:</label>
               <input type="date" name="dob" value="<%=dob%>" class="form-control " readonly=""/><br/>
               <div class="custom-control custom-radio">
         
                   <label for="gen">Gender:</label>
                   <input type="radio" name="g" value="1" <%if(Integer.parseInt(gen)==1){%> checked="checked" <%} else {%> disabled="disabled" <%}%> class="custom-control-input" /> 
                   <label for="male">Male</label>
                   <input type="radio" name="g" value="2" <%if(Integer.parseInt(gen)==2){%> checked="checked" <%} else {%> disabled="disabled" <%}%> class="custom-control-input"  />
                   <label for="female">Female</label><br/>   
               </div> <br/>
               <label for="ph num">Phone number:</label>
               <input type="text" name="phno" value="<%=phno%>" class="form-control c "  readonly="" /><br/>
               <label for="addr">Address:</label>
               <textarea class="form-control rounded-0 " name="addr"  rows="3" readonly=""><%=add%></textarea><br/>
               <a href="changepass.jsp" style="color: blue">Change Password</a>
               <button type="submit" class="btn form-control " style="color:black">Edit Profile</button><br/>
<%}%>
           </div>
       </form>
    
       
       </div>
  </div>
            <div class="col-md-4">
                
            </div>
         </div>
        </div>
       
    </body>
</html>

