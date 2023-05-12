package com.likelion.beshop.controller;

import com.likelion.beshop.dto.ItemFormDto;
import com.likelion.beshop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList){ // 이미지들의 리스트를 받아오기 위한 파라미터 추가

        if(bindingResult.hasErrors()){ // 검증 오류 발생 시
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getCode() == null){ // 상품 등록 시, 첫 번째 이미지가 없다면
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        try { // 상품 저장 로직 호출하여 매개변수로 상품 정보와 상품 이미지 정보를 담고 있는 itemFormDto와 itemImgFileList를 넘겨줌
            itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm"; // 상품 등록 페이지로 전환
        }

        return "redirect:/"; // 정상 등록 시, 메인 페이지로 이동 = /(root)로 리다이렉트
    }

    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model) { // 상품의 ID에 따라 경로가 달라져야하므로 그에 맞는 어노테이션과 함께 매개변수 설정

        ItemFormDto itemFormDto = itemService.getItemDtl(itemId); // 상품의 ID로 상품 데이터 불러오기
        model.addAttribute("item", itemFormDto); // 모델에 불러온 상품 데이터를 item이라는 이름으로 담아 데이터 넘겨주기
        return "item/itemDtl";
    }
}
