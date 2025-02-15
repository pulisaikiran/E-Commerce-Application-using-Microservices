package com.example.inventoryservice;

import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

  @Bean
  public CommandLineRunner loadData(InventoryRepository inventoryRepository){
     return args ->{
       Inventory inventory =  new Inventory();
     inventory.setSkuCode("iphone 16");
     inventory.setQuantity(100);

       Inventory inventory1 =  new Inventory();
       inventory1.setSkuCode("iphone 17");
       inventory1.setQuantity(300);
       inventoryRepository.save(inventory);
       inventoryRepository.save(inventory1);
     };


  }
}
