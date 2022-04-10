package xyz.fusheng.project.tools.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @FileName: AliyunOssUtils
 * @Author: code-fusheng
 * @Date: 2022/3/13 12:02 上午
 * @Version: 1.0
 * @Description:
 */

@Component
public class AliyunOssUtils {

    private static final Logger logger = LoggerFactory.getLogger(AliyunOssUtils.class);

    public static final ThreadLocal<SimpleDateFormat> dateFormat = ThreadLocal.withInitial(() ->
            new SimpleDateFormat("yyyyMMddHHmmss"));

    @Resource
    private AliyunOssConfig aliyunOssConfig;

    public String uploadFile(MultipartFile file) {
        OSS ossClient = new OSSClientBuilder().build(aliyunOssConfig.getEndpoint(), aliyunOssConfig.getAccessKeyId(), aliyunOssConfig.getAccessKeySecret());
        String fileName = dateFormat.get().format(new Date()) + "/" + file.getOriginalFilename();
        try {
            ossClient.putObject(aliyunOssConfig.getBucketName(), fileName, new ByteArrayInputStream(file.getBytes()));
            // 判断容器是否存在,不存在就创建
            if (!ossClient.doesBucketExist(aliyunOssConfig.getBucketName())) {
                ossClient.createBucket(aliyunOssConfig.getBucketName());
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(aliyunOssConfig.getBucketName());
                createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                ossClient.createBucket(createBucketRequest);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
        String url = "https://" + aliyunOssConfig.getBucketName() + "." + aliyunOssConfig.getEndpoint() + "/" + fileName;
        logger.info("文件上传返回结果预览:{}", url);
        return url;
    }

}
