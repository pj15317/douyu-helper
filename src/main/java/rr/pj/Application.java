package rr.pj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "rr.pj")
public class Application {
    public static void main(String[] args) {
        //
        SpringApplication.run(Application.class,args);
    }
}
