package com.example.productservice.Controller;

import com.example.productservice.Service.ProductService;
import com.example.productservice.dto.ProductRequest;
import com.example.productservice.dto.ProductResponse;
import com.example.productservice.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController
{

  private final ProductService productService;
  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  public void createProduct(@RequestBody ProductRequest productRequest)

  {
        productService.createProduct(productRequest);
  }

  @GetMapping()
  @ResponseStatus(HttpStatus.OK)
  public List<ProductResponse> getAllProducts()
  {
    return productService.getAllProducts();
  }

}
