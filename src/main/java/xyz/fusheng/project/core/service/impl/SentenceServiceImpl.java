package xyz.fusheng.project.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.text.StringEscapeUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import xyz.fusheng.project.common.enums.UnityLangEnum;
import xyz.fusheng.project.common.utils.OfficeUtils;
import xyz.fusheng.project.common.utils.TxtUtils;
import xyz.fusheng.project.core.service.ISentenceService;
import xyz.fusheng.project.model.dto.TmxFileDto;
import xyz.fusheng.project.model.dto.TmxSentenceDto;
import xyz.fusheng.project.model.request.TmxDataVo;
import xyz.fusheng.project.tools.oss.AliyunOssUtils;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    public String exportTmx(TmxDataVo tmxDataVo) {
        String url = "";
        try {
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
            String tmxFileName = tmxFile1.getFileName() + "-" + tmxFile2.getFileName() + "|" + "对齐文档";
            tmx.addElement("prop").addAttribute("type", "title").addText(tmxFileName);
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
                    tuv1.addElement("seg").addText(StringEscapeUtils.escapeXml11(sentence.getValue1()));
                    Element tuv2 = tu.addElement("tuv");
                    tuv2.addAttribute("xml:lang", tgtLang);
                    tuv2.addElement("seg").addText(StringEscapeUtils.escapeXml11(sentence.getValue2()));
                }
            }
            // 自定义xml样式
            OutputFormat format = new OutputFormat();
            format.setIndentSize(2);  // 行缩进
            format.setNewlines(true); // 一个结点为一行
            format.setTrimText(true); // 去重空格
            format.setPadText(true);
            format.setNewLineAfterDeclaration(false); // 放置xml文件中第二行为空白行
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            XMLWriter writer = new XMLWriter(baos, format);
            writer.write(document);
            InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
            url = aliyunOssUtils.uploadFile(inputStream, tmxFileName, "tmx");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
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
        logger.info("{}", JSON.toJSONString(tmxDataVo, SerializerFeature.PrettyFormat));
    }

}
