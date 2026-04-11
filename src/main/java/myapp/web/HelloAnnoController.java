package myapp.web;

import java.util.Date;

import jakarta.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller()
@RequestMapping("/tests")
public class HelloAnnoController {

    protected final Log logger = LogFactory.getLog(getClass());

    @GetMapping("/welcome")
    public ModelAndView sayHello() {
        String now = (new Date()).toString();
        logger.info("Running " + this);
        return new ModelAndView("hello", "now", now);
    }

    @GetMapping("/counter")
    public ModelAndView count(
            HttpSession session,
            @RequestParam(value = "inc", required = false, defaultValue = "1") int inc) {

        logger.info("Appel du compteur avec incrément : " + inc);

        Integer counter = (Integer) session.getAttribute("myCounter");
        if (counter == null) {
            counter = 0;
        }

        counter += inc;

        session.setAttribute("myCounter", counter);

        ModelAndView mav = new ModelAndView("counter");
        mav.addObject("currentCount", counter);
        mav.addObject("lastIncrement", inc);

        return mav;
    }

    @GetMapping("/plus10")
    public ModelAndView plus10(
            @RequestParam(value = "num", defaultValue = "100") Integer value) {
        logger.info("Running plus10 controler with param = " + value);
        return new ModelAndView("hello", "now", value + 10);
    }

    @GetMapping("/dateTest")
    public ModelAndView checkDate(
            @RequestParam(value = "day")
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date day) {

        logger.info("Date reçue : " + day);

        ModelAndView mav = new ModelAndView("hello");
        mav.addObject("now", "Date choisie : " + day.toString());
        return mav;
    }

    @GetMapping("/voir/{year}/{month}")
    public ModelAndView archive(
            @PathVariable("year") int year,
            @PathVariable("month") String month) {

        logger.info("Recherche archives : " + month + " " + year);
        String result =  month + " " + year;

        return new ModelAndView("hello", "now", result);
    }

    @GetMapping("/matrix/{param}")
    @ResponseBody
    public String testMatrix(//
                             @PathVariable("param") String param, //
                             @MatrixVariable(name = "a", defaultValue = "A") String a, //
                             @MatrixVariable(name = "b", defaultValue = "1") Integer b//
    ) {
        return String.format("param=%s, a=%s, b=%d", param, a, b);
    }

}