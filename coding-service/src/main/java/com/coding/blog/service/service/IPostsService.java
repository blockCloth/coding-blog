package com.coding.blog.service.service;

import com.coding.blog.service.dto.PostsParam;
import com.coding.blog.service.entity.Posts;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 文章 服务类
 * </p>
 *
 * @author blockCloth
 * @since 2023-12-04
 */
public interface IPostsService extends IService<Posts> {

    boolean savePost(PostsParam postsParam);

    boolean updatePost(PostsParam postsParam);

    boolean deletePostById(Long postId);
}
