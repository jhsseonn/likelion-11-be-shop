package com.likelion.beshop.Post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostListRepository extends JpaRepository<PostList, Long> {
    // 해당 인터페이스에 필요한 추가적인 메소드가 있다면 선언할 수 있습니다.
}
