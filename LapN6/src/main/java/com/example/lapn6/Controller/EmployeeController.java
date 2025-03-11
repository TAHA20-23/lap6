package com.example.lapn6.Controller;

import com.example.lapn6.ApiResponse.ApiResponse;
import com.example.lapn6.Model.EmployeeModel;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/employees")
public class EmployeeController {

    private List<EmployeeModel> employees = new ArrayList<>();

    @GetMapping
    public List<EmployeeModel> getEmployees() {
        return employees;
    }

    @PostMapping("/add")
    public ResponseEntity addEmployee(@Valid @RequestBody EmployeeModel employee, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(message));
        }
        employees.add(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Employee added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateEmployee(@PathVariable String id, @Valid @RequestBody EmployeeModel updateEmployee, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(message));
        }
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId().equals(id)) {
                employees.set(i, updateEmployee);
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Employee updated successfully"));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Employee not found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable String id) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId().equals(id)) {
                employees.remove(i);
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Employee deleted successfully"));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Employee not found"));
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<?> searchEmployee(@PathVariable String id) {
        for (EmployeeModel employee : employees) {
            if (employee.getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.OK).body(employee);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Employee not found"));
    }

    @GetMapping("/searchByPosition/{position}")
    public ResponseEntity<?> searchEmployeesByPosition(@PathVariable String position) {
        if (!position.equals("supervisor") && !position.equals("coordinator")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Invalid position"));
        }
        List<EmployeeModel> result = employees.stream()
                .filter(emp -> emp.getPosition().equals(position))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/searchByAgeRange/{minAge}/{maxAge}")
    public ResponseEntity<?> getEmployeesByAgeRange(@PathVariable int minAge, @PathVariable int maxAge) {
        List<EmployeeModel> result = employees.stream()
                .filter(emp -> emp.getAge() >= minAge && emp.getAge() <= maxAge)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping("/applyLeave/{id}")
    public ResponseEntity applyLeave(@PathVariable String id) {
        for (EmployeeModel emp : employees) {
            if (emp.getId().equals(id)) {
                if (emp.isOnLave()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Employee is already on leave"));
                }
                if (emp.getAnnualLave() <= 0) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("No annual leave remaining"));
                }
                emp.setOnLave(true);
                emp.setAnnualLave(emp.getAnnualLave() - 1);
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Leave applied successfully"));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Employee not found"));
    }

    @GetMapping("/noAnnualLeave")
    public ResponseEntity getEmployeesWithNoAnnualLeave() {
        List<EmployeeModel> result = employees.stream()
                .filter(emp -> emp.getAnnualLave() == 0)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping("/promote/{id}")
    public ResponseEntity<?> promoteEmployee(@PathVariable String id, @RequestParam String requesterRole) {
        if (!requesterRole.equals("supervisor")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Only supervisors can promote employees"));
        }
        for (EmployeeModel emp : employees) {
            if (emp.getId().equals(id)) {
                if (emp.getAge() < 30) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Employee must be at least 30 years old"));
                }
                if (emp.isOnLave()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Employee is currently on leave"));
                }
                emp.setPosition("supervisor");
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Employee promoted to supervisor"));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Employee not found"));
    }
}
