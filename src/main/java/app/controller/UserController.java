package app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import app.model.User;
import app.repository.UserRepository;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String defaultPage(HttpSession session) {
        if (session.getAttribute("username") != null) {
            return "redirect:/home";
        }
        return "redirect:/login";
    }

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
    public String processLogin(@ModelAttribute User user, Model model, HttpSession session) {
        User foundUser = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        if (foundUser != null) {
            session.setAttribute("username", foundUser.getUsername());
            return "redirect:/home";
        } else {
            model.addAttribute("error", "Usuário ou senha inválidos");
            return "login";
        }
    }

    @GetMapping("/home")
    public String homePage(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        model.addAttribute("username", username);
        model.addAttribute("cupcakes", getCupcakes());
        return "home";
    }

    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        List<Map<String, String>> cart = getOrInitializeCart(session);
        model.addAttribute("cart", cart);
        return "cart";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(HttpSession session, String name, String price) {
        List<Map<String, String>> cart = getOrInitializeCart(session);
        cart.add(Map.of("name", name, "price", price));
        session.setAttribute("cart", cart);
        return "redirect:/home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("cart");
        session.invalidate();
        return "redirect:/login";
    }

    private List<Map<String, String>> getOrInitializeCart(HttpSession session) {
        Object cartObject = session.getAttribute("cart");
        if (cartObject instanceof List<?>) {
            @SuppressWarnings("unchecked")
            List<Map<String, String>> cart = (List<Map<String, String>>) cartObject;
            return cart;
        } else {
            List<Map<String, String>> cart = new ArrayList<>();
            session.setAttribute("cart", cart);
            return cart;
        }
    }

    private List<Map<String, String>> getCupcakes() {
        return List.of(
                Map.of("name", "Chocolate", "description", "Delicioso cupcake de chocolate", "price", "R$ 5,00",
                        "image", "/images/chocolate.jpg"),
                Map.of("name", "Baunilha", "description", "Cupcake clássico de baunilha", "price", "R$ 4,50", "image",
                        "/images/baunilha.jpg"),
                Map.of("name", "Red Velvet", "description", "Cupcake com sabor e cor únicos", "price", "R$ 6,00",
                        "image", "/images/red_velvet.jpg"),
                Map.of("name", "Limão", "description", "Refrescante cupcake de limão", "price", "R$ 5,50", "image",
                        "/images/limao.jpg"));
    }
}
