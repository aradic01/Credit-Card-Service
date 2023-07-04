package hr.rba.creditcardservice.jpa.repository;

import hr.rba.creditcardservice.jpa.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, Long> {
    Optional<PersonEntity> findByOib(String oib);
    @Query("SELECT p.personId from people p where p.oib = :oib")
    Optional<Long> findPersonIdByOib(String oib);
    void deleteByOib(String oib);
}
