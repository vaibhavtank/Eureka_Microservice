package com.spring.configuration;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;


import lombok.Data;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@EnableFeignClients
@EnableCircuitBreaker
@EnableDiscoveryClient
@EnableZuulProxy
@SpringBootApplication
public class ErrorHandlerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ErrorHandlerServiceApplication.class, args);
	}

}

@Data
class FlowerItem{
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}

@FeignClient("flower-catalog-service")
interface FlowerItemClient{

	@GetMapping("/items")
	Resources<FlowerItem> readItems();
}

@RestController
class GoodItemAPIAdapterRestController{
	
	private final FlowerItemClient itemClient;

	public GoodItemAPIAdapterRestController(FlowerItemClient itemClient){
		this.itemClient=itemClient;
	}

	public Collection<FlowerItem> fallBack(){
		return new ArrayList<>();
	}


	@HystrixCommand(fallbackMethod = "fallback")
	@GetMapping("/top-brands")
	@CrossOrigin(origins = "*")
	public Collection<FlowerItem> goodItems(){
		return itemClient.readItems().getContent().stream().filter(this::isGreat).collect(Collectors.toList());
	}

	private boolean isGreat(FlowerItem flowerItem) {
		return flowerItem.getName().equals("Satin Garland") && flowerItem.getName().equals("Satin Garland");
	}

}