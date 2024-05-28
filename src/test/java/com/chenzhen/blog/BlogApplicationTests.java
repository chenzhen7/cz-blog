package com.chenzhen.blog;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Set;


@SpringBootTest
@Slf4j
@EnableTransactionManagement
class BlogApplicationTests {


    public static void main(String[] args) {

//        // 设置 Chrome WebDriver 的路径
//        System.setProperty("webdriver.chrome.driver", "D:\\IntelliJ IDEA 2022.3.3\\project\\cz-blog\\src\\main\\resources\\chromedriver.exe");
//        // 创建 Chrome WebDriver 实例
//        WebDriver driver = new ChromeDriver();
//        // 打开网页
//        driver.get("http://example.com");
//
//        // 获取当前页面的所有 Cookie
//        Set<Cookie> cookies = driver.manage().getCookies();
//
//        // 打印 Cookie 信息
//        for (Cookie cookie : cookies) {
//            System.out.println("Name: " + cookie.getName());
//            System.out.println("Value: " + cookie.getValue());
//            System.out.println("Domain: " + cookie.getDomain());
//            System.out.println("Path: " + cookie.getPath());
//            System.out.println("Expiry: " + cookie.getExpiry());
//            System.out.println("Secure: " + cookie.isSecure());
//            System.out.println("HttpOnly: " + cookie.isHttpOnly());
//            System.out.println("--------------------------------------");
//        }
//
//        // 关闭浏览器
//        driver.quit();

        //登录获取token
        HttpRequest.post("https://passport.csdn.net/v1/register/pc/login/doLogin")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.142.86 Safari/537.36")
                .header("Content-Type", "application/json;charset=UTF-8")
                .body();




        HttpResponse response = HttpRequest.get("https://bizapi.csdn.net/blog-console-api/v3/editor/getArticle?id=138047741")
                .header("Cookie", "uuid_tt_dd=10_19482482760-1713750282821-358991; c_segment=15; UserName=ShockChen7; UserInfo=c392575b31a443d3817feb3f1e113df5; UserToken=c392575b31a443d3817feb3f1e113df5; UserNick=%E9%99%88%E9%9C%87_; AU=F23; UN=ShockChen7; BT=1713750303390; p_uid=U010000; Hm_up_6bcd52f51e9b3dce32bec4a3997715ac=%7B%22islogin%22%3A%7B%22value%22%3A%221%22%2C%22scope%22%3A1%7D%2C%22isonline%22%3A%7B%22value%22%3A%221%22%2C%22scope%22%3A1%7D%2C%22isvip%22%3A%7B%22value%22%3A%220%22%2C%22scope%22%3A1%7D%2C%22uid_%22%3A%7B%22value%22%3A%22ShockChen7%22%2C%22scope%22%3A1%7D%7D; historyList-new=%5B%5D; SESSION=8b7275d6-496f-48d0-a841-fbe4105dac06; chat-version=2.1.1; ShockChen7comment_new=1715741284527; JSESSIONID=23656630C9A5EE193A39F6BD0C19F91D; Hm_lvt_6bcd52f51e9b3dce32bec4a3997715ac=1716368056; c_dl_prid=1716447542320_215835; c_dl_rid=1716464472934_836059; c_dl_fref=https://blog.csdn.net/ZGL_cyy/article/details/128782594; c_dl_fpage=/download/qq_41604569/87639900; c_dl_um=distribute.pc_relevant.none-task-blog-2%7Edefault%7Ebaidujs_utm_term%7Edefault-1-135988584-blog-128782594.235%5Ev43%5Epc_blog_bottom_relevance_base2; c_hasSub=true; _clck=1a1n3ch%7C2%7Cfm4%7C0%7C1573; creative_btn_mp=3; c_first_ref=default; ssxmod_itna=YqGx2DgDBDcAewxl4iwr8Dy7D9mrWDu0ebmrGphx0v2eGzDAxn40iDto=TZYaQ0GG+QL4K4q+46ebAaRIYbjfofdDU4i8DCdnDo3mDYAkDt4DTD34DYDixib8xi5GRD0KDFWqvz19Dm4GWnqDg04Gg/TiD08tcYkiD4qDB+xdDKu09YDDlY0T4iYQ3iiiDY4LxvuRB4EWqQi=DjuiD/+x8oi=oLZF4RLnFsP0aYqGy3KGufLrBjeDH8kNMDn53FOD3mDpxDwhF42DPDhGiBYMR=YmR50qemAmt4bx22iMSoDDAS0zqeD; ssxmod_itna2=YqGx2DgDBDcAewxl4iwr8Dy7D9mrWDu0ebmrGpxnI3p4DsGCDL7paqyrqn4bBQwHUI6kD6GK7iGA4euQKw4rtdT/8qrpunlRY2xKIpM9tqb=iAMR+y7igagOdg7BcS0QRlbxrqhsMWOnihzh6uNql+hqQTheZwKfD07Ex08DYKx4D===; tfstk=fOYoiY16M3SWYQVKEEbSubR9X4oAP8_CIpUdpwBE0tWjOX6JT9XeCKIFvb6pn6J2TQLRearVu_13Yyrz8oPWod4LyYiWTybd8AHtWVd7Nw_EB_BmAYB5OIWUsT9URQ_C8x3lfr5WNdxaz1nh8jbVt62F8gSFujfN9wzPYzr43t1F89JFYt7V912zUwWzubFL8OYelEltKrnAvw7Jo_jqDQW4z1TcZiBwaU4U8o5lqORPhAFpMls2OMYQOyjpah9C_LyrK9TDgU-wQxERUhfH6HvZ9u1AxU_cLUu_st8ktEjWD8rPsaXlbUSZ-fANYnYcyUkQfIdcUGbvDmMRvavkfOsrcx9HiTperiyoH9K9MUSDQxUDdMxeyZ8r3VjzgoraZBUCgXL4AksPGsXs3hBNeAXwTEcmili54s1-Bjc0AksPGsXtijq_dg5fwAC..; cf_clearance=_lik6kaaiU4CemMugDJzo49p9l_WGNVHI6HnOGOpLcQ-1716796233-1.0.1.1-5H5IIPxInLK_YYriCuvQiKoNRojxcMwFYYnIuoRJcSOAFQaCC8BmTQxv1f3BmzQqKFyxumg7tCuje9qG2gCA.Q; https_ydclearance=642cd183ee8d1d6a44542fb3-ecbc-47e1-904a-c0d5e8a1de45-1716809034; c_utm_term=csdn%E5%8D%9A%E5%AE%A2%E5%90%8C%E6%AD%A5%E8%87%B3%E7%A4%BE%E5%8C%BA; dc_sid=8412f4bc53159b860c9b4089a0f00228; referrer_search=1716801846941; __gads=ID=7cfaa1b27a623311:T=1713750863:RT=1716801847:S=ALNI_MZvzRTpFJHSrww8FY4hOEMtI87_DQ; __gpi=UID=00000df51148eb9c:T=1713750863:RT=1716801847:S=ALNI_MbakHPVJRkUnthrZiJG4Xkv8EAWgQ; __eoi=ID=9534d75b1c6243aa:T=1713750863:RT=1716801847:S=AA-AfjYn224lVy8SuGyxAwRvekEJ; FCNEC=%5B%5B%22AKsRol_BfT8ehort91DT6Nbt5HjvbO6JpS_4LQoIv9af6N6tIqVRuYXdK7LqIXWRgrTT5ykTuPdu3ULvZGnfcOWgKcV0rzsTWnYAmfB3B14JC38fO1gDkhdBxX41Mhmmo0tA5yiap2CnCg-F_jq0zYv9LPBD5emsDg%3D%3D%22%5D%5D; c_utm_relevant_index=10; relevant_index=10; c_page_id=default; _clsk=kjv1c5%7C1716801983370%7C6%7C0%7Cw.clarity.ms%2Fcollect; c_utm_medium=distribute.pc_toolbar_associateword.none-task-associate_word-opensearch_query-1-csdn%3Cem%3E%E5%8D%9A%E5%AE%A2%E5%90%8C%E6%AD%A5%3C/em%3E%E8%87%B3%E7%A4%BE%E5%8C%BA-null-null.179%5Ev5%5Epv; fe_request_id=1716802057076_8682_0960211; Hm_lpvt_6bcd52f51e9b3dce32bec4a3997715ac=1716802057; log_Id_view=16811; log_Id_click=770; dc_tos=se533k; dc_session_id=10_1716804809759.613945; c_pref=default; c_ref=default; c_first_page=https%3A//editor.csdn.net/md/%3FarticleId%3D138047741; c_dsid=11_1716804848653.777530; log_Id_pv=344")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36")
                .header("X-Ca-Key","203803574")
                .execute();

        System.out.println("response = " + response);
    }

    @Test
    public void test() {


    }



}
