package com.coding.blog.web.admin;

import com.coding.blog.common.enumapi.StatusEnum;
import com.coding.blog.service.service.ImageService;
import com.coding.blog.service.vo.ImageVo;
import com.coding.blog.service.vo.ResultObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @User Administrator
 * @CreateTime 2023/12/29 17:20
 * @className com.coding.blog.web.admin.ImagesController
 */
@Slf4j
@RestController
@RequestMapping("image")
public class ImagesController {
    @Autowired
    private ImageService imageService;

    /**
     * 图片上传
     *
     * @return
     */
    @RequestMapping(path = "upload")
    public ResultObject<ImageVo> upload(HttpServletRequest request) {
        ImageVo imageVo = new ImageVo();
        try {
            String imagePath = imageService.saveImg(request);
            imageVo.setImagePath(imagePath);
        } catch (Exception e) {
            log.error("上传文件失败!", e);
            return ResultObject.failed(StatusEnum.UPLOAD_PIC_FAILED.getMsg());
        }
        return ResultObject.success(imageVo);
    }
}
