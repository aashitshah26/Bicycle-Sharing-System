<%-- 
    Document   : index
    Created on : 10 Feb, 2019, 3:46:50 PM
    Author     : Yash
--%>

<%@page import="java.sql.Statement"%>
<%@page import="p1.C"%>
<%@page import="com.sun.java.swing.plaf.windows.resources.windows"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title>My Rides</title>

        <style>
            html{
                box-sizing:border-box;
                background-color:#70c5c0;

            }
            #searchbar{
                box-sizing: content-box;
                margin-left: 35px;

            }
            .row{

                padding-top: 100px; 
            }
            *, *:before, *:after {
                box-sizing: inherit;
            }
            .column{
                margin-top: 50px;
                float:left;
                width:20%;
                margin-bottom:16px;
                padding:0 30px;
            }
            .card{

                box-shadow:0 12px 20px 0 rgba(0, 0, 0, 0.2);
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
            .maps{
                height:300px;
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
                transition-duration: 0.8s;
                width: 100%;

            }
            @media screen and (max-width: 1000px) {
                .column {
                    width: 100%;
                    display: block;
                }
            }
            .btn:hover{
                background-color:#002266;
                color: white;

            }
            input[type=date] {
                width: 50%;
                padding: 12px 20px;
                margin: 8px 0;
                box-sizing: border-box;
            }
        </style>

    </head>
    <body>
        
        <%HttpSession sess=request.getSession(true);
        sess.setAttribute("about",0);
        sess.setAttribute("ride",1);
        %>
        <jsp:include page="header.jsp" ></jsp:include>
            <div class="row">

                

            <%
                String start, end;
                session = request.getSession();
                Connection con = C.getConnected();
                Statement s = con.createStatement();
                Statement s1 = con.createStatement();
                String ph=session.getAttribute("userid").toString();
                String sql ="SELECT * FROM rides where ph_num="+ph;
          
                ResultSet rs = s.executeQuery(sql);
                while (rs.next()) {
                    String loc1 = rs.getString("location1");
                    String loc2 = rs.getString("location2");
                    System.out.println("loc1:"+loc1);
                    System.out.println("loc2:"+loc2);
                    String fare = rs.getString("fare");
                    String calories = rs.getString("cal")+" cal";
                    String pollution = rs.getString("pollution") +" g of CO2";
              
                    
                   String From = rs.getString("From");
                    String To = rs.getString("To");
                   

            %>


            <div class="column">
                <div class="card" style="background-color: #fff;">
                    <!--                    <img src="map_image.jpeg" alt="Map" style=" width:100%;">-->

                    <div id="<%=rs.getString("srno")%>" class="maps" data-gps1="<%=rs.getString("location1")%>" data-gps2="<%=rs.getString("location2")%>"></div>
                    <div class="container" >
                        <h2>Your ride</h2>
                        <p class="title">From:<%=From%>          <br> TO:<%=To%> </p>
                        <!--                      <p>TO:</p>-->
                        <p><form name="submitform" action="details.jsp" method="post">
                            <input type="hidden" value="<%=rs.getString("srno")%>" name="id"/>
                            <input type="submit" class="btn" name="button" value="Get Details"/>
                        </form></p> 
                    </div>

                </div>
            </div>
            <%} %>
        </div>
        <script type="text/javascript">
            var map;
            function initMap() {
                // Get all map canvas with ".maps" and store them to a variable.
                var maps = document.getElementsByClassName("maps");

                var ids, gps1, gps2, mapId = '';

                // Loop: Explore all elements with ".maps" and create a new Google Map object for them
                var i = 0;
                while (i < maps.length)
                {

                    // Get ID of single div
                    mapId = document.getElementById(maps[i].id);

                    // Get LatLng stored in data attribute. 
                    // !!! Make sure there is no space in data-attribute !!!
                    // !!! and the values are separated with comma !!!
                    gps1 = mapId.getAttribute('data-gps1');
                    gps2 = mapId.getAttribute('data-gps2');

                    // Convert LatLng to an array
                    gps1 = gps1.split(",");
                    gps2 = gps2.split(",");

                    // Create new Google Map object for single canvas 
                    map = new google.maps.Map(mapId, {
                        zoom: 13,
                        // Use our LatLng array bellow
                        center: new google.maps.LatLng((parseFloat(gps1[0]) + parseFloat(gps1[0])) / 2, (parseFloat(gps1[1]) + parseFloat(gps2[1])) / 2),
                        mapTypeId: 'roadmap',
                        mapTypeControl: true,
                        zoomControlOptions: {
                            position: google.maps.ControlPosition.RIGHT_TOP
                        }
                    });

                    // Create new Google Marker object for new map
                    var marker = new google.maps.Marker({
                        // Use our LatLng array bellow
                        position: new google.maps.LatLng(parseFloat(gps1[0]), parseFloat(gps1[1])),
                        map: map
                    });

                    marker = new google.maps.Marker({
                        // Use our LatLng array bellow
                        position: new google.maps.LatLng(parseFloat(gps2[0]), parseFloat(gps2[1])),
                        map: map
                    });

                    i++;
                }
            }
        </script>
        <script async defer
                src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAKApfyqAxkKJfX_o-vaCdIiyBDHIgKheU&callback=initMap">
        </script>
  </body>
</html>
