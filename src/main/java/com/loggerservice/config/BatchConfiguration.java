package com.loggerservice.config;

import com.loggerservice.dto.LogFileEventDto;
import com.loggerservice.entity.Event;
import com.loggerservice.listener.JobCompletionNotificationListener;
import com.loggerservice.processor.EventItemProcessor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Slf4j
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Bean
    public FlatFileItemReader<LogFileEventDto> reader() {
        FlatFileItemReader<LogFileEventDto> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("logfile.txt"));

        LogFileJsonLineMapper lineMapper = new LogFileJsonLineMapper();
        reader.setLineMapper(lineMapper);
        return reader;
    }

    @Bean
    public EventItemProcessor processor() {
        return new EventItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Event> writer() {
        JdbcBatchItemWriter<Event> writer = new JdbcBatchItemWriter<Event>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Event>());
        writer.setSql("INSERT INTO events (id, duration, type, host, alert) VALUES (:id, :duration, :type, :host, :alert)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<LogFileEventDto, Event> chunk(1000)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper;
    }

}
