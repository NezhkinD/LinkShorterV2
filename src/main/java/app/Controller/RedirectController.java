package app.Controller;

import app.Entity.LinkEntity;
import app.Repository.LinkRepository;
import app.Service.LinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/s")
public class RedirectController {
    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private LinkService linkService;

    @Operation(summary = "Редирект на оригинальный ресурс")
    @ApiResponse(responseCode = "200", description = "ОК")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @GetMapping("/{shortLinkCode}")
    public ResponseEntity<?> redirect(@PathVariable String shortLinkCode) {
        try {
            Optional<LinkEntity> link = linkRepository.findByShortLink(shortLinkCode);
            if (link.isEmpty()) {
                throw new RuntimeException("Ссылка не найдена");
            }

            if (link.get().getTransitionCurrent() >= link.get().getTransitionLimit()) {
                linkRepository.delete(link.get());
                throw new RuntimeException("Вы превысили максимальное кол-во переходов по ссылке. Ссылка удалена!");
            }

            if (LocalDateTime.now().isAfter(link.get().getExpiredAt())) {
                linkRepository.delete(link.get());
                throw new RuntimeException("Время жизни ссылки истекло. Ссылка удалена!");
            }

            linkService.incrementTransitionCurrent(link.get().getId());

            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(link.get().getOriginal()))
                    .build();
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка: " + ex.getMessage());
        }
    }
}
