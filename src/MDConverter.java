import java.util.ArrayList;
import Structure.*;

public class MDConverter {
    static String name;
    static ArrayList<Document> docList = new ArrayList<Document>();

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        //여기가 시작점이다.
        //커맨드 라인 분석해서 알맞는 Document를 만들게 된다.
            createDoc("doc1.md");

        //docList iterate하면서 MDParser이 시작된다.
        for(Document doc : docList){
            MDParser d = new MDParser(doc);
        }
    }

    static void createDoc(String name){
        Document doc = new Document(name);
        docList.add(doc);
    }
}
