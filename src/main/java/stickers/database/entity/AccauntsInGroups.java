package stickers.database.entity;

import javax.persistence.*;

@Entity
@Table(name = "accaunts_in_groups")
public class AccauntsInGroups {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @JoinColumn(name = "accaunt")
    @ManyToOne(fetch = FetchType.EAGER)
    private Accaunt accaunt;

    @JoinColumn(name = "groupT")
    @ManyToOne(fetch = FetchType.EAGER)
    private Group group;

    public AccauntsInGroups(Accaunt accaunt, Group group) {
        this.accaunt = accaunt;
        this.group = group;
    }

    public AccauntsInGroups() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Accaunt getAccaunt() {
        return accaunt;
    }

    public void setAccaunt(Accaunt accaunt) {
        this.accaunt = accaunt;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
