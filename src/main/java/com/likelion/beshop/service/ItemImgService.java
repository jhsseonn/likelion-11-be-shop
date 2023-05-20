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

    // 상품 이미지 저장
    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String originalImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgPath = "";

        // 파일 업로드
        if (!StringUtils.isEmpty(originalImgName)) {
            imgName = fileService.uploadFile(itemImgLocation, originalImgName, itemImgFile.getBytes());
            imgPath = "/images/item/" + imgName;
        }

        // 상품 이미지 정보 저장
        itemImg.updateItemImg(originalImgName, imgName, imgPath);
        itemImgRepository.save(itemImg);
    }

    // 아이템 이미지 수정
    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {
        if (!itemImgFile.isEmpty()) {
            // 상품 id를 통해 상품 이미지 조회한 후 itemImg 변수에 저장
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId).orElseThrow(EntityNotFoundException::new);

            // 기존 등록된 상품 이미지가 있는 경우 삭제
            if (!StringUtils.isEmpty(savedItemImg.getOriginalImgName())) {
                fileService.deleteFile(savedItemImg.getImgPath());
            }

            // 수정하고자 하는 상품 이미지 정보 세팅(업데이트)
            String originalImgName = savedItemImg.getOriginalImgName();
            String imgName = fileService.uploadFile(itemImgLocation, originalImgName, itemImgFile.getBytes());
            String imgPath = "/images/item/" + imgName;

            savedItemImg.updateItemImg(originalImgName, imgName, imgPath);
        }
    }
}
