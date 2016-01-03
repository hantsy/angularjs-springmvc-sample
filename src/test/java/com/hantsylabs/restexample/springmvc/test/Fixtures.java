package com.hantsylabs.restexample.springmvc.test;

import com.hantsylabs.restexample.springmvc.domain.Post;
import com.hantsylabs.restexample.springmvc.model.PostForm;

/**
 *
 * @author hantsy
 */
public class Fixtures {

    public static Post createPost(String title, String content) {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);

        return post;
    }

    public static PostForm createPostForm(String title, String content) {
        PostForm post = new PostForm();
        post.setTitle(title);
        post.setContent(content);

        return post;
    }

}
