package xyz.fusheng.project.core.service;

import org.springframework.web.multipart.MultipartFile;
import xyz.fusheng.project.common.enums.UnityLangEnum;

import java.util.Map;

/**
 * @FileName: ISentenceService
 * @Author: code-fusheng
 * @Date: 2022/4/10 09:44
 * @Version: 1.0
 * @Description:
 */

public interface ISentenceService {

    Map<Integer, String> uploadFileAndParserSentences(MultipartFile file, UnityLangEnum language);

}
