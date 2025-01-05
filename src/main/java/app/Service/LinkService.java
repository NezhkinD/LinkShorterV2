package app.Service;

import app.Entity.LinkEntity;
import app.Repository.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LinkService {
    private final LinkRepository linkRepository;

    @Autowired
    public LinkService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public void incrementTransitionCurrent(Long id) {
        LinkEntity link = linkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ссылка не найдена с ID: " + id));
        link.setTransitionCurrent(link.getTransitionCurrent() + 1);
        linkRepository.save(link);
    }

    public void updateTransitionLimit(LinkEntity linkEntity, int transitionLimit) {
        linkEntity.setTransitionLimit(transitionLimit);
        linkRepository.save(linkEntity);
    }
}
