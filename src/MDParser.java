import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import Structure.*;
import Text.*;


// Create Doc - the main Driver
public class MDParser {
    private Document doc;

    public MDParser(Document doc) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        this.doc = doc;
        System.out.println("Start parsing the file name " + doc.getName());
        this.fileread(doc.getName());
    }

    // 여기서 file read하면서 적절한 Structure를 만들어낸다.
    private void fileread(String docName) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
//       System.out.println(System.getProperty("user.dir"));
////        Scanner scanner = null;
////        try{
////            scanner = new Scanner(new File(System.getProperty("user.dir").toString() + "/doc1.md"));
////        } catch(FileNotFoundException e){
////            System.out.println(e);
////        }
////
////        while(scanner.hasNext()){
////            String a = scanner.next();
////            System.out.println(a);
////
////        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(System.getProperty("user.dir").toString() + "/doc1.md"));
            int i = 0;
            while ((i = br.read()) != -1) {
                System.out.print((char) i);

                //여기에 코딩짜시면 됩니다
                
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void createStructure(String string) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        Structure s = new Structure(string);
        doc.structureList.add(s);
    }
}
