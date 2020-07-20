package com.example.android.gymhelp;

public class Member {

    private String name;
    private String eDate;
    private Integer price;
    private String email;

    public Member(String name, String eDate, Integer price, String email) {
        this.name = name;
        this.eDate = eDate;
        this.price = price;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public Integer getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String geteDate() {
        return eDate;
    }
}
