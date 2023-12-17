package com.coding.blog.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coding.blog.common.enumapi.StatusEnum;
import com.coding.blog.common.enumapi.TermRelationType;
import com.coding.blog.common.util.ExceptionUtil;
import com.coding.blog.service.dto.PostsParam;
import com.coding.blog.service.entity.PostTag;
import com.coding.blog.service.entity.Posts;
import com.coding.blog.service.entity.TermRelationships;
import com.coding.blog.service.entity.TermTaxonomy;
import com.coding.blog.service.mapper.PostsMapper;
import com.coding.blog.service.mapper.TermRelationshipsMapper;
import com.coding.blog.service.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coding.blog.service.vo.PostDetailVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLDataException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 文章 服务实现类
 * </p>
 *
 * @author blockCloth
 * @since 2023-12-04
 */
@Slf4j
@Service
public class PostsServiceImpl extends ServiceImpl<PostsMapper, Posts> implements IPostsService {
    @Autowired
    private PostsMapper postsMapper;
    @Autowired
    private IUsersService usersService;
    @Autowired
    private ITermRelationshipsService termRelationshipsService;
    @Autowired
    private IPostTagRelationService postTagRelationService;



    @Override
    @Transactional(rollbackFor = SQLDataException.class)
    public boolean savePost(PostsParam postsParam) {
        Posts posts = new Posts();
        BeanUtils.copyProperties(postsParam,posts);

        // TODO 设置文章属性
        // TODO 是否开启定时任务
        // TODO 对文章图片进行转链

        //设置初始评论数
        posts.setCommentCount(0L);

        //设置创建用户
        posts.setPostAuthor(usersService.getCurrentUserId());

        //设置文章创建时间以及修改时间
        posts.setPostDate(LocalDateTime.now());
        posts.setPostModified(LocalDateTime.now());

        //设置默认浏览量
        posts.setPageView(0L);

        //保存文章
        save(posts);

        //保存专栏信息
        insertPostTermRelation(postsParam,posts);

        //保存标签信息
        insertOrUpdateTags(postsParam,posts);

        return true;
    }

    @Override
    @Transactional(rollbackFor = SQLDataException.class)
    public boolean updatePost(PostsParam postsParam) {
        if (postsParam.getPostsId() == null){
            log.warn("文章ID必须存在！");
            ExceptionUtil.of(StatusEnum.SYSTEM_POST_ID_NOT_EXISTS);
        }

        // 查询文章是否存在
        Posts selectPost = postsMapper.selectById(postsParam);
        if (selectPost == null){
            log.error("文章不存在，文章ID{}",postsParam.getPostsId());
            ExceptionUtil.of(StatusEnum.SYSTEM_POST_NOT_EXISTS);
        }

        Posts posts = new Posts();
        BeanUtils.copyProperties(postsParam,posts);

        // TODO 设置文章属性
        // TODO 是否开启定时任务
        // TODO 对文章图片进行转链

        //设置文章修改时间
        posts.setPostModified(LocalDateTime.now());

        //保存文章
        updateById(posts);

        //保存专栏信息
        insertPostTermRelation(postsParam,posts);

        //保存标签信息
        insertOrUpdateTags(postsParam,posts);

        return true;
    }

    @Override
    public boolean deletePostById(Long postId) {
        //先删除标签属性
        postTagRelationService.deletePostsTag(postId);
        //删除专栏信息
        termRelationshipsService.deleteTermRelationships(postId);

        // TODO 删除浏览量、点赞（还未做缓存）
        return removeById(postId);
    }

    @Override
    public PostDetailVo queryPostDetailById(Long postId) {
        PostDetailVo detailVo = new PostDetailVo();
        //查询文章信息
        Posts posts = postsMapper.selectById(postId);
        detailVo.setPosts(posts);
        //查询专栏信息
        TermTaxonomy termTaxonomy = termRelationshipsService.queryTermTaxonomyById(postId);
        if (termTaxonomy != null){
            detailVo.setTermTaxonomy(termTaxonomy);
        }
        //查询标签信息
        List<PostTag> postTags = postTagRelationService.queryPostTagsById(postId);
        if (CollUtil.isNotEmpty(postTags)){
            detailVo.setPostTagList(postTags);
        }
        return detailVo;
    }

    @Override
    public IPage<Posts> queryPostsList(Integer pageNum, Integer pageSize) {
        Page<Posts> page = new Page(pageNum,pageSize);
        return postsMapper.selectPage(page,null);
    }

    @Override
    public boolean insertPostToTerm(Long postId, Long termTaxonomyId) {
        return termRelationshipsService.insertOrUpdate(termTaxonomyId,postId);
    }

    @Override
    public boolean insertPostToTags(Long postId, List<Long> tagsId) {
        return postTagRelationService.insertOrUpdate(tagsId,postId);
    }

    @Override
    public boolean updatePostToTerm(Long postId, Long termTaxonomyId) {
        return termRelationshipsService.insertOrUpdate(termTaxonomyId,postId);
    }

    @Override
    public boolean updatePostToTags(Long postId, List<Long> tagsId) {
        return postTagRelationService.insertOrUpdate(tagsId,postId);
    }

    @Override
    public boolean deletePostToTerm(Long postId) {
        return termRelationshipsService.deleteTermRelationships(postId);
    }

    @Override
    public boolean deletePostToTags(Long postId) {
        return postTagRelationService.deletePostsTag(postId);
    }

    /**
     * 保存或修改标签信息
     */
    private boolean insertOrUpdateTags(PostsParam postsParam, Posts posts) {
        log.info("准备文章{}标签{}信息",posts.getPostsId(),postsParam.getTagIds());
        //判断IDS是否为空
        if (CollUtil.isEmpty(postsParam.getTagIds())){
            return false;
        }
        //准备修改标签信息
        return postTagRelationService.insertOrUpdate(postsParam.getTagIds(),posts.getPostsId());
    }

    /**
     * 保存文章专栏信息
     * @return
     */
    private boolean insertPostTermRelation(PostsParam postsParam, Posts posts) {
        //判断专栏ID是否为空
        if (postsParam.getTermTaxonomyId() == null){
            return false;
        }
        return termRelationshipsService.insertOrUpdate(postsParam.getTermTaxonomyId(),posts.getPostsId());
    }
}
