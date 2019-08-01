package com.zzz.o2o.enums;

public enum ProductCategoryStateEnum {
    OFFLINE(-1,"非法类别"),SUCCESS(1,"操作成功"),
    INNER_ERROR(-1001,"系统内部错误"),NULL_CATEGORYID(-1002,"类别编号为空"),EMPTY_LIST(-1003,"无此类别");
    private int state;
    private String stateInfo;
    ProductCategoryStateEnum(int state, String stateInfo){
        this.state=state;
        this.stateInfo=stateInfo;
    }
    //根据state返回enum对象
    public static ProductCategoryStateEnum stateOf(int state){
        for(ProductCategoryStateEnum stateEnum :values() )
            if (stateEnum.getState() == state) {
                return stateEnum;
            }
        return null;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }
}
