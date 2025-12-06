package com.clandestock.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class ClandeStockBackendApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(ClandeStockBackendApplication.class, args);

        Environment env = ctx.getEnvironment();
        System.out.println("✅ Backend corriendo en http://localhost:" + env.getProperty("server.port"));
    }
}
