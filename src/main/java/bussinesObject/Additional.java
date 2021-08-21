package bussinesObject;


import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class Additional implements Serializable {  //for Book.class
    private Integer pageCount;
    private Size size;
}
