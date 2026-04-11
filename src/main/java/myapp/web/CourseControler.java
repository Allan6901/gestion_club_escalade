package myapp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import myapp.service.CourseService;

@Controller
@RequestMapping("/course")
public class CourseControler {

	/*
	 * Injection de la couche de service pour manipulation des cours.
	 */
	@Autowired
	CourseService service;

	@RequestMapping("")
	public ModelAndView listCourses() {
		return new ModelAndView("course", "courses", service.findCourses());
	}

	@RequestMapping("/new")
	public String newCourse() {
		service.newCourse();
		return "redirect:/course";
	}

	@RequestMapping("/find")
	public ModelAndView findCourses(String name) {
		final var result = service.findCourses(name);
		return new ModelAndView("course", "courses", result);
	}

}
