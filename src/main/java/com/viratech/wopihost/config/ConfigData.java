package com.viratech.wopihost.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "wopi")
@Configuration
public class ConfigData {
    private String defaultWordFilePath;
    private String wordFilesPath;
}
