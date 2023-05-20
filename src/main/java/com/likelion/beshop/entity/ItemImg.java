package com.likelion.beshop.entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "item_img")
@Getter
@Setter
@ToString
public class ItemImg extends BaseEntity{

    @Id
    @Column(name="itemImg_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageName;

    private String originalImageName;

    private String imagePath;

    // 대표 이미지 여부 (”Y”인 경우, 메인 페이지에서 상품 보여줄 때 사용)
    private String repImage;

    @ManyToOne(fetch = FetchType.LAZY) // 상품 엔티티와 N:1 단방향 매핑, 지연 로딩으로 설정
    @JoinColumn(name = "item_id")
    private Item item;

    public void updateItemImg(String originalImageName, String imageName, String imagePath) {
        this.originalImageName = originalImageName;
        this.imageName = imageName;
        this.imagePath = imagePath;
    }
}

