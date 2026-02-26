package adapter;

public class Main {
    public static void main(String[] args){
        BasicFormatter bf = new BasicFormatter("src/resources/text.txt");

        String lineas = bf.extractText(2, 6);

        System.out.println(lineas);

    }
}
