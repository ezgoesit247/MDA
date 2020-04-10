package capgemini.aif.machinedataanalytics.service;

import java.util.Collection;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import capgemini.aif.machinedataanalytics.service.Metadata.VariableType;

@RepositoryRestResource(collectionResourceRel = "metadata", path = "metadata")
public interface MetadataRepository extends PagingAndSortingRepository<Metadata, Integer> {
	Metadata findByVariablename(@Param("variablename") String variablename);
	Collection<Metadata> findAllByType(@Param("type") VariableType type);
	Metadata findByShortname(@Param("shortname") String shortname);
	
//	Collection<TelemetryMetadata> findByLinemachine(@Param("machinename") String machinename);
//	Collection<TelemetryMetadata> findByEquipment(@Param("equipment") String equipment);
}