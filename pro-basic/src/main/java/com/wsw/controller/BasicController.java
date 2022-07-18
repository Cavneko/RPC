package com.wsw.controller;

import com.wsw.service.BasicService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.wsw")
public class BasicController {
    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = new AnnotationConfigApplicationContext(BasicController.class);
        BasicService basicService = context.getBean(BasicService.class);
        basicService.testSaveUser();
        //context.wait();
    }
}
