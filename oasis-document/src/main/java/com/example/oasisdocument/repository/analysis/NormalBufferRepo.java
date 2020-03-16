package com.example.oasisdocument.repository.analysis;

import com.example.oasisdocument.model.docs.analysis.NormalBuffer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigInteger;

public interface NormalBufferRepo extends MongoRepository<NormalBuffer, BigInteger> {
}