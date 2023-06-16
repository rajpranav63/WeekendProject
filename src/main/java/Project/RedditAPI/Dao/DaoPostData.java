package Project.RedditAPI.Dao;

import Project.RedditAPI.entity.PostData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DaoPostData extends MongoRepository<PostData,String> {

}
