package com.coding.blog.service.service.impl;

import com.coding.blog.service.entity.Site;
import com.coding.blog.service.mapper.SiteMapper;
import com.coding.blog.service.service.ISiteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 站点配置 服务实现类
 * </p>
 *
 * @author blockCloth
 * @since 2023-12-04
 */
@Service
public class SiteServiceImpl extends ServiceImpl<SiteMapper, Site> implements ISiteService {

}
