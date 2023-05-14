package com.likelion.beshop.service;
//상품 이미지 업로드, 상품 이미지 저장을 위한
import com.likelion.beshop.entity.ItemImg;
import com.likelion.beshop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;
    private final FileService fileService;

    //상품 이미지 업로드 메소드
    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception{

        //원래의 파일명 받아오기
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName="";
        String imgUrl="";

        //FileService 이용해 파일 업로드, 해당 업로드한 파일 이름을 imgName에 저장
        if(!StringUtils.isEmpty(oriImgName)){
            imgName = fileService.uploadFile(itemImgLocation, oriImgName,itemImgFile.getBytes());
            imgUrl = "/images/item/" + imgName;
        }

        //입력받은 상품 이미지 정보를 itemImg와 itemImgRepository에 저장
        itemImg.updateItemImg(oriImgName,imgName,imgUrl);
        itemImgRepository.save(itemImg);
    }

    //updateItemImg 메서드 추가
    //itemImgId, itemImgFile 매개변수로 받고 예외처리
    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception{
        if(!itemImgFile.isEmpty()) {
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId).orElseThrow(EntityNotFoundException::new);

            if (!StringUtils.isEmpty(savedItemImg.getImgName())) {
                fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName());
            }


            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            String imgUrl = "/images/item" + imgName;

            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
        }
    }

}
