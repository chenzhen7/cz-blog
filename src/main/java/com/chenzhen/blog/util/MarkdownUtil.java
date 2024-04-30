package com.chenzhen.blog.util;

import cn.hutool.core.util.CharUtil;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.AttributeProviderContext;
import org.commonmark.renderer.html.AttributeProviderFactory;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ChenZhen
 * @Description
 * @create 2022/9/13 10:24
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
public class MarkdownUtil {
    /**
     * markdown格式转换成HTML格式
     * @param markdown
     * @return
     */
    public static String markdownToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    /**
     * 增加扩展[标题锚点，表格生成]
     * Markdown转换成HTML
     * @param markdown
     * @return
     */
    public static String markdownToHtmlExtensions(String markdown) {


        //h标题生成id
        Set<Extension> headingAnchorExtensions = Collections.singleton(HeadingAnchorExtension.create());
        //转换table的HTML
        List<Extension> tableExtension = Arrays.asList(TablesExtension.create());
        Parser parser = Parser.builder()
                .extensions(tableExtension)
                .build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder()
                .extensions(headingAnchorExtensions)
                .extensions(tableExtension)
                .attributeProviderFactory(new AttributeProviderFactory() {
                    public AttributeProvider create(AttributeProviderContext context) {
                        return new CustomAttributeProvider();
                    }
                })
                .build();
        // 正则表达式来匹配 <code> 标签并添加类
        return renderer.render(document);
    }

    /**
     * 处理标签的属性
     */
    static class CustomAttributeProvider implements AttributeProvider {
        @Override
        public void setAttributes(Node node, String tagName, Map<String, String> attributes) {
            //改变a标签的target属性为_blank
            if (node instanceof Link) {
                attributes.put("target", "_blank");
            }
            if (node instanceof TableBlock) {
                attributes.put("class", "ui celled table");
            }


        }
    }


    /**
     * 将文本中的所有英文单词包裹在``符号内，形成markdown行内代码块的形式。
     *
     * @param text 要处理的文本内容
     * @return 处理后的文本，所有英文单词已经包裹在``符号内
     */
    public static String wrapWordsInCodeBlock(String text) {
        char[] chars = text.toCharArray();

        StringBuilder result = new StringBuilder();
        boolean inWord = false;
        //'文本中原先已存在的反引号'的字符栈
        int existingBackticksStack = 0;
        //位于'文本中原先已存在的反引号'中
        boolean inExistingBackticks = false;
        //开始反引号包裹
        boolean inStartWrap = false;


        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            // 反引号
            if (ch == '`') {
                //如果当前不在反引号内
                if (!inExistingBackticks){
                    //反引号字符栈数 + 1
                    existingBackticksStack++;
                }else{
                    //反引号字符栈数 - 1
                    existingBackticksStack--;
                    //如果当前已经不在反引号内
                    if (existingBackticksStack == 0){
                        inExistingBackticks = false;
                    }
                }
            }
            //英文字符
            else if (CharUtil.isLetter(ch) || isSpecialCharInWord(i, text)) {
                if (existingBackticksStack <= 0 && !inStartWrap) {
                    // 如果尚未在反引号内部，则开始反引号包裹英文单词
                    result.append('`');
                    inStartWrap = true;
                    inWord = true;
                }else{
                    // 如果已经在反引号内部，则不包裹英文单词
                    inExistingBackticks = true;
                }
            }
            //不是反引号，也不是英文字符
            else {
                if (existingBackticksStack > 0){
                    // 如果已经在反引号内部，则不包裹英文单词
                    inExistingBackticks = true;
                }
                else if (inWord && inStartWrap) {
                    // 如果英文单词已经结束了，关闭单词的反引号包裹
                    result.append('`');
                    inStartWrap = false;
                    inWord = false;
                }
            }
            // 添加字符
            result.append(ch);
        }

        // 如果最后一个字符是单词的一部分，则关闭反引号包裹
        if (inWord && inStartWrap) {
            result.append('`');
        }

        return result.toString();

    }

    /**
     * 如果是'_' '.' '-' '/'等特殊的字符，如果他是单词的一部分，返回true
     * 例如： "emp_contract_serial" -> true , "emp.contract" -> true , "emp-contract" -> true ,
     *
     * @param index
     * @param text
     * @return
     */
    public static boolean isSpecialCharInWord(int index, String text) {
        char ch = text.charAt(index);
        if (!(ch == '_' || ch == '.' || ch == '-' || ch == '/' )) {
            return false;
        }

        if (index > 0 && index < text.length() - 1) {
            char preChar = text.charAt(index - 1);
            char nextChar = text.charAt(index + 1);
            if (CharUtil.isLetter(preChar) && CharUtil.isLetter(nextChar)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isChineseChar(char ch) {
        return ch >= 0x4E00 && ch <= 0x9FA5;
    }

    public static void main(String[] args) {
        String text = "不同于 Python 基础团队遭解雇的不解和惋惜，不少网友对 Flutter 团队被裁表示：“Flutter 就是个笑话，所以这是个好消息”，“Flutter 还有那么多问题要解决，裁什么裁，谷歌应该加大对它的投入吧！”\n" +
                "\n" +
                "针对这个传闻，今日 Flutter 和 Dart 的产品经理 Kevin 在社交平台亲自回应：\n";
        //统计代码执行时间
        long start = System.currentTimeMillis();
        String result = wrapWordsInCodeBlock(text);
        long end = System.currentTimeMillis();
        System.out.println("耗时:" + (end - start) + "ms");
        System.out.println(result);



    }

}
