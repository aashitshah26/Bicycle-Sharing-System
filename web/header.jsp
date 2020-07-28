<%-- 
    Document   : header
    Created on : Apr 20, 2019, 7:56:34 PM
    Author     : NIKHIL
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="p1.Login"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <style>body{

                margin: 0px;

            }
            .image{


                display: inline-block;
                position: fixed;
                float: left;
                align-content: center;
                margin-left: 15px;
                margin-top: 10px;
                height: 60px;
                width: 140px;
            }

            .header {
                background:#000;
                height: 80px;
                position: fixed;
                width: 100%;

            }
            li{
                padding-right: 5px;
            }
            ul.main{

                list-style: none;
                margin-top: 20px;    
                font-family: sans-serif;
                letter-spacing: 1px;

                font-size: 16px;
                float: right;
                padding-right: 40px;
                display: inline-block;
                align-content: center;

            }
            ul li ul li {

                display: none; 
                background-color: black;


            }

            ul li{

                list-style-type: none;
                padding-left: 20px;
                float: left;
                line-height: 40px;



            }

            /*dont look*/
            ul li a{

                color: gray;
                text-decoration: none;
                float: right;


            }
            ul li:hover ul li{

                display: list-item;
                float: right;

            }
            a:hover{
                color: white;
            }
            .active{
                color: white;
            }
        </style>

    </head>
    <body>

        <div class="header">

            <div >
                <a><img class="image" src="images/logo.png" title="Home" alt="cycle"/></a>
            </div>  

            <ul class="main">
                <%

                    if (session.getAttribute("login") != null) {
                        int about =Integer.parseInt(session.getAttribute("about").toString());
                        if (about==1) {

                %>

                <li><a href="index.jsp">Home</a></li>
                <li><a href="about.jsp" class="active">About us</a></li>
                <li><a href="myrides.jsp">My Rides</a></li>
                <li><a href="Profile.jsp">Profile</a></li>         
                <li><a href="Logout">Logout</a></li >
                    <%} else {%>
                <li><a href="index.jsp">Home</a></li>
                <li><a href="about.jsp">About us</a></li>
                <li><a href="myrides.jsp" class="active">My Rides</a></li>
                <li><a href="Profile.jsp">Profile</a></li>         
                <li><a href="Logout">Logout</a></li >        
                    <%}%>           
                    <%                 } else {
                    %>

                <li><a href="index.jsp">Home</a></li>
                <li><a href="about.jsp" class="active">About us</a></li>
                <li><a href="login.jsp">Login</a></li >
                <li><a href="reg1.jsp">Sign Up</a></li >

                <%}%>
            </ul>



        </div>
    </body>
</html>
