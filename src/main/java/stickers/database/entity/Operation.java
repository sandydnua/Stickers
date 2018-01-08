package stickers.database.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "operations")
@Getter
@Setter
@NoArgsConstructor
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "name")
    @NonNull
    private String name;

    public Operation(String name) {
        this.name = name;
    }
}
