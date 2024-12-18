package project.festival.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class LoginEntity {
    @Id
    private Long id;

    @NotEmpty
    private String loginId;
    @NotEmpty
    private String password;
}
