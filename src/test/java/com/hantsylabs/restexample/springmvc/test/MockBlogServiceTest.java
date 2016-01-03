package com.hantsylabs.restexample.springmvc.test;

import com.hantsylabs.restexample.springmvc.domain.Post;
import com.hantsylabs.restexample.springmvc.exception.ResourceNotFoundException;
import com.hantsylabs.restexample.springmvc.model.PostDetails;
import com.hantsylabs.restexample.springmvc.model.PostForm;
import com.hantsylabs.restexample.springmvc.repository.CommentRepository;
import com.hantsylabs.restexample.springmvc.repository.PostRepository;
import com.hantsylabs.restexample.springmvc.service.BlogService;
import javax.inject.Inject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MockDataConfig.class})
public class MockBlogServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(MockBlogServiceTest.class);

    @Inject
    private PostRepository postRepository;

    @Inject
    private CommentRepository commentRepository;

    private BlogService blogService;

    public MockBlogServiceTest() {
    }

    @Before
    public void setUp() {
        blogService = new BlogService(postRepository, commentRepository);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSavePost() {
        PostForm form = new PostForm();
        form.setTitle("saving title");
        form.setContent("saving content");

        PostDetails details = blogService.savePost(form);

        LOG.debug("post details @" + details);
        assertNotNull("saved post id should not be null@", details.getId());
        assertTrue(details.getId() == 1L);

        Page<PostDetails> posts = blogService.searchPostsByCriteria("any keyword", Post.Status.DRAFT, new PageRequest(0, 10));

        assertTrue(posts.getTotalPages() == 1);
        assertTrue(!posts.getContent().isEmpty());
        assertTrue(posts.getContent().get(0).getId() == 1L);
        
        PostForm updatingForm = new PostForm();
        updatingForm.setTitle("updating title");
        updatingForm.setContent("updating content");
        PostDetails updatedDetails = blogService.updatePost(1L, updatingForm);
        
        assertNotNull(updatedDetails.getId());
        assertTrue("updating title".equals(updatedDetails.getTitle()));
        assertTrue("updating content".equals(updatedDetails.getContent()));

    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetNoneExistingPost() {
        blogService.findPostById(1000L);
    }

}
