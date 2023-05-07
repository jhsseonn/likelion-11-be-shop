package com.likelion.beshop.dto;

import com.likelion.beshop.constant.ItemSellStatus;
import com.likelion.beshop.entity.Item;
import com.likelion.beshop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.List;

@Getter
@Setter
public class ItemFormDto {
    private Long id;
    private String name;
    private Integer price;
    private String Description;
    private Integer count;
    private ItemSellStatus itemSellStatus;

    private List<ItemImg> itemImages;
    private List<Long> itemImageIds;

    private static ModelMapper modelMapper = new ModelMapper();
    public Item convertItem() {
        return modelMapper.map(this, Item.class);
    }
    public static ItemFormDto convertItemFormDto(Item item) {
        return modelMapper.map(item, ItemFormDto.class);
    }
}
