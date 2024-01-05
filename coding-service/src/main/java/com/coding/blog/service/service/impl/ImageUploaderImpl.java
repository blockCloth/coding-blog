package com.coding.blog.service.service.impl;

import com.coding.blog.common.enumapi.StatusEnum;
import com.coding.blog.common.properties.ImageProperties;
import com.coding.blog.common.util.ExceptionUtil;
import com.coding.blog.service.service.ImageUploader;
import com.github.hui.quick.plugin.base.file.FileWriteUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * @User Administrator
 * @CreateTime 2023/12/29 17:33
 * @className com.coding.blog.service.service.impl.ImageUploaderImpl
 */
@Slf4j
@Service
public class ImageUploaderImpl implements ImageUploader {

    @Autowired
    private ImageProperties imageProperties;
    private Random random;

    public ImageUploaderImpl() {
        random = new Random();
    }

    @Override
    public String upload(InputStream input, String fileType) {
        try {
            if (fileType == null) {
                // 根据魔数判断文件类型
                byte[] bytes = StreamUtils.copyToByteArray(input);
                input = new ByteArrayInputStream(bytes);
                fileType = getFileType((ByteArrayInputStream) input, fileType);
            }

            String path = imageProperties.getAbsTmpPath() + imageProperties.getWebImgPath();
            String fileName = genTmpFileName();

            FileWriteUtil.FileInfo file = FileWriteUtil.saveFileByStream(input, path, fileName, fileType);
            return imageProperties.buildImgUrl(imageProperties.getWebImgPath() + file.getFilename() + "." + file.getFileType());
        } catch (Exception e) {
            log.error("Parse img from httpRequest to BufferedImage error! e:", e);
            throw ExceptionUtil.of(StatusEnum.UPLOAD_PIC_FAILED);
        }
    }

    /**
     * 获取文件临时名称
     *
     * @return
     */
    private String genTmpFileName() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmssSSS")) + "_" + random.nextInt(100);
    }
}
