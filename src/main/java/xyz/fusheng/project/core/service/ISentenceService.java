package xyz.fusheng.project.core.service;

import org.springframework.web.multipart.MultipartFile;
import xyz.fusheng.project.common.enums.UnityLangEnum;
import xyz.fusheng.project.model.request.TmxDataVo;

import java.util.Map;

/**
 * @FileName: ISentenceService
 * @Author: code-fusheng
 * @Date: 2022/4/10 09:44
 * @Version: 1.0
 * @Description:
 */

public interface ISentenceService {

    /**
     * 上传文件并解析成句子
     *
     * @param file
     * @param language
     * @return
     */
    Map<Integer, String> uploadFileAndParserSentences(MultipartFile file, UnityLangEnum language);

    /**
     * 导出 Tmx 文件
     *
     * @param tmxDataVo
     * @return
     */
    String exportTmx(TmxDataVo tmxDataVo);
}
