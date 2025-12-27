package oxygen.services.jobs_processor.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import oxygen.services.jobs_processor.persistence.entities.OrchestratorEvent;

public interface OrchestratorEventRepository extends JpaRepository<OrchestratorEvent, Long> {
}
