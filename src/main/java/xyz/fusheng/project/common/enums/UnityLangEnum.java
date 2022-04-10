package xyz.fusheng.project.common.enums;

import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @FileName: StandardLangEnum
 * @Author: code-fusheng
 * @Date: 2022/3/20 11:05
 * @Version: 1.0
 * @Description: 标准语言类型枚举
 */

@Getter
public enum UnityLangEnum {

    AUTO("auto", "自动"),
    EN("en", "英文", Locale.ENGLISH),
    EN_US("en_US", "英文（美国）", Locale.US, "en-US"),
    ZH("zh_CN", "中文", Locale.CHINESE, "zh-CN"),
    ZH_HK("zh_HK", "中文（香港）", Locale.CHINESE, "zh-HK"),
    ZH_TW("zh_TW", "中文（台湾/繁体）", Locale.CHINESE, "zh-TW"),
    JP("ja", "日文", Locale.JAPAN, "ja_JP", "ja-JP");

    private String baseLang;

    private String desc;

    private List<String> refLang;

    private Locale locale;

    UnityLangEnum(String baseLang, String desc) {
        this.baseLang = baseLang;
        this.desc = desc;
        this.locale = null;
        this.refLang = Collections.emptyList();
    }

    UnityLangEnum(String baseLang, String desc, Locale locale) {
        this.baseLang = baseLang;
        this.desc = desc;
        this.locale = locale;
        this.refLang = Collections.emptyList();
    }

    UnityLangEnum(String baseLang, String desc, Locale locale, String... refLang) {
        this.baseLang = baseLang;
        this.desc = desc;
        this.locale = locale;
        this.refLang = refLang != null ? Arrays.asList(refLang) : Collections.emptyList();
    }

    public static UnityLangEnum matchLangStr(String langStr) {
        List<UnityLangEnum> collect = Arrays.stream(values()).filter(item ->
                item.baseLang.equalsIgnoreCase(langStr) || item.refLang.contains(langStr)
        ).collect(Collectors.toList());
        return CollectionUtils.isEmpty(collect) ? null : collect.get(0);
    }

    public static void main(String[] args) {
        System.out.println(matchLangStr("zh-CN"));
    }

}
