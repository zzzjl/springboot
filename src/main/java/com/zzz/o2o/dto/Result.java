package com.zzz.o2o.dto;

public class Result<T> {
    private boolean success;//是否成功的标志
    private String errorMsg;//错误信息
     private T data;//成功时返回的数据
    private int errorCode;
    public Result(){
    }
    //成功时的构造器
    public Result(boolean success ,T data){
        this.success = success;
        this.data = data;
    }
    //失败时的构造器
    public Result(boolean success,String errorMsg,int errorCode){
        this.success = success;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public T getData() {
        return data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
