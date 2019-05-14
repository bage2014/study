package com.bage.jdbc.springboot.enitiy;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Chamith
 */
@Getter
@Setter
//@Entity
//@Table(catalog = "quartz_demo_db", name = "scheduler_job_info")
public class SchedulerJobInfo {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jobName;

    private String jobGroup;

    private String jobClass;

    private String cronExpression;

    private Long repeatTime;

    private Boolean cronJob;
}