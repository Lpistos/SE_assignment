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
				System.out.println("Convert .md file to html: MDConverter -convert file_name.md ");
				System.out.println("Converting multiple .md files to html: MDConverter -convert file_name1.md file_name2.md ");
			}
			else {
				System.out.println("Type \"MDConverter -help\" for the usage.");
			}
		}
		//args > 2
		else if (args.length >= 2) {
			//command -convert
			if(args[0].equals("-convert")) {
				for (int i = 1; i < args.length ; i++) {
					createDoc(args[i]);
				}
			}
		}

        //커맨드 라인 분석해서 알맞는 Document를 만들게 된다.
        createDoc("doc1.md");
        createDoc("doc2.md");
        createDoc("doc3.md");
        createDoc("doc4.md");

        //docList iterate하면서 MDParser과 HTMLConverter이 시작된다.
        for(Document doc : docList){
            MDParser d = new MDParser(doc);
            HTMLConverter c = new HTMLConverter(doc);
            //System.out.print(doc.getStructureList());
        }
    }
    // create Doc
    static void createDoc(String name){
        Document doc = new Document(name);
        docList.add(doc);
    }
}
