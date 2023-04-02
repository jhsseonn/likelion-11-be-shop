package com.likelion.beshop.dto_;

import lombok.Getter;
import lombok.Setter;
import org.yaml.snakeyaml.events.Event;

@Getter
@Setter
public class ShoppingFormDto {
    private String item;
    private String price;
    private String size;
    private String color;

}
