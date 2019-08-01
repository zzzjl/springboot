package com.zzz.o2o.enums;

public enum ShopStateEnum {
    CHECK(0,"审核中"),OFFLINE(-1,"非法店铺"),SUCCESS(1,"操作成功"),PASS(2,"通过"),
    INNER_ERROR(-1001,"系统内部错误"),NULL_SHOPID(-1002,"店铺号为空"),NULL_SHOP(-1003,"无店铺");
    private int state;
    private String stateInfo;
     ShopStateEnum(int state, String stateInfo){
        this.state=state;
        this.stateInfo=stateInfo;
    }
     //根据state返回enum对象
    public static ShopStateEnum stateOf(int state){
        for(ShopStateEnum stateEnum :values() )
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
