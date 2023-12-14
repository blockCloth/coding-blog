package com.coding.blog.web.admin;

import com.coding.blog.service.dto.PostsParam;
import com.coding.blog.service.entity.Posts;
import com.coding.blog.service.service.IPostsService;
import com.coding.blog.service.vo.ResultObject;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 文章 前端控制器
 * </p>
 *
 * @author blockCloth
 * @since 2023-12-04
 */
@RestController
@RequestMapping("/posts")
public class PostsController {

    @Autowired
    private IPostsService postsService;

    @ApiOperation("保持文章信息")
    @PostMapping("save")
    public ResultObject savePost(@Validated PostsParam postsParam){
        if (postsParam != null && postsService.savePost(postsParam)){
            return ResultObject.success();
        }
        return ResultObject.failed();
    }

    @ApiOperation("修改文章信息")
    @PostMapping("update")
    public ResultObject updatePost(@Validated PostsParam postsParam){
        if (postsParam != null && postsService.updatePost(postsParam)){
            return ResultObject.success();
        }
        return ResultObject.failed();
    }

    @ApiOperation("删除文章信息")
    @DeleteMapping("delete")
    public ResultObject deletePostById(@RequestParam Long postId){
        if (postId == null) return ResultObject.failed("文章ID不能为空！");

        if (postsService.deletePostById(postId)){
            return ResultObject.success();
        }
        return ResultObject.failed();
    }

    @ApiOperation("查询文章详细信息")
    @GetMapping("queryPostDetailById")
    public ResultObject queryPostDetailById(@RequestParam Long postId){
        if (postId == null) return ResultObject.failed("文章ID不能为空！");
        postsService.queryPostDetailById(postId);
        if (){
            return ResultObject.success();
        }
        return ResultObject.failed();
    }
}


