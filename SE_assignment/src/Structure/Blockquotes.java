package Structure;

public class Blockquotes extends Structure{
    private String text;

    public String startTag = "<blockquote>";
    public String endTag = "</blockquote>";
    public String tag = null;

    public Blockquotes() {
        super();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
