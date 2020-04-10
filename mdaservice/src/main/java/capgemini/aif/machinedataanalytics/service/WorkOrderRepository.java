package capgemini.aif.machinedataanalytics.service;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "workorder", path = "workorder")
public interface WorkOrderRepository extends PagingAndSortingRepository<WorkOrder, Integer> {
	WorkOrder findByWorkorderidentifier(@Param("workorderidentifier") String workorderidentifier);
}
