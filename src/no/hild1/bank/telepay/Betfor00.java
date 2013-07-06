package no.hild1.bank.telepay;

public class Betfor00 extends Betfor {
    public Betfor00(BetforHeader header, String stringRecord) {
        super(header, stringRecord);
    }

    public String get(Element e) {
        return "";//m.group(((Element)e).name());
    }
    public enum Element {
        NOELEMENTS
    }
}