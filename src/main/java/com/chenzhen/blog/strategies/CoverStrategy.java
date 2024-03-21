package com.chenzhen.blog.strategies;



import javax.servlet.http.HttpServletResponse;


public interface CoverStrategy {

    void download(String filepath, HttpServletResponse response) ;
}
