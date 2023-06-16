package Project.RedditAPI.Dao;

import Project.RedditAPI.entity.RedditData;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DaoReddit extends MongoRepository<RedditData,String> {
    List<RedditData> findBySelftextContaining(String keyword);

    List<RedditData> findByAuthor(String author);

    List<RedditData> findAllByOrderByDateAsc(Sort sort);

    List<RedditData> findBySelftextContainingAndAuthor(String keyword, String author);

    void deleteByAuthor(String author);
}
