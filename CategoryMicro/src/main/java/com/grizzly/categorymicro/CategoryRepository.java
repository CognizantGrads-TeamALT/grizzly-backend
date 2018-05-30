package com.grizzly.categorymicro;


import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, String>
{


    List<Category> findByCategoryIdOrName(@Param("categoryId") String categoryId, @Param("name") String name, Pageable pageable);


}