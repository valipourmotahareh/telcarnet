package com.thimat.sockettelkarnet.classes;

import java.util.Date;
import java.util.Locale;

public class Jalali {
    public static String getEnglishFormattedDate(Date inEnglishDate) {
        Locale loc = new Locale("en_US");
        return (inEnglishDate.getYear() + 1900) + "-"
                + String.format(loc, "%02d", inEnglishDate.getMonth() + 1)
                + "-" + String.format(loc, "%02d", inEnglishDate.getDate())
                + " " + inEnglishDate.getHours() + ":"
                + inEnglishDate.getMinutes() + ":" + inEnglishDate.getSeconds();
    }
    public static String getEnglishFormattedtime(Date inEnglishDate) {
        Locale loc = new Locale("en_US");
        return inEnglishDate.getHours() + ":"
                + inEnglishDate.getMinutes() + ":" + inEnglishDate.getSeconds();
    }
    //-------------------------------------------------------
    public static String getEnglishparseDate(Date inEnglishDate) {
        Locale loc = new Locale("en_US");
        return (inEnglishDate.getYear() + 1900) + "-"
                + String.format(loc, "%02d", inEnglishDate.getMonth() + 1)
                + "-" + String.format(loc, "%02d", inEnglishDate.getDate());
    }
    //-------------------------------------------------------------------------------
    public static String getFarsiToday() {
        Locale loc = new Locale("en_US");
        Jalali util = new Jalali();
        SolarCalendar sc = util.new SolarCalendar();
        return sc.year + "/"
                + String.format(loc, "%02d", sc.month) + "/"
                + String.format(loc, "%02d", sc.date);
    }

    public static String getFarsiDate(Date inEnglishDate) {
        Locale loc = new Locale("en_US");
        Jalali util = new Jalali();
        SolarCalendar sc = util.new SolarCalendar();
        sc.calcSolarCalendar(inEnglishDate);
        return sc.year + "/"
                + String.format(loc, "%02d", sc.month) + "/"
                + String.format(loc, "%02d", sc.date);
    }
    //-----------------------------------------------------------


    private class SolarCalendar {

        public String strWeekDay = "";
        public String strMonth = "";

        int date;
        int month;
        int year;

        public SolarCalendar() {
            Date MiladiDate = new Date();
            calcSolarCalendar(MiladiDate);
        }

        public SolarCalendar(Date MiladiDate) {
            calcSolarCalendar(MiladiDate);
        }

        private void calcSolarCalendar(Date MiladiDate) {

            int ld;

            int miladiYear = MiladiDate.getYear() + 1900;
            int miladiMonth = MiladiDate.getMonth() + 1;
            int miladiDate = MiladiDate.getDate();
            int WeekDay = MiladiDate.getDay();

            int[] buf1 = new int[12];
            int[] buf2 = new int[12];

            buf1[0] = 0;
            buf1[1] = 31;
            buf1[2] = 59;
            buf1[3] = 90;
            buf1[4] = 120;
            buf1[5] = 151;
            buf1[6] = 181;
            buf1[7] = 212;
            buf1[8] = 243;
            buf1[9] = 273;
            buf1[10] = 304;
            buf1[11] = 334;

            buf2[0] = 0;
            buf2[1] = 31;
            buf2[2] = 60;
            buf2[3] = 91;
            buf2[4] = 121;
            buf2[5] = 152;
            buf2[6] = 182;
            buf2[7] = 213;
            buf2[8] = 244;
            buf2[9] = 274;
            buf2[10] = 305;
            buf2[11] = 335;

            if ((miladiYear % 4) != 0) {
                date = buf1[miladiMonth - 1] + miladiDate;

                if (date > 79) {
                    date = date - 79;
                    if (date <= 186) {
                        switch (date % 31) {
                            case 0:
                                month = date / 31;
                                date = 31;
                                break;
                            default:
                                month = (date / 31) + 1;
                                date = (date % 31);
                                break;
                        }
                        year = miladiYear - 621;
                    } else {
                        date = date - 186;

                        switch (date % 30) {
                            case 0:
                                month = (date / 30) + 6;
                                date = 30;
                                break;
                            default:
                                month = (date / 30) + 7;
                                date = (date % 30);
                                break;
                        }
                        year = miladiYear - 621;
                    }
                } else {
                    if ((miladiYear > 1996) && (miladiYear % 4) == 1) {
                        ld = 11;
                    } else {
                        ld = 10;
                    }
                    date = date + ld;

                    switch (date % 30) {
                        case 0:
                            month = (date / 30) + 9;
                            date = 30;
                            break;
                        default:
                            month = (date / 30) + 10;
                            date = (date % 30);
                            break;
                    }
                    year = miladiYear - 622;
                }
            } else {
                date = buf2[miladiMonth - 1] + miladiDate;

                if (miladiYear >= 1996) {
                    ld = 79;
                } else {
                    ld = 80;
                }
                if (date > ld) {
                    date = date - ld;

                    if (date <= 186) {
                        switch (date % 31) {
                            case 0:
                                month = (date / 31);
                                date = 31;
                                break;
                            default:
                                month = (date / 31) + 1;
                                date = (date % 31);
                                break;
                        }
                        year = miladiYear - 621;
                    } else {
                        date = date - 186;

                        switch (date % 30) {
                            case 0:
                                month = (date / 30) + 6;
                                date = 30;
                                break;
                            default:
                                month = (date / 30) + 7;
                                date = (date % 30);
                                break;
                        }
                        year = miladiYear - 621;
                    }
                } else {
                    date = date + 10;

                    switch (date % 30) {
                        case 0:
                            month = (date / 30) + 9;
                            date = 30;
                            break;
                        default:
                            month = (date / 30) + 10;
                            date = (date % 30);
                            break;
                    }
                    year = miladiYear - 622;
                }

            }

            switch (month) {
                case 1:
                    strMonth = "فروردین";
                    break;
                case 2:
                    strMonth = "اردیبهشت";
                    break;
                case 3:
                    strMonth = "خرداد";
                    break;
                case 4:
                    strMonth = "تیر";
                    break;
                case 5:
                    strMonth = "مرداد";
                    break;
                case 6:
                    strMonth = "شهریور";
                    break;
                case 7:
                    strMonth = "مهر";
                    break;
                case 8:
                    strMonth = "آبان";
                    break;
                case 9:
                    strMonth = "آذر";
                    break;
                case 10:
                    strMonth = "دی";
                    break;
                case 11:
                    strMonth = "بهمن";
                    break;
                case 12:
                    strMonth = "اسفند";
                    break;
            }

            switch (WeekDay) {

                case 0:
                    strWeekDay = "شنبه";
                    break;
                case 1:
                    strWeekDay = "یکشنبه";
                    break;
                case 2:
                    strWeekDay = "دوشنبه";
                    break;
                case 3:
                    strWeekDay = "سه شنبه";
                    break;
                case 4:
                    strWeekDay = "چهارشنبه";
                    break;
                case 5:
                    strWeekDay = "پنجشنبه";
                    break;
                case 6:
                    strWeekDay = "جمعه";
                    break;
            }

        }

    }

}