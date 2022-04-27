package xyz.fusheng.project.controller.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.fusheng.project.common.enums.UnityLangEnum;
import xyz.fusheng.project.core.service.ISentenceService;
import xyz.fusheng.project.model.base.BaseResult;
import xyz.fusheng.project.model.request.TmxDataVo;

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
        JSONArray objects = new JSONArray();
        sentencesMap.entrySet().forEach(item -> {
            objects.add(new JSONObject() {{
                put("key", item.getKey());
                put("value", item.getValue());
            }});
        });
        return BaseResult.success(objects);
    }

    @ApiOperation("导出Tmx文件")
    @PostMapping("/exportTmx")
    public BaseResult<String> exportTmx(@RequestBody TmxDataVo tmxDataVo) {
        String fileUrl = iSentenceService.exportTmx(tmxDataVo);
        return BaseResult.success(fileUrl);
    }

}
