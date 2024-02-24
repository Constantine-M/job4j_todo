package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.category.CategoryService;
import ru.job4j.todo.service.priority.PriorityService;
import ru.job4j.todo.service.task.TaskService;

import java.util.List;

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

    private final PriorityService priorityService;

    private final CategoryService categoryService;

    /**
     * Данный метод обрабатывает запрос
     * на отображение всех задач - выполненные,
     * невыполненные, новые.
     *
     * Метод принимает объект Model.
     * Он используется Thymeleaf для поиска
     * объектов, которые нужны отобразить на виде.
     *
     * Чтобы вывести задачи только залогиненого
     * пользователя, в метод передадим
     * пользователя из сессии. Для этого используем
     * аннотацию {@link SessionAttribute}.
     *
     * В Model мы добавляем объект tasks.
     */
    @GetMapping
    public String getAllTasks(Model model,
                              @SessionAttribute User user) {
        var tasks = taskService.findAllOrderByDateTime(user);
        model.addAttribute("tasks", tasks);
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "tasks/all";
    }

    /**
     * Данный метод обрабатывает запрос на
     * создание задачи.
     *
     * Прежде, чем отправить POST запрос
     * на создание задачи, добавим в задачу
     * информацию о том, к какому пользователю
     * данная задача будет прикреплена.
     *
     * Для этого мы воспользовались аннотацией
     * {@link SessionAttribute}, которая
     * позволяет хранить в сессии объекты.
     * В нашем случае это пользователь.
     * Аннотация прописывается в параметрах
     * метода.
     *
     * Чтобы извлечь {@link Priority}
     * из запроса, который прилетает с фронта,
     * используем аннотацию {@link RequestParam}.
     * Поиск параметра производится по
     * ключевому слову "priorityId", который
     * определен в представлении all.html:
     * class="form-control" id="priority" name="priorityId".
     * Находим приоритет по ID и присваиваем
     * задаче.
     */
    @PostMapping("/create")
    public String create(@ModelAttribute Task task,
                         @SessionAttribute User user) {
        task.setUser(user);
        taskService.create(task);
        return "redirect:/tasks";
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
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("task", taskOptional.get());
        return "tasks/edit";
    }

    /**
     * Данный метод обрабатывает запрос на
     * вывод всех выполненных задач.
     */
    @GetMapping("/completed")
    public String getCompletedTasks(Model model,
                                    @SessionAttribute User user) {
        model.addAttribute("completed", taskService.findCompletedTasks(user));
        model.addAttribute("priorities", priorityService.findAll());
        return "tasks/completed";
    }

    /**
     * Метод обрабатывает запрос на
     * вывод всех задач, которые считаются
     * не выполненными и уже не новыми при этом.
     */
    @GetMapping("/expired")
    public String getExpiredUncompletedTasks(Model model,
                                             @SessionAttribute User user) {
        model.addAttribute("expired", taskService.findExpiredUncompletedTasks(user));
        model.addAttribute("priorities", priorityService.findAll());
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
    public String getNewTasks(Model model,
                              @SessionAttribute User user) {
        model.addAttribute("newTasks", taskService.findNewTasks(user));
        model.addAttribute("priorities", priorityService.findAll());
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
     *
     * Для обновления задачи требуется
     * задать приоритет, который получили
     * в запросе от пользователя. Если
     * приоритет не задать - получим NPE.
     */
    @PostMapping("/update")
    public String update(@ModelAttribute Task task) {
        taskService.update(task);
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
