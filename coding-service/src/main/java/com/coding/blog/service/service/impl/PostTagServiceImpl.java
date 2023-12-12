package com.coding.blog.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coding.blog.common.enumapi.StatusEnum;
import com.coding.blog.common.util.ExceptionUtil;
import com.coding.blog.service.entity.PostTag;
import com.coding.blog.service.entity.PostTagRelation;
import com.coding.blog.service.mapper.PostTagMapper;
import com.coding.blog.service.mapper.PostTagRelationMapper;
import com.coding.blog.service.service.IPostTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coding.blog.service.vo.PostTagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 标签表 服务实现类
 * </p>
 *
 * @author blockCloth
 * @since 2023-12-04
 */
@Service
public class PostTagServiceImpl extends ServiceImpl<PostTagMapper, PostTag> implements IPostTagService {

    @Autowired
    private PostTagMapper postTagMapper;
    @Autowired
    private PostTagRelationMapper relationMapper;

    @Override
    public boolean savePostTag(PostTag postTag) {
        if (postTagMapper.selectCount(
                new QueryWrapper<PostTag>().eq("description",postTag.getDescription())) > 0) {
            ExceptionUtil.of(StatusEnum.SYSTEM_DATA_USE);
        }

        return save(postTag);
    }

    @Override
    public boolean deletePostTag(Long postTagId) {
        //查看哪些地方引用了标签ID
        if (relationMapper.selectCount(
                new QueryWrapper<PostTagRelation>().eq("post_tag_id",postTagId)) > 0) {
            ExceptionUtil.of(StatusEnum.SYSTEM_DATA_USE);
        }

        return postTagMapper.deleteById(postTagId) > 0;
    }

    @Override
    public List<PostTag> queryAllPostTag() {
        return postTagMapper.selectList(null);
    }

    @Override
    public List<PostTagVo> queryPostByTagId(Long postTagId) {
        return relationMapper.queryPostByTagId(postTagId);
    }
}
