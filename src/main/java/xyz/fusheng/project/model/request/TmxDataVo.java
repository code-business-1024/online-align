package xyz.fusheng.project.model.request;

import lombok.Data;
import xyz.fusheng.project.model.dto.TmxFileDto;
import xyz.fusheng.project.model.dto.TmxSentenceDto;

import java.util.ArrayList;
import java.util.List;

/**
 * @FileName: TmxDataVo
 * @Author: code-fusheng
 * @Date: 2022/4/26 21:42
 * @Version: 1.0
 * @Description: Tmx 数据 Vo
 */

@Data
public class TmxDataVo {

    private List<TmxFileDto> files = new ArrayList<>(2);

    private List<TmxSentenceDto> sentences = new ArrayList<>();

}
