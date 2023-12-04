package com.coding.blog.service.service.impl;

import com.coding.blog.service.entity.Posts;
import com.coding.blog.service.mapper.PostsMapper;
import com.coding.blog.service.service.IPostsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章 服务实现类
 * </p>
 *
 * @author blockCloth
 * @since 2023-12-04
 */
@Service
public class PostsServiceImpl extends ServiceImpl<PostsMapper, Posts> implements IPostsService {

}
