package com.example.oasisdocument.repository.graph;

import com.example.oasisdocument.model.graph.nodes.AuthorNeo;

public interface AuthorNeoRepo extends BaseNeoRepo<AuthorNeo> {
	AuthorNeo findAuthorNeoByAuthorName(String name);
}
