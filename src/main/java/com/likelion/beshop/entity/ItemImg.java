package com.likelion.beshop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="item_img")
@Getter @Setter @ToString
public class ItemImg extends BaseEntity {
    @Id
    @Column(name="item_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fileName;
    private String originalFileName;
    private String imagePath;
    private String imageMainYN;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    public void UpdateltemImg(String originalFile, String fileName, String imagePath)
    {
        this.originalFileName = originalFile;
        this.fileName = fileName;
        this.imagePath = imagePath;
    }
}
