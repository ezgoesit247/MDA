package capgemini.aif.machinedataanalytics.service;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import capgemini.aif.machinedataanalytics.service.Reel.ReelType;

import java.util.Collection;

@RepositoryRestResource(collectionResourceRel = "reel", path = "reel")
public interface ReelRepository extends PagingAndSortingRepository<Reel, Integer> {
    Reel findByReelidentifier(@Param("reelidentifier") String reelidentifier);
    Collection<Reel> findAllByType(@Param("type") ReelType type);
    Reel findByWorkorder(@Param("workorder") WorkOrder workorder);
}
