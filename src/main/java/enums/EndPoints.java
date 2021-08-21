package enums;

public enum EndPoints {

    GENREURL("http://localhost:8080/api/library/genre"),
    AUTHORURL("http://localhost:8080/api/library/book"),
    END_POINTS("http://localhost:8080/api/library"),
    GENRESURL("http://localhost:8080/api/library/genres"),;

    private EndPoints(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }
}
