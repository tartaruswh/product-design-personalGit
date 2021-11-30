package com.bewg.pd.baseinfo;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import com.bewg.pd.common.util.LoggerUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lizy
 */
@Slf4j
@EnableAutoConfiguration
@ComponentScan("com.bewg.pd")
public class BaseinfoApplication {
    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(BaseinfoApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        LoggerUtil.info("\n----------------------------------------------------------\n\t" + "Application decision is running! Access URLs:\n\t" + "Local: \t\thttp://localhost:" + port + path + "/\n\t" + "External: \thttp://" + ip + ":"
            + port + path + "/\n\t" + "swagger-ui: \thttp://" + ip + ":" + port + path + "/swagger-ui.html\n\t" + "Doc: \t\thttp://" + ip + ":" + port + path + "/doc.html\n" + "----------------------------------------------------------");
    }
}
