//package com.chenzhen.blog;
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.http.HttpRequest;
//import cn.hutool.http.HttpResponse;
//import com.chenzhen.blog.entity.pojo.Site;
//import com.chenzhen.blog.entity.pojo.SiteCategory;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.openqa.selenium.Cookie;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import java.io.IOException;
//import java.util.Set;
//
//
//@SpringBootTest
//@Slf4j
//@EnableTransactionManagement
//class BlogApplicationTests {
//
//
//    public static void main(String[] args) {
//
////        // 设置 Chrome WebDriver 的路径
////        System.setProperty("webdriver.chrome.driver", "D:\\IntelliJ IDEA 2022.3.3\\project\\cz-blog\\src\\main\\resources\\chromedriver.exe");
////        // 创建 Chrome WebDriver 实例
////        WebDriver driver = new ChromeDriver();
////        // 打开网页
////        driver.get("http://example.com");
////
////        String fileName = "D:\\IntelliJ IDEA 2022.3.3\\project\\blog\\src\\main\\resources\\site.json";
////        //读取
////        ObjectMapper objectMapper = new ObjectMapper();
////        // Parse the JSON content into a Category object
////        List<SiteCategory> siteCategories = objectMapper.readValue(new File(fileName), objectMapper.getTypeFactory().constructCollectionType(List.class, SiteCategory.class));
////
////        System.out.println("siteCategories = " + siteCategories);
////
////        for (SiteCategory siteCategory : siteCategories) {
////            siteCategoryService.save(siteCategory);
////            if (CollUtil.isNotEmpty(siteCategory.getSites())){
////                for (Site site : siteCategory.getSites()) {
////                    site.setCategoryId(siteCategory.getId());
////                    siteService.save(site);
////                }
////            }
////            if (CollUtil.isNotEmpty(siteCategory.getSubSiteCategories())){
////                for (SiteCategory subSiteCategory : siteCategory.getSubSiteCategories()) {
////                    subSiteCategory.setParentId(siteCategory.getId());
////                    siteCategoryService.save(subSiteCategory);
////                    if (CollUtil.isNotEmpty(subSiteCategory.getSites())){
////                        for (Site site : subSiteCategory.getSites()) {
////                            site.setCategoryId(subSiteCategory.getId());
////                            siteService.save(site);
////                        }
////                    }
////                }
////            }
////
////
////
////
////    }
////
////
////        System.out.println("response = " + response);
//    }
//
//    @Test
//    public void test() throws IOException {
//
//        String fileName = "D:\\IntelliJ IDEA 2022.3.3\\project\\blog\\src\\main\\resources\\site.json";
//        //读取
//        ObjectMapper objectMapper = new ObjectMapper();
//        // Parse the JSON content into a Category object
//        List<SiteCategory> siteCategories = objectMapper.readValue(new File(fileName), objectMapper.getTypeFactory().constructCollectionType(List.class, SiteCategory.class));
//
//        System.out.println("siteCategories = " + siteCategories);
//
//        for (SiteCategory siteCategory : siteCategories) {
//            siteCategoryService.save(siteCategory);
//            if (CollUtil.isNotEmpty(siteCategory.getSites())){
//                for (Site site : siteCategory.getSites()) {
//                    site.setCategoryId(siteCategory.getId());
//                    siteService.save(site);
//                }
//            }
//            if (CollUtil.isNotEmpty(siteCategory.getSubSiteCategories())){
//                for (SiteCategory subSiteCategory : siteCategory.getSubSiteCategories()) {
//                    subSiteCategory.setParentId(siteCategory.getId());
//                    siteCategoryService.save(subSiteCategory);
//                    if (CollUtil.isNotEmpty(subSiteCategory.getSites())){
//                        for (Site site : subSiteCategory.getSites()) {
//                            site.setCategoryId(subSiteCategory.getId());
//                            siteService.save(site);
//                        }
//                    }
//                }
//            }
//
//
//
//
//    }
//
//
//
//}

//        System.out.println("response = " + response);