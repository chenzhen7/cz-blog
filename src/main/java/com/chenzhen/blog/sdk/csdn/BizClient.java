package com.chenzhen.blog.sdk.csdn;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chenzhen.blog.sdk.CommonResult;
import com.chenzhen.blog.sdk.baidu.LinkSubmitApi;
import com.chenzhen.blog.sdk.baidu.LinkSubmitRequest;
import com.chenzhen.blog.sdk.baidu.LinkSubmitResp;
import lombok.extern.slf4j.Slf4j;

import java.net.HttpCookie;
import java.util.Collections;

/**
 * @author chenjixian
 * @date 2024/7/18 10:20
 * @description:
 */
@Slf4j
public class BizClient {


    public static CommonResult<ListResp> list(ListRequest request) throws Exception {

        final String path =  BizApi.LIST_URL
                + "?pageSize=" + request.getPageSize();

        HttpResponse response = getHttpResponse(request, path);

        //失败
        if (!response.isOk()){
            return new CommonResult<>(false,null,null,null);
        }

        JSONObject jsonObject = JSON.parseObject(response.body());
        String code = jsonObject.getString("code");
        String message = jsonObject.getString("message");
        ListResp resp = JSON.parseObject(jsonObject.getString("data"), ListResp.class);

        return new CommonResult<>(true,code,message,resp);

    }

    public static CommonResult<GetArticleResp> list(GetArticleRequest request) throws Exception {
        final String path =  BizApi.GET_ARTICLE_URL
                + "?id=" + request.getId();

        HttpResponse response = getHttpResponse(request, path);

        //失败
        if (!response.isOk()){
            return new CommonResult<>(false,null,null,null);
        }

        JSONObject jsonObject = JSON.parseObject(response.body());
        String code = jsonObject.getString("code");
        String message = jsonObject.getString("message");
        GetArticleResp resp = JSON.parseObject(jsonObject.getString("data"), GetArticleResp.class);

        return new CommonResult<>(true,code,message,resp);
    }

    private static HttpResponse getHttpResponse(BizRequest request, String path) throws Exception {
        String xCaNonce = request.newXCaNonce();
        HttpResponse response = HttpRequest
                .get(BizApi.HOST + path)
//                .header("content-type", "application/json;charset=utf-8")
                .header("Accept-Language", "zh-CN,zh;q=0.9")
                .header("Accept", "application/json, text/plain, */*")
                .header("Connection", "keep-alive")
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36")
                .header("origin", "https://mp.csdn.net")
                .header("referer", "https://mp.csdn.net/mp_blog/manage/article")
                .header("sec-ch-ua", "\"Google Chrome\";v=\"105\", \"Not)A;Brand\";v=\"8\", \"Chromium\";v=\"105\"")
                .header("sec-ch-ua-mobile", "?0")
                .header("sec-ch-ua-platform", "\"Windows\"")
                .header("sec-fetch-dest", "empty")
                .header("sec-fetch-mode", "cors")
                .header("sec-fetch-site", "same-site")
                .header("uri-name", "feige")
                .header("x-ca-key", request.getXCaKey())
                .header("x-ca-nonce",xCaNonce)
                .header("x-ca-signature", request.newXCaSignature(path,"get",xCaNonce))
                //根据浏览器开发者工具请求头设置
                .header("x-ca-signature-headers", "x-ca-key,x-ca-nonce")
                .header("Cookie", request.getCookie())
                .timeout(60 * 1000)
                .execute();
        return response;
    }


