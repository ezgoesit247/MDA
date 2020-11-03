package capgemini.aif.machinedataanalytics.service;

import java.util.Collection;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "fftresult", path = "fftresult")
public interface FFTDetailsRepository extends PagingAndSortingRepository<FFTDetails, Long> {
	Collection<FFTDetails> findAllByWorkorder(@Param("workorder") WorkOrder workorder);
	Collection<FFTDetails> findAllByReel(@Param("reel") Reel reel);
}
