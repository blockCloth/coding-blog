package com.coding.blog.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coding.blog.common.enumapi.RedisConstants;
import com.coding.blog.common.enumapi.StatusEnum;
import com.coding.blog.common.util.ExceptionUtil;
import com.coding.blog.common.util.RedisTemplateUtil;
import com.coding.blog.service.entity.Resource;
import com.coding.blog.service.entity.RoleResourceRelation;
import com.coding.blog.service.mapper.ResourceMapper;
import com.coding.blog.service.mapper.RoleResourceRelationMapper;
import com.coding.blog.service.service.IResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private RoleResourceRelationMapper relationMapper;
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

    @Override
    public boolean saveResource(Resource resource) {
        // 查询该名称跟路径是否存在
        if (resourceMapper.selectCount(
                new QueryWrapper<Resource>().eq("name", resource.getName())
                        .or()
                        .eq("url", resource.getUrl())) > 0) {
            ExceptionUtil.of(StatusEnum.SYSTEM_RESOURCE_EXISTS);
        }
        resource.setCreateTime(LocalDateTime.now());

        //删除缓存
        delResourceCache();
        return save(resource);
    }

    @Override
    public boolean deleteResource(Long resourceId) {
        if (relationMapper.selectCount(
                new QueryWrapper<RoleResourceRelation>().eq("resource_id", resourceId)) > 0) {
            ExceptionUtil.of(StatusEnum.SYSTEM_DATA_USE);
        }
        // 删除缓存
        delResourceCache();
        return resourceMapper.deleteById(resourceId) > 0;
    }

    @Override
    public IPage<Resource> queryListAll(Integer pageNum, Integer pageSize) {
        IPage<Resource> resourceCache =
                (IPage<Resource>) redisTemplateUtil.get(RedisConstants.REDIS_KEY_RESOURCE);
        if (resourceCache == null) {
            Page<Resource> page = new Page<>(pageNum, pageSize);
            Page<Resource> resourcePage = resourceMapper.selectPage(page, null);
            redisTemplateUtil.set(RedisConstants.REDIS_KEY_RESOURCE,resourcePage);
            resourceCache = resourcePage;
        }
        return resourceCache;
    }

    @Override
    public void delResourceCache(){
        redisTemplateUtil.del(RedisConstants.REDIS_KEY_RESOURCE);
        redisTemplateUtil.del(RedisConstants.REDIS_KEY_RESOURCE_ADMIN);
    }
}
