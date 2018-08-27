package MP;

import java.util.ArrayList;

public class PostOfficeAddresses {
    String postoffice;
    ArrayList<Entry> addresses;

    public PostOfficeAddresses(String postoffice, ArrayList<Entry> addresses) {
        this.postoffice = postoffice;
        this.addresses = addresses;
    }

    public String getPostoffice() {
        return postoffice;
    }

    public ArrayList<Entry> getAddresses() {
        return addresses;
    }

    public boolean doesAddressExist(String address){
        for(int i = 0; i < addresses.size();i++){
            if(address.equals(addresses.get(i).getFrom()) || address.equals(addresses.get(i).getGoingto()))
                return true;
        }

        return false;
    }
}
