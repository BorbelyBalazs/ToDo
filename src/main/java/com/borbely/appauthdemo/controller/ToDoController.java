package com.borbely.appauthdemo.controller;

import com.borbely.appauthdemo.auth.AppUserService;
import com.borbely.appauthdemo.form.ToDoForm;
import com.borbely.appauthdemo.model.AppUser;
import com.borbely.appauthdemo.model.ToDo;
import com.borbely.appauthdemo.repository.ToDoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class ToDoController {

    private ToDoRepository toDoRepository;

    private AppUserService userDetailsService;

    public ToDoController(ToDoRepository repo, AppUserService uds) {
        this.toDoRepository = repo;
        this.userDetailsService = uds;
    }

    @GetMapping(path = {"/todos"})
    public String lisToDos(Model model, Principal principal) {

        AppUser owner = (AppUser) userDetailsService.loadUserByUsername(principal.getName());
        List<ToDo> todos = toDoRepository.findByOwner(owner);
        model.addAttribute("todos", todos);
        return "todos";
    }


    @GetMapping(path = {"/home", "/", ""})
    public String homePage() {
        return "home";
    }


    @GetMapping("/todo")
    public String addTodo(Model model) {
        model.addAttribute("newtodo", new ToDoForm());
        return "todo-form";
    }

    @PostMapping("/todo")
    public String saveTodo(@ModelAttribute("newtodo")
                           @Validated
                           ToDoForm toDoform,
                           BindingResult bind,
                           Principal principal) {

        if (bind.hasErrors()) {
            return "todo-form";
        }

        AppUser owner = (AppUser) userDetailsService.loadUserByUsername(principal.getName());
        ToDo entity = new ToDo(toDoform.getDescription());
        entity.setDeadLine(toDoform.getDeadLine());
        entity.setDone(false);
        entity.setOwner(owner);
        toDoRepository.save(entity);

        return "redirect:/todos";
    }

    @PostMapping("todo-delete")
    public String todoDelete(@RequestParam("todo_id") Long id, Principal principal) {

        AppUser owner = (AppUser) userDetailsService.loadUserByUsername(principal.getName());

        toDoRepository.deleteByIdAndOwner(id, owner);
        return "redirect:/home";
    }

//    @PostMapping("todo-done")
//    public String todoDone(@RequestParam("todo_id") Long id,
//                           @RequestParam("done") boolean b) {
//        toDoRepository.updateDoneById(!b, id);
//        return "redirect:/home";
//    }

    @PostMapping("todo-done")
    public String todoDone(@RequestParam("todo_id") Long id,
                           @RequestParam(value = "done", required = false) Boolean done,
                           Principal principal) {
        done = done != null;
        AppUser owner = (AppUser) userDetailsService.loadUserByUsername(principal.getName());
        toDoRepository.updateDoneByIdAndOwner(done, id, owner);
        return "redirect:/home";
    }

}
