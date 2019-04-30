package findit.sedi.viktor.com.findit.data;

public class Game {

    String dateFrom;
    String dateTo;
    boolean IncludePrelim;
    Type mType;

    enum Type {
        OneByOne(0),
        Gropups(2);

        Type(long i) {
        }
    }

}
