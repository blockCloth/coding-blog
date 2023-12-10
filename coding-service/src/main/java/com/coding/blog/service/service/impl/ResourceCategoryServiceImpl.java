package com.coding.blog.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coding.blog.service.entity.Resource;
import com.coding.blog.service.entity.ResourceCategory;
import com.coding.blog.service.mapper.ResourceCategoryMapper;
import com.coding.blog.service.mapper.ResourceMapper;
import com.coding.blog.service.service.IResourceCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 资源分类表 服务实现类
 * </p>
 *
 * @author blockCloth
 * @since 2023-12-04
 */
@Service
public class ResourceCategoryServiceImpl extends ServiceImpl<ResourceCategoryMapper, ResourceCategory> implements IResourceCategoryService {

    @Autowired
    private ResourceCategoryMapper resourceCategoryMapper;
    @Autowired
    private ResourceMapper resourceMapper;

    @Override
    public IPage<ResourceCategory> getListAll(Integer pageNum, Integer pageSize) {
        IPage<ResourceCategory> pages = new Page<>(pageNum,pageNum);
        return resourceCategoryMapper.selectPage(pages,null);
    }

    @Override
    public boolean saveResourceCategory(ResourceCategory resourceCategory) {
        if (resourceCategoryMapper.selectCount(
                new QueryWrapper<ResourceCategory>().eq("name",resourceCategory.getName())) > 0)
            return false;

        resourceCategory.setCreateTime(LocalDateTime.now());
        resourceCategory.setSort(0);
        return save(resourceCategory);
    }

    @Override
    public boolean deleteById(Integer resourceCategoryId) {
        //判断该分类是否被引用
        if (resourceMapper.selectCount(
                new QueryWrapper<Resource>().eq("category_id",resourceCategoryId)) > 0){
            //提示信息
            return false;
        }
        return resourceCategoryMapper.deleteById(resourceCategoryId) > 0;
    }
}
