package com.example.bfs;

/**
 * Created by surpr on 2017/12/27.
 */

public class DateSelection {

    public String displayDay(int year, int monthOfYear, int dayOfMonth) {
        String Date = null;
        String strYear = String.valueOf(year);
        String strMonth = String.valueOf(monthOfYear+1);
        String strDay = String.valueOf(dayOfMonth);
        if(monthOfYear >= 10 && dayOfMonth>=10){
            Date = strYear+"-"+strMonth+"-"+strDay;
        }else if(monthOfYear < 10 && dayOfMonth >= 10){
            Date = strYear+"-0"+strMonth+"-"+strDay;
        }else if(monthOfYear >= 10 && dayOfMonth < 10){
            Date = strYear+"-"+strMonth+"-0"+strDay;
        }else if(monthOfYear < 10 && dayOfMonth < 10){
            Date = strYear+"-0"+strMonth+"-0"+strDay;
        }
        return Date;
    }

    public String displayTime(int hourOfDay, int minute) {
        String Time = null;
        String strHour = String.valueOf(hourOfDay);
        String strMinute = String.valueOf(minute);

        if(hourOfDay >= 10 && minute>=10){
            Time = strHour+":"+strMinute+":00";
        }else if(hourOfDay < 10 && minute >= 10){
            Time = "0"+strHour+":"+strMinute+":00";
        }else if(hourOfDay >= 10 && minute < 10){
            Time = strHour+":0"+strMinute+":00";
        }else if(hourOfDay < 10 && minute < 10){
            Time = "0"+strHour+":0"+strMinute+":00";
        }

        return Time;
    }
}

