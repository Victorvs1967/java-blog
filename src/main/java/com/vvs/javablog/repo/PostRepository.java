package com.vvs.javablog.repo;

import com.vvs.javablog.models.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PostRepository extends CrudRepository<Post, Long> {

}
