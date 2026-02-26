package adapter;

import adapter.BasicFormatter;

public class main {
    public static void main(String[] args){
        BasicFormatter bf = new BasicFormatter("src/resources/text.txt");

        String lineas = bf.extractText(2, 6);

        bf.appendText(lineas);

        System.out.println(lineas);

    }
}
