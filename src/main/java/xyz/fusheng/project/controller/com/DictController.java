package xyz.fusheng.project.controller.com;

import com.alibaba.fastjson.JSONArray;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.fusheng.project.common.enums.IEnumCode;
import xyz.fusheng.project.common.utils.DictUtil;
import xyz.fusheng.project.model.base.BaseResult;
import xyz.fusheng.project.model.request.DictQuery;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @FileName: DictController
 * @Author: code-fusheng
 * @Date: 2022/4/10 22:30
 * @Version: 1.0
 * @Description:
 */

@RestController
@RequestMapping("/dict")
public class DictController {

    @PostMapping("/list")
    public BaseResult<JSONArray> dict(@RequestBody DictQuery dictQuery) {
        JSONArray dictArray = null;
        // 获取枚举包的包名
        String packageName = IEnumCode.class.getPackage().getName();
        // 拼接枚举类Class名
        String enumClassName = new StringBuilder(packageName).append(".").append(dictQuery.getDictName()).append("Enum").toString();
        try {
            // 反射获取枚举类class
            Class<?> clazz = Class.forName(enumClassName);
            if (clazz.isEnum()) {
                // 反射获取前端需要的字段
                List<Field> fieldList = new ArrayList<>(dictQuery.getFields().length);
                for (String fieldName : dictQuery.getFields()) {
                    fieldList.add(clazz.getDeclaredField(fieldName));
                }
                // 调用字典解析工具类解析枚举类，生成字典项
                dictArray = DictUtil.resolveEnum(clazz, fieldList);
            }
        } catch (NoSuchFieldException e) {
            return BaseResult.error(MessageFormat.format("字典：{0} 指定字段不存在", dictQuery.getDictName()));
        } catch (Exception e) {
            return BaseResult.error(MessageFormat.format("字典：{0}不存在", dictQuery.getDictName()));
        }
        return BaseResult.success(dictArray);
    }

}
