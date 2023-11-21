package efub.toy2.papers.domain.category.repository;

import efub.toy2.papers.domain.category.domain.Category;
import efub.toy2.papers.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCategoryName(String categoryName);
}
