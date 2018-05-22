package com.eclat.firebreathers.epos.Modeclasses;

import android.support.annotation.NonNull;

/**
 * Created by Acer on 9/12/2016.
 */
public class Modelclass {
    @NonNull
    String[] tpproid=new String[20];
    @NonNull
    String[] tpname=new String[20];
    @NonNull
    String[] tpprice=new String[20];
    @NonNull
    String[] tpimg=new String[20];
    String[] tpsubcatid=new String[20];
    @NonNull
    String[] rel_name=new String[20];
    @NonNull
    String[] rel_price=new String[20];
    @NonNull
    String[] rel_img=new String[20];

    public void setRel_flag(@NonNull String rel_flag,int i) {
        this.rel_flag[i] = rel_flag;
    }

    @NonNull
    String[] rel_flag=new String[20];

    public String getRel_proid(int i) {
        return rel_proid[i];
    }

    public void setRel_proid(String rel_proid,int i) {
        this.rel_proid[i] = rel_proid;
    }

    public void setRel_subcatid(String rel_subcatid,int i) {
        this.rel_subcatid[i] = rel_subcatid;
    }

    String[] rel_proid=new String[20];

    String[] rel_subcatid=new String[20];
    @NonNull
    public String getTpproid(int i) {
        return tpproid[i];
    }

    public void setTpproid(String tpproid,int i) {
        this.tpproid[i] = tpproid;
    }

    public String getTpsubcatid(int i) {
        return tpsubcatid[i];
    }

    public void setTpsubcatid(String tpsubcatid,int i) {
        this.tpsubcatid[i] = tpsubcatid;
    }

    public String getRel_img(int i) {
        return rel_img[i];
    }

    public void setRel_img(String rel_img,int i) {
        this.rel_img[i] = rel_img;
    }

    public String getRel_name(int i) {
        return rel_name[i];
    }

    public void setRel_name(String rel_name,int i) {
        this.rel_name[i] = rel_name;
    }

    public String getRel_price(int i) {
        return rel_price[i];
    }

    public void setRel_price(String rel_price,int i) {
        this.rel_price[i] = rel_price;
    }

    public String getTpname(int i) {
        return tpname[i];
    }

    public void setTpname(String tpname,int i) {
        this.tpname[i] = tpname;
    }

    public String getTpprice(int i) {
        return tpprice[i];
    }

    public void setTpprice(String tpprice,int i) {
        this.tpprice[i] = tpprice;
    }

    public String getTpimg(int i) {
        return tpimg[i];
    }

    public void setTpimg(String tpimg,int i) {
        this.tpimg[i] = tpimg;
    }






}

