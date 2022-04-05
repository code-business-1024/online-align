package xyz.fusheng.project.common.utils;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @FileName: OfficeToolUtils
 * @Author: code-fusheng
 * @Date: 2022/4/5 11:12
 * @Version: 1.0
 * @Description: Office 工具类
 */

public class OfficeUtils {

    private static final String DOCX_SUFFIX = ".docx";
    private static final String DOC_SUFFIX = ".doc";

    public static String docxParser2Paragraph(String fileName) throws IOException, InvalidFormatException {
        StringBuilder content = new StringBuilder();
        if (fileName.endsWith(DOCX_SUFFIX)) {
            FileInputStream fis = new FileInputStream(fileName);
            XWPFDocument docx = new XWPFDocument(fis);
            List<XWPFParagraph> paragraphs = docx.getParagraphs();
            for (XWPFParagraph paragraph : paragraphs) {
                content.append(paragraph.getText()).append("\n");
            }
            fis.close();
        } else if (fileName.endsWith(DOC_SUFFIX)) {
            FileInputStream fis = new FileInputStream(fileName);
            HWPFDocument doc = new HWPFDocument(fis);
            WordExtractor we = new WordExtractor(doc);
            String[] paragraphs = we.getParagraphText();
            for (String paragraph : paragraphs) {
                content.append(paragraph.toString() + "\n");
            }
            fis.close();
        } else {
            return null;
        }
        return content.toString();
    }

    public static void main(String[] args) throws IOException {
        String docxFileName = "/Users/zhanghao/IdeaProjects/fusheng-work/github/online-align/src/main/resources/source/test1-docx.docx";
        // String docxFileName = "/Users/zhanghao/IdeaProjects/fusheng-work/github/online-align/src/main/resources/source/test2-doc.doc";
        String content = OfficeUtils.docxParser2Paragraph(docxFileName);
        System.out.println(content);
    }

}
