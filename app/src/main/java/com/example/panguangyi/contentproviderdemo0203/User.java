package com.example.panguangyi.contentproviderdemo0203;

/**
 * ************************
 * $claass
 * <p>
 * ${date} $Created by panguangyi on 2017/7/3.
 */

public class User {
    public int userId;
    public String userName;
    public boolean isMale;

    @Override
    public String toString() {
        return "userId:" + userId + " userName:" + userName + " isMale:" + isMale;
    }
}
