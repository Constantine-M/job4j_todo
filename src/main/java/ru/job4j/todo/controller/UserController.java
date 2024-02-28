package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.user.UserService;
import ru.job4j.todo.util.TimeZoneUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.TimeZone;

/**
 * @author Constantine on 31.01.2024
 */
@RequestMapping("/users")
@AllArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String getLoginPage() {
        return "users/login";
    }

    /**
     * Данный метод обрабатывает запрос на
     * аутентификацию пользователя.
     *
     * Чтобы закрепить открытую сессию
     * за пользователем, воспользуемся
     * интерфейсом {@link HttpServletRequest}
     * и получим сессию.
     *
     * Метод {@link HttpServletRequest#getSession()}
     * вернет нам объект {@link HttpSession}.
     *
     * А затем добавим данные в сессию
     * с помощью метода {@link HttpSession#setAttribute}.
     * Например, добавим туда пользователя.
     * Т.о. мы закрепили пользователя за сессией.
     * Данные сессии привязываются к клиенту
     * и доступны во всем приложении.
     *
     * Обрати внимание на то, что внутри HttpSession
     * используется многопоточная коллекция
     * ConcurrentHashMap. Это связано с многопоточным
     * окружением. Увидеть это можно в реализации
     * модуля catalina -> session
     * {@link org.apache.catalina.session.StandardSession}.
     */
    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user,
                            Model model,
                            HttpSession session) {
        var userOptional = userService.findByLoginAndPassword(user.getLogin(), user.getPassword());
        if (userOptional.isEmpty()) {
            model.addAttribute("error", "The email or password is incorrect");
            return "users/login";
        }
        session.setAttribute("user", userOptional.get());
        return "redirect:/";
    }

    /**
     * Метод обрабатывает запрос на получение
     * страницы регистрации пользователя.
     *
     * Во время регистрации пользователь может
     * выбрать часовой пояс. Логика работы с часовыми
     * поясами вынесена в утилитный класс.
     * В контроллере это излишне.
     */
    @GetMapping("/register")
    public String getRegistrationPage(Model model) {
        model.addAttribute("timeZones", TimeZoneUtil.getAllAvailableTimeZones());
        return "users/register";
    }

    /**
     * Данный метод обрабытвает запрос
     * на регистрацию пользователя.
     *
     * Если регистрация прошла успешно,
     * то пользователя перебрасывает на
     * страницу входа.
     */
    @PostMapping("/register")
    public String register(@ModelAttribute User user,
                           Model model) {
        var userOptional = userService.save(user);
        if (userOptional.isEmpty()) {
            model.addAttribute("error", "User already exists with this login!");
            return "users/register";
        }
        return "users/login";
    }

    /**
     * Данный метод обрабатывает запрос
     * пользователя, который закрывает сессию
     * (разлогинивается).
     *
     * В данном случае, чтобы удалить
     * все данные, связанные с пользователем,
     * нужно использовать метод
     * {@link HttpSession#invalidate()}.
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:users/login";
    }
}
