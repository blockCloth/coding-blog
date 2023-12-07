package com.coding.blog.service.mapper;

import com.coding.blog.service.entity.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coding.blog.service.vo.UserDetailVo;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author blockCloth
 * @since 2023-12-04
 */
public interface UsersMapper extends BaseMapper<Users> {

    UserDetailVo getUserDetail(Integer userId);
}
