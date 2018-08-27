package MP;

import java.util.Comparator;

public class Mail {
    private String recipient;
    private String destination;
    private String postoffice;

    private int month;
    private int day;
    private int year;
    private int time;
  

    public Mail(String recipient, String destination, String postoffice) {
        this.recipient = recipient;
        this.destination = destination;
        this.postoffice = postoffice;
    }

    public Mail(String recipient, String destination, String postoffice, int month, int day, int year) {
        this.recipient = recipient;
        this.destination = destination;
        this.postoffice = postoffice;

        this.month = month;
        this.day = day;
        this.year = year;
    }
    

    public String getPostoffice() {
        return postoffice;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getDestination() {
        return destination;
    }


    public void printMail(){
        System.out.println("=================");
        System.out.println("Mailed to " + this.recipient + " going to " + this.destination);

        System.out.println("=================");
    }

    public String[] getString(){
        String arts[] = new String[6];
        arts[0] =   "__________________";
        arts[1] = "|\\                                /| Mailed to: " + this.recipient + "\r\n";
        arts[2] = "| \\                              / |" + this.destination + " ";
        arts[3] = "| /\\______________/\\ |";
        arts[4] = "|/                                \\|";
        arts[5] = "|_________________|";


        //"__________________\n
        //|\                /| Mailed to: " + this.recipient + "\n
        //| \              / | " + this.destination + " " + this.potodestination + "km.\n
        //| /\____________/\ | " + "this.month" + "/" + this.day + "/" + this.year + "\n
        //|/                \| \n
        //|__________________| \n"

        return arts;
    }
    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getYear() {
        return year;
    }

    public int getTime() {
        return time;
    }


}

class Sortbydate implements Comparator<Mail> {
    public int compare(Mail a, Mail b) {
        int i = 1;
        if(a.getYear() == b.getYear()){
            if(a.getMonth() == b.getMonth()){
                if(a.getDay() == b.getDay())
                    if (a.getTime() < b.getTime()){
                        i = -1;
                    }
                    else
                        i = 1;
            }
            else if(a.getMonth() < b.getMonth()) i = -1;
        }
        else if(a.getYear() < b.getYear()) i = -1;
        return i;
    }
}






