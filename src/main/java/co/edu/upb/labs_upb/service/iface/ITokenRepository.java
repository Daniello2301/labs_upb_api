package co.edu.upb.labs_upb.service.iface;

import co.edu.upb.labs_upb.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ITokenRepository extends JpaRepository<Token, Long> {

    @Query(value = """
            select t from Token t\s
            inner join Usuario u\s
            on t.user.id = u.id\s
            where u.id = :id
            and (t.expired = false or t.revoked = false)\s
            """)
    List<Token> findAllValidTokenByUser(Long id);

    Optional<Token> findByToken(String token);
}
