package com.example.panguangyi.contentproviderdemo0203;

/**
 * ************************
 * $claass
 * <p>
 * ${date} $Created by panguangyi on 2017/7/3.
 */

public class Book {
    public int bookId;
    public String bookName;

    @Override
    public String toString() {
        return " bookId:" + bookId + " bookName:" + bookName;
    }
}
