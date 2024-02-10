package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

/**
 * Данный класс находится в слое
 * контроллеров и обрабатывает запросы
 * пользователя.
 *
 * Здесь будут сгруппированы запросы,
 * касающиеся работ с задачами.
 *
 * @author Constantine on 22.01.2024
 */
@AllArgsConstructor
@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    /**
     * Данный метод обрабатывает запрос
     * на отображение всех задач - выполненные,
     * невыполненные, новые.
     *
     * Метод принимает объект Model.
     * Он используется Thymeleaf для поиска
     * объектов, которые нужны отобразить на виде.
     *
     * В Model мы добавляем объект tasks.
     */
    @GetMapping
    public String getAllTasks(Model model) {
        model.addAttribute("tasks", taskService.findAllOrderByDateTime());
        return "tasks/all";
    }

    /**
     * Данный метод обрабатывает запрос на
     * выбор конкретной задачи и переводит
     * пользователя на страницу с подробным
     * описанием задачи.
     *
     * Здесь используется аннотация
     * {@link PathVariable}, которая указывает
     * на то, что параметр id получается из
     * адресной строки.
     */
    @GetMapping("/{id}")
    public String getDetailInfoById(@PathVariable int id,
                          Model model) {
        var taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("error", String.format("Task with ID %s not found!", id));
            return "errors/404";
        }
        model.addAttribute("task", taskOptional.get());
        return "tasks/detail";
    }

    /**
     * Данный метод обрабатывает запрос
     * на получение страницы редактирования
     * задачи.
     *
     * Фактически, это то же самое, что и
     * {@link TaskController#getDetailInfoById},
     * только ведет на другую страницу, с
     * немного отличающейся вертской.
     */
    @GetMapping("/edit/{id}")
    public String getById(@PathVariable int id,
                          Model model) {
        var taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("error", String.format("Task with ID %s not found!", id));
            return "errors/404";
        }
        model.addAttribute("task", taskOptional.get());
        return "tasks/edit";
    }

    /**
     * Данный метод обрабатывает запрос на
     * вывод всех выполненных задач.
     */
    @GetMapping("/completed")
    public String getCompletedTasks(Model model) {
        model.addAttribute("completed", taskService.findCompletedTasks());
        return "tasks/completed";
    }

    /**
     * Метод обрабатывает запрос на
     * вывод всех задач, которые считаются
     * не выполненными и уже не новыми при этом.
     */
    @GetMapping("/expired")
    public String getExpiredUncompletedTasks(Model model) {
        model.addAttribute("expired", taskService.findExpiredUncompletedTasks());
        return "tasks/expired";
    }

    /**
     * Данный метод обрабатывает запрос на
     * вывод всех новых задач.
     *
     * Напомню, что под "новыми" задачами
     * имеются ввиду те, которым меньше 2 часов.
     */
    @GetMapping("/new")
    public String getNewTasks(Model model) {
        model.addAttribute("newTasks", taskService.findNewTasks());
        return "tasks/new";
    }

    /**
     * Данный метод обрабытвает запрос на
     * обновление задачи.
     *
     * Сперва производится поиск задачи.
     * Если обновляемая задача не будет найдена,
     * то пользовател будет перенаправлен
     * на страницу с текстом ошибки.
     */
    @PostMapping("/update")
    public String update(@ModelAttribute Task task) {
        taskService.update(task);
        return "redirect:/tasks";
    }

    /**
     * Данный метод обрабатывает запрос на
     * создание задачи.
     */
    @PostMapping("/create")
    public String create(@ModelAttribute Task task, Model model) {
        taskService.create(task);
        return "redirect:/tasks";
    }

    /**
     * Данный метод обрабатывает запрос на
     * удаление задачи из списка задач.
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        taskService.delete(id);
        return "redirect:/tasks";
    }

    /**
     * Данный метод обрабатывает запрос
     * на изменение состояния выполнения задачи,
     * т.е. выполнено/в процессе выполнения.
     *
     * Изначально предполагалось, что это будет
     * аналогично методу {@link TaskController#update},
     * т.е. считал, что будет POST-запрос.
     *
     * В ходе экспериментов нашел такой способ.
     * Т.е. точно так же как и удаление,
     * только в нашем случае уже внутри метода
     * присходит обновление поля isDone у объекта
     * {@link Task}. Данный вариант отлично
     * выполняет свою задачу.
     */
    @GetMapping("/complete/{id}")
    public String completeTask(@PathVariable int id) {
        taskService.complete(id);
        return "redirect:/tasks";
    }
}
