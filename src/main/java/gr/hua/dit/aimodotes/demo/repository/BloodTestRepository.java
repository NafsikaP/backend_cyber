package gr.hua.dit.aimodotes.demo.repository;


import gr.hua.dit.aimodotes.demo.entity.BloodTest;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Hidden
public interface BloodTestRepository extends JpaRepository<BloodTest, Integer> {
    Optional<BloodTest> findByAppForm_Id(Integer appform_id);
}
