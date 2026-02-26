package adapter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;

public class BasicFormatter {
    private File file;

    public BasicFormatter(File file) {
        this.file = file;
    }

    public BasicFormatter(String path) {
        this.file = new File(path);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void appendText(String text) {
        try (FileWriter writer = new FileWriter(this.file, true)) {
            writer.write("\n" + text);
            System.out.println("Text appended succesfully.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public String extractText(int start, int end){
        String ret_v = "";
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                int counter = 1;
                while ((line = br.readLine()) != null) {
                    if(counter >= start && counter <= end){
                        ret_v += line + "\n";
                    }
                    counter++;
                }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return ret_v;
    }
}
