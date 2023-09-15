package hr.rba.creditcardservice.jpa.entity.token;

import hr.rba.creditcardservice.jpa.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "tokens")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long tokenId;

    @Column(unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    private boolean revoked;

    @ManyToOne
    @JoinColumn(referencedColumnName = "userId")
    private UserEntity user;
}
