package com.example.oasisdocument.repository;

import com.example.oasisdocument.docs.Paper;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.awt.print.Pageable;
import java.math.BigInteger;
import java.util.List;

public interface PaperRepository extends MongoRepository<Paper, BigInteger> {
    List<Paper> findAllById(BigInteger id);

    List<Paper> findAllByYear(int year);
}
