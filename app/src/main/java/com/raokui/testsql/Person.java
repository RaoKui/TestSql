package com.raokui.testsql;

/**
 * Created by 饶魁 on 2017/12/22.
 */

@DbTable("db_person")
public class Person {

    @DbField("tb_name")
    public String name;

    @DbField("tb_password")
    public Long password;

    @DbField("tb_photo")
    public byte[] photo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPassword() {
        return password;
    }

    public void setPassword(Long password) {
        this.password = password;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
}
