package gr.hua.dit.aimodotes.demo.repository;

import gr.hua.dit.aimodotes.demo.entity.Secretary;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(path= "secretary")
@Hidden
public interface SecretaryRepository extends JpaRepository<Secretary, Integer> {
    Optional<Secretary> findByAFM(String AFM);
    Optional<Secretary> findByEmail(String email);
}
