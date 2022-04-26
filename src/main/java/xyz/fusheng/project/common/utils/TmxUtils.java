package xyz.fusheng.project.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import xyz.fusheng.project.model.dto.TmxFileDto;
import xyz.fusheng.project.model.dto.TmxSentenceDto;
import xyz.fusheng.project.model.request.TmxDataVo;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @FileName: TmxUtils
 * @Author: code-fusheng
 * @Date: 2022/4/26 20:07
 * @Version: 1.0
 * @Description: Tmx 文件工具类 本质上还是 XML 文档
 */

public class TmxUtils {

    private static final Logger logger = LoggerFactory.getLogger(TmxUtils.class);

    public static String buildTmx(TmxDataVo tmxDataVo) {
        try {
            //
            File file = new File("/Users/zhanghao/IdeaProjects/fusheng-work/github/online-align-project/online-align/src/main/resources/source/demo.tmx");
            // 1. 通过 dom4j 创建 document 实例
            Document document = DocumentHelper.createDocument();
            // 2. 添加根节点 <tmx><tmx/>
            Element tmx = document.addElement("tmx");
            // 3. 在根节点下添加子节点 --- 分别是 header 和 body
            // 3.1 header
            Element header = tmx.addElement("header");
            TmxFileDto tmxFile1 = tmxDataVo.getFiles().get(0);
            String srcLang = tmxFile1.getLanguage();
            TmxFileDto tmxFile2 = tmxDataVo.getFiles().get(1);
            String tgtLang = tmxFile2.getLanguage();
            // 3.2 body
            Element body = tmx.addElement("body");
            // 3.2 ==> 4
            // 4. 遍历创建 tu > tuv > seg
            List<TmxSentenceDto> sentences = tmxDataVo.getSentences();
            if (!CollectionUtils.isEmpty(sentences)) {
                for (TmxSentenceDto sentence : sentences) {
                    Element tu = body.addElement("tu");
                    tu.addAttribute("tuid", String.valueOf(sentence.getKey() - 1));
                    Element tuv1 = tu.addElement("tuv");
                    tuv1.addAttribute("xml:lang", srcLang);
                    tuv1.addElement("seg").addText(sentence.getValue1());
                    Element tuv2 = tu.addElement("tuv");
                    tuv2.addAttribute("xml:lang", tgtLang);
                    tuv2.addElement("seg").addText(sentence.getValue2());
                }
            }
            // 自定义xml样式
            OutputFormat format = new OutputFormat();
            format.setIndentSize(2);  // 行缩进
            format.setNewlines(true); // 一个结点为一行
            format.setTrimText(true); // 去重空格
            format.setPadText(true);
            format.setNewLineAfterDeclaration(false); // 放置xml文件中第二行为空白行
            // 输出文件
            XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
            writer.write(document);
        } catch (Exception e) {

        }
        return null;
    }

    public static String writeTmx(Object object) {

        return "";
    }

    public static String readTmx(Object object) {

        return "";
    }

    public static void main(String[] args) {

        TmxDataVo tmxDataVo = new TmxDataVo();
        ArrayList<TmxFileDto> tmxFiles = new ArrayList<>();
        tmxFiles.add(new TmxFileDto() {{
            setFileName("测试文件1.txt");
            setLanguage("en-US");
            setType("src");
        }});
        tmxFiles.add(new TmxFileDto() {{
            setFileName("测试文件2.txt");
            setLanguage("zh-CN");
            setType("tgt");
        }});
        tmxDataVo.setFiles(tmxFiles);
        ArrayList<TmxSentenceDto> tmxSentences = new ArrayList<>();
        tmxSentences.add(new TmxSentenceDto() {{
            setKey(1);
            setValue1("哈哈哈哈哈1");
            setValue2("hahahahaha1");
        }});
        tmxSentences.add(new TmxSentenceDto() {{
            setKey(2);
            setValue1("哈哈哈哈哈2");
            setValue2("hahahahaha2");
        }});
        tmxDataVo.setSentences(tmxSentences);
        logger.info("tmxDataVo:{}", JSON.toJSONString(tmxDataVo, SerializerFeature.PrettyFormat));

        buildTmx(tmxDataVo);

    }

}
