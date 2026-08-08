package com.hostel;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


/*
 * ============================================================
 * HOSTEL BACKEND
 * ============================================================
 *
 * Single-file Spring Boot backend for:
 *
 * 1. Students
 * 2. Complaints
 * 3. Notices
 * 4. Mess Menu
 * 5. Feedback
 *
 * Server:
 * http://localhost:8080
 *
 * ============================================================
 */
@SpringBootApplication
public class HostelBackend {

    public static void main(String[] args) {

        SpringApplication.run(
                HostelBackend.class,
                args
        );

    }
}


/*
 * ============================================================
 * STUDENT ENTITY
 * ============================================================
 */
@Entity
@Table(name = "students")
class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String rollNumber;
    private String roomNumber;
    private String hostel;
    private String branch;
    private String email;
    private String phone;
    private String guardianName;
    private String guardianPhone;

    public Student() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getHostel() {
        return hostel;
    }

    public void setHostel(String hostel) {
        this.hostel = hostel;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getGuardianPhone() {
        return guardianPhone;
    }

    public void setGuardianPhone(String guardianPhone) {
        this.guardianPhone = guardianPhone;
    }
}


/*
 * ============================================================
 * COMPLAINT ENTITY
 * ============================================================
 */
@Entity
@Table(name = "complaints")
class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String complaintId;
    private String studentName;
    private String rollNumber;
    private String roomNumber;
    private String title;
    private String category;
    private String description;
    private String priority;
    private String status;
    private String imagePath;
    private LocalDate date;

    public Complaint() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}


/*
 * ============================================================
 * NOTICE ENTITY
 * ============================================================
 */
@Entity
@Table(name = "notices")
class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String priority;
    private String category;
    private String title;

    private String description;

    private LocalDate date;

    public Notice() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}


/*
 * ============================================================
 * MESS MENU ENTITY
 * ============================================================
 */
@Entity
@Table(name = "mess_menu")
class MessMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String day;
    private String breakfast;
    private String lunch;
    private String snacks;
    private String dinner;

    public MessMenu() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(String breakfast) {
        this.breakfast = breakfast;
    }

    public String getLunch() {
        return lunch;
    }

    public void setLunch(String lunch) {
        this.lunch = lunch;
    }

    public String getSnacks() {
        return snacks;
    }

    public void setSnacks(String snacks) {
        this.snacks = snacks;
    }

    public String getDinner() {
        return dinner;
    }

    public void setDinner(String dinner) {
        this.dinner = dinner;
    }
}


/*
 * ============================================================
 * FEEDBACK ENTITY
 * ============================================================
 */
@Entity
@Table(name = "feedback")
class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentName;
    private String category;

    private int rating;

    private String reaction;

    private String message;

    private boolean anonymous;

    private LocalDate date;

    public Feedback() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}


/*
 * ============================================================
 * REPOSITORIES
 * ============================================================
 */
interface StudentRepository
        extends JpaRepository<Student, Long> {
}

interface ComplaintRepository
        extends JpaRepository<Complaint, Long> {
}

interface NoticeRepository
        extends JpaRepository<Notice, Long> {
}

interface MessMenuRepository
        extends JpaRepository<MessMenu, Long> {
}

interface FeedbackRepository
        extends JpaRepository<Feedback, Long> {
}


/*
 * ============================================================
 * STUDENT CONTROLLER
 * ============================================================
 */
@RestController
@RequestMapping("/students")
@CrossOrigin
class StudentController {

    private final StudentRepository repository;

    StudentController(StudentRepository repository) {

        this.repository = repository;

    }

