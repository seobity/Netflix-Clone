package com.likelion.NetflixClone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // 💡 Auditing 기능 활성화
@SpringBootApplication
public class NetflixCloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(NetflixCloneApplication.class, args);
	}
}