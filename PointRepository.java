package de.jonashackt.springbootvuejs.repository;


import de.jonashackt.springbootvuejs.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PointRepository extends CrudRepository<User, Long> {
    List<User> findByUserId(@Param("userId") long userId);
}
