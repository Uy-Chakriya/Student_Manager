package kh.edu.num.controller;

import kh.edu.num.model.Student;
import kh.edu.num.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // 1. LIST (Read: View list of all students & Filtering/Searching - Step 7)
    @GetMapping
    public String listStudents(@RequestParam(value = "major", required = false) String major, Model model) {
        
        List<Student> students = studentService.filterStudentsByMajor(major);

        model.addAttribute("students", students);
        // Add all distinct majors for the filter dropdown
        model.addAttribute("majors", studentService.listAllStudents().stream()
                .map(Student::getMajor).distinct().collect(Collectors.toList()));
        model.addAttribute("currentMajor", major);

        return "list-students"; // Maps to src/main/resources/templates/list-students.html
    }

    // 2. CREATE - Show form
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("student", new Student());
        return "create-student"; // Maps to src/main/resources/templates/create-student.html
    }

    // 2. CREATE - Submit form (using validation - Step 7)
    @PostMapping
    public String createStudent(@Valid @ModelAttribute("student") Student student, BindingResult result) {
        if (result.hasErrors()) {
            return "create-student";
        }
        // Pass the form-bound student object directly. The Service layer now assigns the ID.
        studentService.addStudent(student);
        return "redirect:/students"; // Redirect to the list view (Step 6)
    }

    // 3. EDIT - Show form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Student student = studentService.findStudentById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + id));
        model.addAttribute("student", student);
        return "edit-student"; // Maps to src/main/resources/templates/edit-student.html
    }

    // 3. EDIT - Submit form (Update)
    @PostMapping("/edit/{id}")
    public String updateStudent(@PathVariable("id") Long id, @Valid @ModelAttribute("student") Student student, BindingResult result) {
        if (result.hasErrors()) {
            // Keep the ID for re-displaying the form, though the hidden field handles this too.
            student.setId(id); 
            return "edit-student";
        }
        
        // The ID is correctly bound to the 'student' object via the hidden form field in edit-student.html
        studentService.updateStudent(student);
        return "redirect:/students";
    }

    // 4. DELETE
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable("id") Long id) {
        studentService.deleteStudent(id); 
        return "redirect:/students";
    }
}