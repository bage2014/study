package com.bage.jdbc.springboot.controller;

import com.bage.jdbc.springboot.enitiy.SchedulerJobInfo;
import com.bage.jdbc.springboot.repository.SchedulerDao;
import com.bage.jdbc.springboot.service.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/scheduler")
public class SchedulerController {

    @Autowired
    SchedulerService schedulerService;
    @Autowired
    SchedulerDao schedulerDao;

    /**
     * 暂停第一个
     * @return
     */
    @RequestMapping("/pause")
    public String pauseJob(){
        List<SchedulerJobInfo> list = schedulerDao.findAll();
        SchedulerJobInfo schedulerJobInfo = list.get(0);
        schedulerService.pauseJob(schedulerJobInfo);
        return "ok";
    }

    /**
     * 启动第一个
     * @return
     */
    @RequestMapping("/start")
    public String startJobNow(){
        List<SchedulerJobInfo> list = schedulerDao.findAll();
        SchedulerJobInfo schedulerJobInfo = list.get(0);
        schedulerService.startJobNow(schedulerJobInfo);
        return "ok";
    }

}
