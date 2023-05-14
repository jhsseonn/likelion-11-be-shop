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


public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{
    private JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    };

    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
        return searchSellStatus == null ? null : QItem.item.status.eq(searchSellStatus); // searchSellStatus가 null일 경우 null 반환하고 null이 아닐 경우 판매중/품절 상태에 해당하는 상품만 조회
    }

    private BooleanExpression regDtsAfter(String searchDateType) {

        LocalDateTime dateTime = LocalDateTime.now();

        // searchDateType에 따라 반환 값 달라짐
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

        return QItem.item.regTime.after(dateTime); // 최종적으로 셋팅이 끝난 시간 리턴
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {

        // searchBy 값에 따라 조건값 반환
        if(StringUtils.equals("name", searchBy)) { // 상품명
            return QItem.item.name.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("createdBy", searchBy)) { // 상품 생성자 아이디
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {

        List<Item> content = queryFactory		// queryFactory 이용해 쿼리 생성
                .selectFrom(QItem.item) // 상품 데이터를 조회하기 위해 QItem의 item 지정
                .where(regDtsAfter(itemSearchDto.getSearchDateType()), // where 조건절: BooleanExpression 반환하는 조건문들
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()), // ',' 단위로 넣어줄 경우 and 조건으로 인식함
                        searchByLike(itemSearchDto.getSearchBy(),
                                itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc()) // 아이템 id 내림차순으로 나열
                .offset(pageable.getOffset()) // 데이터를 가지고 올 시작 인덱스 지정(pageable에서 get)
                .limit(pageable.getPageSize()) // 한번에 가지고 올 최대 개수 지정(pageable에서 get)
                .fetch(); // fetch()로 조회 대상 리스트로 반환

        // unboxing of may produce 'nullpointerexception' 경고로 long에서 Long으로 수정
        Long total = queryFactory.select(Wildcard.count).from(QItem.item) // queryFactory에서 where절로 해당하는 데이터 불러오기
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .fetchOne(); // fetchOne으로 조회 대상이 한 건이면 해당 타입 반환하고, 아니면 에러 발생 시키는 구문 추가

        return new PageImpl<>(content, pageable, total); // 최종적으로 new PageImpl<>(content, pageable, total) 반환

    }
}
