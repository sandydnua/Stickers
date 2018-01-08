package stickers.database.entity;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@Table(name="boards")
@Getter
@Setter
@NoArgsConstructor
public class Boards {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "title")
    @NonNull
    private String title;

    @JoinColumn(name = "creator")
    @ManyToOne(fetch = FetchType.EAGER)
    @NonNull
    private Accaunt creator;

    @Column(name = "exposed")
    private  boolean exposed;

    public boolean isExposed() {
        return exposed;
    }

    public Boards(String title, Accaunt creator) {
        this.title = title;
        this.creator = creator;
    }
}
