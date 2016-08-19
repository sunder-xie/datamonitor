/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.datamonitor.service.impl;

import javax.transaction.Transactional;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ymatou.datamonitor.dao.jpa.MonitorRepository;
import com.ymatou.datamonitor.dao.mapper.MonitorMapper;
import com.ymatou.datamonitor.model.RunStatusEnum;
import com.ymatou.datamonitor.model.StatusEnum;
import com.ymatou.datamonitor.model.pojo.Monitor;
import com.ymatou.datamonitor.model.vo.MonitorVo;
import com.ymatou.datamonitor.service.MonitorService;
import com.ymatou.datamonitor.service.SchedulerService;

import static com.ymatou.datamonitor.util.Constants.JOB_SPEC;

/**
 * 
 * @author qianmin 2016年8月18日 下午2:50:49
 *
 */
@Service
public class MonitorServiceImpl  extends BaseServiceImpl<Monitor> implements MonitorService{
    
    private static final Logger logger = LoggerFactory.getLogger(MonitorServiceImpl.class);
    
    private MonitorRepository monitorRepository;
    
    @Autowired
    private MonitorMapper monitorMapper;
    
    @Autowired
    private SchedulerService schedulerService;
    
    @Autowired
    public MonitorServiceImpl(MonitorRepository monitorRepository) {
        super(monitorRepository);
        this.monitorRepository = monitorRepository;
    }

    @Override
    @Transactional
    public void addMonitor(MonitorVo monitorVo) throws SchedulerException {
        //save monitor info 
        Monitor monitor = new Monitor();
        BeanUtils.copyProperties(monitorVo, monitor);
        save(monitor);
        
        //add quartz scheduler job
        schedulerService.addJob(ExecuteJob.class, getJobName(monitor), monitor.getCronExpression());
        
        //update nextFireTime
        monitor.setNextFireTime(schedulerService.getNextFireTime(JOB_SPEC + monitor.getId()));
        save(monitor);
        
        logger.info("Monitor added. {}", JSON.toJSONString(monitor));
    }

    @Override
    @Transactional
    public void modifyMonitor(MonitorVo monitorVo) throws SchedulerException {
        schedulerService.modifyScheduler(getJobName(monitorVo), monitorVo.getCronExpression());
    
        Monitor monitor = monitorRepository.findOne(monitorVo.getId());
        monitor.setCronExpression(monitorVo.getCronExpression());
        monitor.setNextFireTime(schedulerService.getNextFireTime(JOB_SPEC + monitor.getId()));
        monitorRepository.save(monitor);
        
        logger.info("Monitor modified. {}", JSON.toJSONString(monitor));
    }

    @Override
    @Transactional
    public void removeMonitor(MonitorVo monitorVo) throws SchedulerException {
        schedulerService.removeScheduler(getJobName(monitorVo));
        
        Monitor monitor = monitorRepository.findOne(monitorVo.getId());
        monitor.setNextFireTime(null);
        monitor.setStatus(StatusEnum.DISABLE.name());
        monitorRepository.save(monitor);
        
        logger.info("Monitor removed. {}", JSON.toJSONString(monitor));
    }

    @Override
    @Transactional
    public void pauseMonitor(MonitorVo monitorVo) throws SchedulerException {
        schedulerService.pauseScheduler(getJobName(monitorVo));
        
        Monitor monitor = monitorRepository.findOne(monitorVo.getId());
        monitor.setRunStatus(RunStatusEnum.STOPPED.name());
        monitor.setNextFireTime(null);
        monitorRepository.save(monitor);
        
        logger.info("Monitor paused. {}", JSON.toJSONString(monitor));
    }

    @Override
    @Transactional
    public void resumeMonitor(MonitorVo monitorVo) throws SchedulerException {
        schedulerService.resumeScheduler(getJobName(monitorVo));
        
        Monitor monitor = monitorRepository.findOne(monitorVo.getId());
        monitor.setRunStatus(RunStatusEnum.RUNNING.name());
        monitor.setNextFireTime(schedulerService.getNextFireTime(JOB_SPEC + monitor.getId()));
        monitorRepository.save(monitor);
        
        logger.info("Monitor resumed. {}", JSON.toJSONString(monitor));
    }

    @Override
    public void runNow(MonitorVo monitorVo) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Page<MonitorVo> listMonitor(MonitorVo monitorVo, Pageable pageable) {
        Page<MonitorVo> monitorVos = monitorMapper.findByMonitorVo(monitorVo, pageable);

        return monitorVos;
    }
    
    private String getJobName(MonitorVo monitorVo){
        return JOB_SPEC + monitorVo.getId();
    }
    
    private String getJobName(Monitor monitor){
        return JOB_SPEC + monitor.getId();
    }
}