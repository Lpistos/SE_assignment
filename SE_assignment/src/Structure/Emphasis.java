package Structure;

public class Emphasis extends Structure {
    private String text;
    public boolean isStrong;

    public String startTag = "<em>";
    public String endTag = "</em>";

    public Emphasis() {
        super();
    }

    public String getText() {
        return text;
    }

    public void setText(String emphasis) {
        this.text = emphasis;
        if(isStrong){
            startTag = "<strong>";
            endTag = "</strong>";
        }
    }
}
