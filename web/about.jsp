<%-- 
    Document   : about
    Created on : 3 Mar, 2019, 7:02:42 PM
    Author     : Yash
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="p1.Login"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        
        <style>
html{
	box-sizing:border-box;
	background-color:#70c5c0
	}
*, *:before, *:after {
  box-sizing: inherit;
    }
    .row{
        

padding-left: 130px;
    }
	.column{
		float:left;
		width:23%;
		margin-bottom:16px;
		padding:0 10px;
	}
.card{
	transition: transform .2s;
	box-shadow:0 12px 20px 0 rgba(0, 0, 0, 0.2);
        background-color: #ffffff;
}
.container{
	padding: 0 50px;
}
.container::after, .row::after {
  content: "";
  clear: both;
  display: table;
}
p,h2{
	
	text-align:center;
}

.title {
  color: grey;
  text-align:center;
}
.btn{
  border: none;
  outline: 0;
  display: inline-block;
  padding: 8px;
  color: white;
  background-color: #000;
  text-align: center;
  cursor: pointer;
  width: 100%;
	
}
@media screen and (max-width: 1000px) {
  .column {
    width: 100%;
    display: block;
  }
}
.btn:hover {
  background-color: #555;
}



.card:hover {
  -ms-transform: scale(1.5); 
  -webkit-transform: scale(1.5); 
  transform: scale(1.2); 
}
</style>
        
    </head>
    <body>
        <%HttpSession sess=request.getSession(true);
        sess.setAttribute("about",1);
        sess.setAttribute("ride",0);
        %>
        <jsp:include page="header.jsp"></jsp:include>
       <br>
<br>
<br>
<br>
<br>
<br>
<h2>The team who developed this site </h2>
<br>
<br>
<br>
<div class="row">
<div class="column">
<div class="card">
<img src="images/yash1.jpg" alt="yash" style=" width:100%;">
<div class="container">
<h2>Yash</h2>
<p class="title">Designer</p>
<p>khatri7yash@gmail.com</p>

</div>

</div>
</div>

<div class="column">
    
<div class="card">
<img src="images/aashit1.jpg" alt="Aashit" style=" width:100%;">
<div class="container">
<h2>Aashit</h2>
<p class="title">Developer</p>
<p>shahaashit26@gmail.com</p>



</div>
        </div>
</div>

<div class="column">
<div class="card">
<img src="images/shaily1.jpeg" alt="Shaily" style=" width:100%;">
<div class="container">
<h2>Shaily</h2>
<p class="title">Developer</p>
<p>shahshaily@gmail.com</p>

</div>

</div>
</div>

<div class="column">
<div class="card">
<img src="images/shivani1.jpeg"  style=" width:100%;">
<div class="container">
<h2>Shivani</h2>
<p class="title">Designer</p>
<p>shivaniparekh2197@gmail.com</p>

</div>

</div>
</div>
</div>

    </body>
</html>
