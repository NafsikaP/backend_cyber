package gr.hua.dit.aimodotes.demo.rest;

import gr.hua.dit.aimodotes.demo.dao.AimodotisDAO;
import gr.hua.dit.aimodotes.demo.entity.*;
import gr.hua.dit.aimodotes.demo.repository.*;
import gr.hua.dit.aimodotes.demo.service.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@RestController
@RequestMapping("/api/aimodotis")
public class AimodotisRestController {

    @Autowired
    private AimodotisRepository aimodotisRepository;

    @Autowired
    private AimodotisDAO aimodotisDAO;

    @Autowired
    private DonationRequestService donationRequestService;

    @Autowired
    private AppFormService appFormService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AppFormRepository appFormRepository;

    @Autowired
    private BloodTestRepository bloodTestRepository;

    @Autowired
    private SecretaryRepository secretaryRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private EmailService emailService;

    //admin and secretary can see all the blood donors
    @GetMapping("")
    @Secured({"ROLE_ADMIN","ROLE_SECRETARY","ROLE_USER"})
    public List<Aimodotis> getAimodotes(){
        return aimodotisDAO.getAimodotes();
    }


    //admin and secretary can see one blood donor
    @GetMapping("{aimodotis_id}")
    @Secured({"ROLE_ADMIN","ROLE_SECRETARY"})
    public Aimodotis getAimodotis(@PathVariable Integer aimodotis_id) {
        return aimodotisDAO.getAimodotis(aimodotis_id);
    }


