package com.tobe.fishking.v2.enums.common;

//import com.tobe.fishking.v2.enums.IEnumModel;

public enum ChannelType  /*implements IEnumModel*/ {

    fishkingwow("낚시가즐거워"),
    channel2("채널2"),
    channel3("채널3");

    private String value;
    ChannelType(String value) {
        this.value = value;
    }

    /*@Override
    public String getKey() {
        return name();
    }

    @Override
    public String getValue() {
        return this.value;
    }*/

}
