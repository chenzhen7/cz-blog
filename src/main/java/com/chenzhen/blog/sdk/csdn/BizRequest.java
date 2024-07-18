package com.chenzhen.blog.sdk.csdn;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author chenjixian
 * @date 2024/7/18 14:12
 * @description:
 */
@SuperBuilder
@Data
public class BizRequest {
    public String cookie;

    public final String xCaKey = "203803574";

    public String xCaNonce;

    public String xCaSignature;

    public String xCaSignatureHeaders;

    private static List<String> chats = new ArrayList<>();

    static {
        //生成 [a, b, c, d, e, f, g, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
        for (int i = 0; i < 7; i++) {
            char c = (char) (i + 97);
            chats.add(String.valueOf(c));
        }
        for (int i = 0; i < 10; i++) {
            char c = (char) (i + 48);
            chats.add(String.valueOf(c));
        }
    }

    public String newXCaNonce() {
        List<String> strs = new ArrayList<>();
        Random rd = new Random();
        for (int i = 0; i < 30; i++) {
            strs.add(chats.get(rd.nextInt(chats.size())));
        }
        return String.format("%s%s%s%s%s%s%s%s-%s%s%s%s-4%s%s%s-9%s%s%s-%s%s%s%s%s%s%s%s%s%s%s%s", strs.toArray());
    }

    public String newXCaSignature(String fullUrl, String method, String onceKey) throws Exception {
        final String ekey = "9znpamsyl2c7cdrr9sas0le9vbc3r6ba";
        final String xcakey = "203803574"; // 开发工具 network 请求头 x-ca-key

        String[] wholdUrl = fullUrl.split("\\?");
        String url, params = "";
        if ("get".equals(method)) {
            url = wholdUrl[0];
            params = wholdUrl[1];
        } else {
            url = wholdUrl[0];
        }
        String _url = url + (!"".equals(params) ? "?" + params : "");
        String to_enc = "";
        if ("get".equals(method)) {
            to_enc = String.format("GET\napplication/json, text/plain, */*\n\n\n\nx-ca-key:%s\nx-ca-nonce:%s\n%s", xcakey, onceKey, _url);
        } else {
            to_enc = String.format("POST\n%s\n\n%s\n\nx-ca-key:%s\nx-ca-nonce:%s\n%s"
                    , "application/json, text/plain, */*", "application/json;charset=UTF-8", xcakey, onceKey, _url);
        }
        return getSHA256StrJava(to_enc, ekey);
    }

    private static String getSHA256StrJava(String content, String secret) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        mac.init(secret_key);
        // System.out.println("key: " + secret + " | 内容是：\n" + content);
        byte[] binaryData = mac.doFinal(content.getBytes());
        return Base64.encodeBase64String(binaryData);
    }




}
