package gr.hua.dit.aimodotes.demo.rest;

import gr.hua.dit.aimodotes.demo.entity.DonationRequest;
import gr.hua.dit.aimodotes.demo.entity.Secretary;
import gr.hua.dit.aimodotes.demo.entity.User;
import gr.hua.dit.aimodotes.demo.repository.DonationRequestRepository;
import gr.hua.dit.aimodotes.demo.repository.SecretaryRepository;
import gr.hua.dit.aimodotes.demo.service.DonationRequestService;
import gr.hua.dit.aimodotes.demo.service.EmailService;
import gr.hua.dit.aimodotes.demo.service.UserDetailsServiceImpl;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/donationrequest")
public class DonationRequestRestController {
    @Autowired
    private DonationRequestRepository donationRequestRepository;

    @Autowired
    private DonationRequestService donationRequestService;

    @Autowired
    private SecretaryRepository secretaryRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private EmailService emailservice;


    //admin and secretary can see all the donation requests
    @GetMapping("")
    @Secured({"ROLE_ADMIN", "ROLE_SECRETARY"})
    public List<DonationRequest> getDonationRequests() {
        return donationRequestService.getDonationRequests();
    }


    @PostMapping("/{user_id}/new")
    @Secured("ROLE_SECRETARY")
    public ResponseEntity<Map<String, String>> saveDonationRequest(@PathVariable Integer user_id, @RequestBody DonationRequest donationRequest) {
        Map<String, String> response = new HashMap<>();

        if (donationRequestRepository.findByLocationAndDate(donationRequest.getLocation(), donationRequest.getDate()).isPresent()) {
            System.out.println("Donation Request already exists.");
            response.put("error", "Error. Donation Request already exists.");
            return ResponseEntity.badRequest().body(response);
        } else if (donationRequest.getDate() == null || donationRequest.getLocation().isBlank() || donationRequest.getDate().isBefore(LocalDate.now())) {
            response.put("error", "Invalid donation request details.");
            return ResponseEntity.badRequest().body(response);
        } else {
            User user = userDetailsService.getUser(user_id);
            String email = user.getEmail();
            Secretary secretary = secretaryRepository.findByEmail(email).orElse(null);
            if (secretary != null) {
                System.out.println("GDGDHFGDFG");
                donationRequest.setSecretary(secretary);
                donationRequestService.saveDonationRequest(donationRequest);

                // Send confirmation email
                try {
                    String subject = "New Donation Request Submitted";
                    String body = "A new donation request has been created for " + donationRequest.getLocation() + " on " + donationRequest.getDate() + ".";
                    emailservice.sendEmail(email, subject, body);
                } catch (IOException e) {
                    e.printStackTrace();
                    response.put("error", "Your request has been done, but mail failed to send");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                }

                response.put("success", "Created New Donation Request and sent confirmation email.");
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "Secretary not found.");
                return ResponseEntity.badRequest().body(response);
            }
        }
    }

    //secretary can delete one donation request
    //OXI STO FRONTEND
    @DeleteMapping("/delete/{donation_request_id}")
    @Secured("ROLE_SECRETARY")
    public void deleteDonationRequest(@PathVariable Integer donation_request_id) {
        donationRequestService.deleteDonationRequest(donation_request_id);
    }

    //secretary can see one donation request
    @GetMapping("{donation_request_id}")
    @Secured("ROLE_SECRETARY")
    public DonationRequest getDonationRequest(@PathVariable Integer donation_request_id) {
        return donationRequestService.getDonationRequest(donation_request_id);
    }

}
