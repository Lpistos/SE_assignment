package Text;

import Structure.Structure;

public class HorizontalRule extends Text {
    private String text ="<hr />";
    public HorizontalRule() {
        //System.out.println("HorizontalRule created");
        super();
    }
	public String getText() {
		return text;
	}
	public void setText(String content) {
		this.text = text;
	}
}