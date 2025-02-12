package com.project.cheerha.domain.jobOpening.controller;


import com.project.cheerha.domain.jobOpening.service.JobOpeningService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RequestMapping("/data")
@RestController
@RequiredArgsConstructor
public class JobOpeningController {

    private final JobOpeningService jobOpeningService;

    @GetMapping("/{id}")
    public RedirectView getRedirectedView (@PathVariable Long id) {
        String url = jobOpeningService.getJobOpeningUrlAndIncreaseViewCount(id);
        return new RedirectView(url);
    }
}
