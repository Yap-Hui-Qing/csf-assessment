package vttp.batch5.csf.assessment.server;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static Date convertTimestamp(long timestamp) throws ParseException{

        System.out.println(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date timestampDate = new Date(timestamp);
        System.out.println(timestampDate);
        System.out.println(sdf.parse(sdf.format(timestampDate)));
        return sdf.parse(sdf.format(timestampDate));
    }
}