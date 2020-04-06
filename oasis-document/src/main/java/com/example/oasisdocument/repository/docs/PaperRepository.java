package com.example.oasisdocument.repository.docs;

import com.example.oasisdocument.model.docs.Paper;
import com.example.oasisdocument.repository.BaseMongoRepo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface PaperRepository extends BaseMongoRepo<Paper> {
    List<Paper> findAllById(String id);

    List<Paper> findAllByYear(int year);
}
