package MP;


import MP.gfx.*;
import java.io.*;
import java.util.*;

public class Main {


    /* ------------------------------ MAIN FUNCTION -------------------------------- */
    public static ArrayList<PostOfficeAddresses> addresslist;

    public static void main(String args[]){

        Scanner sc = new Scanner(System.in);


        addresslist = generateAddresses(readFile());

        String mailmanloc = "";
        int postindex =-1;

        ArrayList<Mail> maillist = new ArrayList<>();
        ArrayList<Mail> bag = new ArrayList<>(); // mail list for different post office

        while(postindex == -1){
            System.out.print("Enter the location of the mailman: ");
            mailmanloc = "Manila City"; //sc.nextLine(); // Manila City
            postindex = getPostOfficeindex(mailmanloc,addresslist);
        }

        gawaingPostOffice(maillist,bag,mailmanloc);


        while(!bag.isEmpty()){
            mailmanloc = bag.get(0).getPostoffice();
            gawaingPostOffice(maillist,bag,mailmanloc);
        }

        System.exit(0);
    }

    static void gawaingPostOffice(ArrayList<Mail> maillist, ArrayList<Mail> bag, String location){

        System.out.println("Currently in " + location.toUpperCase());
        int postindex;
        Scanner sc = new Scanner(System.in);

        //transfers the laman of the bag if parehas yung post office ng mail
        for(int i = 0; i < bag.size();i++){
            if(bag.get(i).getPostoffice().equals(location)){
                maillist.add(bag.get(i));
                bag.remove(i);
                i--;
            }
        }


        int numofmails;
        System.out.print("Enter number of mails: ");
        numofmails = sc.nextInt();

        Mail mail;

        PostOfficeAddresses poaddresslist;
        postindex = getPostOfficeindex(location,addresslist);
        poaddresslist = addresslist.get(postindex);

        //ask for mails tapos if parehas yung location add to mail list else wag
        for(int i = 0; i < numofmails; i++){
            mail = askMail(i);

            if(mail.getPostoffice().equals(location)){
                maillist.add(mail);
            }
            else{
                bag.add(mail);
            }
        }

        //pang test shortcut

//        for(int i = 0; i < 10; i++){
//            mail = new Mail("Recepient", addresslist.get(0).getAddresses().get(i * 3).getFrom(), addresslist.get(0).getPostoffice());
//            if(mail.getPostoffice().equals(location)){
//                maillist.add(mail);
//            }
//            else{
//                bag.add(mail);
//            }
//        }

        System.out.println(maillist.size());

        Collections.sort(bag, new Sortbydate());
        distributeMails(maillist,poaddresslist); //mail list
        return;
    }

    static Mail askMail(int mn){
        String recipient;
        String destination = "";
        boolean exist = false;
        boolean instance = false;
        int index = 0;
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter details of mail number " + (mn + 1) +" below.");
//        System.out.print("Enter the Recepient's Date of Delivery (MM/DD/YYYY): ");
//        String date = sc.nextLine();
//        String datestr[] = date.split("/",3);
//        int month = Integer.parseInt(datestr[0]);
//        int day = Integer.parseInt(datestr[1]);
//        int year = Integer.parseInt(datestr[2]);
//        sc.nextLine();

        System.out.print("Enter the Recepient: ");
        recipient = sc.nextLine();


        while(!exist){
            System.out.print("Enter the Recipient Address: ");
            destination = sc.nextLine();
            for(int i = 0; i < addresslist.size();i++){
                instance = addresslist.get(i).doesAddressExist(destination);
                if(instance){
                    exist = instance;
                    index = i;
                    break;
                }
            }
        }



        return new Mail(recipient,destination,addresslist.get(index).getPostoffice());
        //return new MP.Mail(recipient,destination,addresslist.get(index).getPostoffice(),month,day,year);

    }


    static int getPostOfficeindex(String location, ArrayList<PostOfficeAddresses> addresslist){
        for(int i = 0; i < addresslist.size();i++){
            if(addresslist.get(i).getPostoffice().equals(location))
                return i;
        }

        return -1;
    }



