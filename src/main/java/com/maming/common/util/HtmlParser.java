package com.maming.common.util;

import java.util.regex.*;
/**
 * 解析html内容，将<>html标签删除，仅保留文本内容
 */
public class HtmlParser {

    // 正则表达式匹配HTML标签
    private static final Pattern HTML_TAG_PATTERN = Pattern.compile("<[^>]+>");//去除html标签内容
    // 正则表达式匹配段落结束标签
    private static final Pattern PARAGRAPH_END_TAG_PATTERN = Pattern.compile("</div>", Pattern.CASE_INSENSITIVE);//将div标签特殊识别出来，用于拆分

    public static String removeHtmlTags(String html) {
        if (html == null || html.isEmpty()) {
            return html;
        }
        
        // 首先将段落结束标签替换为逗号
        Matcher paragraphMatcher = PARAGRAPH_END_TAG_PATTERN.matcher(html);
        String withCommas = paragraphMatcher.replaceAll(",");
        
        // 然后去除所有HTML标签
        Matcher matcher = HTML_TAG_PATTERN.matcher(withCommas);
        return matcher.replaceAll("").replaceAll(",+", "\r\n");//连续的逗号，转换成回车
    }
    
	public static void main(String[] args) {
		System.out.println("------");
		String html = "";
		String html_convert = removeHtmlTags(html);
		System.out.println(html_convert);
	}

}
