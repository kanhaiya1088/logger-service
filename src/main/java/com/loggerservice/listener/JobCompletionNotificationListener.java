package com.loggerservice.listener;

import com.loggerservice.entity.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("!!! JOB FINISHED! Time to verify the results");

			String query = "SELECT id, duration, type, host, alert FROM events";

			jdbcTemplate.query(query, (rs, row) -> new Event(rs.getString(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getBoolean(5)))
					.forEach(event -> log.info("Found < {} > in the database.", event));

		}
	}
}