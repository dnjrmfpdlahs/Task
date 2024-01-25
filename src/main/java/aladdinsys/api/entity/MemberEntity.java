package aladdinsys.api.entity;

import aladdinsys.api.role.Role;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;

@Entity
@Table(name = "member")
@Data
public class MemberEntity {

    @Id
    private String userId;
    private String password;
    private String name;
    private String regNo;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    public MemberEntity() {}

    public MemberEntity(String userId, String hashedPassword, String name, String regNo, Set<Role> roles) {
        this.userId = userId;
        this.password = hashedPassword;
        this.name = name;
        this.regNo = regNo;
        this.roles = roles;
    }
}
