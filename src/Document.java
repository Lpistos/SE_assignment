import java.util.ArrayList;
import Structure.*;


public class Document {
    private String name;

    private ArrayList<Structure> structureList = new ArrayList<Structure>();

    public Document(String name) {
        this.setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Structure> getStructureList() {
        return structureList;
    }


}
