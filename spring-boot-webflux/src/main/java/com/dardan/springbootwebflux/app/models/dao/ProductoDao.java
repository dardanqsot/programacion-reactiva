package com.dardan.springbootwebflux.app.models.dao;

import com.dardan.springbootwebflux.app.models.documents.Producto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductoDao extends ReactiveMongoRepository<Producto, String>{

}
