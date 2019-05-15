package com.spring.configuration;

import java.util.stream.Stream;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@EnableDiscoveryClient
@SpringBootApplication
public class FlowerCatalogServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlowerCatalogServerApplication.class, args);
	}
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
class FlowerItem{
	
	public FlowerItem(String name) {
		this.name=name;
	}
	
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
}

@RepositoryRestResource
interface FlowerItemRepository extends JpaRepository<FlowerItem, Long>{}


@Component
class FlowerItemInititor implements CommandLineRunner{
	
	private final FlowerItemRepository flowerItemRepository;
	
	FlowerItemInititor(FlowerItemRepository temRepository){
		this.flowerItemRepository=temRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		Stream.of("Satin Garland","Booque","Natural Garland","Stage Decoration","Car Decoration").forEach(item -> flowerItemRepository.save(new FlowerItem(item)));
		
		flowerItemRepository.findAll().forEach(System.out::println);
	}
	
}