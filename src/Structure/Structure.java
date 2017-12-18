package Structure;
import Text.*;

import java.util.ArrayList;

public class Structure {
    ArrayList<Text> textList = new ArrayList<Text>();

    public Structure() {
        System.out.println(this.getClass().getName() + " created");
    }

    public Structure(String structName) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        create(structName);
    }

    private Structure create(String structName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return (Structure) Class.forName("Structure." + structName.toString()).newInstance();
    }
}

