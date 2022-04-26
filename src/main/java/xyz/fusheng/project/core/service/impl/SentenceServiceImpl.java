package xyz.fusheng.project.core.service.impl;

import org.apache.commons.collections4.map.LinkedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.fusheng.project.common.enums.UnityLangEnum;
import xyz.fusheng.project.common.utils.OfficeUtils;
import xyz.fusheng.project.common.utils.TxtUtils;
import xyz.fusheng.project.core.service.ISentenceService;
import xyz.fusheng.project.model.request.TmxDataVo;
import xyz.fusheng.project.tools.oss.AliyunOssUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * @FileName: SentenceServiceImpl
 * @Author: code-fusheng
 * @Date: 2022/4/10 09:45
 * @Version: 1.0
 * @Description:
 */

@Service
public class SentenceServiceImpl implements ISentenceService {

    private static final Logger logger = LoggerFactory.getLogger(SentenceServiceImpl.class);

    @Resource
    private AliyunOssUtils aliyunOssUtils;

    @Override
    public Map<Integer, String> uploadFileAndParserSentences(MultipartFile file, UnityLangEnum language) {
        Map<Integer, String> sentencesMap = new LinkedMap<>();
        String[] split = file.getOriginalFilename().split("\\.");
        String fileType = Arrays.asList(split).get(split.length - 1);
        logger.info("[文件详情展示] -> fileName:{}, fileType:{}", file.getOriginalFilename(), fileType);
        if ("docx".equals(fileType) || "doc".equals(fileType)) {
            sentencesMap = OfficeUtils.docxParser2Sentences(file, language);
        } else if ("txt".equals(fileType)) {
            sentencesMap = TxtUtils.txtParser2Sentences(file, language);
        }
        return sentencesMap;
    }

    @Override
    public String exportTmx(TmxDataVo tmxDataVo) {

        // 通过 tmxDataVo 中的参数构建 tmx document 节点树


        return null;
    }
}
