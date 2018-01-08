package stickers.database.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "accaunts_in_groups")
@Getter
@Setter
@NoArgsConstructor
public class AccauntsInGroups {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @JoinColumn(name = "accaunt")
    @ManyToOne(fetch = FetchType.EAGER)
    @NonNull
    private Accaunt accaunt;

    @JoinColumn(name = "groupT")
    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Group group;

    public AccauntsInGroups(Accaunt accaunt, Group group) {
        this.accaunt = accaunt;
        this.group = group;
    }
}
