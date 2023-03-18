package ru.derendyaev.SpringMVC.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/first")
public class FirstController {
    @GetMapping("/hello")
    public String helloPage(@RequestParam(value="name", required = false) String name,
                            @RequestParam(value="surname", required = false) String surname, Model model){

        //System.out.println("Name: " + name + ", surname: " + surname);
        String answer = "Name: " + name + ", surname: " + surname;
        model.addAttribute("message", answer);

        return "first/hello";
    }

    @GetMapping("/goodbye")
    public String gooByePage(){
        return "first/goodbye";

    }

    @GetMapping("/calculator")
    public String calculator(@RequestParam(value = "a", required = false) String a,
                             @RequestParam(value = "b", required = false) String b,
                             @RequestParam(value = "action", required = false) String action,
                             Model calcModel){
        switch (action){
            case "multiplication":
                calcModel.addAttribute("answer", "Answer of multiplication: " + (Integer.parseInt(a) * Integer.parseInt(b)));
                break;
            case "addition":
                calcModel.addAttribute("answer", "Answer of addition: " + (Integer.parseInt(a) + Integer.parseInt(b)));
                break;
            case "subtraction":
                calcModel.addAttribute("answer", "Answer of subtraction: " + (Integer.parseInt(a) - Integer.parseInt(b)));
                break;
            case "division":
                calcModel.addAttribute("answer", "Answer of division: " + (Integer.parseInt(a) / Double.parseDouble(b)));
                break;
            default:
                calcModel.addAttribute("answer", "Incorrect action");
        }

        return "calc/answer";
    }
}
