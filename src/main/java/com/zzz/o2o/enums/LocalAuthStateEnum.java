package com.zzz.o2o.enums;

public enum LocalAuthStateEnum {
    SUCCESS(1,"操作成功"),
    INNER_ERROR(-1001,"系统内部错误"),NULL_USERID(-1002,"用户编号为空");
    private int state;
    private String stateInfo;
    LocalAuthStateEnum(int state, String stateInfo){
        this.state=state;
        this.stateInfo=stateInfo;
    }
    //根据state返回enum对象
    public static LocalAuthStateEnum stateOf(int state){
        for(LocalAuthStateEnum stateEnum :values() )
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
