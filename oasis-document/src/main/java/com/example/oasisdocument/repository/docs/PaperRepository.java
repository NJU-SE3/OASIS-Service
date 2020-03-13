package com.example.oasisdocument.repository.docs;

import com.example.oasisdocument.model.docs.Paper;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface PaperRepository extends MongoRepository<Paper, BigInteger> {
    List<Paper> findAllById(BigInteger id);

    List<Paper> findAllByYear(int year);
}
