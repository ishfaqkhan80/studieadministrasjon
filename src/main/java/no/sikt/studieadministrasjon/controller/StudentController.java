package no.sikt.studieadministrasjon.controller;

import no.sikt.studieadministrasjon.entity.Student;
import no.sikt.studieadministrasjon.service.StudentService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;

@Controller
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @MutationMapping
    public Student registerStudent(
            @Argument String firstName,
            @Argument String lastName,
            @Argument String email,
            @Argument String dateOfBirth) {
        LocalDate dob = LocalDate.parse(dateOfBirth);
        return studentService.registerStudent(firstName, lastName, email, dob);
    }
}
