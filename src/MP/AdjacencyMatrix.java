package MP;

import java.util.ArrayList;


public class AdjacencyMatrix {
    String postoffice;  // ID
    Vertice vertices[]; // Addresses
    float edges[][];   // Distances
    float traffic[][];
    int size;

    public AdjacencyMatrix(ArrayList<Entry> entrylist, int size, String postoffice) {
        this.postoffice = postoffice;
        vertices = new Vertice[size];
        edges = new float[size][size];
        traffic = new float[size][size];

        this.size = size;
        int c = 1;


        vertices[0] = new Vertice(0, postoffice + " Post Office");


        for(int i = 0; i < entrylist.size(); i++){
            if(!isAddressinitialized(vertices, entrylist.get(i).getFrom())){
                vertices[c] = new Vertice(c, entrylist.get(i).getFrom());
                c++;
            }
            if(!isAddressinitialized(vertices, entrylist.get(i).getGoingto())){
                vertices[c] = new Vertice(c, entrylist.get(i).getGoingto());
                c++;
            }
        }

        for(int i = 0; i < vertices.length; i++){
            System.out.println(vertices[i].getAddress() + " " + vertices[i].getId());
        }

        for(int i = 0; i < entrylist.size();i++){
            Entry e = entrylist.get(i);
            addEdge(e.getFrom(),e.getGoingto(),e.getDistance());

            if(e.isTwoway()){
                addEdge(e.getGoingto(),e.getFrom(),e.getDistance());
            }

            else{
                addEdge(e.getGoingto(),e.getFrom(), Float.MAX_VALUE);
            }

            entrylist.remove(i);
            i--;
        }


    }

    private void addEdge(String source, String destination, float distance){
        //System.out.println("adding edge " + source + " to " + destination + " distance: " + distance + "|| " + getID(source) + " " + getID(destination));
        edges[getID(source)][getID(destination)] = distance;
        traffic[getID(source)][getID(destination)] = 0.0f;
    }

    private int getID(String address){
        for(int i = 0; i < vertices.length; i++){
            if(address.equals(vertices[i].getAddress()))
                return vertices[i].getId();
        }

        return 0;
    }

    private boolean isAddressinitialized(Vertice[] vertices, String address){

        for(int i = 0; i < vertices.length; i++){
            if(vertices[i] == null)
                return false;

            if(address.equals(vertices[i].getAddress()))
                return true;
        }

        return false;
    }

    public float[][] getTraffic() {
        return traffic;
    }

    private void setTraffic(String source, String destination, float trafficv){

        traffic[getID(source)][getID(destination)] = trafficv;
    }

    public void printMatrix(){
        System.out.println("");
        for(int x = 0; x < size; x++){
            String addresses = vertices[x].getAddress() + " ";

            addresses = String.format("%40.40s%3.40s",this.vertices[x].getAddress() + " ","");
            System.out.print(addresses);
            for(int y = 0; y < size; y++){
                System.out.print(edges[x][y] + " || ");
            }
            System.out.println("");
        }
    }

    public String getPostoffice() {
        return postoffice;
    }

    public Vertice[] getVertices() {
        return vertices;
    }

    public float[][] getEdges() {
        return edges;
    }

    public int getSize() {
        return size;
    }
}
