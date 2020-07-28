<%-- 
    Document   : reg2
    Created on : Dec 19, 2018, 10:31:55 AM
    Author     : dell
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>REGISTER YOURSELF</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <link href="login1.css" type="text/css" rel="stylesheet">
    </head>
    <body>
        <script>
        function check(myform)
        {  
            var unm=myform.name.value
           var psw1= myform.pswd.value;
           var psw2=myform.pass1.value;
    var passw=/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,20}$/;
        if(psw1 == unm) 
            {
            alert("Error: Password must be different from Username!");
            myform.pswd.focus();
            return false;
            }
        if(psw2 != psw1) 
            {
            alert("Error: Password must be same !");
            myform.pass1.focus();
            return false;
            }
    if(psw1.match(passw)) 
    { 
    alert('Registered sucessfully :) ')
    return true;
    }
    else
    { 
    alert('Enter valid password.')
    return false;
    }
}
function matched(){
    
    var pass=document.getElementById("pas").value;
    var para=document.getElementById("para");
    var passw=/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,20}$/;
    if(pass.match(passw)) 
    { 
    para.style.display='none';
    }
    else{
        para.style.display='block';
    }
    
}
</script>

        <%
            String phno=request.getParameter("phno");
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
                         <li><a href="reg1.jsp" class="active">Sign Up</a></li>
                         <li><a href="login.jsp">Login</a></li>
                     </ul>
                </div>      
            </div>
            
            </nav>
        
         <div class="container-fluid bg">
         <div class="row">
        <div class="col-md-4 col-sm-4 "></div>
        <div class="col-md-4 col-sm-4 ">
            <form name="myform" method="post" action="Register" class="form-container" style="background-color: #ececec;" onsubmit="return check(this)">
             
                <div class="form-group">
                    <h1>SIGN UP</h1>
                    <input type="hidden" class="form-control" name="phno" value="<%=phno%>" />
                    <label for="name">Name:</label>
                    <input type="text" name="name" value="" class="form-control" required=""/>
                    <label for="name">Email:</label>
                    <input type="email" name="email" class="form-control" required=""/>
                    <label for="dob">Date of birth:</label>
                    <input type="date" name="dob" class="form-control" required=""/>
                    <label for="age">Age:</label>
                    <input type="number" name="age" class="form-control" required=""/>
                    <label for="pass">Password:</label>
                    <input type="password" name="pswd" id="pas" value="" class="form-control" required="" onkeyup="matched()"/>
                    <p style="color: red" id="para">password must be between 6 to 20 characters which contain at least one numeric digit, one uppercase and one lowercase letter.</p>
                    <label for="repass">Re-enter Password:</label>
                    <input type="password" name="pass1" value="" class="form-control" required=""/>
                </div>
                    <div class="custom-control custom-radio">
                        <label for="gen">Gender:</label>
                        <input type="radio" name="g" value="1" checked="checked" class="custom-control-input"/> 
                        <label for="male">Male</label>
                        <input type="radio" name="g" value="2" class="custom-control-input"/>
                        <label for="female">Female</label><br/>   
                    </div>
                    <input type="submit" name="sub2" value="Register" class="btn-success"/>
        </form>
        </div>
                <div class="col-md-4 col-sm-4 "></div>
         </div>
         </div>
    </body>
</html>
