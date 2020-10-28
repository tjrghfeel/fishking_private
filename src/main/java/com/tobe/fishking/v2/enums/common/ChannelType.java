package com.tobe.fishking.v2.enums.common;

public enum ChannelType {

    fishkingwow("낚시가즐거워"),
    channel2("채널2"),
    channel3("채널3");

    private String value;
    ChannelType(String value) {
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }

}
