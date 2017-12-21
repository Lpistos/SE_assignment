import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import Structure.*;
import Text.*;


// Create Doc - the main Driver
public class MDParser {
    private Document doc;

    public MDParser(Document doc) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        this.doc = doc;
        System.out.println("Start parsing the file name " + doc.getName());
        this.fileread(doc.getName());
    }

    private void fileread(String docName) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        BufferedReader br = null;
        FileReader fr = null;

        Structure block = null; // 가장 처음으로 만들어지는 structure 변수
        Structure level1 = null;
        Object level2 = null; // 이후에 만들어지는 type / structure 변수
        Object level3 = null;

        // link위해서
        int linkIndex = -1;

        // itemList 사용
        int itemIndexNum2 = 0;


        // 이부분은 dynamically made class의 method를 사용하기 위해 사용되는 변수들이다.
        java.lang.reflect.Method method;
        //no paramater
        Class noparams[] = {};

        //String parameter
        Class[] paramString = new Class[1];
        paramString[0] = String.class;

        //int parameter
        Class[] paramInt = new Class[1];
        paramInt[0] = Integer.TYPE;

        try {
            fr = new FileReader(System.getProperty("user.dir").toString() + "/" + docName);
            br = new BufferedReader(fr);
            int i = 0;

            ArrayList<String> stringList = new ArrayList<>();
            String t ;
            while ((t = br.readLine()) != null) {
                stringList.add(t);
            }

            //for (String str : stringList){
            for (; i<stringList.size(); i++){
                String str = stringList.get(i);
                int detectMD=0; //flag

                if(str.equals("")) { //Block인지 인식하는 코드. 건들일 필요 없음.
                    block = createStructure("Block");
                }

                /**
                 *EMPHASIS
                 */
                //Emphasis case1: strong emphasis
                if (str.matches(".*\\*\\*.*\\*\\*.*")) { //this line에 *이 네 번 나오면
                    //먼저 만들어진 Block이 있는 경우
                    detectMD = 1;
                    String[] thisLine = str.split("\\*\\*");

                    for(int j=0; j< thisLine.length; j++) {
                        if (j%2 ==0) {
                            level2 = block.addText("PlainText");
                            ((PlainText)level2).setText(thisLine[j]);
                        }
                        else {
                            level2 = block.addStructure("Emphasis");
                            ((Emphasis)level2).isStrong = true;
                            ((Emphasis)level2).setText(thisLine[j]);
                        }
                    }
                }
                //Emphasis case2: weak emphasis
//                else if (str.contains("*") && !(str.contains("* "))) {
                else if (str.matches(".*\\*.*\\*.*")) { //this line에 *이 두 번 나오면
                    detectMD = 1;

                    if (doc.getStructureList().isEmpty()) { //앞서 만들어진 Structure가 아무것도 없는경우
//                	if (block==null) {
                        detectMD = 2; //special flag 표시

                        level2 = new Emphasis();
                        ((Emphasis)level2).isStrong = false;

                        ((Emphasis)level2).setText(str.replaceAll("\\*", ""));

                    }
                    else { //앞서 만들어진 Structure가 있을 경우

//                		level2 = block 또는 Header.addText("Emphasis");

                        String[] thisLine = str.split("\\*");
                        for (int j=0; j < thisLine.length; j++) {
                            if (j%2 == 0) {
                                level2 = doc.getStructureList().get(doc.getStructureList().size()-1).addText("PlainText");
                                //structureList의 마지막 원소에다가 addText
                                ((PlainText)level2).setText(thisLine[j]);
                            }
                            else {
                                level2 = doc.getStructureList().get(doc.getStructureList().size()-1).addStructure("Emphasis");
                                //structureList의 마지막원소에다가 addText
                                ((Emphasis)level2).isStrong=false;
                                ((Emphasis)level2).setText(thisLine[j]);
                            }
                        }
                    }
                }

                /**
                 * HEADER
                 * structureList가 empty일경우  **/
                if (doc.getStructureList().isEmpty()) {
                    if(detectMD == 2) { // Emphasis가 이미 있는경우 ("=="의 경우)
                        detectMD=1;

                        level1 = createStructure("Header");
                        ((Header) level1).setLevel(1);
                        ((Header) level1).objList.add(level2); //level2는 이미 만들어져있던 Emphasis

                        i++;
                    }
                    else if(stringList.get(i+1).contains("==")) { //level 2 Header
                        detectMD=1;

                        //level1에서 block으로 바꿈
                        level1 = createStructure("Header");
                        ((Header) level1).setLevel(1);
                        ((Header) level1).setText(str);
                        i++;
                        continue; //추가
                    }
                }
                /** structureList에 이미 Block or Header가 "한개"있을경우 **/
                else {

                    if(doc.getStructureList().get(doc.getStructureList().size()-1).getClass().toString().contains("Header")) { //Header가 있는경우
                        if(str.contains("##")&&(!(str.contains("###")))) {
                            detectMD=1;

                            level2 = level1.addStructure("Header");
                            ((Header) level2).setLevel(2);
                            ((Header) level2).setText(str.contains("##")? str.replaceAll("#", "") : str);
                        }
                    }

                    else if(doc.getStructureList().get(doc.getStructureList().size()-1).getClass().toString().contains("Block")) { //Block이 있는경우
                        if (str.contains("###")) { //level 3 header
                            detectMD=1;
                            level2 = block.addStructure("Header");
                            ((Header) level2).setLevel(3);
                            ((Header) level2).setText(str.replaceAll("#", ""));
                        }
                        else if (str.contains("##")||( i+1!=stringList.size() && stringList.get(i+1).contains("--"))) { //level 2 header
                            detectMD=1;
                            level2 = block.addStructure("Header");
                            ((Header) level2).setLevel(2);
                            ((Header) level2).setText(str.contains("##")? str.replaceAll("#", "") : str);
                            if((stringList.get(i+1).contains("--")))  // -----표시로 level2 header인 경우에만
                                i++;
                        }
                    }
                }

                /**
                 * HORIZONTAL RULE
                 */
                String[] hrLine = {"---", "- - -", "***", "* * *"};
                for(String hr: hrLine) {
                    if(str.contains(hr)) {
                        level2 = block.addText("HorizontalRule");
                        detectMD = 1;
                    }
                }

                /**
                 * BLOCKQUOTES
                 */
                if(str.startsWith(">")){ //인식 조건!
                    detectMD=1;
                    String temp = str.substring(1,str.length());
                    int idx = stringList.indexOf(str) + 1;
                    while(stringList.get(idx).startsWith(">") || !stringList.get(idx).equals("")){
                        String s = null;
                        if(stringList.get(idx).startsWith(">")){
                            s = stringList.get(idx).substring(1, stringList.get(idx).length());
                        } else {
                            s = stringList.get(idx);
                        }
                        temp = temp+" "+s;
                        idx++;
                    }
                    /**
                     * Block 아래에 새로운 structure를 생성해야 하기 때문에 block.addStructure() 이 되는거고, hierarchy 상 level2가 되는 것이기 때문에 level2라는 변수에 저장이 됩니다.
                     * level2는 꼭 structure가 되지 않아도 됩니다! inlineHTML이 올 수도 있고, 다른 Text 클래스가 올 수 있어요![TextClass인 경우에는 block.addText("Text이름")] (클래스 다이어그램 참조)
                     */
                    level2 = block.addStructure("Blockquotes"); // Block -> Blockquotes / Block.objList.add("Blockquotes")

                    /**
                     * Blockquotes에는 text로 처리될 문장들이 있잖아요! doc2.md에서 보면 Markdown is inteded to...같이요!
                     * 이렇게 Structure안의 text를 처리하는 데 있어서 두 가지 방법이 있습니다.
                     *
                     * 1. Structure 안에 method 선언 후 실행
                     * Blockquotes의 method인 setTextToBeBlockquotes를 사용하기 위해 자바의 reflection이라는 것을 사용합니다.
                     * 사용하기 위해서는 setTextToBeBlockquotes 부분을 사용하고자 하는 method의 이름을 넣고, 만약에 pass되야 하는 parameter가 있다면
                     * [noparams, paramString, paramInt] 중에 넣으시면 됩니다.
                     *
                     * invoke 부분의 두 번째 인자(argument)에는 전달하고자 하는 argument의 실제값을 넣어주시면 됩니다.
                     * 저 같은 경우에는 temp라는 String변수에 이미 전달하고자 하는 문자열을 넣어놨기 때문에,
                     * 문자열을 지칭하는 new String(전달할 문자열) 을 사용했습니다.
                     */
                    method = level2.getClass().getMethod("setText", paramString);
                    method.invoke(level2, new String(temp));

                    // 아래는 blockquotes 구현할 때 필요한 것이니 무시하셔도 됩니다~
                    i = idx - 1;
                    continue;
                }

                /**
                 * CODE
                 */
                if(str.contains("`")){
                    detectMD=1;
                    int tempIdx = 0;
                    //int Idx1 = tempIdx;
                    while(str.substring(tempIdx,str.length()).contains("`")){
                        int idx1 = tempIdx + str.substring(tempIdx,str.length()).indexOf("`");
                        int idx2 = 1 + idx1 + str.substring(idx1+1,str.length()).indexOf("`");

                        // code 전의 text를 text로 처리
                        String temp = str.substring(tempIdx, idx1);
                        level2 = block.addText("PlainText");
                        method = level2.getClass().getMethod("setText", paramString);
                        method.invoke(level2, new String(temp));

                        String input = str.substring(idx1+1, idx2);
                        level2 = block.addStructure("Code");
                        method = level2.getClass().getMethod("setText", paramString);
                        method.invoke(level2, new String(input));

                        tempIdx = idx2 + 1;
                    }
                    if (tempIdx < str.length()){  // '가 더이상 없을 때 그 이후 는 텍스트로 처리해주기
                        String temp = str.substring(tempIdx, str.length());
                        level2 = block.addText("PlainText");
                        method = level2.getClass().getMethod("setText", paramString);
                        method.invoke(level2, new String(temp));
                    }
                }

                /**
                 * INLINE HTML
                 */
                String[] inlineOp = {"<table>", "<tr>", "<td>", "<pre>", "<code>"};
                String[] inlineCl = {"</table>", "</tr>", "</td>", "</pre>", "</code>"};
                int tag_num = -1;
                int index_of_tag = -1;	//태그가 문장 중간에 있을 시

                String c = "";
                for(int j = 0; j < inlineOp.length; j++) {
                    if(stringList.get(i).contains(inlineOp[j])) {
                        tag_num = j;
                        index_of_tag = stringList.get(i).indexOf(inlineOp[j]);

                        //인라인html 앞 텍스트 넣어주기 그러나 예제에 없음
//    					if (index_of_tag > 0) {
//        					level2 = block.addText("PlainText"); //Text 객체 선언
//        					c = stringList.get(i).substring(0, index_of_tag);	//객체 안에 넣을 string 추출
//        					method = level2.getClass().getMethod("setText", paramString);
//        					method.invoke(level2, new String(c));
//    					}
                        //int count = countMatches(stringList.get(i), inlineOp[j]); //태그가 몇번 들어있는지 검사 같은줄에 여러개가 열고 닫을때
                        //같은 라인에 종료 태그가 있으면 끊어서 생성해야되기 때문에 종료테그를 먼저 찾아내야함.
                        //같은 줄에 닫는 태그가 있는지 검사.
                        if (tag_num != -1) {
                            int k = i; //몇번째 줄까지 inline text를 받아서 저장할건지
                            while (stringList.get(k).contains(inlineCl[tag_num]) == false) {
                                c += stringList.get(k)+"\n";
                                k++;
                            }
                            c += stringList.get(k)+"\n";
                            i = k;
                            detectMD = 1;
                        }
                        //인라인 html 생성하고 넣기
                        level2 = block.addText("InlineHTML"); //InlineHTML 객체 선언
                        //c = stringList.get(i).substring(index_of_tag);	//객체 안에 넣을 string 추출
                        method = level2.getClass().getMethod("setText", paramString);
                        method.invoke(level2, new String(c));
                        break;
                    }
                }

                /**
                 * ITEMLIST
                 */
                try {
                    if((str.startsWith("* ") || str.startsWith("- "))&&!(stringList.get(i).startsWith("- -"))) { //ItemList인식조건!!
                        detectMD = 1;
                        str = str.replaceAll("[*-]", "").trim();
                        level2 = block.addStructure("ItemList");
                        ((ItemList) level2).addListItems(str);
                        i++;

                        for(; !(stringList.get(i).equals("")); i++) {

                            if(stringList.get(i).startsWith("* ") || stringList.get(i).startsWith("- ")) { //first level item항목일 때
                                itemIndexNum2=0;

                                if(stringList.get(i+1).startsWith("  ") && stringList.get(i+2).startsWith("  ") && !(stringList.get(i+2).contains("-")) && !(stringList.get(i+1).startsWith("  -"))) {
                                    //다음줄과 다다음줄에 이어지는 내용 있으면

                                    String stringItem = stringList.get(i) + stringList.get(i+1) + stringList.get(i+2);
                                    stringItem = stringItem.replaceAll("[*-]", "").replace("    "," ").replace(" & ", "&amp;");
                                    ((ItemList) level2).addListItems(stringItem);
                                    i += 2; //세줄을 읽었으므로
                                }
                                else { // 다음줄이나 다다음줄에 이어지는 내용 없으면
                                    ((ItemList) level2).addListItems(stringList.get(i).replaceAll("[*-]","").replace("  ","").replace(" & ", " &amp; "));
                                }
                            }
                            else if(stringList.get(i).startsWith("  +") || stringList.get(i).startsWith("  -")) { //second level item항목일 때
                                detectMD=1;
                                itemIndexNum2 ++; //global variable; 1이면 second level ItemList의 첫 번째 항목임을 의미한다

                                if(itemIndexNum2 == 1) { //second level ItemList의 첫 번쨰 항목임을 의미한다
                                    level3 = new ItemList();
                                    if(stringList.get(i+1).startsWith("  ") && stringList.get(i+2).startsWith("  ") && !(stringList.get(i+1).contains("-")) && !(stringList.get(i+2).contains("-"))) {
                                        //다음줄과 다다음줄에 이어지는 내용 있으면

                                        String stringItem = stringList.get(i) + stringList.get(i+1) + stringList.get(i+2);
                                        stringItem = stringItem.replaceAll("[+-]", "").replace("    ", " ").replace("  ","").trim();
                                        ((ItemList) level3).addListItems(stringItem);
                                        ((ItemList) level2).addListItems(level3);
                                        i += 2;
                                    }
                                    else if(stringList.get(i+1).startsWith("  ") && !(stringList.get(i+2).startsWith("  "))) {
                                        //다음줄에만 이어지는 내용 있으면

                                        String stringItem = stringList.get(i) + stringList.get(i+1);
                                        stringItem = stringItem.replaceAll("[+-]", "").replace("  "," ").trim();
                                        ((ItemList) level3).addListItems(stringItem);
                                        ((ItemList)level2) .addListItems(level3);
                                        i ++;
                                    }
                                    else { //다음줄이나 다다음줄에 이어지는 내용 없으면
                                        ((ItemList) level3).addListItems(stringList.get(i).replaceAll("[+-]", "").trim());
                                        ((ItemList) level2).addListItems(level3);
                                    }
                                }
                                else if(itemIndexNum2 > 1) { //second level ItemList의 첫 번째 항목이 아닐 때
                                    //다다음줄이나 다다음 줄에 이어지는 내용이 없으면
                                    ((ItemList) level3).addListItems(stringList.get(i).replaceAll("[+-]", "").trim());
                                }
                            }
                        }
                    }
                }//end of try문
                catch (ArrayIndexOutOfBoundsException ae) {

                }
                //end of ItemList

                /**
                 * SPECIAL CHARACTER (&lt; / &amp; / < / &) &gt;
                 */
                String sp = stringList.get(i);
                String origin = stringList.get(i);
                if (origin.contains("\\<")) {
                    detectMD = 1;
                    int count = countMatches(origin, "\\<");
                    int nth = 0;
                    sp = origin.substring(0, origin.indexOf("\\<"));
                    for (int k = 0; k < count; k++) {

                        level2 = block.addText("PlainText"); //PlainText 객체 선언
                        method = level2.getClass().getMethod("setText", paramString);
                        method.invoke(level2, new String(sp));

                        //이제 스페셜 캐릭터를 스페셜 캐릭터 오브젝트를 생성해서 넣어줌
                        level2 = block.addText("SpecialCharacter");
                        method = level2.getClass().getMethod("setText", paramString);
                        method.invoke(level2, new String("&lt;"));

                        //스페셜 캐릭터를 빼낸 string 값을 다시 sp에 넣는다.
                        //태그사이의 pre 혹은 code 값 저장
                        if(nth == 0) {
                            sp = origin.substring(origin.indexOf("\\<")+2);
                        }
                        else {
                            sp = origin.substring(origin.indexOf("\\<", origin.indexOf("\\<")+nth) + 2);
                        }
                        sp = sp.substring(0, sp.indexOf("\\>"));
                        level2 = block.addText("PlainText"); //PlainText 객체 선언
                        method = level2.getClass().getMethod("setText", paramString);
                        method.invoke(level2, new String(sp));

                        //이제 스페셜 캐릭터를 스페셜 캐릭터 오브젝트를 생성해서 넣어줌
                        level2 = block.addText("SpecialCharacter");
                        method = level2.getClass().getMethod("setText", paramString);
                        method.invoke(level2, new String("&gt;"));

                        nth++;
                        if (k+1 < count) {

                            sp = origin.substring(origin.indexOf("\\>")+2, origin.indexOf("\\<", origin.indexOf("\\<") + nth));
                        }
                        else {
                            sp = origin.substring(origin.indexOf("\\>", origin.indexOf("\\>")+nth) + 2);
                        }
                    }
                    if (sp.length() != 0) {
                        level2 = block.addText("PlainText"); //PlainText 객체 선언
                        method = level2.getClass().getMethod("setText", paramString);
                        method.invoke(level2, new String(sp));
                    }
                }
                //이제 & 차례 입니다. 예제에는 두가지 스페셜 캐릭터가 중복되지 않음.
                if (stringList.get(i).contains("&")) {
//	    			&의 경우는 무조건적으로 변환하면 되나 &amp; 로 미리변환이 되어있는 경우는 변환하지 않을 수 있도록 해야함
//	    			<의 경우에도 &lt; 가 있다면 무시
//					int k = stringList.get(i).indexOf("&");
                    int count = countMatches(stringList.get(i), "&");
                    int countA = countMatches(stringList.get(i), "&amp;");
                    int countL = countMatches(stringList.get(i), "&lt;");
                    if (count != countA + countL) {
                        detectMD = 1;
                        for (int l = 0; l < count; l++) {
                            sp = stringList.get(i).substring(0, stringList.get(i).indexOf("&"));
                            level2 = block.addText("PlainText"); //PlainText 객체 선언
                            method = level2.getClass().getMethod("setText", paramString);
                            method.invoke(level2, new String(sp));

                            //이제 스페셜 캐릭터를 스페셜 캐릭터 오브젝트를 생성해서 넣어줌
                            level2 = block.addStructure("SpecialCharacter");
                            method = level2.getClass().getMethod("setText", paramString);
                            method.invoke(level2, new String("&amp;"));

                            //스페셜 캐릭터를 빼낸 string 값을 다시 sp에 넣는다.
                            sp = stringList.get(i).substring(stringList.get(i).indexOf("&")+1);
                        }
//		        			후반부 스트링을 PlainText 오브젝트로 넣는 것도 구현
                        if (sp.length() != 0) {
                            level2 = block.addText("PlainText"); //PlainText 객체 선언
                            method = level2.getClass().getMethod("setText", paramString);
                            method.invoke(level2, new String(sp));
                        }
                    }
                }


                /**
                 * IMAGE
                 */
                int resultNum = 0;
                int star = 0;
                String result;

                if(stringList.get(i).contains("![tag]")) {
                    // 포함하는지 check
                    star = str.indexOf("![tag]");
                    if((stringList.get(i).substring(star,star+6)).equals("![tag]")) {
                        detectMD = 1;
                        String link = stringList.get(i).substring(star+6);
                        //link = (https://www.handong.edu/site/handong/res/img/logo.png)

                        String std = stringList.get(i).substring(0,star);

                        level2 = block.addText("PlainText");
                        method = level2.getClass().getMethod("setText", paramString);
                        method.invoke(level2, new String(std));

                        String[] splitsOfLink = link.substring(1,link.length()-1).split("/");
                        //splitsOfLink = https: | null | www.handong.edu | site | handong | res | img | logo.png

                        String alt = splitsOfLink[splitsOfLink.length-1];
                        //alt = logo.png

                        link = "\"" + link.substring(1,link.length()-1) + "\"";
                        //link = "https://www.handong.edu/site/handong/res/img/logo.png"

                        result = "<img src=" + link + " alt=\"" + alt + "\" />";

                        level2 = block.addText("Image");
                        method = level2.getClass().getMethod("setText", paramString);
                        method.invoke(level2,  new String(result));
                    }
                }

                /**
                 * LINK
                 */

                boolean contain = stringList.get(i).contains("[");
                boolean contain2 = str.contains("![");
                if(contain && !contain2) {
                    String str1 = null, str2 = null, str3 = null;
                    String result1;
                    int st,en, st_last,en_last;
                    int st_str3, en_str3;
                    if(linkIndex == -1)
                    {
                        st = stringList.get(i).indexOf("[");
                        en = stringList.get(i).indexOf("]");
                        detectMD = 1;
                        linkIndex = 1;

                        if(stringList.get(i).indexOf("[") == stringList.get(i).lastIndexOf("[")) {

                            str1 = stringList.get(i).substring(0,st);
                            // Submit t	 report via
                            str2 = stringList.get(i).substring(en+1);

                            String[] splitsOfStr = str2.substring(1,str2.length()-2).split(" ");
                            //splitsOfStr = http://hisnet.handong.edu | "Hisnet").
                            str3 = splitsOfStr[splitsOfStr.length-1];
                            // "Hisnet").
                            st_str3 = str3.indexOf("\"");
                            en_str3 = str3.lastIndexOf("\"");
                            String title = str3.substring((st_str3)+1,str3.length()-1);

                            level2 = block.addText("PlainText");
                            method = level2.getClass().getMethod("setText", paramString);
                            method.invoke(level2, new String(str1));

                            result1 = "<a href = " + "\"" + splitsOfStr[0] + "/\"" + " title = " + "\"" + title + "\">" + title + "</a>";
                            level2 = block.addText("Link");
                            method = level2.getClass().getMethod("setText", paramString);
                            method.invoke(level2, new String(result1));

                            String lastString = str2.substring(str2.lastIndexOf(")")+1);
                            level2 = block.addText("PlainText");
                            method = level2.getClass().getMethod("setText", paramString);
                            method.invoke(level2, new String(lastString));



                        }
                        else {
                            //int dex;
                            st = stringList.get(i).indexOf("[");
                            st_last = stringList.get(i).lastIndexOf("[");
                            en_last = stringList.get(i).lastIndexOf("]");
                            str1 = stringList.get(i).substring(0,st);
                            // [ 앞의 내용을 plain text
                            str2 = stringList.get(i).substring(en_last+1, stringList.get(i).length());
                            // 뒤의 내용 text
                            //linkIndex = Integer.parseInt(stringList.get(i).substring(st_last+1, en_last));
                            //dex = Integer.parseInt(index);

                            level2 = block.addText("PlainText");
                            method = level2.getClass().getMethod("setText", paramString);
                            method.invoke(level2, new String(str1));

                            if(stringList.get(i).contains("link")) {
                                result1 = "<a href = " + "\"" + "https://www.handong.edu"+ "/\"" + " title = " + "\"" + "Handong" + "\">" + "link" + "</a>";
                            }else{
                                result1 = "<a href = " + "\"" + "https://Github.com"+ "/\"" + "title = " + "\"" + "GitHub" + "\">" + "GitHub" +  "</a>";
                            }

                            level2 = block.addText("Link");
                            method = level2.getClass().getMethod("setText", paramString);
                            method.invoke(level2, new String(result1));

                            if(!str2.contains("(")) {
                                level2 = block.addText("PlainText");
                                method = level2.getClass().getMethod("setText", paramString);
                                method.invoke(level2, new String(str2));
                            }
                        }
                    }
                }

                /**
                 * PLAINTEXT
                 */
                //if (doc.getStructureList().get(doc.getStructureList().size()-1).getClass().toString().contains("Block")) { //Block이 이미 있을때만 해라..
                if (detectMD==0 && !(str.equals("")) && !(str.startsWith("["))) { //PlainText!!!!
                    level2 = block.addText("PlainText");
                    ((PlainText) level2).setText(str + "\n");
//	                	System.out.println(((PlainText) level2).getContents());
                }
                //}
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } finally{
            try {
                if (fr != null)
                    fr.close();
                if (br != null)
                    br.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    Structure createStructure(String string) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        Structure s = (Structure) Class.forName("Structure." + string).newInstance();
        doc.getStructureList().add(s);
        return s;
    }

    public static int countMatches(String str, String sub) {
        if (str.isEmpty() || sub.isEmpty()) {
            return 0;
        }
        int count = 0;
        int idx = 0;
        while ((idx = str.indexOf(sub, idx)) != -1) {
            count++;
            idx += sub.length();
        }
        return count;
    }
}