    @GetMapping
    public List<Student> getAllStudents() {

        return repository.findAll();

    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(
            @PathVariable Long id) {

        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @PostMapping
    public Student addStudent(
            @RequestBody Student student) {

        return repository.save(student);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(
            @PathVariable Long id,
            @RequestBody Student student) {

        return repository.findById(id)
                .map(existing -> {

                    existing.setName(student.getName());
                    existing.setRollNumber(student.getRollNumber());
                    existing.setRoomNumber(student.getRoomNumber());
                    existing.setHostel(student.getHostel());
                    existing.setBranch(student.getBranch());
                    existing.setEmail(student.getEmail());
                    existing.setPhone(student.getPhone());
                    existing.setGuardianName(student.getGuardianName());
                    existing.setGuardianPhone(student.getGuardianPhone());

                    return ResponseEntity.ok(
                            repository.save(existing)
                    );

                })
                .orElse(ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    public String deleteStudent(
            @PathVariable Long id) {

        repository.deleteById(id);

        return "Student deleted successfully";

    }
}


/*
 * ============================================================
 * COMPLAINT CONTROLLER
 * ============================================================
 */
@RestController
@RequestMapping("/complaints")
@CrossOrigin
class ComplaintController {

    private final ComplaintRepository repository;

    ComplaintController(ComplaintRepository repository) {

        this.repository = repository;

    }

    @GetMapping
    public List<Complaint> getComplaints() {

        return repository.findAll();

    }

    @GetMapping("/{id}")
    public ResponseEntity<Complaint> getComplaint(
            @PathVariable Long id) {

        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @PostMapping
    public Complaint submitComplaint(
            @RequestBody Complaint complaint) {

        complaint.setStatus("Pending");

        complaint.setDate(LocalDate.now());

        complaint.setComplaintId(
                "HP-" + System.currentTimeMillis()
        );

        return repository.save(complaint);

    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Complaint> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        return repository.findById(id)
                .map(complaint -> {

                    complaint.setStatus(status);

                    return ResponseEntity.ok(
                            repository.save(complaint)
                    );

                })
                .orElse(ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    public String deleteComplaint(
            @PathVariable Long id) {

        repository.deleteById(id);

        return "Complaint deleted successfully";

    }
}


/*
 * ============================================================
 * NOTICE CONTROLLER
 * ============================================================
 */
@RestController
@RequestMapping("/notices")
@CrossOrigin
class NoticeController {

    private final NoticeRepository repository;

    NoticeController(NoticeRepository repository) {

        this.repository = repository;

    }

    @GetMapping
    public List<Notice> getNotices() {

        return repository.findAll();

    }

    @GetMapping("/{id}")
    public ResponseEntity<Notice> getNotice(
            @PathVariable Long id) {

        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @PostMapping
    public Notice addNotice(
            @RequestBody Notice notice) {

        notice.setDate(LocalDate.now());

        return repository.save(notice);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Notice> updateNotice(
            @PathVariable Long id,
            @RequestBody Notice notice) {

        return repository.findById(id)
                .map(existing -> {

                    existing.setType(notice.getType());
                    existing.setPriority(notice.getPriority());
                    existing.setCategory(notice.getCategory());
                    existing.setTitle(notice.getTitle());
                    existing.setDescription(notice.getDescription());

                    return ResponseEntity.ok(
                            repository.save(existing)
                    );

                })
                .orElse(ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    public String deleteNotice(
            @PathVariable Long id) {

        repository.deleteById(id);

        return "Notice deleted successfully";

    }
}


/*
 * ============================================================
 * MESS MENU CONTROLLER
 * ============================================================
 */
@RestController
@RequestMapping("/mess")
@CrossOrigin
class MessMenuController {

    private final MessMenuRepository repository;

    MessMenuController(MessMenuRepository repository) {

        this.repository = repository;

    }

    @GetMapping
    public List<MessMenu> getMenu() {

        return repository.findAll();

    }

    @GetMapping("/{id}")
    public ResponseEntity<MessMenu> getMenuItem(
            @PathVariable Long id) {

        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @PostMapping
    public MessMenu addMenu(
            @RequestBody MessMenu menu) {

        return repository.save(menu);

    }

    @PutMapping("/{id}")
    public ResponseEntity<MessMenu> updateMenu(
            @PathVariable Long id,
            @RequestBody MessMenu menu) {

        return repository.findById(id)
                .map(existing -> {

                    existing.setDay(menu.getDay());
                    existing.setBreakfast(menu.getBreakfast());
                    existing.setLunch(menu.getLunch());
                    existing.setSnacks(menu.getSnacks());
                    existing.setDinner(menu.getDinner());

                    return ResponseEntity.ok(
                            repository.save(existing)
                    );

                })
                .orElse(ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    public String deleteMenu(
            @PathVariable Long id) {

        repository.deleteById(id);

        return "Mess menu deleted successfully";

    }
}


/*
 * ============================================================
 * FEEDBACK CONTROLLER
 * ============================================================
 */
@RestController
@RequestMapping("/feedback")
@CrossOrigin
class FeedbackController {

    private final FeedbackRepository repository;

    FeedbackController(FeedbackRepository repository) {

        this.repository = repository;

    }

    @GetMapping
    public List<Feedback> getFeedback() {

        return repository.findAll();

    }

    @PostMapping
    public Feedback submitFeedback(
            @RequestBody Feedback feedback) {

        feedback.setDate(LocalDate.now());

        return repository.save(feedback);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Feedback> getFeedbackById(
            @PathVariable Long id) {

        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    public String deleteFeedback(
            @PathVariable Long id) {

        repository.deleteById(id);

        return "Feedback deleted successfully";

    }
}
