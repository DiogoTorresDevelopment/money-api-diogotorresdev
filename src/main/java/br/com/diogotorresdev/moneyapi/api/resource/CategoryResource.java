package br.com.diogotorresdev.moneyapi.api.resource;

import java.net.URI;
import java.util.List;

import br.com.diogotorresdev.moneyapi.api.event.RecursoCriadoEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import br.com.diogotorresdev.moneyapi.api.model.Category;
import br.com.diogotorresdev.moneyapi.api.repository.CategoryRepository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/category")
public class CategoryResource {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_VIEW_CATEGORY') and #oauth2.hasScope('read')")
    public List<Category> list() {
        return categoryRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_REGISTER_CATEGORY') and #oauth2.hasScope('write')")
    public ResponseEntity<Category> save(@Valid @RequestBody Category category, HttpServletResponse response) {
        Category newCategory = categoryRepository.save(category);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(newCategory.getId()).toUri();
        response.setHeader("Location", uri.toASCIIString());

        publisher.publishEvent(new RecursoCriadoEvent(this,response, newCategory.getId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_VIEW_CATEGORY') and #oauth2.hasScope('read')")
    public ResponseEntity<Category> getById(@PathVariable Long id) {
        Category category = categoryRepository.findOne(id);
        return category != null? ResponseEntity.ok(category) : ResponseEntity.notFound().build();
    }

}
