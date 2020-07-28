<%-- 
    Document   : reg1
    Created on : Dec 18, 2018, 11:36:55 PM
    Author     : dell
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <link href="login1.css" type="text/css" rel="stylesheet">
    
        <title>JSP Page</title>
        <script>
        function check(abc)
        { 
        
    var phn=abc.phno.value;
    var len=phn.length;
    if(isNaN(phn))
    {
     alert("only digits");   
    return false;    
    }
     
    if(len!=10)
    {
    alert("Phone num should be of 10 digits.");
    return false;
    }
    
    return true;
    
    }
    
    function fun1()
        {
            var url_string = window.location.href; 
             var url = new URL(url_string);
            var a = url.searchParams.get("a");
            console.log(a);
            x=document.getElementById("submit");
           
            z=document.getElementById("divC");
           if(a==1)
           {
               
            x.setAttribute("name","sub1");
            x.setAttribute("value","Submit");
            
            z.style.display="inline";
           }
           else if(a==2)
           {
            x.setAttribute("name","sub2");
           }
        }
        
       
        </script>
    </head>
    
    <body onload="fun1()">
         
              <%
              String num="";
              if(request.getParameter("phno")!=null)
              {
                  num=request.getParameter("phno");
              }
              %>
              <nav class="navbar navbar-default navbar-fixed-top" >
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar-collapse-main">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>   
                    </button>
                    <a class="navbar-brand" href="#"><img style="width: 139px ; margin-top: -9px;" src="images/logo.png" ></a>
                </div>
                <div class="collapse navbar-collapse" id="navbar-collapse-main">
                     <ul class="nav navbar-nav navbar-right">
                        
                         
                         <li><a href="index.jsp">Home</a></li>
                         <li><a href="about.jsp">About Us</a></li>
                          <li><a href="login.jsp">Login</a></li>
                         <li><a href="reg1.jsp" class="active">Sign Up</a></li>
                        
                         
                    </ul>
                </div>      </div>
            
            </nav>
              <div class="container-fluid bg">
           <div class="row">
              
              <div class="col-md-4 col-sm-4 col-xs-12"></div>
             
              <div class="col-md-4 col-sm-4 col-xs-12">
               <form name="a" method="post" action="Register" class="form-container" style="background-color: #ececec;" onsubmit="return check(this)">
                <div class="form-group">
                    <label for="phonenum"><h3>Phone Number : </h3></label>
                    <input type="text" class="form-control" name="phno" value="<%=num%>" />
                </div>
         
                <div class="form-group" id="divC" style="display: none;">
                    <label for="OTP"><h3>OTP : </h3></label>
                <input type="text" class="form-control" id="otp" name="otp" value="" />
                </div><br/>
           
                
                <input type="submit" id="submit" name="sub" value="Verify" class="btn btn-success"/>
           
           
               </form>
              </div>
              <div class="col-md-4 col-sm-4 col-xs-12"></div>
           
           
           </div>
              </div>
            
    </body>
</html>
