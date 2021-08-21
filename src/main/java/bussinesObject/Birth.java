package bussinesObject;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class Birth { //for Author
    private String date;


    private String country;


    private String city;
}
