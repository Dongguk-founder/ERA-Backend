package founders.EasyRouteAssistant.domain;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User {

        @Id //primary key
        private String id;
        @Nonnull
        private String name;
        @Nonnull
        private String email;
        @Nonnull
        private String password;
        public User(String id, String name, String email,String password){
            this.id = id;
            this.name = name;
            this.email = email;
            this.password = password;
        }

}

