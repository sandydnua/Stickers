package stickers.database.entity;


import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name="stickers")
public class Stickers {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "text")
    @Type(type = "text")
    private String text;

    @JoinColumn(name = "creator")
    @ManyToOne(fetch = FetchType.EAGER)
    private Accaunt creator;

    @JoinColumn(name = "board")
    @ManyToOne(fetch = FetchType.EAGER)
    private Boards board;

    public Stickers(String text, Accaunt creator, Boards board) {
        this.text = text;
        this.creator = creator;
        this.board = board;
    }

    public Stickers() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Accaunt getCreatorId() {
        return creator;
    }

    public void setCreatorId(Accaunt creator) {
        this.creator = creator;
    }

    public Boards getBoard() {
        return board;
    }

    public void setBoard(Boards board) {
        this.board = board;
    }
}
