package xyz.fusheng.project.common.utils;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import xyz.fusheng.project.common.enums.UnityLangEnum;

import java.io.IOException;
import java.io.InputStream;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @FileName: OfficeToolUtils
 * @Author: code-fusheng
 * @Date: 2022/4/5 11:12
 * @Version: 1.0
 * @Description: Office 工具类
 */

public class OfficeUtils {

    private static final Logger logger = LoggerFactory.getLogger(OfficeUtils.class);

    private static final String DOCX_SUFFIX = ".docx";
    private static final String DOC_SUFFIX = ".doc";

    /**
     * doc 文件格式化成段落
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    public static Map<Integer, String> docxParser2Sentences(MultipartFile file, UnityLangEnum language) {
        Map<Integer, String> sentencesMap = new LinkedMap<>();
        AtomicInteger sentenceNum = new AtomicInteger(0);
        try {
            InputStream fis = file.getInputStream();
            String fileName = file.getOriginalFilename();
            AtomicInteger paragraphNum = new AtomicInteger(0);
            Map<Integer, String> paragraphsMap = new LinkedMap<>();
            if (fileName.endsWith(DOCX_SUFFIX)) {
                XWPFDocument docx = new XWPFDocument(fis);
                List<XWPFParagraph> paragraphs = docx.getParagraphs();
                for (XWPFParagraph paragraph : paragraphs) {
                    if (StringUtils.isNotBlank(paragraph.getText())) {
                        paragraphsMap.put(paragraphNum.incrementAndGet(), paragraph.getText());
                    }
                }
                fis.close();
            } else if (fileName.endsWith(DOC_SUFFIX)) {
                HWPFDocument doc = new HWPFDocument(fis);
                WordExtractor we = new WordExtractor(doc);
                String[] paragraphs = we.getParagraphText();
                for (String paragraph : paragraphs) {
                    if (StringUtils.isNotBlank(paragraph)) {
                        paragraphsMap.put(paragraphNum.incrementAndGet(), paragraph);
                    }
                }
                fis.close();
            }
            if (!paragraphsMap.isEmpty()) {
                for (Map.Entry<Integer, String> entry : paragraphsMap.entrySet()) {
                    List<String> sentences = paragraph2Sentences(entry.getValue(), language);
                    sentences.forEach(item -> {
                        sentencesMap.put(sentenceNum.incrementAndGet(), item);
                    });
                }
                sentencesMap.forEach((key, value) -> logger.info("[句子解析结果] -> key:{}, value:{}", key, value));
            }
            return sentencesMap;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    private static List<String> paragraph2Sentences(String paragraphStr, UnityLangEnum language) {
        List<String> sentenceList = new ArrayList();
        BreakIterator sentenceIterator = BreakIterator.getSentenceInstance(language.getLocale());
        sentenceIterator.setText(paragraphStr);
        int start = sentenceIterator.first();
        for (int end = sentenceIterator.next(); end != BreakIterator.DONE; start = end, end = sentenceIterator.next()) {
            String sentence = paragraphStr.substring(start, end);
            if (StringUtils.isNotBlank(sentence)) {
                sentenceList.add(sentence);
            }
        }
        return sentenceList;
    }

}
