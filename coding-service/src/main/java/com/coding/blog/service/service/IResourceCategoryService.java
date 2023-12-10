package com.coding.blog.service.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coding.blog.service.entity.ResourceCategory;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 资源分类表 服务类
 * </p>
 *
 * @author blockCloth
 * @since 2023-12-04
 */
public interface IResourceCategoryService extends IService<ResourceCategory> {

    IPage<ResourceCategory> getListAll(Integer pageNum, Integer pageSize);

    boolean saveResourceCategory(ResourceCategory resourceCategory);

    boolean deleteById(Integer resourceCategoryId);

}
