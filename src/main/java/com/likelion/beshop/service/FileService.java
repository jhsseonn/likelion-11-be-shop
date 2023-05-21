package com.likelion.beshop.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service // 서비스 구성 요소이며 Spring 프레임워크에 의해 자동으로 감지되고 구성되어야 함을 나타냄
@Log
public class FileService {

    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception{
        UUID uuid = UUID.randomUUID(); // 보편적으로 고유한 식별자를 생성하는 방법을 제공하는 표준 Java 클래스인 UUID 클래스를 사용하여 고유한 식별자를 생성
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")); // 하위 문자열 방법을 사용하여 원래 파일 이름의 확장자를 추출
        String savedFileName = uuid.toString() + extension; // UUID를 확장자와 연결하여 새 파일 이름을 만듦
        String fileUploadFullUrl = uploadPath + "/" + savedFileName; // 업로드할 파일의 전체 경로는 uploadPath를 새 파일 이름과 연결하여 구성함
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl); // 새로운 FileOutputStream 객체가 fileUploadFullUrl 경로로 생성
        fos.write(fileData); // 파일의 내용이 write 메서드를 사용하여 출력 스트림에 기록됨
        fos.close(); // 출력 스트림이 닫힘
        return savedFileName; // 파일이 저장된 후 파일 이름을 나타내는 문자열을 반환
    }

    public void deleteFile(String filePath) throws Exception{
        File deleteFile = new File(filePath);
        if(deleteFile.exists()) {
            deleteFile.delete();
            log.info("파일을 삭제하였습니다."); // 성공적으로 삭제되면 Lombok의 @Log 주석에서 log 메서드를 사용하여 메시지가 기록됨
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }

}