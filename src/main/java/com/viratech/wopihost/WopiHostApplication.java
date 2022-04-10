package com.viratech.wopihost;

import com.viratech.wopihost.config.ConfigData;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@ConfigurationPropertiesScan("com.viratech")
public class WopiHostApplication {

    public static void main(String[] args) {
        SpringApplication.run(WopiHostApplication.class, args);
    }

}