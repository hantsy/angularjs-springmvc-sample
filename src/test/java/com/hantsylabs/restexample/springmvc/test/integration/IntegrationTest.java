package com.hantsylabs.restexample.springmvc.test.integration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.hantsylabs.restexample.springmvc.domain.Post;
import com.hantsylabs.restexample.springmvc.model.PostForm;
import com.hantsylabs.restexample.springmvc.test.Fixtures;

@RunWith(BlockJUnit4ClassRunner.class)
public class IntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(IntegrationTest.class);

    private static final String BASE_URL = "http://localhost:8080/angularjs-springmvc-sample/";

    private RestTemplate template;

    @BeforeClass
    public static void init() {
        log.debug("==================before class=========================");
    }

    @Before
    public void beforeTestCase() {
        log.debug("==================before test case=========================");
        template = new BasicAuthRestTemplate("admin", "test123");
    }

    @After
    public void afterTestCase() {
        log.debug("==================after test case=========================");
    }

    @Test
    public void testPostCrudOperations() throws Exception {
        PostForm newPost = Fixtures.createPostForm("My first post", "content of my first post");
        String postsUrl = BASE_URL + "api/posts";

        ResponseEntity<Void> postResult = template.postForEntity(postsUrl, newPost, Void.class);
        assertTrue(HttpStatus.CREATED.equals(postResult.getStatusCode()));
        String createdPostUrl = postResult.getHeaders().getLocation().toString();
        assertNotNull("created post url should be set", createdPostUrl);

        ResponseEntity<Post> getPostResult = template.getForEntity(createdPostUrl, Post.class);
        assertTrue(HttpStatus.OK.equals(getPostResult.getStatusCode()));
        log.debug("post @" + getPostResult.getBody());
        assertTrue(getPostResult.getBody().getTitle().equals(newPost.getTitle()));

        ResponseEntity<Void> deleteResult = template.exchange(createdPostUrl, HttpMethod.DELETE, null, Void.class);
        assertTrue(HttpStatus.NO_CONTENT.equals(deleteResult.getStatusCode()));

    }

    @Test
    public void noneExistingPost() throws Exception {
        String noneExistingPostUrl = BASE_URL + "api/posts/1000";
        try {
            template.getForEntity(noneExistingPostUrl, Post.class);
        } catch (HttpClientErrorException e) {
            assertTrue(HttpStatus.NOT_FOUND.equals(e.getStatusCode()));
        }
    }

}
