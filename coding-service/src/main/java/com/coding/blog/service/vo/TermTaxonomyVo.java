package com.coding.blog.service.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @User Administrator
 * @CreateTime 2023/12/13 9:35
 * @className com.coding.blog.service.vo.TermTaxonomyVo
 */
@Data
@ToString
public class TermTaxonomyVo {

    /**
     * id
     */
    private Long termTaxonomyId;

    /**
     * 说明
     */
    private String description;

    /**
     * 栏目名称
     */
    private String name;

    /**
     * 父栏目id
     */
    private Long parentId;

    /**
     * 创建人id
     */
    private Long createUserId;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 属性
     */
    private String attribute;

    private List<TermTaxonomyVo> children ;

    public TermTaxonomyVo() {
        children = new ArrayList<>();
    }
}
