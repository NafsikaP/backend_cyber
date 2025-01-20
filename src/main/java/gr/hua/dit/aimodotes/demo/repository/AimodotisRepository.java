package gr.hua.dit.aimodotes.demo.repository;

import gr.hua.dit.aimodotes.demo.entity.Aimodotis;
import org.springframework.data.jpa.repository.JpaRepository;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//@RepositoryRestResource(path= "aimodotis")
//@Hidden
@Repository
@Hidden
public interface AimodotisRepository extends JpaRepository<Aimodotis, Integer> {
    Optional<Aimodotis> findByAMKA(String AMKA);
    Optional<Aimodotis> findByEmail(String email);

}
