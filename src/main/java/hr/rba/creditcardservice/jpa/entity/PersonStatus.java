package hr.rba.creditcardservice.jpa.entity;

public enum PersonStatus {
    CLIENT("client"), NON_CLIENT("non-client");

    private final String code;

    PersonStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
