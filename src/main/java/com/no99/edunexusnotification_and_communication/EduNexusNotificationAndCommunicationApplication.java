package com.no99.edunexusnotification_and_communication;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
// @EnableDiscoveryClient  // Nacos discovery disabled
@MapperScan("com.no99.edunexusnotification_and_communication.mapper")
public class EduNexusNotificationAndCommunicationApplication {

    public static void main(String[] args) {
        SpringApplication.run(EduNexusNotificationAndCommunicationApplication.class, args);
    }

}
