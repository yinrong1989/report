package com.yinrong.report.config;

import com.yinrong.report.util.VelocityUtil;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.ToolManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @Classname VelocityConfig
 * @Description
 * @Date 2020-04-14 14:08
 * @Created by yinrong
 */

@Configuration
public class VelocityConfig {
    @Value("${spring.velocity.toolbox-config-location}")
    String velocityToolBoxPath;
    private Properties properties;
    VelocityConfig(){
        properties = new Properties();
    }

    @Bean
    VelocityEngine velocityEngine(){
        VelocityEngine velocityEngine =  new VelocityEngine();
        velocityEngine.init();
        return velocityEngine ;
    }
    @Bean
    ToolManager toolManager(){
        ToolManager manager = new ToolManager();
        //     manager.configure("/velocity/toolbox.xml");
        manager.configure(velocityToolBoxPath);
        return  manager;
    }

    @Bean
    VelocityUtil velocityUtil(){
        VelocityUtil  velocityUtil =new VelocityUtil(velocityEngine(),toolManager());

        return velocityUtil;
    }
}
