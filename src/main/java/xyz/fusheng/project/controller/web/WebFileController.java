package xyz.fusheng.project.controller.web;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.fusheng.project.common.enums.UnityLangEnum;
import xyz.fusheng.project.core.service.ISentenceService;
import xyz.fusheng.project.model.base.BaseResult;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @FileName: WebFileController
 * @Author: code-fusheng
 * @Date: 2022/4/9 13:46
 * @Version: 1.0
 * @Description: 业务文件上传接口
 */

@RestController
@RequestMapping("/web/file")
public class WebFileController {

    @Resource
    private ISentenceService iSentenceService;

    @ApiOperation("上传文件并解析")
    @PostMapping("/uploadAndParser")
    public BaseResult uploadAndParser(@RequestParam MultipartFile file,
                                      @RequestParam String language) {
        UnityLangEnum unityLangEnum = UnityLangEnum.matchLangStr(language);
        Map<Integer, String> sentencesMap = iSentenceService.uploadFileAndParserSentences(file, unityLangEnum);
        return BaseResult.success(sentencesMap);
    }

}
