package findit.sedi.viktor.com.findit.exceptions;

public class CrashlicsExceptionHelper extends Exception {


    private String information;

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }

    @Override
    public String getMessage() {
        return information;
    }


    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

}
