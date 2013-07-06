package no.hild1.bank.telepay;

import java.util.ArrayList;
import java.util.regex.Pattern;

public abstract class Betfor {
	ArrayList<Betfor> childs;
	Betfor parent;
	BetforHeader header;
    String  stringRecord;
	public Betfor(BetforHeader header, String stringRecord) {
		childs = new ArrayList<Betfor>();
		parent = null;
        this.header = header;
        this.stringRecord =  stringRecord;
	}

	public int numChilds() {
		return childs.size();
	}

	public void addChild(Betfor child) {
		childs.add(child);
	}

	public void setParent(Betfor parent) {
		this.parent = parent;
	}
}