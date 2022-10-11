package lv.savchuk.country.calling.codes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "lv.savchuk.country.calling.codes.wiki.client")
public class CountryCallingCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CountryCallingCodeApplication.class, args);
    }

}