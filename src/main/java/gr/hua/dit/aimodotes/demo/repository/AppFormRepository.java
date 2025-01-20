package gr.hua.dit.aimodotes.demo.repository;


import gr.hua.dit.aimodotes.demo.entity.Aimodotis;
import gr.hua.dit.aimodotes.demo.entity.AppForm;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(path= "appform")
@Hidden
public interface AppFormRepository extends JpaRepository<AppForm, Integer> {

    Optional<AppForm> findById(Integer appform_id);

    Optional<AppForm> findByAimodotis(Aimodotis aimodotis);
}
