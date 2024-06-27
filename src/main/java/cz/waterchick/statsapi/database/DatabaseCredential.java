package cz.waterchick.statsapi.database;

public enum DatabaseCredential {

    HOST("localhost"),
    PORT("3306"),
    DATABASE("statsapi"),
    USERNAME("user"),
    PASSWORD("password")
    ;

    private String value;



    DatabaseCredential(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
