package com.likelion.beshop.service;

import com.likelion.beshop.dto.ItemFormDto;
import com.likelion.beshop.entity.Item;
import com.likelion.beshop.entity.ItemImg;
import com.likelion.beshop.repository.ItemImgRepository;
import com.likelion.beshop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    private final ItemImgService itemImgService;

    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{

        // ItemFormDto로 입력받은 데이터를 Item에 받아와서 상품 데이터 저장
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        for(int i=0;i<itemImgFileList.size();i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);

            // 첫 번째 이미지일 경우, 대표 상품 이미지 여부 값을 Y로 설정, 이외는 N으로 설정
            if(i == 0)
                itemImg.setRepImage("Y");
            else
                itemImg.setRepImage("N");

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i)); // 상품 이미지 정보 저장
        }

        return item.getCode(); // 상품의 Code(ID) 값 리턴
    }
}