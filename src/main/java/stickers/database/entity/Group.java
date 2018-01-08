package stickers.database.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "groups")
@Getter
@Setter
@NoArgsConstructor
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "name")
    @NonNull
    private String name;

    @JoinColumn(name = "boards")
    @ManyToOne(fetch = FetchType.EAGER)
    @NonNull
    private Boards board;

    public Group(String name, Boards board) {
        this.name = name;
        this.board = board;
    }
}
