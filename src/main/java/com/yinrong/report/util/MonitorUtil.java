package com.yinrong.report.util;

import org.apache.commons.lang3.time.FastDateFormat;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MonitorUtil {
    public  static String getCurrentGroup(Date date){
       return getGroup(date,0);
    }
    public static String getLastGroup(Date date){
      return   getGroup(date,-15);
    }
    public static String getGroup(Date date,int amount){
        Calendar calendar =Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, amount);
        int minute =calendar.get(Calendar.MINUTE);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHH");
        String group =simpleDateFormat.format(calendar.getTime());

        if (minute>=0&&minute<15){
            return group+"00";
        }
        if (minute>=15&&minute<30){
            return group+"15";
        }
        if (minute>=30&&minute<45){
            return group+"30";
        }
        if (minute>=45&&minute<=59){
            return group+"45";
        }
        return group;

    }
    public static List<String> getGroups(Date date, int size,int step){
        List<String> groups = new ArrayList<>();
        for (int i=0;i<size;i++){
            groups.add(getGroup(date,step*i));
        }
        Collections.reverse(groups);
        return groups;


    }
    public static  List<String> getDefaultConfigGroups(Date date){
        Calendar calendar =Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, 30);
        return getGroups(calendar.getTime(), 8,-15);
    }
    public static String  division(long num1,long num2){
        String rate="0.00";
        //定义格式化起始位数
        String format="0.00";
        if(num2 != 0 && num1 != 0){
            DecimalFormat dec = new DecimalFormat(format);
            rate =  dec.format((double) num1 / num2*100);
            while(true){
                if(rate.equals(format)){
                    format=format+"0";
                    DecimalFormat dec1 = new DecimalFormat(format);
                    rate =  dec1.format((double) num1 / num2*100);
                }else {
                    break;
                }
            }
        }else if(num1 != 0 && num2 == 0){
            rate = "0";
        }
        return rate;
    }

    public static void main(String[] args) {
        System.out.println(getDefaultConfigGroups(new Date()));
    }

    public static List<String> convertGrouplist2TimeStr(List<String> grouplist) {
        List<String>  timeStrList = new ArrayList<>();

        grouplist.forEach(x ->{
            try {
                Date date = FastDateFormat.getInstance("yyyyMMddHHmm").parse(x);
                timeStrList.add(FastDateFormat.getInstance("HH:mm").format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        });

        return timeStrList;
    }
}
