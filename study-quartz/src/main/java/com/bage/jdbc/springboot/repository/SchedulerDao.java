package com.bage.jdbc.springboot.repository;

import com.bage.jdbc.springboot.enitiy.SchedulerJobInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Chamith
 */
public interface SchedulerDao {

    void save(SchedulerJobInfo jobInfo);

    List<SchedulerJobInfo> findAll();
}
