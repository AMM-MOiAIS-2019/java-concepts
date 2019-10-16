package com.apakhomov;

public interface TextProvider {
    String getText();

    void close(String string);
}
