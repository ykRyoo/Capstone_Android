package com.example.capstone;


public class CommentInfo {

    private String boardName;
    private String who;
    private String time;
    private String comment;


    public CommentInfo(String boardName, String who, String time, String comment) {
        this.boardName = boardName;
        this.who = who;
        this.time = time;
        this.comment = comment;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
