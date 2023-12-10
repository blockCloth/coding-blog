package com.coding.blog.web.admin;

import com.coding.blog.service.vo.ResultObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 后台资源表 前端控制器
 * </p>
 *
 * @author blockCloth
 * @since 2023-12-04
 */
@Api(tags = "后台资源管理")
@RestController
@RequestMapping("/resource")
public class ResourceController {

    @ApiOperation("添加资源信息")
    @PostMapping("saveResource")
    public ResultObject saveResource(@Validated ResourceParam resourceParam){

    }
}
