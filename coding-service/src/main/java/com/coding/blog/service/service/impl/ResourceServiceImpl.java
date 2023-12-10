package com.coding.blog.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coding.blog.common.enumapi.StatusEnum;
import com.coding.blog.common.util.ExceptionUtil;
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
    @Override
    public boolean saveResource(Resource resource) {
        //查询该名称跟路径是否存在
        if (resourceMapper.selectCount(
                new QueryWrapper<Resource>().eq("name",resource.getName())
                        .or()
                        .eq("url",resource.getUrl())) > 0) {
            ExceptionUtil.of(StatusEnum.SYSTEM_RESOURCE_EXISTS);
        }
        resource.setCreateTime(LocalDateTime.now());

        return save(resource);
    }

    @Override
    public boolean deleteResource(Long resourceId) {
        if (relationMapper.selectCount(
                new QueryWrapper<RoleResourceRelation>().eq("resource_id",resourceId)) > 0) {
            ExceptionUtil.of(StatusEnum.SYSTEM_DATA_USE);
        }

        return resourceMapper.deleteById(resourceId) > 0;
    }

    @Override
    public IPage<Resource> queryListAll(Integer pageNum, Integer pageSize) {
        Page<Resource> page = new Page<>(pageNum,pageSize);
        return resourceMapper.selectPage(page,null);
    }
}
