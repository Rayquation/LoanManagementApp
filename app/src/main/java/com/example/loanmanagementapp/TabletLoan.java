package com.example.loanmanagementapp;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

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
    private String loanId; // Use a unique identifier for comparison
    private String tabletName; // Example field

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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Compare memory addresses first
        if (o == null || getClass() != o.getClass()) return false; // Ensure the same class
        TabletLoan that = (TabletLoan) o;
        return Objects.equals(loanId, that.loanId); // Compare unique identifiers
    }

    @Override
    public int hashCode() {
        return Objects.hash(loanId); // Generate hash based on unique identifier
    }

}
