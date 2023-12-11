package com.coding.blog.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coding.blog.common.enumapi.StatusEnum;
import com.coding.blog.common.util.ExceptionUtil;
import com.coding.blog.common.util.JwtTokenUtil;
import com.coding.blog.service.entity.*;
import com.coding.blog.service.mapper.AdminRoleRelationMapper;
import com.coding.blog.service.mapper.RoleMapper;
import com.coding.blog.service.mapper.UsersMapper;
import com.coding.blog.service.model.AdminUserDetails;
import com.coding.blog.service.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coding.blog.service.vo.UserDetailVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLDataException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author blockCloth
 * @since 2023-12-04
 */
@Slf4j
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {


    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private AdminRoleRelationMapper adminRoleMapper;

    @Autowired
    private RoleMapper roleMapper;


    public String login(String userLogin, String userPass) {
        String token = null;
        // 密码需要客户端加密后传递
        try {
            // 查询用户+用户资源
            UserDetails userDetails = loadUserByUsername(userLogin);

            // 验证密码
            if (!passwordEncoder.matches(userPass, userDetails.getPassword())) {
                // Asserts.fail("密码不正确");
                ExceptionUtil.of(StatusEnum.LOGIN_FAILED_PASS);
            }

            // 返回 JWT
            token = jwtTokenUtil.generateToken(userDetails);
        } catch (AuthenticationException e) {
            log.warn("登录异常:{}", e.getMessage());
        }
        return token;
    }

    public UserDetails loadUserByUsername(String userLogin) {
        // 查询用户
        Users users = getAdminByUsername(userLogin);
        if (users != null) {
            List<Resource> resourceList = getResourceList(users.getUsersId());
            return new AdminUserDetails(users,resourceList);
        }

        throw new UsernameNotFoundException("用户名或密码错误");
    }

    private List<Resource> getResourceList(Long adminId) {
        log.info("根据用户{}找到资源", adminId);
        List<Resource> resourceList = adminRoleMapper.getResourceList(adminId);
        log.info("根据用户获取数据库中的资源大小{}, 内容{}", resourceList.size(), resourceList);

        return resourceList;
    }


    private Users getAdminByUsername(String userLogin) {
        return usersMapper.selectOne(
                new QueryWrapper<Users>().eq("user_login", userLogin)
        );
    }

    @Override
    public boolean register(Users users) {
        // 先查看该用户是否存在
        Long userLogin = usersMapper.selectCount(new QueryWrapper<Users>()
                .eq("user_login", users.getUserLogin()));
        if (userLogin > 0) {
            ExceptionUtil.of(StatusEnum.USER_EXISTS);
        }
        // 设置其他内容
        String encodePass = passwordEncoder.encode(users.getUserPass());
        users.setUserPass(encodePass);
        users.setUserRegistered(LocalDateTime.now());
        users.setUserStatus(0);
        users.setUserType(0);

        return save(users);
    }

    @Override
    @Transactional(rollbackFor = SQLDataException.class) //需要添加回滚异常，等待自定义
    public boolean deleteBatch(List<Integer> ids) {
        return usersMapper.deleteBatchIds(ids) >= 1;
    }

    @Override
    public UserDetailVo getUserDetail(Long userId) {
        Users users = usersMapper.selectById(userId);

        if (users != null){
            UserDetailVo userDetailVo = new UserDetailVo();
            BeanUtils.copyProperties(users,userDetailVo);
            //查询角色信息
            List<Role> roles = adminRoleMapper.getRoleByUserId(userId);
            userDetailVo.setRoles(roles);
            return userDetailVo;
        }
        return null;
    }



    @Override
    public IPage<Users> getAllUserDetail(int pageNum, int pageSize) {
        Page<Users> page = new Page<>(pageNum, pageSize);
        return usersMapper.selectPage(page,new QueryWrapper<>());
    }

    @Override
    public boolean updateUserPass(String newPass,String oldPass) {
        AdminUserDetails adminUserDetails = (AdminUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //获取用户
        Users users = adminUserDetails.getUsers();

        if (!passwordEncoder.matches(oldPass, users.getUserPass())){
            //需要提示信息
            ExceptionUtil.of(StatusEnum.LOGIN_FAILED_NOT_OLDPASS);
        }
        //设置新密码
        users.setUserPass(passwordEncoder.encode(newPass));

        return usersMapper.updateById(users) > 0;
    }

    @Override
    @Transactional(rollbackFor = SQLDataException.class)
    public boolean roleSave(Long userId, List<Long> roleIds) {
        //判断用户是否为空
        if (usersMapper.selectById(userId) == null){
            ExceptionUtil.of(StatusEnum.USER_NOT_EXISTS);
        }
        //判断角色是否存在
        for (Long roleId : roleIds) {
            if (roleMapper.selectById(roleId) == null){
                ExceptionUtil.of(StatusEnum.SYSTEM_ROLE_NOT_EXISTS);
            }
        }

        // 检查用户是否已经拥有这些角色
        List<Long> existingRoleIds = adminRoleMapper.selectRoleIdsByUserId(userId);
        roleIds.removeAll(existingRoleIds);

        // 添加用户角色
        for (Long roleId : roleIds) {
            AdminRoleRelation relation = new AdminRoleRelation();
            relation.setUsersId(userId);
            relation.setRoleId(roleId);
            adminRoleMapper.insert(relation);
        }

        return true;
    }

    @Override
    public List<Role> queryRoles(Long userId) {
        return adminRoleMapper.queryRoles(userId);
    }

    @Override
    public boolean roleRemove(Long userId, Long roleId) {
        return adminRoleMapper.roleRemove(userId,roleId) > 0;
    }

    @Override
    public Map<String, Object> getInfo() {
        AdminUserDetails adminUserDetails =
                (AdminUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //获取users
        Users users = adminUserDetails.getUsers();
        users.setUserPass("");
        //获取用户菜单
        List<Menu> menus = roleMapper.getListMensByUserId(users.getUsersId());
        Map<String, Object> data = new HashMap<>();
        data.put("userDetail",users);
        data.put("username", users.getUserLogin());
        data.put("menus", menus);
        data.put("icon", users.getDisplayName());
        //通过用户ID查询角色信息
        List<Role> roleList =  roleMapper.getListRoleByUserId(users.getUsersId());

        if (CollUtil.isNotEmpty(roleList)){
            List<String> roles = roleList.stream().map(Role::getName).collect(Collectors.toList());
            data.put("roles", roles);
        }

        return data;
    }

}
