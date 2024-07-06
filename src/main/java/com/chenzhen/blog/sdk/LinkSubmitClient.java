package com.chenzhen.blog.sdk;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author ChenZhen
 * @Description
 * @create 2023/12/21 22:07
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Slf4j
public class LinkSubmitClient {

    public static CommonResult<LinkSubmitResp> linkSubmit(LinkSubmitRequest request) {

        final String URL = LinkSubmitApi.HOST + LinkSubmitApi.SUBMIT_URL
                + "?site=" + request.getSite()
                + "&token=" + request.getToken();

        HttpResponse response = HttpRequest
                .post(URL)
                .body(String.join("\n", request.getUrls()))
                .timeout(60 * 1000)
                .header("Content-Type", "text/plain")
                .execute();
        //失败
        if (!response.isOk()){

            JSONObject jsonObject = JSON.parseObject(response.body());
            Integer error = jsonObject.getInteger("error");
            String message = jsonObject.getString("message");

            return new CommonResult<>(false,error.toString(),message,null);
        }
        //成功响应
        LinkSubmitResp resp = JSON.parseObject(response.body(), LinkSubmitResp.class);

        return new CommonResult<>(true,null,null,resp);


    }

    public static void main(String[] args) {
        LinkSubmitRequest submitRequest = LinkSubmitRequest.builder()
                .site("https://www.chenzhen.space/")
                .token("dGk5KjwA4OlWEidb")
                .urls(Collections.singletonList("https://www.chenzhen.space/blog/62"))
                .build();
        CommonResult<LinkSubmitResp> submit = linkSubmit(submitRequest);
        System.out.println("submit = " + submit);

//        System.out.println("content = " + result.getResult().getChoices().get(0).getMessage().getContent());

    }

}
