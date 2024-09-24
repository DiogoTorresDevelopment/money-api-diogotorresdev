package br.com.diogotorresdev.moneyapi.api.resource;

import br.com.diogotorresdev.moneyapi.api.dto.FileAttachment;
import br.com.diogotorresdev.moneyapi.api.dto.PostingStatisticCategory;
import br.com.diogotorresdev.moneyapi.api.dto.PostingStatisticDay;
import br.com.diogotorresdev.moneyapi.api.event.ResourceCreatedEvent;
import br.com.diogotorresdev.moneyapi.api.model.Posting;
import br.com.diogotorresdev.moneyapi.api.repository.PostingRepository;
import br.com.diogotorresdev.moneyapi.api.repository.filter.PostingFilter;
import br.com.diogotorresdev.moneyapi.api.repository.projection.PostingProjection;
import br.com.diogotorresdev.moneyapi.api.service.PostingService;
import br.com.diogotorresdev.moneyapi.api.service.exception.PersonNonexistentOrInactiveException;
import br.com.diogotorresdev.moneyapi.api.storage.S3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import br.com.diogotorresdev.moneyapi.api.exceptionhandler.MoneyApiExceptionHandler.Error;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/posting")
public class PostingResource {

    @Autowired
    private PostingRepository postingRepository;

    @Autowired
    private PostingService postingService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private S3 s3;

    @PreAuthorize("hasAuthority('ROLE_REGISTER_POSTING') and #oauth2.hasScope('write')")
    @PostMapping("/upload_file")
    public FileAttachment uploadFile(@RequestParam MultipartFile fileAttachment) throws IOException {
//        OutputStream out = new FileOutputStream(
//                "/Users/emid.dev.macbook.pro/Desktop/code-centauri/cursos/springBootFullStackCourseAlgaWorks/my-projects/files/postings/"
//                        + file.getOriginalFilename());
//
//        out.write(file.getBytes());
//        out.close();

        String name = s3.saveTemporary(fileAttachment);

        return new FileAttachment(name, s3.configurationURL(name));
    }

    @GetMapping("/reports/by-person")
    @PreAuthorize("hasAuthority('ROLE_VIEW_POSTING') and #oauth2.hasScope('read')")
    public ResponseEntity<byte[]> reportByPerson(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate init,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) throws Exception {
        byte[] report = postingService.reportByPerson(init,end);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE).body(report);
    }

    @GetMapping("/statistic/by-category")
    @PreAuthorize("hasAuthority('ROLE_VIEW_POSTING') and #oauth2.hasScope('read')")
    public List<PostingStatisticCategory> byCategory(){
        return this.postingRepository.byCategory(LocalDate.now());
    }

    @GetMapping("/statistic/by-day")
    @PreAuthorize("hasAuthority('ROLE_VIEW_POSTING') and #oauth2.hasScope('read')")
    public List<PostingStatisticDay> byDay(){
        return this.postingRepository.byDay(LocalDate.now());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_VIEW_POSTING') and #oauth2.hasScope('read')")
    public Page<Posting> find(PostingFilter postingFilter, Pageable pageable) {
        Page<Posting> postings = postingService.find(postingFilter, pageable);

        return postings;
    }

    @GetMapping(params = "projection")
    @PreAuthorize("hasAuthority('ROLE_VIEW_POSTING') and #oauth2.hasScope('read')")
    public Page<PostingProjection> projection(PostingFilter postingFilter, Pageable pageable) {
        Page<PostingProjection> postings = postingService.projection(postingFilter, pageable);

        return postings;
    }

    @PreAuthorize("hasAuthority('ROLE_VIEW_POSTING') and #oauth2.hasScope('read')")
    @GetMapping("/{id}")
    public ResponseEntity<Posting> getById(@PathVariable Long id) {
        Posting existingPosting = postingRepository.findOne(id);

        return existingPosting!= null? ResponseEntity.ok(existingPosting) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasAuthority('ROLE_REGISTER_POSTING') and #oauth2.hasScope('write')")
    @PostMapping
    public ResponseEntity<Posting> save(@Valid @RequestBody Posting posting, HttpServletResponse response) {
        Posting newPosting = postingService.save(posting);
        publisher.publishEvent(new ResourceCreatedEvent(this,response, newPosting.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(newPosting);
    }

    @ExceptionHandler({ PersonNonexistentOrInactiveException.class })
    public ResponseEntity<Object> handlePersonNonexistentOrInactive(PersonNonexistentOrInactiveException ex) {
        String userMessage = messageSource.getMessage("person.inactive-or-non-existent", null,LocaleContextHolder.getLocale());
        String devMessage = ex.toString();
        List<Error> errors = Arrays.asList(new Error(userMessage, devMessage));
        return ResponseEntity.badRequest().body(errors);
    }

    @PreAuthorize("hasAuthority('ROLE_DELETE_POSTING') and #oauth2.hasScope('write')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        postingRepository.delete(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE_POSTING') and #oauth2.hasScope('write')")
    public ResponseEntity<Posting> update(@PathVariable Long id, @RequestBody Posting posting) {
        Posting postingUpdated = postingService.update(id, posting);

        return ResponseEntity.ok(postingUpdated);
    }


}
