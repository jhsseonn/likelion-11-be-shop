package com.likelion.beshop.Post;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;


    //게시글 등록
    public Post createPost(PostFromDto postFromDto) throws Exception {
        Post post = new Post();
        post.setTitle(postFromDto.getTitle());
        post.setBody(postFromDto.getBody());
        post.setAuthor(postFromDto.getAuthor());
        post.setStatus(postFromDto.getStatus());
        post.setDate(LocalDateTime.now());

        return postRepository.save(post);
    }



    //게시글 수정한 뒤 수정한 게시글 아이디 반환
    public Long updatePost(PostFromDto postFromDto) {
        Post post = postRepository.findById(postFromDto.getId()).orElseThrow(EntityNotFoundException::new);
        post.updatePost(postFromDto);

        return post.getId();
    }

    public void deletePost(PostFromDto postFromDto) {
        Post post = postRepository.findById(postFromDto.getId()).orElseThrow(EntityNotFoundException::new);
        postRepository.deleteById(post.getId());
    }

    //게시글 전체 조회, DTO로 반환함
    @Transactional(readOnly = true)
    public List<PostListDto> getAllPosts(PostStatus postStatus) {
        List<Post> posts;
        if (postStatus != null) {
            posts = postRepository.findByStatus(postStatus);
        } else {
            posts = postRepository.findAll();
        }

        return posts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    //게시글 상세 조회
    @Transactional(readOnly = true)
    public PostReturnDto getPostDetails(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(EntityNotFoundException::new);
        return convertToDtoDtl(post);
    }

    //게시글 상세 조회를 위한거
    private PostReturnDto convertToDtoDtl(Post post) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(post, PostReturnDto.class);
    }

//게시글 전체 조회를 위한거
    private PostListDto convertToDto(Post post) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(post, PostListDto.class);
    }

    //게시글 필터링



}
