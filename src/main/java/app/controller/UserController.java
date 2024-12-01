package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import app.model.User;
import app.repository.UserRepository;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/cadastro")
    public String showCadastroPage(Model model) {
        model.addAttribute("user", new User());
        return "cadastro"; 
    }

    @PostMapping("/cadastro")
    public String processCadastro(@ModelAttribute User user) {
        userRepository.save(user);
        return "login"; 
    }

    
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; 
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute User user, Model model) {
        User foundUser = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        if (foundUser != null) {
            return "home";  
        } else {
            model.addAttribute("error", "Usuário ou senha inválidos");
            return "login"; 
        }
    }
}
