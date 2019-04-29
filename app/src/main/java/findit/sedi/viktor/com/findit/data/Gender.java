package findit.sedi.viktor.com.findit.data;

public enum Gender {

    Male(1),
    Female(2),
    None(3);

    long gender;

    Gender(long i) {
        gender = i;
    }

    public long getGender() {
        return gender;
    }
}
