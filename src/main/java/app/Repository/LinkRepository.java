package app.Repository;

import app.Entity.LinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LinkRepository extends JpaRepository<LinkEntity, Long> {

    Optional<LinkEntity> findByShortLink(String shortLink);

    List<LinkEntity> findByUser(String user);
}