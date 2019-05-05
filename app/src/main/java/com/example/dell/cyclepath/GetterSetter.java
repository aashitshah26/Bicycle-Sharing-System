package com.example.dell.cyclepath;

class GetterSetter {
    String name, Walmoney, mobno;
    GetterSetter(String name, String Walmoney, String mobno){
        this.name=name;
        this.Walmoney=Walmoney;
        this.mobno=mobno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobno() {
        return mobno;
    }

    public void setMobno(String mobno) {
        this.mobno = mobno;
    }

    public String getWalmoney() {
        return Walmoney;
    }

    public void setWalmoney(String walmoney) {
        Walmoney = walmoney;
    }
}
