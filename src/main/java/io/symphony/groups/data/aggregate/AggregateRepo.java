package io.symphony.groups.data.aggregate;

import org.springframework.data.repository.CrudRepository;

public interface AggregateRepo extends CrudRepository<Aggregate, String> {

}
