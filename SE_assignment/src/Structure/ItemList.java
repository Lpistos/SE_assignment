package Structure;

import java.util.ArrayList;

public class ItemList extends Structure {

	private ArrayList<Object> listItems = new ArrayList<Object>();

    public String startTag = "<ul>";
    public String endTag = "</ul>";
    public String itemSTag = "<li>";
    public String itemETag = "</li>";
	
	public ItemList() {
		super();
	}

	public ArrayList<Object> getListItems() {
		return listItems;
	}
//
//	public void setListItems(ArrayList<Object> listItems) {
//		this.listItems = listItems;
//	}
	
	public void setListItems(int index, Object o) {
		this.listItems.set(index, o);
	}
	
	public void addListItems(Object o) {
		this.listItems.add(o);
	}

}
