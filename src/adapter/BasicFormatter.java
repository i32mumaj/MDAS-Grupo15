package adapter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

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
            System.out.println("Text appended successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            throw new RuntimeException(e);
        }
    }

    public String extractText(int start, int end){
        String ret_v = "";
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                int counter = 1;
                while ((line = br.readLine()) != null) {
                    if(counter >= start && counter <= end)  ret_v = ret_v.concat(line + "\n");
                    counter++;
                }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ret_v;
    }

    public File[] splitFileInTwo(int cutLine) {
        File part1 = new File(file.getParent(), "split1_" + file.getName());
        File part2 = new File(file.getParent(), "split2_" + file.getName());

        try (BufferedReader br = new BufferedReader(new FileReader(file));
             FileWriter fw1 = new FileWriter(part1);
             FileWriter fw2 = new FileWriter(part2)) {

            String line;
            int counter = 1;

            while ((line = br.readLine()) != null) {
                if (counter < cutLine) {
                    fw1.write(line + "\n");
                } else {
                    fw2.write(line + "\n");
                }
                counter++;
            }
            System.out.println("File split in two successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while splitting the file.");
            throw new RuntimeException(e);
        }

        return new File[]{part1, part2};
    }
}
