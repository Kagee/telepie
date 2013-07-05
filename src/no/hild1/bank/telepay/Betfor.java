package no.hild1.bank.telepay;

import java.util.ArrayList;

public class Betfor {
	ArrayList<Betfor> childs;
	Betfor parent;
	BetforHeader header;
	public Betfor() {
		childs = new ArrayList<Betfor>();
		parent = null;
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