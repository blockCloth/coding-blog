package com.coding.blog.service.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coding.blog.service.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coding.blog.service.vo.UserDetailVo;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author blockCloth
 * @since 2023-12-04
 */
public interface IUsersService extends IService<Users> {

    String login(String userLogin, String userPass);

    boolean register(Users users);

    UserDetails loadUserByUsername(String userLogin);

    boolean deleteBatch(List<Integer> ids);

    Users getUserDetail(Integer userId);

    IPage<Users> getAllUserDetail(int pageNum, int pageSize);

    boolean updateUserPass(String newPass,String oldPass);
}
