package com.bage.jdbc.springboot.repository;

import com.bage.jdbc.springboot.enitiy.SchedulerJobInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SchedulerDaoImpl implements  SchedulerDao{


    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void save(SchedulerJobInfo jobInfo) {
        String sql = "INSERT INTO scheduler_job_info (cron_expression, job_class, job_group, job_name, cron_job, repeat_time) VALUES ( ?, ?, ?, ?, ?, ?)";
        Object[] args = new Object[]{jobInfo.getCronExpression(),jobInfo.getJobClass(),jobInfo.getJobGroup(),jobInfo.getJobName(),jobInfo.getCronJob(),jobInfo.getRepeatTime()};
        jdbcTemplate.update(sql,args);
    }

    @Override
    public List<SchedulerJobInfo> findAll() {
        String sql = "select id,cron_expression, job_class, job_group, job_name, cron_job, repeat_time from scheduler_job_info ";
        return jdbcTemplate.query(sql, new RowMapper<SchedulerJobInfo>() {
            @Override
            public SchedulerJobInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                SchedulerJobInfo schedulerJobInfo = new SchedulerJobInfo();

                schedulerJobInfo.setId(rs.getLong("id"));
                schedulerJobInfo.setCronExpression(rs.getString("cron_expression"));
                schedulerJobInfo.setJobClass(rs.getString("job_class"));
                schedulerJobInfo.setJobGroup(rs.getString("job_group"));
                schedulerJobInfo.setJobName(rs.getString("job_name"));
                schedulerJobInfo.setCronJob(rs.getBoolean("cron_job"));
                schedulerJobInfo.setRepeatTime(rs.getLong("repeat_time"));


                return schedulerJobInfo;
            }
        });
    }
}
