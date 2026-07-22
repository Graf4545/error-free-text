package com.errorfreetext.domain;

public enum Language {
    EN,
    RU;

    public String toSpellerLang() {
        return name().toLowerCase();
    }
}
