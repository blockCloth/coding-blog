package com.coding.blog.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coding.blog.common.enumapi.StatusEnum;
import com.coding.blog.common.util.ExceptionUtil;
import com.coding.blog.service.entity.*;
import com.coding.blog.service.mapper.*;
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
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private AdminRoleRelationMapper adminRoleRelationMapper;
    @Autowired
    private RoleMenuRelationMapper roleMenuRelationMapper;
    @Autowired
    private RoleResourceRelationMapper resourceRelationMapper;


    @Override
    public boolean addRole(Role role) {
        // 查询此角色是否存在
        if (roleMapper.selectCount(
                new QueryWrapper<Role>().eq("name", role.getName())) > 0) {
            ExceptionUtil.of(StatusEnum.SYSTEM_ROLE_EXISTS);
        }
        // 设置创建时间以及角色状态
        role.setCreateTime(LocalDateTime.now());
        role.setStatus(1); // 0表示禁用，1表示启用
        return save(role);
    }

    @Override
    @Transactional(rollbackFor = SQLDataException.class)
    public boolean deleteRoleBatch(List<Integer> ids) {

        // 没有判断角色是否被使用
        return roleMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public IPage<Role> queryRolePages(Integer pageNum, Integer pageSize) {
        Page<Role> page = new Page<>(pageNum, pageSize);
        return roleMapper.selectPage(page, null);
    }

    @Override
    @Transactional(rollbackFor = SQLDataException.class)
    public boolean allocMenu(Long roleId, List<Long> menuIds) {
        // 判断角色是否存在
        if (roleMapper.selectById(roleId) == null) {
            // 此角色不存在
            ExceptionUtil.of(StatusEnum.SYSTEM_ROLE_NOT_EXISTS);
        }
        // 判断菜单是否存在
        for (Long menuId : menuIds) {
            if (menuMapper.selectById(menuId) == null) {
                ExceptionUtil.of(StatusEnum.SYSTEM_MENU_NOT_EXISTS);
            }
        }
        // 检查角色是否拥有此菜单
        List<Long> existingMenuIds = roleMenuRelationMapper.selectMenuIdsByRoleId(roleId);
        menuIds.removeAll(existingMenuIds);

        // 添加菜单
        for (Long menuId : menuIds) {
            RoleMenuRelation roleMenuRelation = new RoleMenuRelation();
            roleMenuRelation.setRoleId(roleId);
            roleMenuRelation.setMenuId(menuId);
            roleMenuRelationMapper.insert(roleMenuRelation);
        }
        return true;
    }

    @Override
    public List<Menu> listMenus(Long roleId) {

        return roleMenuRelationMapper.listMenu(roleId);
    }

    @Override
    public boolean allocResources(Long roleId, List<Long> resIds) {
        // 判断角色是否存在
        if (roleMapper.selectById(roleId) == null) {
            // 此角色不存在
            ExceptionUtil.of(StatusEnum.SYSTEM_ROLE_NOT_EXISTS);
        }
        // 判断资源菜单是否存在
        for (Long resId : resIds) {
            if (resourceMapper.selectById(resId) == null) {
                ExceptionUtil.of(StatusEnum.SYSTEM_RESOURCE_NOT_EXISTS);
            }
        }
        // 检查角色是否拥有此资源菜单
        List<Long> existingMenuIds = resourceRelationMapper.selectResIdsByRoleId(roleId);
        resIds.removeAll(existingMenuIds);

        // 添加资源菜单
        for (Long resId : resIds) {
            RoleResourceRelation roleResourceRelation = new RoleResourceRelation();
            roleResourceRelation.setRoleId(roleId);
            roleResourceRelation.setResourceId(resId);
            resourceRelationMapper.insert(roleResourceRelation);
        }
        return true;
    }

    @Override
    public List<Resource> listResource(Long roleId) {

        return resourceRelationMapper.listResource(roleId);
    }

    @Override
    public boolean deleteRoleById(Long roleId) {
        QueryWrapper adminRole = new QueryWrapper<AdminRoleRelation>().eq("role_id", roleId);
        QueryWrapper roleMenu = new QueryWrapper<RoleMenuRelation>().eq("role_id", roleId);
        QueryWrapper roleResource = new QueryWrapper<RoleResourceRelation>().eq("role_id", roleId);
        // 判断角色是否被引用
        if (adminRoleRelationMapper.selectCount(adminRole) > 0 ||
                resourceRelationMapper.selectCount(roleResource) > 0 ||
                roleMenuRelationMapper.selectCount(roleMenu) > 0) {
            ExceptionUtil.of(StatusEnum.SYSTEM_DATA_USE);
        }
        return removeById(roleId);
    }
}
