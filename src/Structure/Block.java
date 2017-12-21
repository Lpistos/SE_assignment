package Structure;

public class Block extends Structure {
    public Block() {
        super();
    }

    public String startTag = "<p>";
    public String endTag = "</p>";
    public String tag = null;

    public String getStartTag() {
        return startTag;
    }

    public String getEndTag() {
        return endTag;
    }

    public String getTag() {
        return tag;
    }
}
