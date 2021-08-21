package bussinesObject;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class Book implements Serializable {

    private Integer bookId;


    private String bookName;


    private String bookLanguage;


    private String bookDescription;


    private Additional additional;


    private Double volume;


      private Integer publicationYear;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(bookId, book.bookId) && Objects.equals(bookName, book.bookName) && Objects.equals(bookLanguage, book.bookLanguage) && Objects.equals(bookDescription, book.bookDescription) && Objects.equals(additional, book.additional) && Objects.equals(volume, book.volume) && Objects.equals(publicationYear, book.publicationYear);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, bookName, bookLanguage, bookDescription, additional, volume, publicationYear);
    }
}