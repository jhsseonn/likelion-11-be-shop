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
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";
        //파일 upload
        if (!StringUtils.isEmpty(oriImgName))
        {
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            imgUrl = "/images/item/" + imgName;
        }
        //상품 이미지 정보 저장
        itemImg.UpdateltemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }

    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {
        //만약 들어온 이미지가 있다면
        if (!itemImgFile.isEmpty()){
            //아이템이미지 조회
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
                    .orElseThrow(EntityNotFoundException::new);
            //아이템이미지가 이미 존재한다면 삭제
            if(!StringUtils.isEmpty(savedItemImg.getOriginalFileName())){
                fileService.deleteFile(savedItemImg.getImagePath());
            }
            //saveItemImg에서 저장했던 로직과 똑같이 파일 이름은 난수로 저장
            String imgName = fileService.uploadFile(itemImgLocation, savedItemImg.getOriginalFileName(), itemImgFile.getBytes());
            String imgUrl = "/images/item/" + imgName;
            //조회한 아이템이미지를 데이터베이스에 반영
            savedItemImg.UpdateltemImg(savedItemImg.getOriginalFileName(), imgName, imgUrl);
        }
    }
}
