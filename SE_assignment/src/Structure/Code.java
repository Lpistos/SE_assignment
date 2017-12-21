package Structure;

public class Code extends Structure {
    private String text = null;

    public String startTag = "<code>";
    public String endTag = "</code>";
    public String tag = null;

    public Code() {
        super();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
