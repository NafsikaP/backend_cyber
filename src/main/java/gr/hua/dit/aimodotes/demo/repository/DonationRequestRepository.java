package gr.hua.dit.aimodotes.demo.repository;


import gr.hua.dit.aimodotes.demo.entity.DonationRequest;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

//@Repository
@RepositoryRestResource(path= "donationrequest")
@Hidden
public interface DonationRequestRepository extends JpaRepository<DonationRequest, Integer> {
    Optional<DonationRequest> findByLocationAndDate(String location, LocalDate date);
}
