import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: popit
 * Date: 10.11.11
 * Time: 10:03
 * To change this template use File | Settings | File Templates.
 */
public class TextHandler {

    private static String configFileName = "temaparkConfig.txt";
    private static String defaultSize = "800x600";

    public static void main(String[] args) throws IOException {
        List<String> allTextFiles = findAllTextFilesInFolder(findInputFolder(configFileName));
        writeBatFile("makeTextLabels.bat", allTextFiles);
    }

    public static String findInputFolder(String configFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(configFile));
        String tmp;
        String inputFolder = null;
        while ((tmp = reader.readLine()) != null) {
            String[] params = tmp.split(":");
            if (params.length > 1) {
                if (params[0].equals("Inputfolder")) {
                    inputFolder = params[1];
                }
            }
        }
        return inputFolder;
    }

    public static String findOutputFolder(String configFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(configFile));
        String tmp;
        String outputFolder = null;
        while ((tmp = reader.readLine()) != null) {
            String[] params = tmp.split(":");
            if (params.length > 1) {
                if (params[0].equals("Outputfolder")) {
                    outputFolder = params[1];
                }
            }
        }
        return outputFolder;
    }

    public static String findBackgroundColor(String configFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(configFile));
        String tmp;
        String backgroundColor = null;
        while ((tmp = reader.readLine()) != null) {
            String[] params = tmp.split(":");
            if (params.length > 1) {
                if (params[0].equals("Background")) {
                    backgroundColor = params[1];
                }
            }
        }
        return backgroundColor;
    }

    public static String findSize(String configFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(configFile));
        String tmp;
        String size = defaultSize;
        while ((tmp = reader.readLine()) != null) {
            String[] params = tmp.split(":");
            if (params.length > 1) {
                if (params[0].equals("Size")) {
                    size = params[1];
                }
            }
        }
        return size;
    }

    public static String findTextColor(String configFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(configFile));
        String tmp;
        String textColor = null;
        while ((tmp = reader.readLine()) != null) {
            String[] params = tmp.split(":");
            if (params.length > 1) {
                if (params[0].equals("Textcolor")) {
                    textColor = params[1];
                }
            }
        }
        return textColor;
    }

    public static String findFont(String configFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(configFile));
        String tmp;
        String font = "";
        while ((tmp = reader.readLine()) != null) {
            String[] params = tmp.split(":");
            if (params.length > 2) {
                if (params[0].equals("Font")) {
                    for (int i = 1; i < params.length -1; i++) {
                        font = params[i];
                    }
                    font = font + ":" + params[params.length-1];
                }
            }
            else if(params.length == 2) {
                if(params[0].equals("Font")) {
                    font = params[1];
                }
            }
        }
        return font;
    }

    public static List<String> findAllTextFilesInFolder(String path) {
        String files;
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        List<String> fileNames = new ArrayList<String>();

        for (int i = 0; i < listOfFiles.length; i++) {

            if (listOfFiles[i].isFile()) {
                files = listOfFiles[i].getName();
                if (files.endsWith(".txt") || files.endsWith(".TXT")) {
                    fileNames.add(files);
                }
            }
        }
        return fileNames;
    }

    public static void writeBatFile(String outfileName, List<String> filenames) throws IOException {
        PrintWriter printWriter = new PrintWriter(new File(outfileName));
        String font = findFont(configFileName);
        String script1 = "convert -gravity Center -density 100 -background #00000000 -fill ";
        String script2;
        if(font != "") {
            script2 = " -font " + font + " -size  ";
        }
        else
            script2 = " -size ";
        System.out.println(script2);
        String script3 = "convert label1.gif -rotate 90 label1_90.gif";
        String script4 = "convert label1.gif -rotate 180 label1_180.gif";
        String script5 = "convert label1.gif -rotate -90 label1_-90.gif";
        String script6 = "montage  null: label1.gif null: label1_-90.gif null: label1_90.gif null: label1_180.gif null: -tile 3x3  -geometry +2+2 -background #00000000 test.gif";
        String script7 = "composite -gravity Center test.gif -geometry 1024x768 ";
        String outputFolder = findOutputFolder(configFileName);
        printWriter.println("chcp 65001 &");
        for (String filename : filenames) {
            String[] tmp = filename.split("[.]");
            String jpgFileName = tmp[0] + ".jpg";
            printWriter.println(script1 + findTextColor(configFileName) + script2 + findSize(configFileName) + " label:@" + findInputFolder(configFileName) + "/" + filename + " label1.gif");
            printWriter.println(script3);
            printWriter.println(script4);
            printWriter.println(script5);
            printWriter.println(script6);
            printWriter.println(script7 + findBackgroundColor(configFileName) + " " + outputFolder + "/" + jpgFileName);
        }
        printWriter.println("& chcp 850");
        printWriter.close();
    }

}
