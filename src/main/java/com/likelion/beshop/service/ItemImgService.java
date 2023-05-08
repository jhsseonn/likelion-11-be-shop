package com.likelion.beshop.service;

import com.likelion.beshop.entity.ItemImg;
import com.likelion.beshop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemImgService {
    @Value("${itemImgLocation}")
    private String itemImgLocation;
    private final ItemImgRepository itemImgRepository;
    private final FileService fileService;

    //상품 이미지 upload
    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String oriImgName = itemImg.getOriginalFileName();
        String imgName = "";
        String imgUrl = "";
        //파일 upload
        if (!StringUtils.isEmpty(oriImgName))
        {
            imgName=fileService.uploadFile(itemImgLocation, oriImgName, );
            imgUrl="/images/item/" +
        }
        //상품 이미지 정보 저장

    }


}
