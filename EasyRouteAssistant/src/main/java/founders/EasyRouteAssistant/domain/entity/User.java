package founders.easyRouteAssistant.domain.entity;


import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity //엔티티에서는 primary키가 반드시 필요하다. (@Id로 지정)
@Table(name = "Users")
public class User {
    @Id //primary key
    private String id;
    @Nonnull
    private String name;
    @Nonnull
    private String email;
    @Nonnull
    private String password;

    public User(String id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
