package com.likelion.beshop.repository;

import com.likelion.beshop.constant.ItemSellStatus;
import com.likelion.beshop.dto.ItemSearchDto;
import com.likelion.beshop.entity.Item;
import com.likelion.beshop.entity.QItem;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {
    private JPAQueryFactory queryFactory;

    // 생성자를 통해 em 주입
    public ItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    // 상품 판매/품절 상태를 조건으로 검색
    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    // 상품 등록일 조건으로 검색
    private BooleanExpression regDtsAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now(); // 현재 시간 불러오기

        if(StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if (StringUtils.equals("1d", searchDateType)) {
            dateTime = dateTime.minusDays(1);
        } else if (StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        } else if (StringUtils.equals("1m", searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        } else if (StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(6);
        }

        // dateTime 보다 이후에 등록한 상품만 검색
        return QItem.item.regTime.after(dateTime);
    }

    // 상품명 or 상품 생성자 아이디를 통해 검색
    // searchBy: 어떤 유형으로 조회할지 선택하는 필드
    // searchQuery: 조회할 검색어 저장 필드
    private BooleanExpression searchByLike(String searchBy, String searchQuery) {

        if (StringUtils.equals("itemName", searchBy)) { // 상품명으로 검색
            return QItem.item.itemName.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("createdBy", searchBy)) { // 생성자 아이디로 검색
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        }
        return null;
    }

    // 검색 페이지 메소드 오버라이드
    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        List<Item> content = queryFactory
                    .selectFrom(QItem.item) // item 객체를 대상으로
                    .where(searchSellStatusEq(itemSearchDto.getSearchSellStatus()), // 상품 판매 상태로 검색
                            regDtsAfter(itemSearchDto.getSearchDateType()), // 상품 등록일로 검색
                            searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery())) // 상품명 or 생성자 아이디로 검색
                    .orderBy(QItem.item.id.desc()) // 상품 id를 기준으로 내림차순 나열한 것에서 조회
                    .offset(pageable.getOffset()) // 데이터를 가져올 시작 인덱스 지정
                    .limit(pageable.getPageSize()) // 한번에 가져올 최대 개수 지정
                    .fetch();

        // 조회 대상이 한 건이면 해당 타입 반환
        long total = queryFactory
                .select(Wildcard.count)
                .from(QItem.item)
                .where(searchSellStatusEq(itemSearchDto.getSearchSellStatus()), // 상품 판매 상태로 검색
                        regDtsAfter(itemSearchDto.getSearchDateType()), // 상품 등록일로 검색
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery())) // 상품명 or 생성자 아이디로 검색
                .fetchOne();

        // 최종적으로 Page 클래스의 구현체인 PageImpl 객체 반환
        return new PageImpl<>(content, pageable, total);
    }

}
