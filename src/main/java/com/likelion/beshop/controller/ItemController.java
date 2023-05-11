package com.likelion.beshop.controller;

import com.likelion.beshop.dto.ItemFormDto;
import com.likelion.beshop.dto.MemberFormDto;
import com.likelion.beshop.repository.ItemRepository;
import com.likelion.beshop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/admin/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }

    @PostMapping("/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList){
        if(bindingResult.hasErrors()){//검증 오류 발생
            return "item/itemForm";
        }
        //상품 등록 시, 첫 번째 이미지가 없을 경우
        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫 번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm"; //에러 발생 - 상품 등록 페이지로 전환
        }
        // 상품 저장 로직 호출 - 매개변수로 상품 정보,이미지 정보를 담고있는 itemFormDto, itemImgFileList 넘겨줌
        try{
            itemService.saveItem(itemFormDto, itemImgFileList);
        }catch (Exception e){ //상품 등록 중 에러 발생
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm"; //에러 발생 - 상품 등록 페이지로 전환
        }
        return "redirect:/"; //정상 등록 시 메인페이지로 이동(리다이렉트)
    }

    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(Model model,  @PathVariable("itemId") Long itemId){
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("item",itemFormDto);
        return "item/itemDtl";
    }
}
