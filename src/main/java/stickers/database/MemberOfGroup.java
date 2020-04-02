package stickers.database;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@AllArgsConstructor
public class MemberOfGroup {
    public int id;
    public String firstName;
    public String lastName;
}
