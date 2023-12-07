package com.coding.blog.service.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @User Administrator
 * @CreateTime 2023/12/6 14:10
 * @className com.coding.blog.service.dto.UsersUpdateParam
 */
@Getter
@Setter
@ToString
@TableName("users")
@ApiModel(value = "用户修改", description = "用户表")
public class UsersUpdateParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("users_id")
    @TableId(value = "users_id", type = IdType.AUTO)
    private Long usersId;

    @ApiModelProperty("昵称")
    @TableField("user_nicename")
    private String userNicename;

    @ApiModelProperty("Email")
    @TableField("user_email")
    private String userEmail;

    @ApiModelProperty("网址")
    @TableField("user_url")
    private String userUrl;

    @ApiModelProperty("图像")
    @TableField("display_name")
    private String displayName;

    @ApiModelProperty("属性")
    @TableField("attribute")
    private String attribute;

}
