package delixus.com.springboot.rest.student;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import io.swagger.annotations.ApiOperation;

@RestController
public class StudentResource {

	@Autowired
	private StudentRepository studentRepository;

	@GetMapping("/students")
	public List<Student> retrieveAllStudents() {
		return studentRepository.findAll();
	}

	@GetMapping("/students/{id}")
	@ApiOperation(value = "Find student by id",
    notes = "Also returns a link to retrieve all students with rel - all-students")
	public EntityModel<Student> retrieveStudent(@PathVariable long id) {
		Optional<Student> student = studentRepository.findById(id);

		if (!student.isPresent())
			throw new StudentNotFoundException("id-" + id);

		EntityModel<Student> resource = EntityModel.of(student.get());

		WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllStudents());

		resource.add(linkTo.withRel("all-students"));

		return resource;
	}

	@DeleteMapping("/students/{id}")
	public void deleteStudent(@PathVariable long id) {
		studentRepository.deleteById(id);
	}

	@PostMapping(value="/createStudent")
	public EntityModel<Student> createStudent(@RequestBody Student student1) {
		System.out.println(" The number of counts of records " + studentRepository.count());
		studentRepository.saveAndFlush(student1);
		Student student = studentRepository.getOne(student1.getId());
		System.out.println(" The value of student " + student.getId());
		System.out.println(" The number of counts of records " + studentRepository.count());
		EntityModel<Student> resource = EntityModel.of(student1);
		WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllStudents());
		resource.add(linkTo.withRel("all-students"));
		return resource;
	}
	
	@PutMapping(value="/override")
	public EntityModel<Student> updateStudent(@RequestBody Student student1) {
		System.out.println(" The number of counts of records " + studentRepository.count());
		studentRepository.saveAndFlush(student1);
		Student student = studentRepository.getOne(student1.getId());
		System.out.println(" The value of student " + student.getId());
		System.out.println(" The number of counts of records " + studentRepository.count());
		EntityModel<Student> resource = EntityModel.of(student);
		WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllStudents());
		resource.add(linkTo.withRel("all-students"));
		return resource;
	}
}
