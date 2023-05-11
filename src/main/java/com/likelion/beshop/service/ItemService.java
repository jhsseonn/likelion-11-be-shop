package com.likelion.beshop.service;

import com.likelion.beshop.dto.ItemFormDto;
import com.likelion.beshop.dto.ItemImgDto;
import com.likelion.beshop.entity.Item;
import com.likelion.beshop.entity.ItemImg;
import com.likelion.beshop.repository.ItemImgRepository;
import com.likelion.beshop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    // 상품 등록
    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        // 상품 등록
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        // 이미지 등록
        for (int i = 0; i < itemImgFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);

            // 첫 번째 이미지이면 대표 이미지 여부 Y
            if (i == 0)
                itemImg.setRepImg("Y");
            else
                itemImg.setRepImg("N");

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }
        return item.getId();
    }


    // 상품 조회
    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId) {
        // 상품 id를 통해 상품에 해당되는 이미지 엔티티 객체 가져옴
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        // 상품 이미지 Dto 배열 생성
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();

        // 상품 이미지 엔티티 객체를 Dto 객체로 modelmapper를 통해 변환
        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.ItemImgMapper(itemImg);
            itemImgDtoList.add(itemImgDto);
        }
        // 상품 id를 통해 상품 엔티티 객체 가져옴
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

        // 상품 엔티티 객체를 상품 Dto 객체로 modelmapper를 통해 변환 후 반환
        ItemFormDto itemFormDto = ItemFormDto.ItemMapper(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }
}
