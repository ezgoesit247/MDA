package capgemini.aif.machinedataanalytics.service;

public interface CustomizedTelemetryRepository {
	void ehsend(Telemetry telemetry);
	<S extends Telemetry> S save(S entity);
}
