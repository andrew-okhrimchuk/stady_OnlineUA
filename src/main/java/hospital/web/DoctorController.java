package hospital.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class DoctorController {

    @RequestMapping("/doctor")
    public String getDoctors() {
        log.debug("Start getDoctors");
        return "doctor";
    }
}
