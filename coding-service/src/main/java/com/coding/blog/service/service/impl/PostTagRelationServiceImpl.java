package com.coding.blog.service.service.impl;

import com.coding.blog.service.entity.PostTagRelation;
import com.coding.blog.service.mapper.PostTagRelationMapper;
import com.coding.blog.service.service.IPostTagRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 标签文章关系表 服务实现类
 * </p>
 *
 * @author blockCloth
 * @since 2023-12-04
 */
@Service
public class PostTagRelationServiceImpl extends ServiceImpl<PostTagRelationMapper, PostTagRelation> implements IPostTagRelationService {

}
