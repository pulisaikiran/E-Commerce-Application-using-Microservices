package com.example.inventoryservice.service;


import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.repository.InventoryRepository;
//import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
  private final InventoryRepository inventoryRepository;

  //@Transactional(readOnly = true)
  @SneakyThrows
  public List<InventoryResponse>  isInStockIn(List<String> skuCode) {
//    log.info("wait started");
//    Thread.sleep(15000);
//    log.info("wait ended");
log.info("checking inventory");
    return inventoryRepository.findBySkuCodeIn(skuCode).stream()
      .map(inventory ->
        InventoryResponse.builder()
          .skuCode(inventory.getSkuCode())
          .isInStock(inventory.getQuantity()>0)
          .build()
        ).toList();
  }
}
