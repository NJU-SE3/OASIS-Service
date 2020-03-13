package com.example.oasisdocument.repository.analysis;

import com.example.oasisdocument.docs.analysis.AuthorCitation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigInteger;

public interface AuthorCitationRepo extends MongoRepository<AuthorCitation, BigInteger> {

}
