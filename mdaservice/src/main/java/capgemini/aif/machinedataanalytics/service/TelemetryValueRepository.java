package capgemini.aif.machinedataanalytics.service;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "telemetryvalue", path = "telemetryvalue")
public interface TelemetryValueRepository extends PagingAndSortingRepository<TelemetryValue, Long> {
}
