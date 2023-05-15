package com.likelion.beshop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="item_img")
@Getter
@Setter
@ToString
public class ItemImg extends BaseEntity{
    // 아이디
    @Id
    @Column(name="item_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // 이미지 파일명
    private String imgName;

    // 원본 이미지 파일명
    private String oriImgName;

    // 이미지 조회 경로
    private String imgUrl;

    // 대표 이미지 여부
    private String repimgYn;

    // 상품 Entity와 N : 1 단방향 매핑
    @ManyToOne(fetch = FetchType.LAZY) //지연 로딩 설정
    @JoinColumn(name = "item_id")
    private Item item;

    // 원본 이미지 파일명, 업뎃할 이미지 파일명, 이미지 경로를 파라미터로 입력받아서 이미지 정보를 업뎃하는 메소드
    public void updateItemImg(String oriImgName, String imgName, String imgUrl) {
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }


}
