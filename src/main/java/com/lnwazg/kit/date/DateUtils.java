package com.lnwazg.kit.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.lnwazg.kit.log.Logs;

/**
 * 日期时间工具类
 * @author nan.li
 * @version 2017年8月3日
 */
public class DateUtils
{
    /**
     * 标准日期时间格式
     */
    public static final String DEFAULT_DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * 日期时间格式，时间精确到分钟
     */
    public static final String DEFAULT_DATE_TIME_HHmm_FORMAT_PATTERN = "yyyy-MM-dd HH:mm";
    
    /**
     * 日期时间格式，时间精确到小时
     */
    public static final String DEFAULT_DATE_TIME_HH_FORMAT_PATTERN = "yyyy-MM-dd HH";
    
    /**
     * 标准日期格式
     */
    public static final String DEFAULT_DATE_FORMAT_PATTERN = "yyyy-MM-dd";
    
    /**
     * 日期格式，无连接符
     */
    public static final String DEFAULT_DATE_FORMAT_PATTERN_NO_CONNECTOR = "yyyyMMdd";
    
    /**
     * 标准时间格式
     */
    public static final String DEFAULT_TIME_FORMAT_PATTERN = "HH:mm:ss";
    
    /**
     * 时间格式，精确到分钟
     */
    public static final String DEFAULT_TIME_HHmm_FORMAT_PATTERN = "HH:mm";
    
    /**
     * 日期时间格式，年月日时分秒，无空格，无连接符<br>
     * 适用于文件名的日期时间格式化<br>
     * 例如：20170702220512
     */
    public static final String DEFAULT_FILE_DATE_TIME_FORMAT_PATTERN = "yyyyMMddHHmmss";
    
    /**
     * 获取当前时间的Calendar对象
     * @author nan.li
     * @return
     */
    public static Calendar getNowCalendar()
    {
        return Calendar.getInstance();
    }
    
