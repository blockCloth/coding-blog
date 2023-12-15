package com.coding.blog.service.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coding.blog.service.dto.PostsParam;
import com.coding.blog.service.entity.Posts;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coding.blog.service.vo.PostDetailVo;

import java.util.List;

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

    PostDetailVo queryPostDetailById(Long postId);

    IPage<Posts> queryPostsList(Integer pageNum, Integer pageSize);

    boolean insertPostToTerm(Long postId, Long termTaxonomyId);

    boolean insertPostToTags(Long postId, List<Long> tagsId);

    boolean updatePostToTerm(Long postId, Long termTaxonomyId);

    boolean updatePostToTags(Long postId, List<Long> tagsId);

    boolean deletePostToTerm(Long postId);

    boolean deletePostToTags(Long postId);
}
