package hr.rba.creditcardservice.jpa.entity.file;

public enum FileStatus {
    ACTIVE("active"), INACTIVE("inactive");

    private final String code;

    FileStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
