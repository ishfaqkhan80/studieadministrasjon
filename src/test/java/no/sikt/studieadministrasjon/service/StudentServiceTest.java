package no.sikt.studieadministrasjon.service;

import no.sikt.studieadministrasjon.entity.Student;
import no.sikt.studieadministrasjon.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private String validFirstName;
    private String validLastName;
    private String validEmail;
    private LocalDate validDateOfBirth;

    @BeforeEach
    void setUp() {
        validFirstName = "Ola";
        validLastName = "Nordmann";
        validEmail = "ola.nordmann@student.no";
        validDateOfBirth = LocalDate.of(2000, 1, 1);
    }

    @Test
    void registerStudent_WithValidData_ShouldSucceed() {
        // Arrange
        when(studentRepository.existsByEmail(validEmail)).thenReturn(false);
        when(studentRepository.existsByStudentNumber(anyString())).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Student result = studentService.registerStudent(validFirstName, validLastName, validEmail, validDateOfBirth);

        // Assert
        assertNotNull(result);
        assertEquals(validFirstName, result.getFirstName());
        assertEquals(validLastName, result.getLastName());
        assertEquals(validEmail, result.getEmail());
        assertEquals(validDateOfBirth, result.getDateOfBirth());
        assertNotNull(result.getStudentNumber());
        assertTrue(result.getStudentNumber().startsWith("S"));
        assertEquals(10, result.getStudentNumber().length());

        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void registerStudent_WithDuplicateEmail_ShouldThrowException() {
        // Arrange
        when(studentRepository.existsByEmail(validEmail)).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> studentService.registerStudent(validFirstName, validLastName, validEmail, validDateOfBirth)
        );
        assertEquals("Email already registered", exception.getMessage());
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void registerStudent_WithEmptyFirstName_ShouldThrowException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> studentService.registerStudent("", validLastName, validEmail, validDateOfBirth)
        );
        assertEquals("First name is required", exception.getMessage());
    }

    @Test
    void registerStudent_WithEmptyLastName_ShouldThrowException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> studentService.registerStudent(validFirstName, "", validEmail, validDateOfBirth)
        );
        assertEquals("Last name is required", exception.getMessage());
    }

    @Test
    void registerStudent_WithInvalidEmail_ShouldThrowException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> studentService.registerStudent(validFirstName, validLastName, "invalid-email", validDateOfBirth)
        );
        assertEquals("Valid email is required", exception.getMessage());
    }

    @Test
    void registerStudent_WithFutureDateOfBirth_ShouldThrowException() {
        // Arrange
        LocalDate futureDate = LocalDate.now().plusDays(1);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> studentService.registerStudent(validFirstName, validLastName, validEmail, futureDate)
        );
        assertEquals("Valid date of birth is required", exception.getMessage());
    }

    @Test
    void registerStudent_ShouldGenerateUniqueStudentNumber() {
        // Arrange
        when(studentRepository.existsByEmail(anyString())).thenReturn(false);
        when(studentRepository.existsByStudentNumber(anyString())).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Student student1 = studentService.registerStudent("Ola", "Nordmann", "ola@test.no", validDateOfBirth);
        Student student2 = studentService.registerStudent("Kari", "Nordmann", "kari@test.no", validDateOfBirth);

        // Assert
        assertNotEquals(student1.getStudentNumber(), student2.getStudentNumber());
    }
}
