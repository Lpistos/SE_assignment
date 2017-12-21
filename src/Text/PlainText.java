package Text;

public class PlainText extends Text{
    private String text;

    public PlainText() {
        super();
    }

    public String getText() {
        return text;
    }

    public void setText(String plaintext) {
        this.text = plaintext;
    }
}
