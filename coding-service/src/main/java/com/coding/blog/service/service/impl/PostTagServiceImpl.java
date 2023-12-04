package com.coding.blog.service.service.impl;

import com.coding.blog.service.entity.PostTag;
import com.coding.blog.service.mapper.PostTagMapper;
import com.coding.blog.service.service.IPostTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
