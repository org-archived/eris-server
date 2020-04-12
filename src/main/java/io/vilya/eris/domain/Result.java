/**
 * 
 */
package io.vilya.eris.domain;

/**
 * @author cafedada <cafedada@vilya.io>
 * @since 2020-03-30 21:41
 */
public class Result {

    private Integer code;

    private String message;

    private Object data;

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static Result succeeded(Object data) {
        Result result = new Result();
        result.code = 0;
        result.data = data;
        return result;
    }

    public static Result failed(String message) {
        Result result = new Result();
        result.code = 1000;
        result.message = message;
        return result;
    }

}
