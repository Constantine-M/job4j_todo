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
     * Данный метод обрабатывает запрос на
     * вывод всех новых задач.
     *
     * Напомню, что под "новыми" задачами
     * имеются ввиду те, которым меньше 24 часов.
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
    public String update(@ModelAttribute Task task, Model model) {
        try {
            var isUpdated = taskService.update(task);
            if (!isUpdated) {
                model.addAttribute("error", String.format("Task with ID = %s not found!", task.getId()));
                return "errors/404";
            }
            return "redirect:/tasks";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "errors/404";
        }
    }

    /**
     * Данный метод обрабатывает запрос на
     * создание задачи.
     */
    @PostMapping("/create")
    public String create(@ModelAttribute Task task, Model model) {
        try {
            taskService.create(task);
            return "redirect:/tasks";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "errors/404";
        }
    }

    /**
     * Данный метод обрабатывает запрос на
     * удаление задачи из списка задач.
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, Model model) {
        var isDeleted = taskService.delete(id);
        if (!isDeleted) {
            model.addAttribute("error", String.format("Task with ID = %s not found!", id));
            return "errors/404";
        }
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
    public String completeTask(@PathVariable int id, Model model) {
        try {
            var isUpdated = taskService.complete(id);
            if (!isUpdated) {
                model.addAttribute("error", String.format("Task with ID = %s not found!", id));
                return "errors/404";
            }
            return "redirect:/tasks";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "errors/404";
        }
    }
}
