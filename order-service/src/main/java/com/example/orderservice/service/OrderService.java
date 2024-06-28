package com.example.orderservice.service;


//import antlr.debug.Tracer;

import com.example.orderservice.dto.InventoryResponse;
import com.example.orderservice.dto.OrderLineItemsDto;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.event.orderPlacedEvent;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderLineItems;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.sleuth.Span;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
private final OrderRepository orderRepository;
private final WebClient.Builder webClientBuilder;
//private final Tracer tracer;
  private final KafkaTemplate<String, orderPlacedEvent> kafkaTemplate;
  public String placeorder(OrderRequest orderRequest) {
    Order order = new Order();
    order.setOrderNumber(UUID.randomUUID().toString());

    // Log order number
    log.info("Placing order with order number: {}", order.getOrderNumber());


    List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
      .stream()
      .map(this::mapToDto)
      .toList();



    order.setOrderLineItemsList(orderLineItems);
    log.info("Order line items: {}", orderLineItems);
   List<String> skuCodes =order.getOrderLineItemsList().stream()
     .map(OrderLineItems::getSkuCode)
     .toList();

//   log.info("calling inventory service");
//   Span inventoryServiceLookup= tracer.nextSpan().name("InventoryServiceLookup");
//   try(tracer.withSpan(inventoryServiceLookup.start())){
//
//   }finally{
//     inventoryServiceLookup.end();
//   };
//check inventory is present or not
  InventoryResponse[]  inventoryResponsesArray =webClientBuilder.build().get()
        .uri("http://inventory-service/api/inventory",
          uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
          .retrieve()
            .bodyToMono(InventoryResponse[].class)
              .block();
    boolean allProductsInStock = Arrays.stream(inventoryResponsesArray).allMatch(InventoryResponse::isInStock);

    if(allProductsInStock){
      orderRepository.save(order);
      log.info("order saved succssfully");
      kafkaTemplate.send("notificationTopic",new orderPlacedEvent(order.getOrderNumber()));
      return "order placed successfully";
    }
    else{
      throw new IllegalArgumentException("product is not in stock please try agan later");
    }


  }

  private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto){
  OrderLineItems orderLineItems = new  OrderLineItems();
  orderLineItems.setPrice(orderLineItemsDto.getPrice());
  orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
  orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
  return orderLineItems;
  }

  }


