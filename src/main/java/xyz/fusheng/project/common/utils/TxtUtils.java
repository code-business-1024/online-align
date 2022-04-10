package xyz.fusheng.project.common.utils;

import org.apache.commons.collections4.map.LinkedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import xyz.fusheng.project.common.enums.UnityLangEnum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @FileName: TextUtils
 * @Author: code-fusheng
 * @Date: 2022/4/9 13:54
 * @Version: 1.0
 * @Description:
 */

public class TxtUtils {

    private static final Logger logger = LoggerFactory.getLogger(TxtUtils.class);

    private static final String TXT_SUFFIX = ".txt";

    public static Map<Integer, String> txtParser2Sentences(MultipartFile file, UnityLangEnum language) {
        Map<Integer, String> sentencesMap = new LinkedMap<>();
        AtomicInteger sentenceNum = new AtomicInteger(0);
        try {
            AtomicInteger paragraphNum = new AtomicInteger(0);
            Map<Integer, String> paragraphsMap = new LinkedMap<>();
            InputStream fis = file.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String lineText = null;
            while ((lineText = br.readLine()) != null) {
                if (StringUtils.isNotBlank(lineText)) {
                    paragraphsMap.put(paragraphNum.incrementAndGet(), lineText);
                }
            }
            br.close();
            fis.close();
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
