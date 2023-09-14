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
    private String value;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    private boolean revoked;
    private boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "userId")
    public UserEntity user;
}
