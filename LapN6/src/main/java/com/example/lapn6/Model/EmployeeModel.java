package com.example.lapn6.Model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class EmployeeModel {

    @NotNull(message = "Can not be null")
    @Size(min = 3, message = "ID must be mor than 2 characters")
    private String id;
//      @Min(value = 1, message = "ID must be a positive integer")
//        private int id;

    @NotNull(message = "Can not be null")
    @Size(min = 5, message = "ID must be mor than 4 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "ID must contain only letters and cannot contain numbers.")
    private String name;

    @NotNull(message = "Can not be null")
    @Email(message = "Must be a valid email format.")
    private String email;

    @NotNull(message = "Can not be null")
    @Pattern(regexp = "^05\\d{8}$", message = "Phone number must start with '05' and consist of exactly 10 digits.")
    private String phoneNumber;

    @NotNull(message = "Can not be null")
    @Min(value = 25, message = "age must be mor thane 25")
    @Digits(integer = 3, fraction = 0, message = "Age must be a valid number.")
    private int age;

    @NotNull(message = "Can not be null")
    @Pattern(regexp = "^(supervisor|cordinator)$",message = "Position must be either 'supervisor' or 'coordinator' only.")
    private String position;

    @AssertFalse(message = "onLeave must be initially set to false.")
    private boolean onLave;

    @NotNull (message = "AnnualLeave cannot be null.")
    @Positive(message = "AnnualLeave must be a positive number.")
    private int annualLave;


    @NotNull(message = "Can not be null")
    @PastOrPresent(message = "hireDate should be a date in the present or the past.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime hireDate;



}
