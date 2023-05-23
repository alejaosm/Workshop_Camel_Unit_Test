package co.com.bancolombia.domain.entities.model.dtos.enums;

public enum TypeEnum {
    TIPDOC_FS000("TIPDOC_FS000"),
    TIPDOC_FS001("TIPDOC_FS001"),
    TIPDOC_FS002("TIPDOC_FS002"),
    TIPDOC_FS003("TIPDOC_FS003"),
    TIPDOC_FS004("TIPDOC_FS004"),
    TIPDOC_FS005("TIPDOC_FS005"),
    TIPDOC_FS006("TIPDOC_FS006"),
    TIPDOC_FS007("TIPDOC_FS007"),
    TIPDOC_FS008("TIPDOC_FS008"),
    TIPDOC_FS009("TIPDOC_FS009");

    private final String value;

    TypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

