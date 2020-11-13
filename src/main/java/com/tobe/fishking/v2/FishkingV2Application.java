package com.tobe.fishking.v2;

import com.tobe.fishking.v2.repository.BaseRepositoryImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl.class)
public class FishkingV2Application {

    public static void main(String[] args) {
        SpringApplication.run(FishkingV2Application.class, args);
    }


   }
