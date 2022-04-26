package xyz.fusheng.project.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author code-fusheng
 */

@Data
public class DictQuery {
    @ApiModelProperty("字典key,例如CaseType")
    private String dictName;

    @ApiModelProperty("字典详情字段。例如[\"code\",\"desc\"]")
    private String[] fields;
}
