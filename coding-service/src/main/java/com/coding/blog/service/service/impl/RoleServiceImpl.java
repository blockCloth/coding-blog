package com.coding.blog.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coding.blog.service.entity.Role;
import com.coding.blog.service.mapper.RoleMapper;
import com.coding.blog.service.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLDataException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 后台用户角色表 服务实现类
 * </p>
 *
 * @author blockCloth
 * @since 2023-12-04
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public boolean addRole(Role role) {
        // 查询此角色是否存在
        if (roleMapper.selectCount(
                new QueryWrapper<Role>().eq("name", role.getName())
        )> 0) {
            return false;
        }
        //设置创建时间以及角色状态
        role.setCreateTime(LocalDateTime.now());
        role.setStatus(1); //0表示禁用，1表示启用
        return save(role);
    }

    @Override
    @Transactional(rollbackFor = SQLDataException.class)
    public boolean deleteRoleBatch(List<Integer> ids) {
        return roleMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public IPage<Role> queryRolePages(Integer pageNum, Integer pageSize) {
        Page<Role> page = new Page<>(pageNum,pageSize);
        return roleMapper.selectPage(page,null);
    }
}
