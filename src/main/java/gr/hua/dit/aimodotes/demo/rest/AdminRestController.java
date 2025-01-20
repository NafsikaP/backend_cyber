package gr.hua.dit.aimodotes.demo.rest;

import gr.hua.dit.aimodotes.demo.dao.AimodotisDAO;
import gr.hua.dit.aimodotes.demo.dao.SecretaryDAO;
import gr.hua.dit.aimodotes.demo.entity.Aimodotis;
import gr.hua.dit.aimodotes.demo.entity.Role;
import gr.hua.dit.aimodotes.demo.entity.Secretary;
import gr.hua.dit.aimodotes.demo.entity.User;
import gr.hua.dit.aimodotes.demo.payload.response.MessageResponse;
import gr.hua.dit.aimodotes.demo.repository.AimodotisRepository;
import gr.hua.dit.aimodotes.demo.repository.RoleRepository;
import gr.hua.dit.aimodotes.demo.repository.SecretaryRepository;
import gr.hua.dit.aimodotes.demo.repository.UserRepository;
import gr.hua.dit.aimodotes.demo.service.EmailService;
import gr.hua.dit.aimodotes.demo.service.RoleService;
import gr.hua.dit.aimodotes.demo.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AimodotisRepository aimodotisRepository;

    @Autowired
    private SecretaryRepository secretaryRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AimodotisDAO aimodotisDAO;

    @Autowired
    private SecretaryDAO secretaryDAO;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    private RoleService roleService;

    @Autowired
    private EmailService emailService;

    //admin can see all the users' details
    @GetMapping("/users")
    @Secured("ROLE_ADMIN")
    public List<User> getUsers() {
        return userDetailsService.getUsers();
    }

    //admin can see one user's details
    @GetMapping("/users/{user_id}")
    @Secured("ROLE_ADMIN")
    public User getUser(@PathVariable Integer user_id) {
        return userDetailsService.getUser(user_id);
    }

    //method to get roles
    @GetMapping("/roles")
    @Secured("ROLE_ADMIN")
    public List<Role> getRoles() {
        return roleService.getRoles();
    }

    //admin can add a role to the user
    @PostMapping("/addroles/{user_id}/{role_id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Map<String,String>> addRole(@PathVariable Integer user_id, @PathVariable Integer role_id){
        Map<String,String> response = new HashMap<>();
        User user = userDetailsService.getUser(user_id);
        Role role = roleRepository.findById(role_id).get();
        if(user.getRoles().contains(role)){
            response.put("error", "Role already exists");
            return ResponseEntity.badRequest().body(response);
        }
        user.getRoles().add(role);
        userRepository.save(user);
        response.put("message","Added Role");
        return ResponseEntity.ok(response);
    }


    //admin can remove a role from a user
    @DeleteMapping("/deleteroles/{user_id}/{role_id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Map<String,String>> deleteRole(@PathVariable Integer user_id, @PathVariable Integer role_id){
        Map<String,String> response = new HashMap<>();
        User user = userRepository.findById(user_id).get();
        Set<Role> roles = user.getRoles();
        Role role = roleRepository.findById(role_id).get();
        if(user.getRoles().contains(role)){
            roles.remove(this.roleRepository.findById(role_id).orElseThrow());
            user.setRoles(roles);
            userRepository.save(user);
            response.put("message","Removed Role");
            return ResponseEntity.ok(response);
        }
        response.put("error", "Role does not exist, to be deleted");
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/user/new")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Map<String, String>> createNewUser(@RequestBody User user){
        Map<String,String> response = new HashMap<>();
        if (userRepository.existsByUsername(user.getUsername())) {
            response.put("error","Error username already exists!");
            return ResponseEntity.badRequest().body(response);
        }
        if(userRepository.existsByEmail(user.getEmail())){
            response.put("error","Error email is already in use!");
            return ResponseEntity.badRequest().body(response);
        }
        Set<Role> roles = new HashSet<>();
        roles.add(this.roleRepository.findByName("ROLE_USER").orElseThrow());
        user.setRoles(roles);
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        try {
            String subject = "New profile created in Aimodosia Website";
            String body = "A new user profile has been created for you by the admin. Your username is: " + user.getUsername() + " with email " + user.getEmail() + ".";
            emailService.sendEmail(user.getEmail(), subject, body);
        } catch (IOException e) {
            e.printStackTrace();
            response.put("error", "Your request has been done, but mail failed to send");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        response.put("success", "Added user!");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/user/update/{user_id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Map<String,String>> updateUser(@PathVariable Integer user_id, @RequestBody User updatedUser){
        Map<String,String> response = new HashMap<>();
        Optional<User> existingUserOptional = userRepository.findById(user_id);
        Role roleAimodotis = roleRepository.findByName("ROLE_AIMODOTIS").get();
        Role roleSecretary = roleRepository.findByName("ROLE_SECRETARY").get();
        String oldEmail;
        if(existingUserOptional.isPresent()){
            User existingUser = existingUserOptional.get();
            oldEmail = existingUser.getEmail();
            if(updatedUser.getEmail().isBlank()){
                updatedUser.setEmail(existingUser.getEmail());
            }
            if (updatedUser.getUsername().isBlank()){
                updatedUser.setUsername(existingUser.getUsername());
            }
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setEmail(updatedUser.getEmail());

            userRepository.save(existingUser);
            if(existingUser.getRoles().contains(roleAimodotis)){
                List<Aimodotis> aimodotes = aimodotisDAO.getAimodotes();
                for (Aimodotis aimodotis : aimodotes) {
                    if (aimodotis.getEmail().equals(oldEmail)) {
                        aimodotis.setEmail(existingUser.getEmail());
                        aimodotisRepository.save(aimodotis);
                    }
                }
            }
            if(existingUser.getRoles().contains(roleSecretary)){
                List<Secretary> secretaries = secretaryDAO.getSecretaries();
                for (Secretary secretary : secretaries) {
                    if (secretary.getEmail().equals(oldEmail)) {
                        secretary.setEmail(existingUser.getEmail());
                        secretaryRepository.save(secretary);
                    }
                }
            }
            response.put("message","User updated!");
            return ResponseEntity.ok(response);
        }else {
            response.put("error","Error editing user!");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/user/delete/{user_id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Map<String,String>> deleteUser(@PathVariable Integer user_id){
        Map<String,String> response = new HashMap<>();
        User user = userRepository.findById(user_id).get();
        Role roleAimodotis = roleRepository.findByName("ROLE_AIMODOTIS").get();
        Role roleSecretary = roleRepository.findByName("ROLE_SECRETARY").get();
        String userEmail = user.getEmail();
        if(userRepository.findById(user_id).isPresent()){

            if(user.getRoles().contains(roleAimodotis)){
                List<Aimodotis> aimodotes = aimodotisDAO.getAimodotes();
                for (Aimodotis aimodotis : aimodotes) {
                    if (aimodotis.getEmail().equals(userEmail)) {
                        aimodotisDAO.deleteAimodotis(aimodotis.getId());
                    }
                }
            }
            userRepository.delete(user);
            response.put("message","Deleted User");
            return ResponseEntity.ok(response);
        }
        response.put("error","Unable to delete user!");
        return ResponseEntity.badRequest().body(response);
    }
}
