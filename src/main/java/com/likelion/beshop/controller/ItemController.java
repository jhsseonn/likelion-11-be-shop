package com.likelion.beshop.controller;

import com.likelion.beshop.dto.ItemFormDto;
import com.likelion.beshop.dto.MemberFormDto;
import com.likelion.beshop.entity.Member;
import com.likelion.beshop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/admin")
@Controller
@RequiredArgsConstructor
public class ItemController {
    @GetMapping(value = "/item/new")
    public String itemForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }

    @PostMapping(value = "/item/new")
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
            ItemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm"; // 상품 등록 페이지로 전환
        }

        return "redirect:/"; // 정상 등록 시, 메인 페이지로 이동 = /(root)로 리다이렉트
    }
}
