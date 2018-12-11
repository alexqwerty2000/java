package com.springmangodb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@SpringBootApplication
public class SpringMangodbApplication {

	@Bean
	RouterFunction<ServerResponse> routs(CustomerRepository cr){
		return RouterFunctions.route(GET("/customer"), serverRequest -> ok().body(cr.findAll(), CustomerInit.class));
	}
	public static void main(String[] args) {
		SpringApplication.run(SpringMangodbApplication.class, args);
	}
}


	@Component
	class DataWriter implements ApplicationRunner {

		private final CustomerRepository customerRepository;

		DataWriter(CustomerRepository customerRepository) {
			this.customerRepository = customerRepository;
		}

		@Override
		public void run(ApplicationArguments args) throws Exception {
			Flux.just("Alex", "Ded","Maxx", "Justin")
					.flatMap(name->customerRepository.save(new CustomerInit(null, name)))
					.subscribe(System.out::println);
		}
	}
	interface CustomerRepository extends ReactiveMongoRepository<CustomerInit, String>{}


	@Document
	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	class CustomerInit {
		String id,name;

		public CustomerInit(Object o, String name) {


		}
	}