    //blood donor can update his details
    @PostMapping("/update/{aimodotis_id}")
    @Secured("ROLE_AIMODOTIS")
    public ResponseEntity<Map<String,String>> updateAimodotis(@PathVariable Integer aimodotis_id, @RequestBody Aimodotis updatedAimodotis) {
        Optional<Aimodotis> existingAimodotisOptional = aimodotisRepository.findById(aimodotis_id);
        Map<String, String> response = new HashMap<>();
        if(existingAimodotisOptional.isPresent()) {
            Aimodotis existingAimodotis = existingAimodotisOptional.get();
            if(updatedAimodotis.getFname().isBlank()){
                updatedAimodotis.setFname(existingAimodotis.getFname());
            }
            if(updatedAimodotis.getLname().isBlank()){
                updatedAimodotis.setLname(existingAimodotis.getLname());
            }
            if(updatedAimodotis.getPhone().isBlank()){
                updatedAimodotis.setPhone(existingAimodotis.getPhone());
            }
            if(updatedAimodotis.getAMKA().isBlank()){
                updatedAimodotis.setAMKA(existingAimodotis.getAMKA());
            }
            if(updatedAimodotis.getSex()==null){
                updatedAimodotis.setSex(existingAimodotis.getSex());
            }
            if(updatedAimodotis.getAge()==null){
                updatedAimodotis.setAge(existingAimodotis.getAge());
            }
            if(updatedAimodotis.getLocation().isBlank()){
                updatedAimodotis.setLocation(existingAimodotis.getLocation());
            }
            existingAimodotis.setFname(updatedAimodotis.getFname());
            existingAimodotis.setLname(updatedAimodotis.getLname());
            existingAimodotis.setPhone(updatedAimodotis.getPhone());
            existingAimodotis.setAMKA(updatedAimodotis.getAMKA());
            existingAimodotis.setSex(updatedAimodotis.getSex());
            existingAimodotis.setAge(updatedAimodotis.getAge());
            existingAimodotis.setLocation(updatedAimodotis.getLocation());

            Aimodotis savedAimodotis = aimodotisRepository.save(existingAimodotis);
            try {
                String subject = "Your personal information has been updated";
                String body = "We have successfully update your personal information";
                emailService.sendEmail(savedAimodotis.getEmail(), subject, body);
            } catch (IOException e) {
                e.printStackTrace();
                response.put("error", "Your request has been done, but mail failed to send");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

            response.put("message", "Aimodotis Updated");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Aimodotis not found");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //blood donor can see all the available donation request where he follows the requirements
    @GetMapping("/donationrequests/{user_id}")
    @ResponseBody
    @Secured("ROLE_AIMODOTIS")
    public List<DonationRequest> getAvailableDonations(@PathVariable Integer user_id){
        User user = userDetailsService.getUser(user_id);
        String email =user.getEmail();
        Aimodotis aimodotis = aimodotisRepository.findByEmail(email).orElse(null);
        if (aimodotis != null) {
            List<DonationRequest> donationRequests = donationRequestService.getDonationRequests();
            List<DonationRequest> availableDonationRequests = new ArrayList<>();
            for (int i = 0; i < donationRequests.size(); i++) {
                if (checkDateLastDon(aimodotis, donationRequests.get(i)) && aimodotis.getLocation().equals(donationRequests.get(i).getLocation())) {
                    availableDonationRequests.add(donationRequests.get(i));
                }
            }
            System.out.println(availableDonationRequests);
            return availableDonationRequests;
        }
        return null;
    }

    //a method that checks if you can participate in a blood donation based on the last time you participated in one
    public boolean checkDateLastDon(Aimodotis aimodotis, DonationRequest donationRequest) {
        LocalDate lastDonDate = aimodotis.getLast_donation();
        LocalDate donReqDate = donationRequest.getDate();
        if(lastDonDate==null) {
            if(donReqDate.isAfter(LocalDate.now())) {
                System.out.println("accepted");
                return true;
            }else{
                return false;
            }
        }
        Period period = Period.between(lastDonDate,donReqDate);
        int diff = Math.abs(period.getMonths());
        if ((diff < 3 || donReqDate.isBefore(lastDonDate)) || donReqDate.isBefore(LocalDate.now())) {
            System.out.println("declined");
            return false;
        }
        return true;
    }

    //blood donor can accept a blood donation request and his last donation date gets updated
    @PostMapping("/donationrequests/{user_id}/{donation_request_id}/accept")
    @Secured("ROLE_AIMODOTIS")
    public ResponseEntity<Map<String,String>> acceptRequest(@PathVariable Integer user_id, @PathVariable Integer donation_request_id) {
        Map<String,String> response = new HashMap<>();
        User user = userDetailsService.getUser(user_id);
        String email =user.getEmail();
        Aimodotis aimodotis = aimodotisRepository.findByEmail(email).orElse(null);
        DonationRequest donationRequest = donationRequestService.getDonationRequest(donation_request_id);
        AppForm appForm = appFormRepository.findByAimodotis(aimodotis).get();
        BloodTest bloodTest = bloodTestRepository.findByAppForm_Id(appForm.getId()).get();
        if(aimodotis!=null){
            if (checkBloodTest(bloodTest,donationRequest)) {
                //edit aimdotis last donation date
                aimodotis.setLast_donation(donationRequest.getDate());
                updateAimodotis(aimodotis.getId(), aimodotis);
                donationRequest.addAimodotis(aimodotis);
                donationRequestService.saveDonationRequest(donationRequest);
                try {
                    String subject = "Accepted Blood Donation";
                    String body = "The blood donation you have requested to attend with location " + donationRequest.getLocation() + " on " + donationRequest.getDate() + " has been accepted.";
                    emailService.sendEmail(email, subject, body);
                } catch (IOException e) {
                    e.printStackTrace();
                    response.put("error", "Your request has been done, but mail failed to send");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                }
                response.put("message","Donation Request accepted!");
                return ResponseEntity.ok(response);
            }else{
                response.put("error","Your Blood Test is out of date");
                return ResponseEntity.badRequest().body(response);
            }
        }
        return  null;
    }


    private boolean checkBloodTest(BloodTest bloodTest, DonationRequest donationRequest){
        LocalDate bloodTestDate = bloodTest.getDate();
        LocalDate donReqDate = donationRequest.getDate();

        Period period = Period.between(bloodTestDate, donReqDate);
        int diffyears = period.getYears();
        int diffmonths = period.getMonths();
        if (diffyears > 1 || (diffyears == 1 && diffmonths>0)){
            return false;
        }
        return true;
    }


    //blood donor can confirm his contact info after they have accepted his application and he gets the blood donors role and he is able to participate in blood donations
    @PostMapping("/confirmcontactinfo/{aimodotis_id}")
    @Secured("ROLE_USER")
    public ResponseEntity<Map<String,String>> confirmContactInfo(@PathVariable Integer aimodotis_id) {
        Aimodotis aimodotis = aimodotisRepository.findById(aimodotis_id).get();
        String email = (String) aimodotis.getEmail();
        AppForm appForm = appFormRepository.findByAimodotis(aimodotis).get();
        Map<String,String> response = new HashMap<>();
        if (userRepository.findByEmail(email).isPresent() && appForm.getStatus().equals(AppForm.Status.ACCEPTED)) {
            User user = userRepository.findByEmail(email).get();
            Set<Role> roles = user.getRoles();
            roles.add(this.roleRepository.findByName("ROLE_AIMODOTIS").orElseThrow());
            user.setRoles(roles);
            userRepository.save(user);
            try {
                String subject = "You are now officially a Blood Donator!";
                String body = "Your application form has been accepted by the secretary. You are officially a blood donor! When you log back in to your account you will have access to blood donations to attend.";
                emailService.sendEmail(email, subject, body);
            } catch (IOException e) {
                e.printStackTrace();
                response.put("error", "Your request has been done, but mail failed to send");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            response.put("message","You are now a Blood Donator!");
            return ResponseEntity.ok(response);
        }
        response.put("error", "User not found!");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @PostMapping("/updatebloodtest/{aimodotis_id}")
    @Secured("ROLE_AIMODOTIS")
    public ResponseEntity<Map<String,String>> updateBloodTest(@PathVariable Integer aimodotis_id, @RequestBody BloodTest updatedBloodTest) {
        Aimodotis aimodotis = aimodotisRepository.findById(aimodotis_id).get();
        AppForm appForm = aimodotis.getAppForm();
        Optional<BloodTest> existingBloodTestOptional = bloodTestRepository.findByAppForm_Id(appForm.getId());
        Map<String, String> response = new HashMap<>();
        if(existingBloodTestOptional.isPresent()) {
            BloodTest existingBloodTest = existingBloodTestOptional.get();
            if(updatedBloodTest.getDetails().isBlank()){
                updatedBloodTest.setDetails(existingBloodTest.getDetails());
            }

            if(updatedBloodTest.getDate()!=null){
                existingBloodTest.setDetails(updatedBloodTest.getDetails());
                existingBloodTest.setDate(updatedBloodTest.getDate());
            }

//            existingBloodTest.setDate(LocalDate.now());
            BloodTest savedBloodTest = bloodTestRepository.save(existingBloodTest);
            try {
                String subject = "Updated Blood Test";
                String body = "Your blood test details have been updated successfully!";
                emailService.sendEmail(aimodotis.getEmail(), subject, body);
            } catch (IOException e) {
                e.printStackTrace();
                response.put("error", "Your request has been done, but mail failed to send");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            response.put("message", "Blood Test Updated");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Blood Test not found");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
