package DomainLayer.Transport;

import java.util.Objects;

public class Date {
    String year;
    String month;
    String day;

    public Date(String day, String month, String year){
        this.day = day;
        this.year = year;
        this.month = month;
    }

    public Date getDate(){
        return this;
    }
    public String toString(){
        return day+"/"+month+"/"+year;
    }

    public void AdvanceDate() {
        if(Integer.parseInt(day)==30){
            day = "01";
            if(Integer.parseInt(month)==12 ){
                month = "01";
                year = String.valueOf(Integer.parseInt(year)+1);
            }
            else {
                if (Integer.parseInt(day) >= 10) {
                    month = String.valueOf(Integer.parseInt(month) + 1);
                }
                else{
                    month = "0"+(Integer.parseInt(month)+1);
                }
            }
        }
        else{
            if(Integer.parseInt(day)>=10){
                day = String.valueOf(Integer.parseInt(day)+1);
            }
            else{
                day = "0"+ (Integer.parseInt(day) + 1);
            }
        }
    }
    public void dayBefore(){
        if(Integer.parseInt(day)==1){
            day = "30";
            if(Integer.parseInt(month)==1 ){
                month = "12";
                year = String.valueOf(Integer.parseInt(year)-1);
            }
            else {
                if (Integer.parseInt(day) > 10) {
                    month = String.valueOf(Integer.parseInt(month) -1);
                }
                else{
                    month = "0"+(Integer.parseInt(month)-1);
                }
            }
        }
        else{
            if(Integer.parseInt(day)>10){
                day = String.valueOf(Integer.parseInt(day)-1);
            }
            else{
                day = "0"+ (Integer.parseInt(day) - 1);
            }
        }
    }
}