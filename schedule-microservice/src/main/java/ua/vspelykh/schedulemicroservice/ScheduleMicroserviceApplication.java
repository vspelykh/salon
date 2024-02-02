package ua.vspelykh.schedulemicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ScheduleMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScheduleMicroserviceApplication.class, args);
    }
}
