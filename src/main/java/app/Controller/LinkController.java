package app.Controller;

import app.Entity.LinkEntity;
import app.Property.AppProperty;
import app.Repository.LinkRepository;
import app.Service.LinkService;
import app.Utils.LinkUtil;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/links")
public class LinkController {
    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private LinkUtil linkUtil;

    @Autowired
    private AppProperty appProperty;

    @Autowired
    private LinkService linkService;

    @Operation(summary = "Создать новую короткую ссылку")
    @ApiResponse(responseCode = "200", description = "Ссылка успешно создана")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @PostMapping
    public ResponseEntity<LinkEntity> create(
            @RequestHeader("userUuid") @Parameter(description = "UUID пользователя") String userUuid,
            @RequestParam @Parameter(description = "Ссылка, которую сокращаем") String link,
            @RequestParam(required = false, defaultValue = "24") @Parameter(description = "Время жизни ссылки в часах") int expired,
            @RequestParam(required = false, defaultValue = "100") @Parameter(description = "Максимальное количество переходов по ссылке") int limit
    ) {
        LinkEntity newLink = LinkEntity.create(
                link,
                linkUtil.createShortLink(appProperty.len, appProperty.chars),
                LocalDateTime.now().plusHours(appProperty.expired > expired ? expired : appProperty.expired),
                validateUserUuid(userUuid),
                appProperty.limit > limit ? limit : appProperty.limit
        );

        return ResponseEntity.ok(linkRepository.save(newLink));
    }

    @Operation(summary = "Получение всех ссылок для конкретного пользователя")
    @ApiResponse(responseCode = "200")
    @GetMapping
    public List<LinkEntity> getLinks(@RequestHeader("userUuid") @Parameter(description = "UUID пользователя") String userUuid) {
        return linkRepository.findByUser(validateUserUuid(userUuid));
    }

    @Operation(summary = "Удалить ссылку")
    @ApiResponse(responseCode = "200", description = "Ссылка успешно удалена")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @DeleteMapping
    public ResponseEntity<?> delete(
            @RequestHeader("userUuid") @Parameter(description = "UUID пользователя") String userUuid,
            @RequestParam @Parameter(description = "Код ссылки") String shortLinkCode
    ) {
        try {
            Optional<LinkEntity> linkEntity = linkRepository.findByShortLink(shortLinkCode);
            if (linkEntity.isEmpty()) {
                throw new RuntimeException("Ссылка не найдена");
            }

            if (!Objects.equals(linkEntity.get().getUser(), validateUserUuid(userUuid))) {
                throw new RuntimeException("Ссылка вам не принадлежит. Только владелец может удалять ссылки!");
            }

            linkRepository.delete(linkEntity.get());

            return ResponseEntity.ok("Ссылка " + linkEntity.get().getShortLink() + " удалена!");

        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка: " + ex.getMessage());
        }
    }

    @Operation(summary = "Обновить лимит переходов по ссылке")
    @ApiResponse(responseCode = "200", description = "Параметры ссылки успешно обновлены")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @PutMapping
    public ResponseEntity<?> updateLinkTransitionLimit(
            @RequestHeader("userUuid") @Parameter(description = "UUID пользователя") String userUuid,
            @RequestParam @Parameter(description = "Код ссылки") String shortLinkCode,
            @RequestParam @Parameter(description = "Новый лимит переходов по ссылке") int limit
    ) {
        try {
            Optional<LinkEntity> linkEntity = linkRepository.findByShortLink(shortLinkCode);
            if (linkEntity.isEmpty()) {
                throw new RuntimeException("Ссылка не найдена");
            }

            if (!Objects.equals(linkEntity.get().getUser(), validateUserUuid(userUuid))) {
                throw new RuntimeException("Ссылка вам не принадлежит. Только владелец может редактировать лимит переходов по ссылке!");
            }

            int newLimit = appProperty.limit > limit ? limit : appProperty.limit;

            linkService.updateTransitionLimit(linkEntity.get(), newLimit);

            return ResponseEntity.ok("Лимит переходов по ссылке " + linkEntity.get().getShortLink() + " изменен на " + newLimit);

        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка: " + ex.getMessage());
        }
    }

    protected String validateUserUuid(String userUuid) {
        if (userUuid == null || userUuid.isBlank() || userUuid.length() < 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user_uuid is required");
        }
        return userUuid;
    }
}
