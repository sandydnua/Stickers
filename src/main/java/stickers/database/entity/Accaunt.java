package stickers.database.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "accaunts")
@Setter
@Getter
@NoArgsConstructor
public class Accaunt implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Setter
    @Getter
    @Column(name = "email")
    @NonNull
    private String email;

    @Column(name ="password")
    @NonNull
    private String password;

    @Column(name = "firstName")
    @NonNull
    private  String firstName;

    @Column(name = "lastName")
    @NonNull
    private  String lastName;

    @Column(name = "confirm")
    private  boolean confirm ;

    public Accaunt(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public boolean isConfirm() {
        return confirm;
    }
}