    /*----------------------------- ROUTE ALGORITHM------------------------------------------*/
    private static float optimalDistance;
    private static String optimalRoute[];
    private static float edges[][];
    private static float traffic[][];


    static void distributeMails(ArrayList<Mail> maillist, PostOfficeAddresses addresses){
        AdjacencyMatrix destinations = generateMatrix(maillist,addresses);
        destinations.printMatrix();

        String route = "";
        edges = destinations.getEdges();
        traffic = destinations.getTraffic();
        optimalDistance = Float.MAX_VALUE;
        optimalRoute = new String[destinations.getVertices().length + 2];


        recursiveroute(destinations.getVertices()[0], destinations.getVertices(), route,0);
        System.out.println(optimalDistance);
        for(int i = 1; i < optimalRoute.length - 1;i++){
            System.out.print(optimalRoute[i] + " - > ");
        }
        System.out.println("END");

        Maprender animation = new Maprender(optimalRoute);
        animation.run();


        while(animation.isRunning()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



        for(int i = 0; i < optimalRoute.length; i++){
            for(int g = 0; g < maillist.size(); g++){
                if (optimalRoute[i].equals(maillist.get(g).getDestination())){
                    maillist.remove(g);
                    g--;
                }
            }
        }
    }

    static float recursiveroute(Vertice start, Vertice addresses[], String route, float distancetravelled){
        route = route + start.getAddress() + ">";
        int naddressesleft = addresses.length;
        float newdistance;

        if(distancetravelled > optimalDistance){
            return 0; // itigil na natin to kasi ang layo na ng iyong narating
        }

        else if(naddressesleft == 0){
            //wala ka nang pupuntahan nasa dulo na
            newdistance = distancetravelled;

            if(newdistance < optimalDistance){
                //eto na ang optimal distance
                optimalDistance = newdistance;
                //optimalRoute = route + "END";
                route = route + "END";
                optimalRoute = route.split(">");
            }

            return 0;
        }

        else{
            //normal case of traversing tha adjacency matrix

            Vertice[][]addressesleft = new Vertice[naddressesleft][naddressesleft - 1];
            float distancecurrent;
            float distancefuture;
            float bestdistance = Float.MAX_VALUE;
            float totaldistance;

            for(int i = 0; i < naddressesleft; i++){
                //for every address left / tinatanggal na yung vertice na napuntahan na
                for(int g = 0, n = 0; g < naddressesleft; g++, n++){
                    if(g == i){
                        n--;
                        continue;
                    }
                    addressesleft[i][n] = addresses[g];
                }

                //get the distance between starting vertice (parent) to the current vertice (child)
                distancecurrent = edges[start.getId()][addresses[i].getId()] + traffic[start.getId()][addresses[i].getId()];

                //final distance from the pinaka simula hanggang sa vertice na itech
                newdistance = distancetravelled + distancecurrent;

                //gets distance of this vertice na pag tinuloy tuloy hanggang sa dulong vertice bale parang distance na nacomplete na yung delivery
                // pero ang catch is automatic siyang titigil if yung distance is greater than the optimal distance
                distancefuture = recursiveroute(addresses[i],addressesleft[i],route,newdistance);

                totaldistance = distancecurrent + distancefuture;

                if (totaldistance < bestdistance)
                    bestdistance = totaldistance;
                }

                return bestdistance;
            }

    }



    /* --------------------- CREATING AN ADJACENCY MATRIX USING CSV  --------------------------- */

    /* --------------------- GENERATE ADJACENCY MATRIX USING ENTRYLIST  --------------------------- */
//    static ArrayList<MP.AdjacencyMatrix> generateAdjacencyMatrices(ArrayList<MP.Entry> entrylist){
//
//        ArrayList<MP.AdjacencyMatrix> maps = new ArrayList<>();
//
//        while(!entrylist.isEmpty()){
//            String po = entrylist.get(0).getPostOffice(); // post office name
//            int poa = 1;                                 // number of addresses
//            ArrayList<MP.Entry> entpo = new ArrayList<>();  // entry list with the same post office
//
//            for(int i = 0; i < entrylist.size(); i++){
//                if(entrylist.get(i).getPostOffice().equals(po)){
//                    entpo.add(entrylist.get(i));
//                    if(entrylist.get(i).getFrom().contains("Post Office"))
//                        poa++;
//                    entrylist.remove(i);
//                    i--;
//
//                }
//            }
//
//            System.out.println(poa);
//            maps.add(new MP.AdjacencyMatrix(entpo, poa, po));
//        }
//
//        return maps;
//    }

    static AdjacencyMatrix generateMatrix(ArrayList<Mail> maillist, PostOfficeAddresses addresslist){

        ArrayList<String> locations = new ArrayList<>();
        ArrayList<Entry> entrylist = new ArrayList<>();

        String postoffice = addresslist.getPostoffice();

        locations.add(addresslist.getPostoffice() + " Post Office");
//        for(int i = 0; i < addresslist.addresses.size();i++){
//            if(!locations.contains(addresslist.addresses.get(i).getFrom())){
//                locations.add(addresslist.addresses.get(i).getFrom());
//            }
//        }
//
//        for(int i = 0; i < addresslist.addresses.size(); i++) {
//            if (locations.contains(addresslist.addresses.get(i).getFrom()) && locations.contains(addresslist.addresses.get(i).getGoingto())) {
//                entrylist.add(addresslist.addresses.get(i));
//            }
//        }

        for(int i = 0; i < maillist.size(); i++){
            if(!locations.contains(maillist.get(i).getDestination())){
                locations.add(maillist.get(i).getDestination());
            }
        }

        for(int i = 0; i < addresslist.addresses.size(); i++){
            if(locations.contains(addresslist.addresses.get(i).getFrom()) && locations.contains(addresslist.addresses.get(i).getGoingto())){
                entrylist.add(addresslist.addresses.get(i));
            }
        }

        AdjacencyMatrix map = new AdjacencyMatrix(entrylist,locations.size(),postoffice);

        return map;
    }

    static ArrayList<PostOfficeAddresses> generateAddresses(ArrayList<Entry> entrylist){
        ArrayList<PostOfficeAddresses> addresslist = new ArrayList<>();

        int g = 0;
        while(!entrylist.isEmpty()){
            String po = entrylist.get(0).getPostOffice(); // post office name
            int poa = 1;                                 // number of addresses
            ArrayList<Entry> entpo = new ArrayList<>();  // entry list with the same post office

            for(int i = 0; i < entrylist.size(); i++){
                if(entrylist.get(i).getPostOffice().equals(po)){
                    entpo.add(entrylist.get(i));
                    entrylist.remove(i);
                    i--;
                }
            }

            addresslist.add(new PostOfficeAddresses(po,entpo));
        }

        return addresslist;
    }
    /* --------------------- FILE READER  --------------------------- */
    static ArrayList<Entry> readFile(){ // Reads the File and store the entries into an arraylist
        String line;
        String[] mapinfo = new String[2];
        String[] quoted  = new String[5];
        ArrayList<Entry> entrylist = new ArrayList<>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream("res/Map (2).csv"), "UTF-8"));
            br.readLine();
            while ((line = br.readLine()) != null) {
                if(line.contains("\""))
                {
                    quoted = line.split("\"");
                    mapinfo[0] = quoted[0].replace(",","");
                    mapinfo[1] = quoted[4].replace(",","");

                    entrylist.add(new Entry(mapinfo[0], quoted[1], quoted[3], Float.parseFloat(mapinfo[1])));
                }

                else{
                    mapinfo = line.split(",");
                    entrylist.add(new Entry(mapinfo[0], mapinfo[1], mapinfo[2], Float.parseFloat(mapinfo[3])));
                }
            }




        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return entrylist;
    }

    /* --------------------- Below is a matter of using the adjacency matrix and apply to a Vehicle Routing Problem  --------------------------- */

}
