package capgemini.aif.machinedataanalytics.service;

import java.sql.Timestamp;
import java.util.Collection;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import capgemini.aif.machinedataanalytics.service.Telemetry.SendStatus;

@RepositoryRestResource(collectionResourceRel = "telemetry", path = "telemetry")
public interface TelemetryRepository extends PagingAndSortingRepository<Telemetry, Long>, CustomizedTelemetryRepository {
	Collection<Telemetry> findAllByReel(@Param("reel") Reel reel);
	Collection<Telemetry> findAllByReelidentifier(@Param("reelidentifier") String reelidentifier);
	Collection<Telemetry> findAllByReelAndSendstatus(@Param("reel") Reel reel, @Param("sendststaus") SendStatus sendstatus);
	Collection<Telemetry> findAllByReelidentifierAndSendstatus(@Param("reelidentifier") String reelidentifier, @Param("sendststaus") SendStatus sendstatus);
	Collection<Telemetry> findAllByTimestamp(@Param("timestamp") Timestamp timestamp);
	Telemetry findByReelAndTimestamp(@Param("reel") Reel reel, @Param("timestamp") Timestamp timestamp);
	Telemetry findByReelidentifierAndTimestamp(@Param("reelidentifier") String reelidentifier, @Param("timestamp") Timestamp timestamp);
	void ehsend(@Param("telemetry") Telemetry telemetry);
	<S extends Telemetry> S save(@Param("telemetry") S telemetry);
	
	//	@Query("select t from Telemetry t where t.reel.reelidentifier=:reelidentifier")
//	Collection<Telemetry> findByReelidentifier(@Param("reelidentifier") String reelidentifier);
}
