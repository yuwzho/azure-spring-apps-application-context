package com.azure.springapps.reloadproperties;

import com.azure.springapps.reloadproperties.configuration.FileWatcher;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class ReloadPropertiesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReloadPropertiesApplication.class, args);
	}


	@Bean
	FileWatcher fileWatcher(ContextRefresher refresher, @Value("${AZURE.CONTEXT.DIR}") String dir) {
		return new FileWatcher(
			dir,
			s -> {
				if (s.isEmpty()) {
					return;
				}
				Set<String> refreshed = refresher.refresh();
				log.info("Context refreshed {}", refreshed.stream().collect(Collectors.joining(", ")));
			});
	}

}
