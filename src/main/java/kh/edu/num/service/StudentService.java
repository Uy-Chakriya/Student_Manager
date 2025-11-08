package kh.edu.num.service;

import kh.edu.num.model.Student;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    // Static counter for simple ID generation, now in the service layer
    private static Long idCounter = 0L;
    
    // In-memory ArrayList to store students
    private final List<Student> students = new ArrayList<>();

    // Initial dummy data
    public StudentService() {
        // Dummy students are created and assigned unique IDs automatically by the addStudent method
        // Note: passing null for ID since the service will assign the real ID
        addStudent(new Student(null, "Bopha K.", "bopha@example.com", "Computer Science"));
        addStudent(new Student(null, "Chann T.", "chann@example.com", "Physics"));
        addStudent(new Student(null, "Dara S.", "dara@example.com", "Mathematics"));
    }

    // Create/Add a new student (assign ID here)
    public void addStudent(Student student) {
        // Assign a new ID before adding to the list
        student.setId(++idCounter);
        students.add(student);
    }

    // Read/List all students
    public List<Student> listAllStudents() {
        return new ArrayList<>(students);
    }

    // Read student by ID
    public Optional<Student> findStudentById(Long id) {
        return students.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();
    }

    // Update student information
    public void updateStudent(Student updatedStudent) {
        findStudentById(updatedStudent.getId()).ifPresent(existingStudent -> {
            existingStudent.setName(updatedStudent.getName());
            existingStudent.setEmail(updatedStudent.getEmail());
            existingStudent.setMajor(updatedStudent.getMajor());
        });
    }

    // Delete student by ID
    public void deleteStudent(Long id) {
        students.removeIf(s -> s.getId().equals(id));
    }

    // Filtering/Searching by Major (Advanced Task / Step 7)
    public List<Student> filterStudentsByMajor(String major) {
        if (major == null || major.trim().isEmpty()) {
            return listAllStudents();
        }
        return students.stream()
                .filter(s -> s.getMajor().equalsIgnoreCase(major.trim()))
                .collect(Collectors.toList());
    }
}