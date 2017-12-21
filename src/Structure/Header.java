package Structure;

public class Header extends Structure {
    private String text;
    private int level;

    public String startTag = null;
    public String endTag = null;
    public String tag = null;

    public Header() {
        super();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        startTag = "<h"+level+">";
        endTag = "</h"+level+">";
    }
}