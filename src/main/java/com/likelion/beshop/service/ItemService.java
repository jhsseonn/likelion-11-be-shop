package com.likelion.beshop.service;

import com.likelion.beshop.dto.ItemFormDto;
import com.likelion.beshop.dto.ItemImgDto;
import com.likelion.beshop.entity.Item;
import com.likelion.beshop.entity.ItemImg;
import com.likelion.beshop.repository.ItemImgRepository;
import com.likelion.beshop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    @Value("${itemImgLocation}")
    private String itemImgLocation;
    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final ItemImgService itemImgService;
    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{
        //상품 등록
        Item item = itemFormDto.convertItem();
        itemRepository.save(item);
        //이미지 등록
        for(int i = 0;i < itemImgFileList.size();i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if (i == 0)
                itemImg.setImageMainYN("Y");
            else
                itemImg.setImageMainYN("N");

            MultipartFile itemImgFile = itemImgFileList.get(i);
            itemImgService.saveItemImg(itemImg, itemImgFile);
        }
        return item.getId();
    }
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId){
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderById(itemId);
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for (ItemImg itemImg : itemImgList){
            ItemImgDto itemImgDto = ItemImgDto.fromEntity(itemImg);
            itemImgDtoList.add(itemImgDto);
        }
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);;

        ItemFormDto itemFormDto = ItemFormDto.convertItemFormDto(item);
        itemFormDto.setItemImageDtos(itemImgDtoList);
        return itemFormDto;
    }

    //파일 수정
    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);

        List<Long> itemImgIds = itemFormDto.getItemImageIds();

        for (int i = 0;i<itemImgIds.size(); i++){
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }


        return item.getId();
    }
}
