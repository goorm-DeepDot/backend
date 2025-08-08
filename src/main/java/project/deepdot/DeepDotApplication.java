package project.deepdot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "project.deepdot")
public class DeepDotApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeepDotApplication.class, args);
    }

}
