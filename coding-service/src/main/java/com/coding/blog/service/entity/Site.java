package com.coding.blog.service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 站点配置
 * </p>
 *
 * @author blockCloth
 * @since 2023-12-04
 */
@Getter
@Setter
@TableName("site")
@ApiModel(value = "Site对象", description = "站点配置")
public class Site implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("site_id")
    @TableId(value = "site_id", type = IdType.AUTO)
    private Long siteId;

    @ApiModelProperty("名称")
    @TableField("site_name")
    private String siteName;

    @ApiModelProperty("关键字")
    @TableField("keywords")
    private String keywords;

    @ApiModelProperty("介绍")
    @TableField("site_desc")
    private String siteDesc;

    @ApiModelProperty("属性")
    @TableField("attribute")
    private String attribute;

    @ApiModelProperty("更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;
}
