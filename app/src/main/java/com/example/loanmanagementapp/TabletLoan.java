package com.example.loanmanagementapp;


import java.io.Serializable;
import java.time.LocalDateTime;

enum Brand {ALL,Samsung,Acer;
@Override
public String toString(){
    switch (this){
        case Samsung: return "Samsung";
        case Acer: return "Acer";
        default: return super.toString();
    }
}}
enum Cabel {ALL,USBC,MicroUSB;
    @Override
    public String toString(){
        switch (this){
            case USBC: return "USB-C";
            case MicroUSB: return "Micro-USB";
            default: return super.toString();
        }
    }}

public class TabletLoan implements Serializable {
    private Brand brand;
    private Cabel cabel;
    private String lendersName;
    private String lendersEmail;
    private String dateForLoan;

    public Brand getBrand(){
        return brand;
    }
    public void setBrand(Brand brand){
        this.brand = brand;
    }
    public Cabel getCabel(){
        return cabel;
    }
    public void setCabel(Cabel cabel){
        this.cabel = cabel;
    }
    public String getLendersName(){
        return lendersName;
    }
    public void setLendersName(String lendersName){
        this.lendersName=lendersName;
    }
    public String getLendersEmail(){
        return lendersEmail;
    }
    public void setLendersEmail(String lendersEmail){
        this.lendersEmail=lendersEmail;
    }
    public void setDateForLoan(String dateForLoan){
        this.dateForLoan=dateForLoan;
    }
    public String getDateForLoan(){
        return dateForLoan;
    }


}
