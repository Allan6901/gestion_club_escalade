package myapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import myapp.model.Course;
import myapp.repo.CourseRepository;

@Service
public class CourseService {

	/*
	 * Injection de la DAO de manipulation des cours.
	 */
	@Autowired
	CourseRepository repo;

	@PostConstruct
	public void init() {
		repo.save(Course.builder().name("Architecture JEE").build());
		repo.save(Course.builder().name("Données post-relationnelles").build());
	}

	public void save(Course c) {
		repo.save(c);
	}

	public void newCourse() {
		final var name = String.format("UE %d", repo.count() + 1);
		save(Course.builder().name(name).build());
	}

	public List<Course> findCourses() {
		return findCourses("");
	}

	public List<Course> findCourses(String name) {
		return repo.findByNameLike("%" + name + "%");
	}

}
