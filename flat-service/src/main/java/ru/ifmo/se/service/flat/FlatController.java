package ru.ifmo.se.service.flat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.ifmo.se.common.FlatRepository;

@Controller
@RequestMapping("/flat")
public class FlatController {
    private final FlatRepository flatRepository;

    public FlatController(FlatRepository flatRepository) {
        this.flatRepository = flatRepository;
    }
}
