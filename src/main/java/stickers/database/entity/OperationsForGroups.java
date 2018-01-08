package stickers.database.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "operations_for_groups")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OperationsForGroups {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @JoinColumn(name = "groupT")
    @ManyToOne(fetch = FetchType.EAGER)
    @NonNull
    private Group group;

    @JoinColumn(name = "operation")
    @ManyToOne(fetch = FetchType.EAGER)
    @NonNull
    private Operation operation;

    public OperationsForGroups(Group group, Operation operation) {
        this.group = group;
        this.operation = operation;
    }
}
