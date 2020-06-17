package com.promise.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.promise.demo.config","com.promise.demo.*"})
@EnableScheduling
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication app=new SpringApplication(DemoApplication.class);
        app.addListeners(new ApplicationPidFileWriter());
        app.run(args);
    }
}
