package ua.vspelykh.feedbackmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class FeedbackMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeedbackMicroserviceApplication.class, args);
    }
}
