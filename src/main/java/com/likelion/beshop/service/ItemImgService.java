package com.likelion.beshop.service;

import com.likelion.beshop.entity.ItemImg;
import com.likelion.beshop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

//어노테이션은 Member와 동일
@Service
@Transactional
@RequiredArgsConstructor
public class ItemImgService {
    // @Value 어노테이션으로 application.properties 파일에 등록해둔 itemImgLocation 값 변수에 저장
    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository; // ItemImgRepository 사용

    private final FileService fileService; // FileService 사용

    // 상품 이미지 업로드 메소드
    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename(); // 원래의 파일명 받아오기
        String imgName = "";
        String imgUrl = "";

        // 파일 upload
        if (!StringUtils.isEmpty(oriImgName)) {
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());  // FileService이용해 파일 업로드, 해당 업로드한 파일 이름을 imgName에 저장
            imgUrl = "/images/item/" + imgName; // 저장한 상품 이미지를 불러올 경로 설정 (uploadPath 경로 뒤에 덧붙여서 진행되는 경로 작성)
        }

        // 입력받은 상품 이미지 정보를 itemImg와 itemImgRepository에 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);

    }
}
