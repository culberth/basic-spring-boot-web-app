package com.example.controlsdemo;

import java.time.LocalDate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/demo")
public class DemoController {
    @PostMapping
    public Submission submit(@Valid @RequestBody Submission submission) {
        // Echo the validated form values; this demo does not persist any data.
        return submission;
    }

    public record Submission(
            @NotBlank @Size(max = 80) String name,
            @NotNull @Pattern(regexp = "Java|Kotlin|Groovy") String language,
            @NotNull @Min(0) @Max(100) Integer enthusiasm,
            @NotNull LocalDate date,
            @NotNull @Pattern(regexp = "Light|Dark|System") String theme,
            boolean notifications,
            @NotNull @Size(max = 500) String notes) {
    }
}
