package sg.edu.nus.comp.cs3219.viz.storage.repository;

import sg.edu.nus.comp.cs3219.viz.common.entity.Conference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// TODO: add relevant methods as necessary to modify/retrieve information about metadata
public interface ConferenceRepository extends JpaRepository<Conference, Long> {

    List<Conference> findByCreatorIdentifier(String creatorIdentifier);

}
