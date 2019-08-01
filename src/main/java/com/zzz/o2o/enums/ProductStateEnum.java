package com.zzz.o2o.enums;

public enum ProductStateEnum {
    OFFLINE(-1,"非法类别"),SUCCESS(1,"操作成功"),
    INNER_ERROR(-1001,"系统内部错误"),NULL_PRODUCTID(-1002,"商品编号为空"),NO_PRODUCT(-1003,"无此商品");
    private int state;
    private String stateInfo;
    ProductStateEnum(int state, String stateInfo){
        this.state=state;
        this.stateInfo=stateInfo;
    }
    //根据state返回enum对象
    public static ProductStateEnum stateOf(int state){
        for(ProductStateEnum stateEnum :ProductStateEnum.values() )
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
