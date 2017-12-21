import Structure.*;
import Text.*;

import javax.print.Doc;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class HTMLConverter {
    private Document doc;
    private  String filename;


    public HTMLConverter(Document doc) throws Exception {
        this.doc = doc;
        this.filename = System.getProperty("user.dir").toString() + "/" + doc.getName().substring(0,4) + ".html";
        System.out.println("Start converting " + doc.getName() + " to HTML file...");
        this.convert(this.doc);
    }

    private void convert(Document d) throws Exception {
        java.lang.reflect.Method method;
        //no paramater
        Class noparams[] = {};
        Class n = null;

        //String parameter
        Class[] paramString = new Class[1];
        paramString[0] = String.class;

        //int parameter
        Class[] paramInt = new Class[1];
        paramInt[0] = Integer.TYPE;

        //file out
        BufferedWriter bw = null;

        // HTML header and footer
        String h = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<body>";

        String f = "</body>\n" +
                "</html>";

        try{
            bw = new BufferedWriter(new FileWriter(filename));
            bw.write(h);

            for(Structure str : d.getStructureList()){
                bw.write(str.getClass().getField("startTag").get(str).toString());

                if(str instanceof Header){
                    if( str.objList.isEmpty() || ! (str.objList.get(0) instanceof Emphasis))  {
                        method = str.getClass().getMethod("getText", noparams);
                        bw.write(method.invoke(str).toString());
                    }
                }
                if(!str.objList.isEmpty()){
                    for(Object o : str.objList){
                        if(o instanceof Text){
                            method = o.getClass().getMethod("getText", noparams);
                            bw.write(method.invoke(o).toString());
                        }else if( o instanceof Structure ){
                            // all the structure clases.. except..
                            bw.write(o.getClass().getField("startTag").get(o).toString());

                            // itemlist
                            if ( o instanceof ItemList ){
                                writeList(bw, (ItemList) o);
                            }else {
                                method = o.getClass().getMethod("getText", noparams);
                                bw.write(method.invoke(o).toString());
                            }
                            bw.write(o.getClass().getField("endTag").get(o).toString());
                        }

                    }
                }
                bw.write(str.getClass().getField("endTag").get(str).toString());
            }
            bw.write(f);
            System.out.println("Markdown file" + doc.getName() + " is successfully converted at " + System.getProperty("user.dir").toString() + " as " + doc.getName().substring(0,4) + ".html");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(bw != null) {
                bw.close();
            }

        }
    }

    private void writeList(BufferedWriter bw, Object o) throws Exception {
        for ( Object e : ((ItemList) o).getListItems()) {
            if(e instanceof ItemList){
                bw.write(o.getClass().getField("startTag").get(o).toString());
                writeList(bw, e);
                bw.write(o.getClass().getField("endTag").get(o).toString());
            }else{
                bw.write(o.getClass().getField("itemSTag").get(o).toString());
                bw.write(e.toString());
            }
            bw.write(o.getClass().getField("itemETag").get(o).toString());
        }
    }
}
