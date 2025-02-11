package com.project.cheerha.domain.data.controller;

import com.project.cheerha.domain.data.entity.Data;
import com.project.cheerha.domain.data.repository.DataRepository;
import com.project.cheerha.domain.data.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RequestMapping("/data")
@RestController
@RequiredArgsConstructor
public class DataController {

    private final DataService dataService;

    @GetMapping("/{id}")
    public RedirectView redirectView (@PathVariable Long id) {
        String url = dataService.urlFindById(id);
        return new RedirectView(url);
    }
}
