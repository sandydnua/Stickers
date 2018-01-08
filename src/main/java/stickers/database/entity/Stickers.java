package stickers.database.entity;


import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name="stickers")
@Getter
@Setter
@NoArgsConstructor
public class Stickers {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "text")
    @Type(type = "text")
    private String text;

    @JoinColumn(name = "creator")
    @ManyToOne(fetch = FetchType.EAGER)
    @NonNull
    private Accaunt creator;

    @JoinColumn(name = "board")
    @ManyToOne(fetch = FetchType.EAGER)
    @NonNull
    private Boards board;

    public Stickers(String text, Accaunt creator, Boards board) {
        this.text = text;
        this.creator = creator;
        this.board = board;
    }
}
