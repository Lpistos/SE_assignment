package Text;

public class Text {
    private String textType;

    public Text(String textName) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        create(textName);
        this.textType = textName;
    }

    public Text() {
        //System.out.println(this.getClass().getName() + " created");
        this.textType = this.getClass().getName();
    }

    private Text create(String textName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        // 넘어오는 패러미터 값이랑 대응되는 클래스를 만든다.
        return (Text) Class.forName("Text." + textName.toString()).newInstance();
    }
}
