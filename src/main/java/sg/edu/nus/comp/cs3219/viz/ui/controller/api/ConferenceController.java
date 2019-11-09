package sg.edu.nus.comp.cs3219.viz.ui.controller.api;

import sg.edu.nus.comp.cs3219.viz.common.datatransfer.UserInfo;
import sg.edu.nus.comp.cs3219.viz.common.entity.Conference;
import sg.edu.nus.comp.cs3219.viz.common.exception.ConferenceNotFoundException;
import sg.edu.nus.comp.cs3219.viz.logic.ConferenceLogic;
import sg.edu.nus.comp.cs3219.viz.logic.GateKeeper;
import sg.edu.nus.comp.cs3219.viz.logic.RecordLogic;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ConferenceController extends BaseRestController {

    private final ConferenceLogic conferenceLogic;
    private final RecordLogic recordLogic;

    private final GateKeeper gateKeeper;

    public ConferenceController(ConferenceLogic conferenceLogic, GateKeeper gateKeeper, RecordLogic recordLogic) {
        this.conferenceLogic = conferenceLogic;
        this.gateKeeper = gateKeeper;
        this.recordLogic = recordLogic;
    }

    @GetMapping("/conferences")
    public List<Conference> all() {
        UserInfo currentUser = gateKeeper.verifyLoginAccess();
        System.out.println("HERE");
        return conferenceLogic.findAllForUser(currentUser);
    }

    @PostMapping("/conferences")
    public ResponseEntity<?> newConference(@RequestBody Conference conference) throws URISyntaxException {
        UserInfo currentUser = gateKeeper.verifyLoginAccess();

        Conference newConference = conferenceLogic.saveForUser(conference, currentUser);

        return ResponseEntity
                .created(new URI("/conferences/" + newConference.getId()))
                .body(newConference);
    }

    @GetMapping("/conferences/{id}")
    public Conference one(@PathVariable Long id) {
        Conference conference = conferenceLogic.findById(id)
                .orElseThrow(() -> new ConferenceNotFoundException(id));

        gateKeeper.verifyAccessForConference(conference);

        return conference;
    }

    @PutMapping("/conferences/{id}")
    public ResponseEntity<?> updateConference(@RequestBody Conference newConference, @PathVariable Long id) throws URISyntaxException {
        Conference oldConference = conferenceLogic.findById(id)
                .orElseThrow(() -> new ConferenceNotFoundException(id));

        gateKeeper.verifyAccessForConference(oldConference);

        Conference updatedConference = conferenceLogic.updateConference(oldConference, newConference);
        return ResponseEntity.created(new URI("/conferences/" + newConference.getId()))
                .body(updatedConference);

    }

    @DeleteMapping("/conferences/{id}")
    public ResponseEntity<?> deleteConference(@PathVariable Long id) {
        Conference toDeleteConference = conferenceLogic.findById(id)
                .orElseThrow(() -> new ConferenceNotFoundException(id));
        gateKeeper.verifyAccessForConference(toDeleteConference);

        recordLogic.removeSubmissionAndAuthorRecordsForConference(toDeleteConference);

        conferenceLogic.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
