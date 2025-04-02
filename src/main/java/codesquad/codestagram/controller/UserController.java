package codesquad.codestagram.controller;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.exception.UserNotFoundException;
import codesquad.codestagram.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;
    private final String LOGIN_USER = "loginUser";

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/form")
    public String showJoinForm() {
        return "user/form";
    }

    @PostMapping("/user/create")
    public String create(
            @RequestParam("userId") String userId,
            @RequestParam("password") String password,
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            HttpSession session) {
        User joinedUser = new User(userId, name, password, email);
        userService.join(joinedUser);
        session.setAttribute(LOGIN_USER, joinedUser);
        return "redirect:/users";
    }

    @GetMapping("/users")
    public String list(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute(LOGIN_USER);
        if (loginUser == null) {
            return "redirect:/user/login";
        }
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "user/list";
    }

    @GetMapping("/user/{userId}")
    public String profile(@PathVariable("userId") String userId, Model model, HttpSession session) {
        User user = userService.findOneUser(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        User loginUser = (User) session.getAttribute(LOGIN_USER);
        if (loginUser == null) {
            return "redirect:/user/login";
        }
        model.addAttribute("user", user);
        return "user/profile";
    }

    @GetMapping("/users/{userId}/form")
    public String showUpdateForm(@PathVariable String userId, Model model, HttpSession session) {
        User user = userService.findOneUser(userId).get();
        User loginUser = (User) session.getAttribute(LOGIN_USER);
        if(!user.equals(loginUser)) {
            throw new UserNotFoundException(userId);
        }
        model.addAttribute("user", user);
        return "user/updateForm";
    }

    @PutMapping("/users/{userId}/update")
    public String update(
            @PathVariable String userId,
            @RequestParam("password") String password,
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute(LOGIN_USER);
        if(!loginUser.getUserId().equals(userId)) {
            throw new UserNotFoundException(userId);
        }
        if (!password.equals(loginUser.getPassword())) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("user", loginUser);
            return "user/updateForm";
        }

        loginUser.setName(name);
        loginUser.setEmail(email);
        userService.update(loginUser);
        return "redirect:/users";
    }

    @GetMapping("/user/login")
    public String showLoginForm() {
        return "user/login";
    }

    @PostMapping("/user/login")
    public String login(@RequestParam("userId") String userId,
                        @RequestParam("password") String password,
                        HttpSession session) {
        Optional<User> user = userService.findOneUser(userId);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            session.setAttribute(LOGIN_USER, user.get());  // 세션에 로그인 사용자 저장, setAttribute 는 세션에 데이터 저장할 때 사용
            return "redirect:/";
        }
        return "user/login_failed";  // 로그인 실패 시 어떤 걸 리턴할 것인가?
    }

    @PostMapping("/user/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
