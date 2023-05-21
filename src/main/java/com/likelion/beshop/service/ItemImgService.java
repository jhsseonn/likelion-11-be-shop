package com.likelion.beshop.service;

import com.likelion.beshop.dto_.ItemFormDto;
import com.likelion.beshop.entity.Item;
import com.likelion.beshop.entity.ItemImg;
import com.likelion.beshop.repository.ItemImgRepository;
import com.likelion.beshop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import java.util.List;

import static com.likelion.beshop.entity.QItemImg.itemImg;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemImgService {

//    @Autowired
//    ItemImgRepository itemImgRepository;
//    @Autowired
//    FileService fileService;

    private final ItemImgRepository itemImgRepository;
    private final FileService fileService;

    private final ItemRepository itemRepository;

    @Value("${itemImgLocation}")//어노테이션을 사용하여 application.properties 파일에서 uploadPath 값을 읽어와서 String 변수인 uploadPath에 할당
    private String itemImgLocation;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile)throws Exception{ //itemImg,MultipartFile 객체를 매개변수로, 예외를 던질 수 있음
        //itemImg 객체는 이미지와 관련된 데이터 포함, itemImgFile 객체는 실제 이미지 파일 의미
        // 이렇게 저장된 itemImg 객체는 나중에 데이터베이스에 저장됨

        String oriImgName = itemImgFile.getOriginalFilename(); //원래의 파일명 받아오기
        String imgName ="";
        String imgUrl ="";

        if(!StringUtils.isEmpty(oriImgName)){ //상품의 이미지가 존재하지 않는다면 건너뛴다, 스트링유틸 ->타임리프로
            imgName = fileService.uploadFile(itemImgLocation,oriImgName,itemImgFile.getBytes());
            imgUrl = "/images/lionimage/" + imgName;
        }

        System.out.println("update 안됨");
        itemImg.updateItemImg(oriImgName,imgName,imgUrl);
        System.out.println("update 됨");
        itemImgRepository.save(itemImg);
    }
    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception{
        if(!itemImgFile.isEmpty()){
        ItemImg savedItemImg = itemImgRepository.findById(itemImgId).orElseThrow(EntityNotFoundException::new);

            if(!StringUtils.isEmpty(savedItemImg.getImgName())){
                fileService.deleteFile(itemImgLocation+"/"+savedItemImg.getImgName());
            }

        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = fileService.uploadFile(itemImgLocation,oriImgName,itemImgFile.getBytes());
        String imgUrl = "/images/lionimage/"+imgName;

        savedItemImg.updateItemImg(oriImgName, imgName,imgUrl);
        }
    }


}