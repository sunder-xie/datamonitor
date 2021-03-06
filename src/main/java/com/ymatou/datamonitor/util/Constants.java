/*
 *
 *  (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.datamonitor.util;

/**
 * Created by lsq on 2015/12/1.
 */
public class Constants {

    public static final String JOB_SPEC = "JOb_";
    
    /**
     * 发送报警邮件中标题的最大长度，255
     */
    public static final int ALARM_MAIL_TITLE_LENGTH = 255;

    /**
     * 发送报警邮件中邮件标题信息
     */
    public static final String ALARM_MAIL_TITLE = "fatal message mail ";

    public static final String LIMIT_MYSQL_TEMPLATE = "select  * from ( %s ) topTab limit 1000";
    public static final String LIMIT_MSSQL_TEMPLATE = "select top 1000 * from ( %s ) topTab";
    public static final String MONGO_SCRIPT_TEMPLATE = "function(){return %s.toArray();}";
}
