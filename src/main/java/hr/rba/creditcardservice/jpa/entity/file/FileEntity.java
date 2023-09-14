package hr.rba.creditcardservice.jpa.entity.file;

import hr.rba.creditcardservice.jpa.entity.person.PersonEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "files")
@Getter
@Setter
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fileName;

    @Enumerated(value = EnumType.STRING)
    private FileStatus status;

    @ManyToOne
    @JoinColumn(referencedColumnName = "personId")
    private PersonEntity person;
}
