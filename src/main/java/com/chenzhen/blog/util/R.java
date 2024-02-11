package com.chenzhen.blog.util;



import java.util.HashMap;
import java.util.Map;

/**
 * @author ChenZhen
 * @Description
 * @create 2022/11/19 11:37
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
public class R {

    private Boolean success;

    private Integer code;

    private String message;

    private Map<String,Object>data = new HashMap<>();

    public static Integer SUCCESS = 20000;//成功

    public static Integer ERROR = 20001;//失败


    private R(){

    }

    public static R success(){
        R r = new R();
        r.setSuccess(true);
        r.setCode(R.SUCCESS);
        r.setMessage("成功");
        return r;
    }

    public static R error(){
        R r = new R();
        r.setSuccess(false);
        r.setCode(R.ERROR);
        r.setMessage("失败");
        return r;
    }

    public static R error(String message){
        R r = new R();
        r.setCode(R.ERROR);
        r.setMessage(message);
        return r;
    }

    public static R error(Integer code,String message){
        R r = new R();
        r.setSuccess(false);
        r.setCode(code);
        r.setMessage(message);
//        r.setMessage("失败");
        return r;
    }

    public R success(Boolean success){
        this.setSuccess(success);
        return this;
    }
    public R message(String message){
        this.setMessage(message);
        return this;
    }
    public R code(Integer code){
        this.setCode(code);
        return this;
    }
    public R data(String key,Object value){
        this.data.put(key, value);
        return this;
    }
    public R data(Map<String,Object>map){
        this.setData(map);
        return this;
    }


    @Override
    public String toString() {
        return "R{" +
                "success=" + success +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
