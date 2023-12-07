package com.coding.blog.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coding.blog.common.util.JwtTokenUtil;
import com.coding.blog.service.entity.Users;
import com.coding.blog.service.mapper.UsersMapper;
import com.coding.blog.service.model.AdminUserDetails;
import com.coding.blog.service.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coding.blog.service.vo.UserDetailVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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

    public String login(String userLogin, String userPass) {
        String token = null;
        // 密码需要客户端加密后传递
        try {
            // 查询用户+用户资源
            UserDetails userDetails = loadUserByUsername(userLogin);

            // 验证密码
            if (!passwordEncoder.matches(userPass, userDetails.getPassword())) {
                // Asserts.fail("密码不正确");
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
            // List<Resource> resourceList = getResourceList(users.getUsersId());
            return new AdminUserDetails(users);
            // return users;
        }

        throw new UsernameNotFoundException("用户名或密码错误");
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
            return false;
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
    @Transactional //需要添加回滚异常，等待自定义
    public boolean deleteBatch(List<Integer> ids) {
        return usersMapper.deleteBatchIds(ids) >= 1;
    }

    @Override
    public Users getUserDetail(Integer userId) {
        return usersMapper.selectById(userId);
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

        if (users == null){
            //提示信息
            return false;
        }
        if (!passwordEncoder.matches(oldPass, users.getUserPass())){
            //需要提示信息
            return false;
        }
        //设置新密码
        users.setUserPass(passwordEncoder.encode(newPass));

        return usersMapper.updateById(users) > 0;
    }

}
