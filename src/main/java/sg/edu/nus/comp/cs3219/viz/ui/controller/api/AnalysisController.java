package sg.edu.nus.comp.cs3219.viz.ui.controller.api;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.AccessLevel;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.AnalysisRequest;
import sg.edu.nus.comp.cs3219.viz.common.entity.Presentation;
import sg.edu.nus.comp.cs3219.viz.common.entity.PresentationSection;
import sg.edu.nus.comp.cs3219.viz.common.exception.PresentationNotFoundException;
import sg.edu.nus.comp.cs3219.viz.logic.AnalysisLogic;
import sg.edu.nus.comp.cs3219.viz.logic.GateKeeper;
import sg.edu.nus.comp.cs3219.viz.logic.PresentationLogic;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class AnalysisController extends BaseRestController {

    private GateKeeper gateKeeper;

    private AnalysisLogic analysisLogic;

    private PresentationLogic presentationLogic;

    private static final Logger log = Logger.getLogger(AnalysisLogic.class.getSimpleName());

    public AnalysisController(GateKeeper gateKeeper, PresentationLogic presentationLogic, AnalysisLogic analysisLogic) {
        this.analysisLogic = analysisLogic;
        this.presentationLogic = presentationLogic;
        this.gateKeeper = gateKeeper;
    }

    @PostMapping("/presentations/{id}/analysis")
    public List<Map<String, Object>> analysis(@PathVariable Long id, @Valid @RequestBody AnalysisRequest analysisRequest) {
        // verify access level
        Presentation presentation = presentationLogic.findById(id)
                .orElseThrow(() -> new PresentationNotFoundException(id));
        gateKeeper.verifyAccessForPresentation(presentation, AccessLevel.CAN_READ);

        Long conferenceId = presentation.getConference().getId();

        List<Map<String, Object>> result = analysisLogic.analyse(analysisRequest, conferenceId);
        log.info("Analysis Result from query: " + result);
        // convert to map with key all in lower case
        return result.stream().map(m -> {
            Map<String, Object> map = new HashMap<>();
            m.forEach((k, v) -> map.put(k.toLowerCase(), v));
            return map;
        }).collect(Collectors.toList());
    }

    @PostMapping("/presentations/{id}/coauthorshipdata")
    public Map<String, List<Map<String, Object>>> coauthorshipData(@PathVariable Long id, @Valid @RequestBody AnalysisRequest analysisRequest) {
        // verify access level
        Presentation presentation = presentationLogic.findById(id)
                .orElseThrow(() -> new PresentationNotFoundException(id));
        gateKeeper.verifyAccessForPresentation(presentation, AccessLevel.CAN_READ);

        Long conferenceId = presentation.getConference().getId();

        List<Map<String, Object>> result = analysisLogic.analyse(analysisRequest, conferenceId);

        return buildForceDirectedGraph(result, "article", "person");
    }

    @PostMapping("/presentations/{id}/coauthorshipdatasimilar")
    public Map<String, List<Map<String, Object>>> coauthorshipDataSimilar(@PathVariable Long id, @Valid @RequestBody AnalysisRequest analysisRequest) {
        // verify access level
        Presentation presentation = presentationLogic.findById(id)
                .orElseThrow(() -> new PresentationNotFoundException(id));
        gateKeeper.verifyAccessForPresentation(presentation, AccessLevel.CAN_READ);

        Long conferenceId = presentation.getConference().getId();

        List<Map<String, Object>> result = analysisLogic.analyse(analysisRequest, conferenceId);

        return buildForceDirectedGraph(result, "person", "person");
    }

    public Map<String, List<Map<String, Object>>> buildForceDirectedGraph(List<Map<String, Object>> analysisResult, String sourceName, String targetName) {
        class Node {
            private String name;
            private String type;

            @Override
            public boolean equals(Object o) {
                if (o instanceof Node) {
                    Node n = (Node) o;
                    return this.name.equals(n.name) && this.type.equals(n.type);
                }
                return false;
            }

            @Override
            public int hashCode() {
                return Objects.hash(this.name, this.type);
            }
        }

        // convert to map with key all in lower case
        List<Map<String, Object>> links_data = analysisResult.stream()
                .filter(m -> !m.get("source").equals(m.get("target"))).map(m -> {
                    Map<String, Object> map = new HashMap<>();
                    m.forEach((k, v) -> map.put(k.toLowerCase(), v.toString()));
                    return map;
                }).collect(Collectors.toList());
        List<Map<String, Object>> nodes_data = links_data.stream().flatMap(m -> {
            Node n = new Node();
            n.name = m.get("source").toString();
            n.type = sourceName;
            Node n2 = new Node();
            n2.name = m.get("target").toString();
            n2.type = targetName;
            return Stream.of(n, n2);
        }).distinct()
                .map(n -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", n.name);
                    map.put("type", n.type);
                    return map;
                }).collect(Collectors.toList());
        Map<String, List<Map<String, Object>>> finalResult = new HashMap<>();
        finalResult.put("nodes", nodes_data);
        finalResult.put("links", links_data);
        return finalResult;
    }
}
