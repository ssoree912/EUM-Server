package eum.backed.server.common.DTO;

import eum.backed.server.domain.community.user.Users;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class TokenDTO {

    private Long id;
    private String username;
    private Boolean isKid;
    private Boolean isFemale;

    public TokenDTO(Users user) {
        this.id = user.getUserId();
        this.username = user.getUsername();
    }
}

