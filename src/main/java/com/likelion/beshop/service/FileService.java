package com.likelion.beshop.service;
//이미지 파일 저장 로직을 담당할 Service 객체

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import java.io.File;
//FileOutputStream 클래스를 이용하여 파일을 저장
import java.io.FileOutputStream;
import java.util.UUID;
//UUID(Universally Unique IDentifier) - 서로 다른 개체들을 구별하기 위한 클래스

@Service
@Log
public class FileService {

    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception{
        //UUID를 이용하여 파일명 새로 작성
        UUID uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid.toString() + extension;     //파일명

        //경로 + 파일명
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;

        //FileOutputStream 객체를 이용하여 경로지정 후 파일 저장
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData);
        fos.close();

        return savedFileName;
    }

    //이미지 파일 삭제
    public void deleteFile(String filePath) throws Exception{
        File deleteFile = new File(filePath);
        if(deleteFile.exists()) {
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }

}
