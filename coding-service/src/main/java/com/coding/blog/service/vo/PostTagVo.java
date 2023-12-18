package com.coding.blog.service.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @User Administrator
 * @CreateTime 2023/12/12 18:25
 * @className com.coding.blog.service.vo.PostTagVo
 */
@Data
public class PostTagVo implements Serializable {
    private String title;
    private String userLogin;
    private String description;
    private LocalDateTime postDate;
}
