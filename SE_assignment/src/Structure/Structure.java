package Structure;
import Text.*;

import java.util.ArrayList;

public class Structure {
    private String structureName;
    public ArrayList<Object> objList = new ArrayList<Object>();

    public Structure() {
        //System.out.println(this.getClass().getName() + " created");
        this.structureName = this.getClass().getName();
    }
    /**
     * 바뀐 것 !!!!!!!!!!!!!!!!! 은 addText와 addStructure 모두 각각 Text, Structure를 리턴 타입으로 갖게됩니다.
     * 따라서 제가 MDParser부분에 사용 예시를 들어놨으니 그거 보시고 addText나 addStructure를 부른 곳에서 block = addStructure("structure") 식으로 고쳐주시면 됩니다!!
     */
    public Text addText(String textName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Text t = (Text) Class.forName("Text." + textName.toString()).newInstance();
        this.objList.add(t);
        return t;
    }

    public Structure addStructure(String sName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Structure s2 = (Structure) Class.forName("Structure." + sName.toString()).newInstance();
        this.objList.add(s2);
        return s2;
    }
}

