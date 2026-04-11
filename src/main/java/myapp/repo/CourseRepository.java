package myapp.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import myapp.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

	List<Course> findByNameLike(String name);

}
