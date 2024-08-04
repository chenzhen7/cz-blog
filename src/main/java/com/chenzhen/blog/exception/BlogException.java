package com.chenzhen.blog.exception;

import com.chenzhen.blog.util.R;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ChenZhen
 * @Description
 * @create 2024/8/4 19:32
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Getter
@Setter
public class BlogException extends RuntimeException{

    private Integer code;
    private String msg;


    public BlogException(String msg) {
        this.code = R.ERROR;
        this.msg = msg;
    }

    public BlogException(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }

}
