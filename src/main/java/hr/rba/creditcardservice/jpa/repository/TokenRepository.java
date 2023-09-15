package hr.rba.creditcardservice.jpa.repository;

import hr.rba.creditcardservice.jpa.entity.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String value);

    @Query(value = """
            select t from tokens t inner join users u\s
            on t.user.userId = u.userId\s
            where u.userId = :userId and (t.expired = false or t.revoked = false)\s
            """)
    List<Token> findAllValidTokensByUserId(Long userId);
}