    public static void main(String[] args) throws Exception {
//        ListRequest submitRequest = ListRequest.builder()
//                .pageSize(20)
//                .cookie("uuid_tt_dd=10_19482482760-1713750282821-358991; c_segment=15; UN=ShockChen7; p_uid=U010000; Hm_up_6bcd52f51e9b3dce32bec4a3997715ac=%7B%22islogin%22%3A%7B%22value%22%3A%221%22%2C%22scope%22%3A1%7D%2C%22isonline%22%3A%7B%22value%22%3A%221%22%2C%22scope%22%3A1%7D%2C%22isvip%22%3A%7B%22value%22%3A%220%22%2C%22scope%22%3A1%7D%2C%22uid_%22%3A%7B%22value%22%3A%22ShockChen7%22%2C%22scope%22%3A1%7D%7D; historyList-new=%5B%5D; chat-version=2.1.1; ssxmod_itna=YqGx2DgDBDcAewxl4iwr8Dy7D9mrWDu0ebmrGphx0v2eGzDAxn40iDto=TZYaQ0GG+QL4K4q+46ebAaRIYbjfofdDU4i8DCdnDo3mDYAkDt4DTD34DYDixib8xi5GRD0KDFWqvz19Dm4GWnqDg04Gg/TiD08tcYkiD4qDB+xdDKu09YDDlY0T4iYQ3iiiDY4LxvuRB4EWqQi=DjuiD/+x8oi=oLZF4RLnFsP0aYqGy3KGufLrBjeDH8kNMDn53FOD3mDpxDwhF42DPDhGiBYMR=YmR50qemAmt4bx22iMSoDDAS0zqeD; ssxmod_itna2=YqGx2DgDBDcAewxl4iwr8Dy7D9mrWDu0ebmrGpxnI3p4DsGCDL7paqyrqn4bBQwHUI6kD6GK7iGA4euQKw4rtdT/8qrpunlRY2xKIpM9tqb=iAMR+y7igagOdg7BcS0QRlbxrqhsMWOnihzh6uNql+hqQTheZwKfD07Ex08DYKx4D===; tfstk=fOYoiY16M3SWYQVKEEbSubR9X4oAP8_CIpUdpwBE0tWjOX6JT9XeCKIFvb6pn6J2TQLRearVu_13Yyrz8oPWod4LyYiWTybd8AHtWVd7Nw_EB_BmAYB5OIWUsT9URQ_C8x3lfr5WNdxaz1nh8jbVt62F8gSFujfN9wzPYzr43t1F89JFYt7V912zUwWzubFL8OYelEltKrnAvw7Jo_jqDQW4z1TcZiBwaU4U8o5lqORPhAFpMls2OMYQOyjpah9C_LyrK9TDgU-wQxERUhfH6HvZ9u1AxU_cLUu_st8ktEjWD8rPsaXlbUSZ-fANYnYcyUkQfIdcUGbvDmMRvavkfOsrcx9HiTperiyoH9K9MUSDQxUDdMxeyZ8r3VjzgoraZBUCgXL4AksPGsXs3hBNeAXwTEcmili54s1-Bjc0AksPGsXtijq_dg5fwAC..; cf_clearance=GkCkG9zYxyif5P7xj3RT0XjTl2p5aROqTabqMA0EFE8-1716807051-1.0.1.1-NdpD3pHVBkyzu7Eu7X_Y4OZwCYB7Pb.io7lsfI1nTDqh9lPUydRAAqmynZXQuriaFnwhRNnTO_20iwvPxU0EEA; JSESSIONID=35BFF08E6478E49836A2E3F5553CC217; Hm_lvt_6bcd52f51e9b3dce32bec4a3997715ac=1718961070; management_ques=1718962698590; HMACCOUNT=9A3B728D9BED6EF7; ShockChen7comment_new=1720583662297; c_dl_prid=1720679977621_922058; c_dl_rid=1721117869762_417102; c_dl_fref=https://so.csdn.net/so/search; c_dl_fpage=/download/chaiqichen/3080374; c_dl_um=distribute.pc_search_result.none-task-blog-2%7Eall%7Esobaiduweb%7Edefault-0-121370711.142%5Ev100%5Epc_search_result_base9; c_first_ref=default; dc_sid=fe60fe74cdef56df4d538a1fa8834118; SESSION=c490e693-591e-4ba4-af70-e434ad2b1e0c; loginbox_strategy=%7B%22taskId%22%3A317%2C%22abCheckTime%22%3A1721184978038%2C%22version%22%3A%22ExpA%22%2C%22nickName%22%3A%22%E9%99%88%E9%9C%87_%22%7D; UserName=ShockChen7; UserInfo=da8f91cbe84c46d28c2b84568348b75b; UserToken=da8f91cbe84c46d28c2b84568348b75b; UserNick=%E9%99%88%E9%9C%87_; AU=F23; BT=1721185017727; c_first_page=https%3A//www.csdn.net/; creative_btn_mp=3; c_hasSub=true; dp_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6OTA0MDI1LCJleHAiOjE3MjE4MTA4ODcsImlhdCI6MTcyMTIwNjA4NywidXNlcm5hbWUiOiJTaG9ja0NoZW43In0._SCgz_XCbzfUMkv-sUT7ZeGJ23NbpLHvV-ZD7ZxZhfY; _clck=1a1n3ch%7C2%7Cfnk%7C0%7C1573; __gads=ID=7cfaa1b27a623311:T=1713750863:RT=1721264885:S=ALNI_MZvzRTpFJHSrww8FY4hOEMtI87_DQ; __gpi=UID=00000df51148eb9c:T=1713750863:RT=1721264885:S=ALNI_MbakHPVJRkUnthrZiJG4Xkv8EAWgQ; __eoi=ID=9534d75b1c6243aa:T=1713750863:RT=1721264885:S=AA-AfjYn224lVy8SuGyxAwRvekEJ; FCNEC=%5B%5B%22AKsRol_gu3R1PQ6UUWQ4ymxkzkFMt3HlNKYUcZpH1f67B-eBx5dDiYGhZSv7P0rWbQ5WqCCepBW5LMbf41VK54u86y1SU9Jvc5aajGDJROs4A-M9xdO3G3_t-Mfz23iCGzjcrRykVilu_JiuaLZAYNp7lbTy3NdzMQ%3D%3D%22%5D%5D; fe_request_id=1721267878781_8181_2245501; _clsk=hyplmg%7C1721267890131%7C3%7C0%7Co.clarity.ms%2Fcollect; log_Id_click=2851; dc_session_id=10_1721273138749.940481; c_dsid=11_1721273138727.762724; c_pref=https%3A//mp.csdn.net/mp_blog/manage/article%3Fspm%3D1001.2101.3001.5448; c_ref=https%3A//editor.csdn.net/; c_page_id=default; creativeSetApiNew=%7B%22toolbarImg%22%3A%22https%3A//img-home.csdnimg.cn/images/20230921102607.png%22%2C%22publishSuccessImg%22%3A%22https%3A//img-home.csdnimg.cn/images/20240229024608.png%22%2C%22articleNum%22%3A38%2C%22type%22%3A2%2C%22oldUser%22%3Atrue%2C%22useSeven%22%3Afalse%2C%22oldFullVersion%22%3Atrue%2C%22userName%22%3A%22ShockChen7%22%7D; Hm_lpvt_6bcd52f51e9b3dce32bec4a3997715ac=1721273205; dc_tos=sgsuwl; log_Id_pv=1372; log_Id_view=66917")
//                .build();
//        CommonResult<ListResp> list = list(submitRequest);
//        System.out.println("list = " + list);


        GetArticleRequest submitRequest = GetArticleRequest.builder()
                .id(140489057)
                .cookie("uuid_tt_dd=10_19482482760-1713750282821-358991; c_segment=15; UN=ShockChen7; p_uid=U010000; Hm_up_6bcd52f51e9b3dce32bec4a3997715ac=%7B%22islogin%22%3A%7B%22value%22%3A%221%22%2C%22scope%22%3A1%7D%2C%22isonline%22%3A%7B%22value%22%3A%221%22%2C%22scope%22%3A1%7D%2C%22isvip%22%3A%7B%22value%22%3A%220%22%2C%22scope%22%3A1%7D%2C%22uid_%22%3A%7B%22value%22%3A%22ShockChen7%22%2C%22scope%22%3A1%7D%7D; historyList-new=%5B%5D; chat-version=2.1.1; ssxmod_itna=YqGx2DgDBDcAewxl4iwr8Dy7D9mrWDu0ebmrGphx0v2eGzDAxn40iDto=TZYaQ0GG+QL4K4q+46ebAaRIYbjfofdDU4i8DCdnDo3mDYAkDt4DTD34DYDixib8xi5GRD0KDFWqvz19Dm4GWnqDg04Gg/TiD08tcYkiD4qDB+xdDKu09YDDlY0T4iYQ3iiiDY4LxvuRB4EWqQi=DjuiD/+x8oi=oLZF4RLnFsP0aYqGy3KGufLrBjeDH8kNMDn53FOD3mDpxDwhF42DPDhGiBYMR=YmR50qemAmt4bx22iMSoDDAS0zqeD; ssxmod_itna2=YqGx2DgDBDcAewxl4iwr8Dy7D9mrWDu0ebmrGpxnI3p4DsGCDL7paqyrqn4bBQwHUI6kD6GK7iGA4euQKw4rtdT/8qrpunlRY2xKIpM9tqb=iAMR+y7igagOdg7BcS0QRlbxrqhsMWOnihzh6uNql+hqQTheZwKfD07Ex08DYKx4D===; tfstk=fOYoiY16M3SWYQVKEEbSubR9X4oAP8_CIpUdpwBE0tWjOX6JT9XeCKIFvb6pn6J2TQLRearVu_13Yyrz8oPWod4LyYiWTybd8AHtWVd7Nw_EB_BmAYB5OIWUsT9URQ_C8x3lfr5WNdxaz1nh8jbVt62F8gSFujfN9wzPYzr43t1F89JFYt7V912zUwWzubFL8OYelEltKrnAvw7Jo_jqDQW4z1TcZiBwaU4U8o5lqORPhAFpMls2OMYQOyjpah9C_LyrK9TDgU-wQxERUhfH6HvZ9u1AxU_cLUu_st8ktEjWD8rPsaXlbUSZ-fANYnYcyUkQfIdcUGbvDmMRvavkfOsrcx9HiTperiyoH9K9MUSDQxUDdMxeyZ8r3VjzgoraZBUCgXL4AksPGsXs3hBNeAXwTEcmili54s1-Bjc0AksPGsXtijq_dg5fwAC..; cf_clearance=GkCkG9zYxyif5P7xj3RT0XjTl2p5aROqTabqMA0EFE8-1716807051-1.0.1.1-NdpD3pHVBkyzu7Eu7X_Y4OZwCYB7Pb.io7lsfI1nTDqh9lPUydRAAqmynZXQuriaFnwhRNnTO_20iwvPxU0EEA; JSESSIONID=35BFF08E6478E49836A2E3F5553CC217; Hm_lvt_6bcd52f51e9b3dce32bec4a3997715ac=1718961070; management_ques=1718962698590; HMACCOUNT=9A3B728D9BED6EF7; ShockChen7comment_new=1720583662297; c_dl_prid=1720679977621_922058; c_dl_rid=1721117869762_417102; c_dl_fref=https://so.csdn.net/so/search; c_dl_fpage=/download/chaiqichen/3080374; c_dl_um=distribute.pc_search_result.none-task-blog-2%7Eall%7Esobaiduweb%7Edefault-0-121370711.142%5Ev100%5Epc_search_result_base9; c_first_ref=default; dc_sid=fe60fe74cdef56df4d538a1fa8834118; SESSION=c490e693-591e-4ba4-af70-e434ad2b1e0c; loginbox_strategy=%7B%22taskId%22%3A317%2C%22abCheckTime%22%3A1721184978038%2C%22version%22%3A%22ExpA%22%2C%22nickName%22%3A%22%E9%99%88%E9%9C%87_%22%7D; UserName=ShockChen7; UserInfo=da8f91cbe84c46d28c2b84568348b75b; UserToken=da8f91cbe84c46d28c2b84568348b75b; UserNick=%E9%99%88%E9%9C%87_; AU=F23; BT=1721185017727; c_first_page=https%3A//www.csdn.net/; creative_btn_mp=3; c_hasSub=true; dp_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6OTA0MDI1LCJleHAiOjE3MjE4MTA4ODcsImlhdCI6MTcyMTIwNjA4NywidXNlcm5hbWUiOiJTaG9ja0NoZW43In0._SCgz_XCbzfUMkv-sUT7ZeGJ23NbpLHvV-ZD7ZxZhfY; _clck=1a1n3ch%7C2%7Cfnk%7C0%7C1573; __gads=ID=7cfaa1b27a623311:T=1713750863:RT=1721264885:S=ALNI_MZvzRTpFJHSrww8FY4hOEMtI87_DQ; __gpi=UID=00000df51148eb9c:T=1713750863:RT=1721264885:S=ALNI_MbakHPVJRkUnthrZiJG4Xkv8EAWgQ; __eoi=ID=9534d75b1c6243aa:T=1713750863:RT=1721264885:S=AA-AfjYn224lVy8SuGyxAwRvekEJ; FCNEC=%5B%5B%22AKsRol_gu3R1PQ6UUWQ4ymxkzkFMt3HlNKYUcZpH1f67B-eBx5dDiYGhZSv7P0rWbQ5WqCCepBW5LMbf41VK54u86y1SU9Jvc5aajGDJROs4A-M9xdO3G3_t-Mfz23iCGzjcrRykVilu_JiuaLZAYNp7lbTy3NdzMQ%3D%3D%22%5D%5D; fe_request_id=1721267878781_8181_2245501; _clsk=hyplmg%7C1721267890131%7C3%7C0%7Co.clarity.ms%2Fcollect; log_Id_click=2851; dc_session_id=10_1721273138749.940481; c_dsid=11_1721273138727.762724; c_pref=https%3A//mp.csdn.net/mp_blog/manage/article%3Fspm%3D1001.2101.3001.5448; c_ref=https%3A//editor.csdn.net/; c_page_id=default; creativeSetApiNew=%7B%22toolbarImg%22%3A%22https%3A//img-home.csdnimg.cn/images/20230921102607.png%22%2C%22publishSuccessImg%22%3A%22https%3A//img-home.csdnimg.cn/images/20240229024608.png%22%2C%22articleNum%22%3A38%2C%22type%22%3A2%2C%22oldUser%22%3Atrue%2C%22useSeven%22%3Afalse%2C%22oldFullVersion%22%3Atrue%2C%22userName%22%3A%22ShockChen7%22%7D; Hm_lpvt_6bcd52f51e9b3dce32bec4a3997715ac=1721273205; dc_tos=sgsuwl; log_Id_pv=1372; log_Id_view=66917")
                .build();
        CommonResult<GetArticleResp> list = list(submitRequest);
        System.out.println("list = " + list);

    }
}
