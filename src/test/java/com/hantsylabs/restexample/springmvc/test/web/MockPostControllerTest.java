package com.hantsylabs.restexample.springmvc.test.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hantsylabs.restexample.springmvc.api.post.PostController;
import com.hantsylabs.restexample.springmvc.domain.Post;
import com.hantsylabs.restexample.springmvc.exception.ResourceNotFoundException;
import com.hantsylabs.restexample.springmvc.model.PostDetails;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.hantsylabs.restexample.springmvc.model.PostForm;
import com.hantsylabs.restexample.springmvc.service.BlogService;
import com.hantsylabs.restexample.springmvc.test.Fixtures;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import static org.hamcrest.CoreMatchers.hasItem;
import org.junit.AfterClass;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@RunWith(MockitoJUnitRunner.class)
public class MockPostControllerTest {

    private static final Logger log = LoggerFactory.getLogger(MockPostControllerTest.class);

    private MockMvc mvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private BlogService blogService;

    @Mock
    Pageable pageable = mock(PageRequest.class);

    @InjectMocks
    PostController postController;

    @BeforeClass
    public static void beforeClass() {
        log.debug("==================before class=========================");
    }

    @AfterClass
    public static void afterClass() {
        log.debug("==================after class=========================");
    }

    @Before
    public void setup() {
        log.debug("==================before test case=========================");
        Mockito.reset();
        MockitoAnnotations.initMocks(this);
        mvc = standaloneSetup(postController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers(new ViewResolver() {
                    @Override
                    public View resolveViewName(String viewName, Locale locale) throws Exception {
                        return new MappingJackson2JsonView();
                    }
                })
                .build();
    }

    @After
    public void tearDown() {
        log.debug("==================after test case=========================");
    }

    @Test
    public void savePost() throws Exception {
        PostForm post = Fixtures.createPostForm("First Post", "Content of my first post!");

        when(blogService.savePost(any(PostForm.class))).thenAnswer(new Answer<PostDetails>() {
            @Override
            public PostDetails answer(InvocationOnMock invocation) throws Throwable {
                PostForm fm = (PostForm) invocation.getArgumentAt(0, PostForm.class);

                PostDetails result = new PostDetails();
                result.setId(1L);
                result.setTitle(fm.getTitle());
                result.setContent(fm.getContent());
                result.setCreatedDate(new Date());

                return result;
            }
        });
        mvc.perform(post("/api/posts").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isCreated());

        verify(blogService, times(1)).savePost(any(PostForm.class));
        verifyNoMoreInteractions(blogService);
    }

    @Test
    public void retrievePosts() throws Exception {
        PostDetails post1 = new PostDetails();
        post1.setId(1L);
        post1.setTitle("First post");
        post1.setContent("Content of first post");
        post1.setCreatedDate(new Date());

        PostDetails post2 = new PostDetails();
        post2.setId(2L);
        post2.setTitle("Second post");
        post2.setContent("Content of second post");
        post2.setCreatedDate(new Date());

        when(blogService.searchPostsByCriteria(anyString(), any(Post.Status.class), any(Pageable.class)))
                .thenReturn(new PageImpl(Arrays.asList(post1, post2), new PageRequest(0, 10, Direction.DESC, "createdDate"), 2));

        MvcResult response = mvc.perform(get("/api/posts?q=test&page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].id", hasItem(1)))
                .andExpect(jsonPath("$.content[*].title", hasItem("First post")))
                .andReturn();

        verify(blogService, times(1))
                .searchPostsByCriteria(anyString(), any(Post.Status.class), any(Pageable.class));
        verifyNoMoreInteractions(blogService);
        log.debug("get posts result @" + response.getResponse().getContentAsString());
    }

    @Test
    public void retrieveSinglePost() throws Exception {

        PostDetails post1 = new PostDetails();
        post1.setId(1L);
        post1.setTitle("First post");
        post1.setContent("Content of first post");
        post1.setCreatedDate(new Date());

        when(blogService.findPostById(1L)).thenReturn(post1);

        mvc.perform(get("/api/posts/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("id").isNumber());

        verify(blogService, times(1)).findPostById(1L);
        verifyNoMoreInteractions(blogService);
    }

    @Test
    public void removePost() throws Exception {
        when(blogService.deletePostById(1L)).thenReturn(true);
        mvc.perform(delete("/api/posts/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(blogService, times(1)).deletePostById(1L);
        verifyNoMoreInteractions(blogService);
    }

    @Test()
    public void notFound() {
        when(blogService.findPostById(1000L)).thenThrow(new ResourceNotFoundException(1000L));
        try {
            mvc.perform(get("/api/posts/1000").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (Exception ex) {
            log.debug("exception caught @" + ex);
        }
    }

}
