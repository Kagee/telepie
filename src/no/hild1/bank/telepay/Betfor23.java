package no.hild1.bank.telepay;

public class Betfor23 extends Betfor {
    public Betfor23(BetforHeader header, String stringRecord) {
        super(header, stringRecord);
    }

    public String get(Element e) {
        return "";//m.group(((Element)e).name());
    }
    public enum Element {
        ENTERPRISENUMBER, ACCOUNTNUMBER, SEQUENCECONTROL, REFERENCENUMBER, PAYMENTDATE, OWNREFORDER, RESERVED, PAYEESACCOUNTNUMBER, PAYEESNAME, ADDRESS1;
    }
}