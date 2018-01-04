package stickers.database.entity;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@Table(name="boards")
public class Boards {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "title")
    private String title;

    @JoinColumn(name = "creator")
    @ManyToOne(fetch = FetchType.EAGER)
    private Accaunt creator;

    @Column(name = "exposed")
    private  boolean exposed;

    public boolean isExposed() {
        return exposed;
    }

    public void setExposed(boolean exposed) {
        this.exposed = exposed;
    }

    public Boards(String title, Accaunt creator) {
        this.title = title;
        this.creator = creator;
    }

    public Boards() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public Accaunt getCreator() {
        return creator;
    }

    public void setCreator(Accaunt creator) {
        this.creator = creator;
    }
}
