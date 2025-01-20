package gr.hua.dit.aimodotes.demo.service;

import gr.hua.dit.aimodotes.demo.dao.AimodotisDAO;
import gr.hua.dit.aimodotes.demo.entity.*;
import gr.hua.dit.aimodotes.demo.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

/**
 * Service to populate database with initial data.
 */
@Service
public class InitialDataService {



    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final SecretaryRepository secretaryRepository;
    private final AppFormRepository appFormRepository;
    private final BloodTestRepository bloodTestRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    private final DonationRequestRepository donationRequestRepository;

    @Autowired
    private AimodotisDAO aimodotisDAO;
    private final AimodotisRepository aimodotisRepository;

    @Autowired
    private DonationRequestService donationRequestService;
    private final PasswordEncoder passwordEncoder;

    public InitialDataService(UserRepository userRepository, RoleRepository roleRepository, SecretaryRepository secretaryRepository, AppFormRepository appFormRepository, BloodTestRepository bloodTestRepository, DonationRequestRepository donationRequestRepository, AimodotisRepository aimodotisRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.secretaryRepository = secretaryRepository;
        this.appFormRepository = appFormRepository;
        this.bloodTestRepository = bloodTestRepository;
        this.donationRequestRepository = donationRequestRepository;
        this.aimodotisRepository = aimodotisRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private void createUsersAndRoles() {
        final List<String> rolesToCreate = List.of("ROLE_ADMIN", "ROLE_USER", "ROLE_SECRETARY", "ROLE_AIMODOTIS");
        for (final String roleName : rolesToCreate) {
            roleRepository.findByName(roleName).orElseGet(() -> {
                roleRepository.save(new Role(roleName));
                return null;
            });
        }


        this.userRepository.findByEmail("georgegolf1234@gmail.com").orElseGet(() -> {
            User user = new User("admin","georgegolf1234@gmail.com", this.passwordEncoder.encode("1234"));
            Set<Role> roles = new HashSet<>();
            roles.add(this.roleRepository.findByName("ROLE_USER").orElseThrow());
            roles.add(this.roleRepository.findByName("ROLE_ADMIN").orElseThrow());
            user.setRoles(roles);
            userRepository.save(user);
            return null;
        });

        this.userRepository.findByEmail("dan.kamper.ou@gmail.com").orElseGet(() -> {
            User user = new User("sec","dan.kamper.ou@gmail.com", this.passwordEncoder.encode("1234"));
            Set<Role> roles = new HashSet<>();
            roles.add(this.roleRepository.findByName("ROLE_USER").orElseThrow());
            roles.add(this.roleRepository.findByName("ROLE_SECRETARY").orElseThrow());
            user.setRoles(roles);
            userRepository.save(user);
            return null;
        });

        this.userRepository.findByEmail("naf.pap2003@gmail.com").orElseGet(() -> {
            User user = new User("naf","naf.pap2003@gmail.com", this.passwordEncoder.encode("1234"));
            Set<Role> roles = new HashSet<>();
            roles.add(this.roleRepository.findByName("ROLE_USER").orElseThrow());
            roles.add(this.roleRepository.findByName("ROLE_AIMODOTIS").orElseThrow());
            user.setRoles(roles);
            userRepository.save(user);
            return null;
        });

        this.userRepository.findByEmail("geo@gmail.com").orElseGet(() -> {
            User user = new User("geo","geo@gmail.com", this.passwordEncoder.encode("1234"));
            Set<Role> roles = new HashSet<>();
            roles.add(this.roleRepository.findByName("ROLE_USER").orElseThrow());
            roles.add(this.roleRepository.findByName("ROLE_AIMODOTIS").orElseThrow());
            user.setRoles(roles);
            userRepository.save(user);
            return null;
        });

        this.userRepository.findByEmail("dan@gmail.com").orElseGet(() -> {
            User user = new User("dan","dan@gmail.com", this.passwordEncoder.encode("1234"));
            Set<Role> roles = new HashSet<>();
            roles.add(this.roleRepository.findByName("ROLE_USER").orElseThrow());
            roles.add(this.roleRepository.findByName("ROLE_AIMODOTIS").orElseThrow());
            user.setRoles(roles);
            userRepository.save(user);
            return null;
        });
    }

    private void createDonationRequest() {
        donationRequestRepository.findByLocationAndDate("Athens", LocalDate.parse("2024-04-05")).orElseGet(() -> {
            DonationRequest donationRequest = donationRequestRepository.save(new DonationRequest("Athens", LocalDate.parse("2024-04-05")));
            donationRequest.setSecretary(secretaryRepository.findByAFM("123456789").get());
            donationRequestService.saveDonationRequest(donationRequest);
            return null;
        });
        donationRequestRepository.findByLocationAndDate("Patra", LocalDate.parse("2024-06-05")).orElseGet(() -> {
            DonationRequest donationRequest = donationRequestRepository.save(new DonationRequest("Patra", LocalDate.parse("2024-06-05")));
            donationRequest.setSecretary(secretaryRepository.findByAFM("123456789").get());
            donationRequestService.saveDonationRequest(donationRequest);
            return null;
        });
    }

    private void createAimodotisProfile() {
        secretaryRepository.findByAFM("123456789").orElseGet(() -> {
            secretaryRepository.save(new Secretary("Maria", "Papa","123456789", "dan.kamper.ou@gmail.com"));
            return null;
        });
        aimodotisRepository.findByAMKA("05110301111").orElseGet(() -> {
            Aimodotis aimodotis = aimodotisRepository.save(new Aimodotis("Nafsika", "Papaioannou", "naf.pap2003@gmail.com", "6985762160", "05110301111", 'F', LocalDate.parse("2024-01-11"), 20, "Athens"));
            AppForm appForm = appFormRepository.save(new AppForm(AppForm.Status.ACCEPTED,LocalDate.parse("2024-01-10")));
            BloodTest bloodTest = bloodTestRepository.save(new BloodTest(LocalDate.parse("2021-11-25"), "White Blood Cell Count (WBC): 7.2 x10^3/µL\n Red Blood Cell Count (RBC): 5.0 x10^6/µL\n Hemoglobin (HGB): 15.5 g/dL\n Hematocrit (HCT): 45%\n Platelet Count: 250 x10^3/µL","0+"));
            appForm.setAimodotis(aimodotis);
            appForm.setBloodTest(bloodTest);
            appForm.setSecretary(secretaryRepository.findByAFM("123456789").get());
            aimodotis.setAppForm(appForm);
            bloodTest.setAppForm(appForm);
            bloodTestRepository.save(bloodTest);
            aimodotisDAO.saveAimodotis(aimodotis);
            appFormRepository.save(appForm);
            return null;
        });
        aimodotisRepository.findByAMKA("25110301550").orElseGet(() -> {
            Aimodotis aimodotis = aimodotisRepository.save(new Aimodotis("Giwrgos", "Gkolfinopoulos", "geo@gmail.com", "6980763944", "25110301550", 'M', LocalDate.parse("2023-12-01"), 20, "Athens"));
            AppForm appForm = appFormRepository.save(new AppForm(AppForm.Status.ACCEPTED,LocalDate.parse("2024-11-28")));
            BloodTest bloodTest = bloodTestRepository.save(new BloodTest(LocalDate.parse("2023-10-30"), "White Blood Cell Count (WBC): 7.2 x10^3/µL\n Red Blood Cell Count (RBC): 5.0 x10^6/µL\n Hemoglobin (HGB): 15.5 g/dL\n Hematocrit (HCT): 45%\n Platelet Count: 250 x10^3/µL","A+"));
            appForm.setAimodotis(aimodotis);
            appForm.setBloodTest(bloodTest);
            appForm.setSecretary(secretaryRepository.findByAFM("123456789").get());
            aimodotis.setAppForm(appForm);
            bloodTest.setAppForm(appForm);
            bloodTestRepository.save(bloodTest);
            aimodotisDAO.saveAimodotis(aimodotis);
            appFormRepository.save(appForm);
            return null;
        });
        aimodotisRepository.findByAMKA("13456789068").orElseGet(() -> {
            Aimodotis aimodotis = aimodotisRepository.save(new Aimodotis("Danai", "Kamperou", "dan@gmail.com", "6935546778", "13456789068", 'F', null, 20, "Patra"));
            AppForm appForm = appFormRepository.save(new AppForm(AppForm.Status.ACCEPTED,LocalDate.parse("2024-01-09")));
            BloodTest bloodTest = bloodTestRepository.save(new BloodTest(LocalDate.parse("2023-12-22"), "White Blood Cell Count (WBC): 7.2 x10^3/µL\n Red Blood Cell Count (RBC): 5.0 x10^6/µL\n Hemoglobin (HGB): 15.5 g/dL\n Hematocrit (HCT): 45%\n Platelet Count: 250 x10^3/µL","B+"));
            appForm.setAimodotis(aimodotis);
            appForm.setBloodTest(bloodTest);
            appForm.setSecretary(secretaryRepository.findByAFM("123456789").get());
            aimodotis.setAppForm(appForm);
            bloodTest.setAppForm(appForm);
            bloodTestRepository.save(bloodTest);
            aimodotisDAO.saveAimodotis(aimodotis);
            appFormRepository.save(appForm);
            return null;
        });
    }



    @PostConstruct
    public void setup() {
        this.createUsersAndRoles();
        this.createAimodotisProfile();
        this.createDonationRequest();
    }
}