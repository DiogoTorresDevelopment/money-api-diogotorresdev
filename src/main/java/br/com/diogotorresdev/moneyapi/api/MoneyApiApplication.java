package br.com.diogotorresdev.moneyapi.api;

import br.com.diogotorresdev.moneyapi.api.config.property.MoneyApiProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties(MoneyApiProperty.class)
public class MoneyApiApplication {

	private static ApplicationContext APPLICATION_CONTEXT;

	public static void main(String[] args) {
		APPLICATION_CONTEXT = SpringApplication.run(MoneyApiApplication.class, args);
	}

	public static <T> T getBean(Class<T> type){
		return APPLICATION_CONTEXT.getBean(type);
	}

}
