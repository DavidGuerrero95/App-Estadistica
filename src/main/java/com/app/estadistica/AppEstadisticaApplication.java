/*****************************************************************************
********	MICROSERVICES WITH SPRING BOOT				******
********	DEVELOPED BY: SANTIAGO GUERRERO				******
********	FROM UNIVERSITY OF ANTIOQUIA				******
*****************************************************************************/
package com.app.estadistica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
public class AppEstadisticaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppEstadisticaApplication.class, args);
	}

}
