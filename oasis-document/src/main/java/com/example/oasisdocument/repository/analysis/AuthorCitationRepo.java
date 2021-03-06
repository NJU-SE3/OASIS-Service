package com.example.oasisdocument.repository.analysis;

import com.example.oasisdocument.model.docs.analysis.AuthorCitation;
import com.example.oasisdocument.repository.BaseMongoRepo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository("AuthorCitationRepo")
public interface AuthorCitationRepo extends BaseMongoRepo<AuthorCitation> {

}
