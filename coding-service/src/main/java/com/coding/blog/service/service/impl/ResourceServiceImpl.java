package com.coding.blog.service.service.impl;

import com.coding.blog.service.entity.Resource;
import com.coding.blog.service.mapper.ResourceMapper;
import com.coding.blog.service.service.IResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 后台资源表 服务实现类
 * </p>
 *
 * @author blockCloth
 * @since 2023-12-04
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements IResourceService {

}
