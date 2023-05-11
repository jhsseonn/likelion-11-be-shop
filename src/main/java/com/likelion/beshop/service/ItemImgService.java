package com.likelion.beshop.service;

import com.likelion.beshop.entity.ItemImg;
import com.likelion.beshop.repository.ItemImgRepository;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemImgService {
    @Value("${itemImgLocation}") // properties 파일에 등록해둔 itemImgLocation 값 변수에 저장
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    // 상품 이미지 업로드 메소드
    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename(); // 원래 파일명 받아오기
        String imgName = "";
        String imgUrl = "";

        // 파일 upload
        if (!StringUtils.isEmpty(oriImgName)) {
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());  // FileService이용해 파일 업로드, 해당 업로드한 파일 이름을 imgName에 저장
            imgUrl = "/images/item/" + imgName;// 저장한 상품 이미지를 불러올 경로 설정
        }

        // (입력받은) 상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);    //itemImg에 저장
        itemImgRepository.save(itemImg);    //itemImgRepository에 저장

    }

}
