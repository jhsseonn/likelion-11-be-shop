package com.likelion.beshop.service;

import com.likelion.beshop.entity.ItemImg;
import com.likelion.beshop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

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

    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception{ // itemImgId, itemImgFile 매개변수로 받고 예외처리
        if(!itemImgFile.isEmpty()){ // 상품 이미지를 수정한 경우 상품 이미지 업데이트 조건 추가
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId).orElseThrow(EntityNotFoundException::new);
            if(!StringUtils.isEmpty(savedItemImg.getImageName())){
                fileService.deleteFile(itemImgLocation+"/"+savedItemImg.getImageName()); // 기존에 등록된 상품 이미지 파일이 있을 경우 해당 파일 삭제(fileService의 deleteFile 메서드 사용)
            }

            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes()); // 업데이트한 상품 이미지 파일 업로드
            String imgUrl = "/images/item/" + imgName;

            savedItemImg.updateItemImg(oriImgName,imgName,imgUrl); // 변경된 상품 이미지 정보 세팅(*savedItemImg의 upedateItemImg 메서드 사용
        }
    }
}
