package com.example.demo.Util;

public class MyUtil {



    public static long parseDateToMilliseconds(String date){

        long number = 0;
        switch (date){
            case "lastday":
                number = 24 * 60 * 60 * 1000;
                break;
            case "lastweek":
                number = 24 * 60 * 60 * 7 * 1000;
                break;
            case "lastmonth":
                number = 24 * 60 * 60 * 30 * 1000;
                break;

        }
        return number;
    }

    public static Boolean validInput(String request){
        String[] input = request.split("/");
        if((validIP(input[2]) && Integer.valueOf(input[3])>0) &&
                input[4].equals("lastday") ||  input[4].equals("lastweek") ||  input[4].equals("lastmonth")){
            return true;
        }
        return false;
    }

    public static boolean validIP (String ip) {
        try {
            if ( ip == null || ip.isEmpty() ) {
                return false;
            }

            String[] parts = ip.split( "\\." );
            if ( parts.length != 4 ) {
                return false;
            }

            for ( String s : parts ) {
                int i = Integer.parseInt( s );
                if ( (i < 0) || (i > 255) ) {
                    return false;
                }
            }
            if ( ip.endsWith(".") ) {
                return false;
            }

            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
