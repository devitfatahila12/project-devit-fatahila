package com.example.contoh;

public class modelActivity {

    private String id, account_id, group, title, message, page_link, data, created_dt, rating;

    public void setId(String id){
        this.id = id;
    }

    public void setAccount_id(String account_id){this.account_id = account_id; }

    public void setGroup(String group){
        this.group = group;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void setPage_link(String page_link){
        this.page_link = page_link;
    }

    public void setData(String data){
        this.data = data;
    }

    public void setCreated_dt(String created_dt){
        this.created_dt = created_dt;
    }

    public void setRating(String rating){
        this.rating = rating;
    }

    public String getId(){
        return  id;
    }

    public String getAccount_id(){
        return  account_id;
    }

    public String getGroup(){
        return  group;
    }

    public String getTitle(){
        return  title;
    }

    public String getMessage(){
        return  message;
    }

    public String getPage_link(){
        return  page_link;
    }

    public String getData(){
        return  data;
    }

    public String getCreated_dt(){
        return  created_dt;
    }

    public String getRating(){
        return  rating;
    }
}
