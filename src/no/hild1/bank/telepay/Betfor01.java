package no.hild1.bank.telepay;

public class Betfor01 extends Betfor {
    /* makeBetforData.sh START */
    /* makeBetforData.sh STOP */
    public Betfor01(BetforHeader header, String stringRecord) {
        super(header, stringRecord);
    }

    public String get(Element e) {
        return "";//m.group(((Element)e).name());
    }
    public enum Element {
        ENTERPRISENUMBER, ACCOUNTNUMBER, SEQUENCECONTROL, REFERENCENUMBER, PAYMENTDATE, OWNREFORDER, RESERVED, PAYEESACCOUNTNUMBER, PAYEESNAME, ADDRESS1;
    }
}