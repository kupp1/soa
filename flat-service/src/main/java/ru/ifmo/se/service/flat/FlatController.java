package ru.ifmo.se.service.flat;

import org.apache.commons.collections4.IteratorUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import ru.ifmo.se.common.repository.HouseRepository;

@Controller
@RequestMapping("/flat")
public class FlatController {
    private final HouseRepository houseRepository;

    public FlatController(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    @GetMapping
    @ResponseBody
    public String test() {
        return String.valueOf(IteratorUtils.size(houseRepository.findAll().iterator()));
    }
}
