package com.example.orderservice.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="t_order_line_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderLineItems {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String skuCode;
  private BigDecimal price;
  private Integer quantity;






}
