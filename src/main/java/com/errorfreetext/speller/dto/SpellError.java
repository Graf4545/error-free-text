package com.errorfreetext.speller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SpellError {
    private int code;
    private int pos;
    private int row;
    private int col;
    private int len;
    private String word;
    private List<String> s;

    public SpellError() {
    }

    public SpellError(int code, int pos, int row, int col, int len, String word, List<String> s) {
        this.code = code;
        this.pos = pos;
        this.row = row;
        this.col = col;
        this.len = len;
        this.word = word;
        this.s = s;
    }

    public int code() {
        return code;
    }

    public int pos() {
        return pos;
    }

    public int row() {
        return row;
    }

    public int col() {
        return col;
    }

    public int len() {
        return len;
    }

    public String word() {
        return word;
    }

    public List<String> s() {
        return s;
    }

    public int getCode() {
        return code;
    }

    public int getPos() {
        return pos;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getLen() {
        return len;
    }

    public String getWord() {
        return word;
    }

    public List<String> getS() {
        return s;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setS(List<String> s) {
        this.s = s;
    }
}
