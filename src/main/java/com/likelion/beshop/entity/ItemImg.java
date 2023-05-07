package com.likelion.beshop.entity;
import com.likelion.beshop.constant.IsRepresentative;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "item_img")
@Getter
@Setter
@ToString
public class ItemImg {

    @Id
    @Column(name="itemImg_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageName;

    private String originalImageName;

    private String imagePath;

    // 대표 이미지 여부 (”Y”인 경우, 메인 페이지에서 상품 보여줄 때 사용)
    @Enumerated(EnumType.STRING) //STRING? boolean?
    private IsRepresentative isRepresentative;
    //private boolean isRepresentative;

    @ManyToOne(fetch = FetchType.LAZY) // 상품 엔티티와 N:1 단방향 매핑, 지연 로딩으로 설정
    private Item item;

    public void updateItemImg(String originalImageName, String newImageName, String imagePath) {
        this.setOriginalImageName(originalImageName);
        this.setImageName(newImageName);
        this.setImagePath(imagePath);
    }
}

