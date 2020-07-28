<%-- 
    Document   : details
    Created on : 10 Feb, 2019, 5:40:48 PM
    Author     : Yash
--%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="p1.C"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Ride Details</title>
        <style type="text/css">
            #map_image>img{

                width: border-box;
                height:500px;
                width: 100%;
               
            }
            .row{
                padding-top: 82px;
            }
            .Activity{
                font-size: 35px;
                color: blue;
            }
            .Activity_source{
                font-size: 35px;
                color: red;

            }
            .SubActivity{
                font-size: 30px;
                color: grey;
            }
            .maps{
                height: 500px;
            }
        </style>

    </head>
    <body>
        
                <jsp:include page="header.jsp" ></jsp:include>
        <%

            int id = Integer.parseInt(request.getParameter("id"));
           
            C c1 = new C();
            Connection con = c1.getConnected();
            String sql = "SELECT * FROM rides where srno="+id+"";
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
        %>
        <div class="row">
        <div id="map_image"><div id="<%=rs.getString("srno")%>" class="maps" data-gps1="<%=rs.getString("location1")%>" data-gps2="<%=rs.getString("location2")%>"></div>
            <div class="content">
                <br><br>
                <label class="Activity_source">Source: <%=rs.getString("From")%> </label> &emsp; &emsp; &emsp; &emsp; &emsp;&emsp; &emsp; &emsp; &emsp; &emsp;&emsp; &emsp; &emsp; &emsp; &emsp;&emsp; &emsp; &emsp; &emsp; &emsp;&emsp; &emsp; &emsp; &emsp; &emsp;
                <label class="Activity">Destination:<%=rs.getString("To")%></label><br><br><br>
                <label class="SubActivity">Start Time:<%=rs.getString("time1")%></label>&emsp; &emsp; &emsp; &emsp; &emsp;&emsp; &emsp; &emsp; &emsp; &emsp;&emsp; &emsp; &emsp; &emsp; &emsp;&emsp; &emsp; &emsp; &emsp; &emsp;&emsp; &emsp; &emsp; &emsp; &emsp;
                <label class="SubActivity">End Time:<%=rs.getString("time2")%></label><br><br>
                <label class="SubActivity">Calories Burned:<%=rs.getString("cal")+" cal"%></label><br><br>
                <label class="SubActivity">Pollution Prevented:<%=rs.getString("pollution")+" gms of CO2"%></label><br><br>
                <label class="SubActivity">Fare Paid:<%=rs.getString("fare")%></label>

            </div>        


        </div>
                </div>
<%}%>

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
