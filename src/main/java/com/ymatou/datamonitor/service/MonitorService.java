/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.datamonitor.service;

import org.quartz.SchedulerException;

import com.ymatou.datamonitor.model.pojo.Monitor;
import com.ymatou.datamonitor.model.vo.MonitorVo;

/**
 * 
 * @author qianmin 2016年8月18日 下午2:50:49
 *
 */
public interface MonitorService extends BaseService<Monitor> {
    
    /**
     * 添加任务
     * @param monitorVo
     * @throws SchedulerException
     */
    public void addMonitor(MonitorVo monitorVo) throws SchedulerException ;

    /**
     * 修改任务
     * @param monitorVo
     * @throws SchedulerException
     */
    public void modifyMonitor(MonitorVo monitorVo) throws SchedulerException ;
    
    /**
     * 删除任务
     * @param monitorVo
     * @throws SchedulerException
     */
    public void removeMonitor(MonitorVo monitorVo) throws SchedulerException ;

    /**
     * 暂停任务
     * @param monitorVo
     * @throws SchedulerException
     */
    public void pauseMonitor(MonitorVo monitorVo) throws SchedulerException ;

    /**
     * 重启任务
     * @param monitorVo
     * @throws SchedulerException
     */
    public void resumeMonitor(MonitorVo monitorVo) throws SchedulerException ;

    /**
     * 立即执行任务
     * @param monitorVo
     */
    public void runNow(MonitorVo monitorVo);
}
