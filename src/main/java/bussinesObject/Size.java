package bussinesObject;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class Size implements Serializable { //for Book.class

    private Double height;


    private Double width;


    private Double length;

}
