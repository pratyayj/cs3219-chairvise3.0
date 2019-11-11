package sg.edu.nus.comp.cs3219.viz.ui.controller.api;

import sg.edu.nus.comp.cs3219.viz.common.entity.Conference;
import sg.edu.nus.comp.cs3219.viz.common.entity.ConferenceRecord;
import sg.edu.nus.comp.cs3219.viz.common.exception.ConferenceNotFoundException;
import sg.edu.nus.comp.cs3219.viz.logic.ConferenceLogic;
import sg.edu.nus.comp.cs3219.viz.logic.ConferenceRecordLogic;
import sg.edu.nus.comp.cs3219.viz.logic.GateKeeper;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConferenceRecordController extends BaseRestController {

    private final ConferenceRecordLogic conferenceRecordLogic;

    private final ConferenceLogic conferenceLogic;

    private final GateKeeper gateKeeper;

    public ConferenceRecordController(ConferenceRecordLogic conferenceRecordLogic, GateKeeper gateKeeper, ConferenceLogic conferenceLogic) {
        this.conferenceRecordLogic = conferenceRecordLogic;
        this.gateKeeper = gateKeeper;
        this.conferenceLogic = conferenceLogic;
    }

    @GetMapping("/conferenceRecords/{id}")
    public List<ConferenceRecord> allRecords(@PathVariable Long id) {
        Conference selectedConference = conferenceLogic.findById(id)
                .orElseThrow(() -> new ConferenceNotFoundException(id));
        return conferenceRecordLogic.findAllForConference(selectedConference);
    }
}
