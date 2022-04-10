package xyz.fusheng.project.tools.oss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @FileName: AliyunOssConfig
 * @Author: code-fusheng
 * @Date: 2022/3/12 4:51 下午
 * @Version: 1.0
 * @Description:
 */

@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
public class AliyunOssConfig {

    private String bucketName;

    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

}
