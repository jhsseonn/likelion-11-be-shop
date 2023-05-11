package com.likelion.beshop.controller;

import com.likelion.beshop.dto.ItemFormDto;
import com.likelion.beshop.entity.Member;
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
@RequestMapping("")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping(value="/admin/item/new")
    public String itemForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }
    @PostMapping(value="/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model,
                          @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {
        if (bindingResult.hasErrors()){
            return "item/itemForm";
        }
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫 번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }
        try {
            //ItemService에 만들었던 saveItem 호출해서 itemId 반환
            Long itemId = itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            //에러 발생 시 에러메세지 반환 후 폼으로 리다이렉트"
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생했습니다.");
            return "item/itemForm";
        }
        return "redirect:/";
    }

    @GetMapping(value="/item/{itemId}")
    public String itemDtl(Model model, @PathVariable Long itemId){
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("item", itemFormDto);
        return "item/itemDtl.html";
    }

}
