import org.junit.Test ;
import static org.junit.Assert.* ;

import java.util.ArrayList;

public class MDTest
{
	public static void main(String [] args) throws Exception {
		MDTest s = new MDTest() ;
		s.coverageTest() ;
		//s.parserTest();
		
		/*
		 *structure classes 
		 */
		//s.blockTest();
		//s.blockquotesTest();
		//s.codeTest();
		//s.emphasisTest();
		//s.headerTest();
		//s.itemListTest();
		
		/*
		 * text classes
		 */
		//s.horizontalRuleTest();
		//s.imageTest();
		//s.inlineHTMLTest();
		//s.linkTest();
		//s.plainTextTest();
		//s.specialCharacterTest();
		
	}
	@Test
	public void coverageTest() throws Exception {
		MDConverter c = new MDConverter() ;
		//c.main(new String[] {});
		c.main(new String[] {"a"});
		c.main(new String[] {"-help"});
		c.main(new String[] {"-convert", "doc1.md", "doc2.md", "doc3.md", "doc4.md", "test.md"});
	}

	@Test
	public void parserTest() throws IllegalAccessException, ClassNotFoundException, InstantiationException {
		Document doc = new Document("test.md");
		MDParser p = new MDParser(doc);
	}
	
	/*
	//Structure Classes Test
	@Test
	public void blockTest() throws Exception {
		MDConverter b = new MDConverter() ;
		b.createDoc("block.md");
	}
	@Test
	public void blockquotesTest() throws Exception {
		MDConverter bq = new MDConverter() ;
		bq.createDoc("blockquotes.md");
	}
	@Test
	public void codeTest() throws Exception {
		MDConverter cd = new MDConverter() ;
		cd.createDoc("code.md");
	}
	@Test
	public void emphasisTest() throws Exception {
		MDConverter em = new MDConverter() ;
		em.createDoc("emphasis.md");
	}
	@Test
	public void headerTest() throws Exception {
		MDConverter h = new MDConverter() ;
		h.createDoc("header.md");
	}
	@Test
	public void itemListTest() throws Exception {
		MDConverter il = new MDConverter() ;
		il.createDoc("itemList.md");
	}

	//Text Classes Test
	@Test
	public void horizontalRuleTest() throws Exception {
		MDConverter hr = new MDConverter() ;
		hr.createDoc("horizontalRule.md");
	}
	@Test
	public void imageTest() throws Exception {
		MDConverter img = new MDConverter() ;
		img.createDoc("image.md");
	}
	@Test
	public void inlineHTMLTest() throws Exception {
		MDConverter in = new MDConverter() ;
		in.createDoc("inlineHTML.md");
	}
	@Test
	public void linkTest() throws Exception {
		MDConverter li = new MDConverter() ;
		li.createDoc("link.md");
	}
	@Test
	public void plainTextTest() throws Exception {
		MDConverter pl = new MDConverter() ;
		pl.createDoc("plainText.md");
	}
	@Test
	public void specialCharacterTest() throws Exception {
		MDConverter spc = new MDConverter() ;
		spc.createDoc("specialCharacter.md");
	}
	*/
	
}