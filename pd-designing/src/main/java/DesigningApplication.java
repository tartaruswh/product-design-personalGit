import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lizy
 */
@Slf4j
@EnableAutoConfiguration
@ComponentScan("com.bewg.pd.designing")
public class DesigningApplication {

    public static void main(String[] args) {
        SpringApplication.run(DesigningApplication.class, args);
        log.info("启动完成");
    }
}
