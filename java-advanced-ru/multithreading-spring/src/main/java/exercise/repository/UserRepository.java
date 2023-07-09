package exercise.repository;

import exercise.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;


// BEGIN
public interface UserRepository extends  ReactiveCrudRepository<User, Integer> {
}
// END
