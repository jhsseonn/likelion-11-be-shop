package com.likelion.beshop.service;

import com.likelion.beshop.dto.ItemFormDto;
import com.likelion.beshop.entity.Item;
import com.likelion.beshop.repository.ItemImgRepository;
import com.likelion.beshop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.UUID;

//파일 업로드 및 파일 삭제 클래스
@Service
@Log
@RequiredArgsConstructor
public class FileService {
    //파일 업로드(파일저장경로, 업로드된 파일의 원본 파일명, 업로드된 파일 데이터)
    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception{
        //랜덤한 고유 식별자 생성
        UUID uuid = UUID.randomUUID();
        //원본 파일명에서 확장자만 분리
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        //고유 식별자+확장자 새로 저장할 파일명 생성
        String savedFileName = uuid.toString() + extension;
        //파일 저장될 전체 경로 생성
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        //파일 스트림에 기록, 스트림 닫기
        fos.write(fileData);
        fos.close();
        return savedFileName;
    }
    //파일 삭제(파일경로)
    public void deleteFile(String filePath) throws Exception{
        File deleteFile = new File(filePath);
        //삭제할 파일이 존재하는지 확인함
        if(deleteFile.exists()) {
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}