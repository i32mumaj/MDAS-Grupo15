package adapter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Adapter implements AdvancedFormatter {

    @Override
    public File concatFiles(File x, File y, String filePath) throws IOException {
        File ret_v = new File(filePath);

        if (ret_v.exists()) {
            ret_v.delete();
        }
        ret_v.createNewFile();

        BasicFormatter bfX = new BasicFormatter(x);
        BasicFormatter bfY = new BasicFormatter(y);
        BasicFormatter bfOut = new BasicFormatter(ret_v);

        String textX = bfX.extractText(1, Integer.MAX_VALUE);
        String textY = bfY.extractText(1, Integer.MAX_VALUE);

        bfOut.appendText(textX.trim());
        bfOut.appendText(textY.trim());

        return ret_v;
    }

    @Override
    public File combineFiles(File x, File y, int start, int end, String filePath) {
        File ret_v = new File(filePath);

        if (ret_v.exists()) {
            ret_v.delete();
        }

        try {
            ret_v.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BasicFormatter bfX = new BasicFormatter(x);
        BasicFormatter bfY = new BasicFormatter(y);
        BasicFormatter bfOut = new BasicFormatter(ret_v);

        String textX = bfX.extractText(start, end);
        String textY = bfY.extractText(start, end);

        bfOut.appendText(textX.trim());
        bfOut.appendText(textY.trim());

        return ret_v;
    }

    @Override
    public List<File> splitFile(File x, int[] positions) {
        List<File> resultFiles = new ArrayList<>();
        BasicFormatter bfX = new BasicFormatter(x);

        for (int i = 0; i < positions.length; i++) {
            int startLine = positions[i];
            int endLine = (i < positions.length - 1) ? positions[i + 1] - 1 : Integer.MAX_VALUE;

            String textChunk = bfX.extractText(startLine, endLine);

            File partFile = new File(x.getParent(), "part_" + (i + 1) + "_" + x.getName());
            if (partFile.exists()) {
                partFile.delete();
            }
            try {
                partFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            BasicFormatter bfPart = new BasicFormatter(partFile);
            bfPart.appendText(textChunk.trim());

            resultFiles.add(partFile);
        }

        return resultFiles;
    }
}