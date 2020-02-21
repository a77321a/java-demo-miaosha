package com.xsn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Hello world!
 *
 */
//@EnableAutoConfiguration
//@MapperScan("com.xsn.dao")
@SpringBootApplication(scanBasePackages = {"com.xsn"})
@MapperScan("com.xsn.dao")
public class App 
{
    public static void main( String[] args ) {
        SpringApplication.run(App.class,args);
    }
}
