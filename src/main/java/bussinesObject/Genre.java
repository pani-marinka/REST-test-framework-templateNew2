package bussinesObject;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class Genre implements Serializable, Cloneable {

    private Integer genreId;

    private String genreName;


    private String genreDescription;

    public Genre(Integer genreId, String genreName, String genreDescription) {
        this.genreId = genreId;
        this.genreName = genreName;
        this.genreDescription = genreDescription;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

   }