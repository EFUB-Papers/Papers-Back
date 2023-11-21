package efub.toy2.papers.domain.tag.repository;

import efub.toy2.papers.domain.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository <Tag, Long> {
    Optional<Tag> findByTagName (String tagName);
    Boolean existsTagByTagName (String tagName);

    List<Tag> findByTagNameContaining(String query);
}
