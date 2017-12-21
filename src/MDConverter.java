import java.util.ArrayList;
import Structure.*;

public class MDConverter {
    static String name;
    static ArrayList<Document> docList = new ArrayList<Document>();

    public static void main(String[] args) throws Exception {
    	//args == 0
		if (args.length == 0) {
			System.out.println("Type \"MDConverter -help\" for the usage.");
		}
		//args > 1 (-help)
		else if (args.length == 1) {
			if(args[0].equals("-help")) {
				System.out.println("To convert markdown file to html: MDConverter -convert file_name.md ");
				System.out.println("To convert multiple markdown files : MDConverter -convert file_name1.md file_name2.md ");
			}
			else {
				System.out.println("\'" + args[0]+ "\'" + " is not a valid command." + " See\"MDConverter -help\" to get help.");
			}
		}
		//args > 2
		else if (args.length >= 2) {
			//command -convert
			if(args[0].equals("-convert")) {
				for (int i = 1; i < args.length ; i++) {
					if(args[i].contains(".md")){
						createDoc(args[i]);
					} else {
						System.out.println("\'"+ args[i] + "\'" + "is an invalid input file extension. This program only receives .md files");
					}
				}
			}else {
				System.out.println("Not a valid command. Type \"MDConverter -help\" to get help.");
			}
		}
//        createDoc("doc1.md");
//        createDoc("doc2.md");
//        createDoc("doc3.md");
//        createDoc("doc4.md");

        //docList iterate하면서 MDParser과 HTMLConverter이 시작된다.
        for(Document doc : docList){
            MDParser d = new MDParser(doc);
            HTMLConverter c = new HTMLConverter(doc);
            //System.out.print(doc.getStructureList());
        }
    }

    static void createDoc(String name){
        Document doc = new Document(name);
        docList.add(doc);
    }
}
