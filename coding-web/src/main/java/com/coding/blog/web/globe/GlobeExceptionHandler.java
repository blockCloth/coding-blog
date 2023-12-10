package com.coding.blog.web.globe;

import com.coding.blog.common.eception.ForumAdviceException;
import com.coding.blog.service.vo.ResultObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

/**
 * @User Administrator
 * @CreateTime 2023/12/10 18:58
 * @className com.coding.blog.web.globe.GlobeExceptionHandler
 */
@RestControllerAdvice
public class GlobeExceptionHandler {

    @ExceptionHandler(value = ForumAdviceException.class)
    public ResultObject<String> resolveException(ForumAdviceException e) {
        return ResultObject.failed(e.getStatus().getMsg());
    }
}
