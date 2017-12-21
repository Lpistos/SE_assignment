package Text;

public class Image extends Text {
	private String text = null;

	public Image() {
		super();
	}

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
