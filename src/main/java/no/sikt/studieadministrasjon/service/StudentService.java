package no.sikt.studieadministrasjon.service;

import no.sikt.studieadministrasjon.entity.Student;
import no.sikt.studieadministrasjon.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Random;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final Random random = new Random();

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional
    public Student registerStudent(String firstName, String lastName, String email, LocalDate dateOfBirth) {
        // Validate input
        validateStudentData(firstName, lastName, email, dateOfBirth);

        // Check if email already exists
        if (studentRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }

        // Generate unique student number
        String studentNumber = generateUniqueStudentNumber();

        // Create and save student
        Student student = new Student(studentNumber, firstName, lastName, email, dateOfBirth);
        return studentRepository.save(student);
    }

    private void validateStudentData(String firstName, String lastName, String email, LocalDate dateOfBirth) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Valid email is required");
        }
        if (dateOfBirth == null || dateOfBirth.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Valid date of birth is required");
        }
    }

    private String generateUniqueStudentNumber() {
        String studentNumber;
        do {
            studentNumber = "S" + String.format("%09d", random.nextInt(1000000000));
        } while (studentRepository.existsByStudentNumber(studentNumber));
        return studentNumber;
    }
}
