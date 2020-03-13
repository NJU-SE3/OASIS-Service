package com.example.oasisdocument.repository.docs;

import com.example.oasisdocument.model.docs.Authority;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface AuthorityRepository extends MongoRepository<Authority, BigInteger> {
}