    /**
     * 获取明天时间的Calandar对象
     * @author nan.li
     * @return
     */
    public static Calendar getTomorrowCalendar()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return calendar;
    }
    
    /**
     * 获取后天时间的Calandar对象
     * @author nan.li
     * @return
     */
    public static Calendar getAfterTomorrowCalendar()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 2);
        return calendar;
    }
    
    /**
     * 获取当前时间的Date对象
     * @author nan.li
     * @return
     */
    public static Date getNowDate()
    {
        return new Date();
    }
    
    /**
     * 获取明天时间的Date对象
     * @author nan.li
     * @return
     */
    public static Date getTomorrowDate()
    {
        return getTomorrowCalendar().getTime();
    }
    
    /**
     * 获取后天时间的Date对象
     * @author nan.li
     * @return
     */
    public static Date getAfterTomorrowDate()
    {
        return getAfterTomorrowCalendar().getTime();
    }
    
    /**
     * 是否是日期格式的字符串
     * @author nan.li
     * @param paramString
     * @return
     */
    public static boolean isDateStr(String paramString)
    {
        return parseStr2DateTime(paramString, DEFAULT_DATE_FORMAT_PATTERN) != null;
    }
    
    /**
     * 是否是日期时间格式的字符串
     * @author nan.li
     * @param paramString
     * @return
     */
    public static boolean isDateTimeStr(String paramString)
    {
        return parseStr2DateTime(paramString, DEFAULT_DATE_TIME_FORMAT_PATTERN) != null;
    }
    
    /**
     * 根据指定的Date格式，将参数日期字符串转换成Date对象<br>
     * 若无法成功转换，则返回null
     * @author nan.li
     * @param paramString
     * @param pattern
     * @return
     */
    public static Date parseStr2DateTime(String paramString, String pattern)
    {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        try
        {
            Date date = dateFormat.parse(paramString);
            return date;
        }
        catch (ParseException e)
        {
            return null;
        }
    }
    
    /**
     * 获取当前无连接符的日期，例如：20150410
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String getNowDateNoConnectorStr()
    {
        return getNowFormattedDateTimeStr(DEFAULT_DATE_FORMAT_PATTERN_NO_CONNECTOR);
    }
    
    /**
     * 获取当前日期，有连接符，例如：2015-04-10
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String getNowDateStr()
    {
        return getNowFormattedDateTimeStr(DEFAULT_DATE_FORMAT_PATTERN);
    }
    
    /**
     * 获取当前日期时间，有连接符，例如：2015-04-10 12:10:13
     * @author Administrator
     * @return
     */
    public static String getNowDateTimeStr()
    {
        return getNowFormattedDateTimeStr(DEFAULT_DATE_TIME_FORMAT_PATTERN);
    }
    
    /**
     * 获取当前日期时间，适用于文件命名的，例如：20170702220512
     * @author nan.li
     * @return
     */
    public static String getNowFileDateTimeStr()
    {
        return getNowFormattedDateTimeStr(DEFAULT_FILE_DATE_TIME_FORMAT_PATTERN);
    }
    
    /**
     * 按指定格式格式化当前日期时间的字符串
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String getNowFormattedDateTimeStr(String pattern)
    {
        return getFormattedDateTimeStr(pattern, new Date());
    }
    
    /**
     * 按指定格式， 格式化指定日期，返回格式化后的字符串
     * @param pattern
     * @param date
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String getFormattedDateTimeStr(String pattern, Date date)
    {
        return new SimpleDateFormat(pattern).format(date);
    }
    
    /**
     * 计算日期时间的时间差，并且取结果的绝对值
     * @author nan.li
     * @param end
     * @param begin
     * @param timeUnit
     * @return
     */
    public static long timeDiffAbs(Calendar end, Calendar begin, TimeUnit timeUnit)
    {
        return Math.abs(timeDiff(end, begin, timeUnit));
    }
    
    /**
     * 计算日期时间的时间差
     * @author nan.li
     * @param end
     * @param begin
     * @param timeUnit
     * @return
     */
    public static long timeDiff(Calendar end, Calendar begin, TimeUnit timeUnit)
    {
        long timeDiff = 0L;
        long diffMills = end.getTime().getTime() - begin.getTime().getTime();
        switch (timeUnit)
        {
            case DAYS:
                timeDiff = diffMills / (1000 * 60 * 60 * 24);
                break;
            case HOURS:
                timeDiff = diffMills / (1000 * 60 * 60);
                break;
            case MINUTES:
                timeDiff = diffMills / (1000 * 60);
                break;
            case SECONDS:
                timeDiff = diffMills / (1000);
                break;
            default:
                break;
        }
        return timeDiff;
    }
    
    /**
     * 计算日期时间的时间差，并且取结果的绝对值
     * @author nan.li
     * @param end
     * @param begin
     * @param timeUnit
     * @return
     */
    public static long timeDiffAbs(Date end, Date begin, TimeUnit timeUnit)
    {
        return Math.abs(timeDiff(end, begin, timeUnit));
    }
    
    /**
     * 计算日期时间的时间差
     * @author nan.li
     * @param end
     * @param begin
     * @param timeUnit
     * @return
     */
    public static long timeDiff(Date end, Date begin, TimeUnit timeUnit)
    {
        long timeDiff = 0L;
        long diffMills = end.getTime() - begin.getTime();
        switch (timeUnit)
        {
            case DAYS:
                timeDiff = diffMills / (1000 * 60 * 60 * 24);
                break;
            case HOURS:
                timeDiff = diffMills / (1000 * 60 * 60);
                break;
            case MINUTES:
                timeDiff = diffMills / (1000 * 60);
                break;
            case SECONDS:
                timeDiff = diffMills / (1000);
                break;
            default:
                break;
        }
        timeDiff = Math.abs(timeDiff);
        return timeDiff;
    }
    
    /**
     * 获取时间差的描述信息
     * @author nan.li
     * @param paramDate
     * @return
     */
    public static String getDateTimeDiffersNowRemark(Date paramDate)
    {
        if (paramDate == null)
        {
            return "-1";
        }
        //时间差的秒数
        long diffs = (System.currentTimeMillis() - paramDate.getTime()) / 1000;
        String suffix = "前";
        String unit = "秒";
        if (diffs < 0)
        {
            diffs = 0 - diffs;
            suffix = "后";
        }
        if (diffs > 60)
        {
            //需要转换成分钟显示
            unit = "分钟";
            diffs = diffs / 60;
            if (diffs > 60)
            {
                //需要转换成小时显示
                unit = "小时";
                diffs = diffs / 60;
                if (diffs > 24)
                {
                    //需要转换成天
                    unit = "天";
                    diffs = diffs / 24;
                    if (diffs > 365)
                    {
                        //需要转换成年显示
                        unit = "年";
                        diffs = diffs / 365;
                        //不管有多少年，有多少年展示多少年！
                    }
                    else
                    {
                        //按照天展示即可
                    }
                }
                else
                {
                    //按照小时展示即可
                }
            }
            else
            {
                //按照分钟展示即可
            }
        }
        else
        {
            //按秒显示即可
        }
        return String.format("%s%s%s", diffs, unit, suffix);
    }
    
    /**
     * Date加减计算，增加指定的字段指定的数量
     * @author nan.li
     * @param date
     * @param amount
     * @param field
     * @return
     */
    public static Date addDateTime(Date date, int field, int amount)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }
    
    /**
     * Calendar加减计算，增加指定的字段指定的数量
     * @author nan.li
     * @param calendar
     * @param amount
     * @param field
     * @return
     */
    public static Calendar addDateTime(Calendar calendar, int field, int amount)
    {
        calendar.add(field, amount);
        return calendar;
    }
    
    /**
     * 获取当前时间的所有信息
     * @author Administrator
     * @return
     */
    public static CalendarDesc getCurrentCalendarDesc()
    {
        CalendarDesc calendarDesc = new CalendarDesc();
        Calendar calendar = getNowCalendar();
        
        // 显示年份  
        int year = calendar.get(Calendar.YEAR);
        //        System.out.println("year is = " + String.valueOf(year));
        //        
        // 显示月份 (从0开始, 实际显示要加一)  
        int month = calendar.get(Calendar.MONTH);
        //        System.out.println("nth is = " + (month + 1));
        
        // 本周几  
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        //        System.out.println("week is = " + week);
        
        // 今年的第 N 天  
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        //        System.out.println("DAY_OF_YEAR is = " + DAY_OF_YEAR);
        
        // 本月第 N 天  
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        //        System.out.println("DAY_OF_MONTH = " + String.valueOf(DAY_OF_MONTH));
        
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        
        //属性设置
        calendarDesc.setYear(year)
            .setMonth(month)
            .setWeek(week)
            .setDayOfYear(dayOfYear)
            .setDayOfMonth(dayOfMonth)
            .setHourOfDay(hourOfDay)
            .setMinute(minute)
            .setSecond(second)
            .setCalendar(calendar);
        return calendarDesc;
    }
    
    /**
     * Calendar的描述信息
     * @author nan.li
     * @version 2016年8月18日
     */
    public static class CalendarDesc
    {
        private Calendar calendar;
        
        private int year;
        
        private int month;
        
        private int week;
        
        private int dayOfYear;
        
        private int dayOfMonth;
        
        private int hourOfDay;
        
        private int minute;
        
        private int second;
        
        public Calendar getCalendar()
        {
            return calendar;
        }
        
        public CalendarDesc setCalendar(Calendar calendar)
        {
            this.calendar = calendar;
            return this;
        }
        
        public int getYear()
        {
            return year;
        }
        
        public CalendarDesc setYear(int year)
        {
            this.year = year;
            return this;
        }
        
        public int getMonth()
        {
            return month;
        }
        
        public CalendarDesc setMonth(int month)
        {
            this.month = month;
            return this;
        }
        
        public int getWeek()
        {
            return week;
        }
        
        public CalendarDesc setWeek(int week)
        {
            this.week = week;
            return this;
        }
        
        public int getDayOfYear()
        {
            return dayOfYear;
        }
        
        public CalendarDesc setDayOfYear(int dayOfYear)
        {
            this.dayOfYear = dayOfYear;
            return this;
        }
        
        public int getDayOfMonth()
        {
            return dayOfMonth;
        }
        
        public CalendarDesc setDayOfMonth(int dayOfMonth)
        {
            this.dayOfMonth = dayOfMonth;
            return this;
        }
        
        public int getHourOfDay()
        {
            return hourOfDay;
        }
        
        public CalendarDesc setHourOfDay(int hourOfDay)
        {
            this.hourOfDay = hourOfDay;
            return this;
        }
        
        public int getMinute()
        {
            return minute;
        }
        
        public CalendarDesc setMinute(int minute)
        {
            this.minute = minute;
            return this;
        }
        
        public int getSecond()
        {
            return second;
        }
        
        public CalendarDesc setSecond(int second)
        {
            this.second = second;
            return this;
        }
        
        /**
         * 是否符合其中的一个时分
         * @author Administrator
         * @param hourMinute  07:15, 08:30, ...
         * @return
         */
        public boolean matchHourMinutes(String... hourMinute)
        {
            if (hourMinute.length > 0)
            {
                for (String hm : hourMinute)
                {
                    if (hm.length() == 5 && hm.indexOf(":") == 2)
                    {
                        String hourStr = hm.substring(0, 2);
                        String minuteStr = hm.substring(3);
                        if (Integer.valueOf(hourStr) == this.hourOfDay && Integer.valueOf(minuteStr) == this.minute)
                        {
                            return true;
                        }
                    }
                    else
                    {
                        Logs.e(String.format("参数\"%s\"格式非法！无法进行有效验证", hm));
                        return false;
                    }
                }
            }
            return false;
        }
        
        /**
         * 是否符合时间间隔区间
         * 例如09:00~23:00 每隔半个小时
         * @author nan.li
         * @param hourMinuteBegin
         * @param hourMinuteEnd
         * @param interval
         * @return
         */
        public boolean matchHourMinutesRange(String hourMinuteBegin, String hourMinuteEnd, int interval)
        {
            if (hourMinuteBegin.length() == 5 && hourMinuteBegin.indexOf(":") == 2 && hourMinuteEnd.length() == 5 && hourMinuteEnd.indexOf(":") == 2)
            {
                int hourBegin = Integer.valueOf(hourMinuteBegin.substring(0, 2));
                int minuteBegin = Integer.valueOf(hourMinuteBegin.substring(3));
                int hourEnd = Integer.valueOf(hourMinuteEnd.substring(0, 2));
                int minuteEnd = Integer.valueOf(hourMinuteEnd.substring(3));
                List<Integer> validMinutes = new ArrayList<>();
                for (int i = 0; i < 60; i += interval)
                {
                    validMinutes.add(i);
                }
                //如果开始时间和结束时间的小时数相同
                if (hourBegin == hourEnd)
                {
                    //必须在那个区间内才行
                    if (this.hourOfDay == hourBegin && validMinutes.contains(this.minute) && this.minute >= minuteBegin && this.minute <= minuteEnd)
                    {
                        return true;
                    }
                }
                else
                {
                    //开始时间与结束时间的小时数不同
                    if ((this.hourOfDay == hourBegin && validMinutes.contains(this.minute) && this.minute >= minuteBegin)
                        || (this.hourOfDay > hourBegin && this.hourOfDay < hourEnd && validMinutes.contains(this.minute))
                        || (this.hourOfDay == hourEnd && validMinutes.contains(this.minute) && this.minute <= minuteEnd))
                    {
                        return true;
                    }
                }
            }
            else
            {
                Logs.e(String.format("参数\"%s %s\"格式非法！无法进行有效验证", hourMinuteBegin, hourMinuteEnd));
                return false;
            }
            return false;
        }
    }
    
}
