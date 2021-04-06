package de.jonashackt.springbootvuejs.repository;


import de.jonashackt.springbootvuejs.domain.Point;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PointRepository extends CrudRepository<Point, Long> {
    List<Point> findByUserId(@Param("userId") long userId);
}
