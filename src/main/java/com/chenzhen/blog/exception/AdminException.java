package com.chenzhen.blog.exception;

import com.chenzhen.blog.util.R;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ChenZhen
 * @Description
 * @create 2024/7/20 21:48
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Getter
@Setter
public class AdminException extends RuntimeException{

    private Integer code;
    private String msg;


    public AdminException(String msg) {
        this.code = R.ERROR;
        this.msg = msg;
    }

    public AdminException(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }


}
