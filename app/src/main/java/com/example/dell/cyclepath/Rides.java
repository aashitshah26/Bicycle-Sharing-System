package com.example.dell.cyclepath;

import java.io.Serializable;

class Rides implements Serializable {


    private String _startloc,_endloc,_day,_date,_start,_end,_fare,_cal,_pol;


    public Rides(String startloc, String endloc, String day, String start, String end, String date, String fare, String cal, String pol) {

   _startloc=startloc;
   _endloc=endloc;
   _day=day;
    _date=date;
    _start=start;
    _end=end;
    _fare=fare;
    _cal=cal;
    _pol=pol;
    }

    public String getstartloc()
    {
        return _startloc;
    }
    public String getendloc()
    {
        return _endloc;
    }

    public String getday() {
        return _day;
    }

    public String getdate()
    {
        return _date;
    }
    public String getstart()
    {
        return _start;
    }
    public String getend()
    {
        return _end;
    }
    public String getfare()
    {
        return _fare;
    }
    public String getcal()
    {
        return _cal;
    }
    public String getpol()
    {
        return _pol;
    }

}
