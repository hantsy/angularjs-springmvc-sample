package com.hantsylabs.restexample.springmvc.test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;

import org.mockito.invocation.InvocationOnMock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.hantsylabs.restexample.springmvc.domain.Post;
import com.hantsylabs.restexample.springmvc.exception.ResourceNotFoundException;
import com.hantsylabs.restexample.springmvc.repository.CommentRepository;
import com.hantsylabs.restexample.springmvc.repository.PostRepository;
import com.hantsylabs.restexample.springmvc.service.BlogService;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@Configuration
public class MockDataConfig {

    @Bean
    public PostRepository postRepository() {
        final Post post = createPost();
        PostRepository posts = mock(PostRepository.class);
        when(posts.save(any(Post.class))).thenAnswer((InvocationOnMock invocation) -> {
            Object[] args = invocation.getArguments();
            Post result = (Post) args[0];
            result.setId(post.getId());
            result.setTitle(post.getTitle());
            result.setContent(post.getContent());
            result.setCreatedDate(post.getCreatedDate());
            return result;
        });

        when(posts.findOne(1000L)).thenThrow(new ResourceNotFoundException(1000L));
        when(posts.findOne(1L)).thenReturn(post);
        when(posts.findAll(any(Specification.class), any(Pageable.class))).thenReturn(new PageImpl(Arrays.asList(post), new PageRequest(0, 10), 1L));
        when(posts.findAll()).thenReturn(Arrays.asList(post));
        return posts;
    }

    @Bean
    public CommentRepository commentRepository() {
        return mock(CommentRepository.class);
    }
    
    @Bean
    public BlogService blogService(PostRepository posts, CommentRepository comments){
        return new BlogService(posts, comments);
    }

    @Bean
    public Post createPost() {
        Post post = new Post();
        post.setCreatedDate(new Date());
        post.setId(1L);
        post.setTitle("First post");
        post.setContent("Content of my first post!");
        post.setCreatedDate(new Date());

        return post;
    }
}
