package jpabook.jpashop.service;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateItemDto {
    Long itemId;
    String name;
    int price;
    int stockQuantity;
}
