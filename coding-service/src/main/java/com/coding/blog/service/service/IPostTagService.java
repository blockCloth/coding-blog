package com.coding.blog.service.service;

import com.coding.blog.service.entity.PostTag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coding.blog.service.vo.PostTagVo;

import java.util.List;

/**
 * <p>
 * 标签表 服务类
 * </p>
 *
 * @author blockCloth
 * @since 2023-12-04
 */
public interface IPostTagService extends IService<PostTag> {

    boolean savePostTag(PostTag postTag);

    boolean deletePostTag(Long postTagId);

    List<PostTag> queryAllPostTag();

    List<PostTagVo> queryPostByTagId(Long postTagId);

    void delPostTagCache();
}
