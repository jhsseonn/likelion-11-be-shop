package com.likelion.beshop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="item_img")
@Getter
@Setter
public class ItemImg {
    @Id
    @Column(name="item_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String imgName;
    private String originalImgName;
    private String imgPath;
    private String repImg;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    public void updateItemImg(String originalImgName, String imgName, String imgPath) {
        this.originalImgName = originalImgName;
        this.imgName = imgName;
        this.imgPath = imgPath;
    }
}