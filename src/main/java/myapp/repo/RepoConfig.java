package myapp.repo;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import myapp.model.Course;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = CourseRepository.class)
@EntityScan(basePackageClasses = Course.class)
public class RepoConfig {

}
