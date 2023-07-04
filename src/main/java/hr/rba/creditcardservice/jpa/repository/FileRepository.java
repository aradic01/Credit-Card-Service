package hr.rba.creditcardservice.jpa.repository;

import hr.rba.creditcardservice.jpa.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    @Query("UPDATE files SET status = 'INACTIVE', person.personId = null where person.personId = :personId")
    @Modifying
    void deactivateFilesAndRemovePersonReferences(Long personId);
}
