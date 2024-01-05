package com.coding.blog.service.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @User Administrator
 * @CreateTime 2023/12/29 17:21
 * @className com.coding.blog.service.service.ImageService
 */
public interface ImageService {
    /**
     * 保存图片
     * @param request
     * @return
     */
    String saveImg(HttpServletRequest request);

}
