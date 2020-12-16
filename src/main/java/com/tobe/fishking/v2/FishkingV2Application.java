package com.tobe.fishking.v2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl.class)
public class FishkingV2Application {

    public static void main(String[] args) {
        SpringApplication.run(FishkingV2Application.class, args);
    }


}
