package com.hantsylabs.restexample.springmvc.test.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hantsylabs.restexample.springmvc.config.AppConfig;
import com.hantsylabs.restexample.springmvc.config.DataJpaConfig;
import com.hantsylabs.restexample.springmvc.config.DataSourceConfig;
import com.hantsylabs.restexample.springmvc.config.Jackson2ObjectMapperConfig;
import com.hantsylabs.restexample.springmvc.config.JpaConfig;
import com.hantsylabs.restexample.springmvc.config.WebConfig;
import com.hantsylabs.restexample.springmvc.domain.Post;
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
import com.hantsylabs.restexample.springmvc.repository.PostRepository;
import com.hantsylabs.restexample.springmvc.test.Fixtures;
import javax.inject.Inject;
import static org.hamcrest.CoreMatchers.is;
import org.junit.AfterClass;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, Jackson2ObjectMapperConfig.class, DataSourceConfig.class, JpaConfig.class, DataJpaConfig.class, WebConfig.class})
@WebAppConfiguration
public class PostControllerTest {

    private static final Logger log = LoggerFactory.getLogger(PostControllerTest.class);

    @Inject
    WebApplicationContext wac;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    private PostRepository postRepository;

    private MockMvc mvc;

    private Post post;

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
        mvc = webAppContextSetup(this.wac).build();

        postRepository.deleteAll();
        post = postRepository.save(Fixtures.createPost("My first post", "content of my first post"));
    }

    @After
    public void tearDown() {
        log.debug("==================after test case=========================");
    }

    @Test
    public void savePost() throws Exception {
        PostForm post = Fixtures.createPostForm("First Post", "Content of my first post!");

        mvc.perform(post("/api/posts").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isCreated());

    }

    @Test
    public void retrievePosts() throws Exception {

        MvcResult response = mvc.perform(get("/api/posts?q=first&page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", is(post.getId().intValue())))
                .andExpect(jsonPath("$.content[0].title", is("My first post")))
                .andReturn();

        log.debug("get posts result @" + response.getResponse().getContentAsString());
    }

    @Test
    public void retrieveSinglePost() throws Exception {

        mvc.perform(get("/api/posts/{id}", post.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title", is("My first post")));

    }

    @Test
    public void removePost() throws Exception {
        mvc.perform(delete("/api/posts/{id}", post.getId()))
                .andExpect(status().isNoContent());
    }

    @Test()
    public void notFound() {
        try {
            mvc.perform(get("/api/posts/1000").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (Exception ex) {
            log.debug("exception caught @" + ex);
        }
    }

}
