package com.example.productservice;

import com.example.productservice.dto.ProductRequest;
import com.example.productservice.repository.ProductRepository;
import net.bytebuddy.utility.dispatcher.JavaDispatcher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//integretion tests
@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {
  @Autowired
  private  ProductRepository productRepository;
  @Container
  static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @DynamicPropertySource
  static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
    dynamicPropertyRegistry.add("spring.data.mongodb.url",mongoDBContainer::getReplicaSetUrl);
  }
	@Test
	void shouldCreateProduct() throws Exception
  {
    ProductRequest productRequest =  getProductRequest();
    String productRequestString =objectMapper.writeValueAsString(productRequest);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
      .contentType(MediaType.APPLICATION_JSON)
      .content(productRequestString))
      .andExpect(status().isCreated());
    Assertions.assertEquals(1,productRepository.findAll().size());


	}

  private ProductRequest getProductRequest()
  {
    return ProductRequest.builder()
      .name("Iphone 13")
      .description("iphone 13 with 128gb")
      .price(BigDecimal.valueOf(1200))
      .build();
  }

}
