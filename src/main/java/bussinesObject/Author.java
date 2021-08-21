package bussinesObject;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class Author implements Serializable {

    private Integer authorId;

    private AuthorName authorName;


    private String nationality;

    private Birth birth;

    private String authorDescription;




}
