package stickers.database.entity;

import javax.persistence.*;

@Entity
@Table(name = "operations_for_groups")
public class OperationsForGroups {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @JoinColumn(name = "groupT")
    @ManyToOne(fetch = FetchType.EAGER)
    private Group group;

    @JoinColumn(name = "operation")
    @ManyToOne(fetch = FetchType.EAGER)
    private Operation operation;

    public OperationsForGroups(Group group, Operation operation) {
        this.group = group;
        this.operation = operation;
    }

    public OperationsForGroups() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }
}
