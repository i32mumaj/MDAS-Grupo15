package adapter;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface AdvancedFormatter {

    public File concatFiles(File x, File y, String filePath) throws IOException;
    public File combineFiles(File x, File y, int start, int end, String filePath);
    public List<File> splitFile(File x, int[] positions);

}
