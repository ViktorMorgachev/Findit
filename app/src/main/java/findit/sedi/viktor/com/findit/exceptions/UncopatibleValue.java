package findit.sedi.viktor.com.findit.exceptions;

public class UncopatibleValue extends Exception {

    public UncopatibleValue() {
        super();
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }

    @Override
    public String getMessage() {
        return "Was trying to put uncopatible value";
    }
}
