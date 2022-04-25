package com.example.capstone;

import java.io.Serializable;
import java.util.Date;

public class PostInfo implements Serializable {
    private String title;
    private String contents;
    private String publisher;
    private String createdAt;
    private String id;
    private String nickName;

    public PostInfo(String title, String contents, String publisher, String createdAt, String id, String nickName){
        this.title = title;
        this.contents = contents;
        this.publisher = publisher;
        this.createdAt = createdAt;
        this.id = id;
        this.nickName = nickName;

    }

    public PostInfo(String title, String contents, String publisher, String nickName,String createdAt){
        this.title = title;
        this.contents = contents;
        this.publisher = publisher;
        this.createdAt = createdAt;
        this.nickName = nickName;
    }

    public String getTitle(){return this.title;}
    public void setTitle(String title){this.title = title;}

    public String getContents(){return this.contents;}
    public void setContents(String contents){this.contents = contents;}

    public String getPublisher(){return this.publisher;}
    public void setPublisher(String publisher){this.publisher = publisher;}

    public String getCreatedAt (){return this.createdAt;}
    public void setCreatedAt (String createdAt){this.createdAt = createdAt;}

    public String getId (){return this.id;}
    public void setId (String id){this.id = id;}

    public String getNickName (){return this.nickName;}
    public void setNickName (String nickName){this.nickName = nickName;}


}
