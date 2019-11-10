package sg.edu.nus.comp.cs3219.viz.common.datatransfer;

import sg.edu.nus.comp.cs3219.viz.common.entity.PresentationSection;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

public class AnalysisRequest {

    @NotEmpty
    private String dataSet;

    private List<PresentationSection.Selection> selections = new ArrayList<>();

    @NotEmpty
    private List<PresentationSection.Record> involvedRecords = new ArrayList<>();

    private List<PresentationSection.Filter> filters = new ArrayList<>();

    private List<PresentationSection.Joiner> joiners = new ArrayList<>();

    private List<PresentationSection.Grouper> groupers = new ArrayList<>();

    private List<PresentationSection.Sorter> sorters = new ArrayList<>();

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public List<PresentationSection.Selection> getSelections() {
        return selections;
    }

    public void setSelections(List<PresentationSection.Selection> selections) {
        this.selections = selections;
    }

    public void addSelection(PresentationSection.Selection selection) {
        this.selections.add(selection);
    }

    public List<PresentationSection.Record> getInvolvedRecords() {
        return involvedRecords;
    }

    public void setInvolvedRecords(List<PresentationSection.Record> involvedRecords) {
        this.involvedRecords = involvedRecords;
    }

    public void addInvolvedRecord(PresentationSection.Record involvedRecord) {
        this.involvedRecords.add(involvedRecord);
    }

    public List<PresentationSection.Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<PresentationSection.Filter> filters) {
        this.filters = filters;
    }

    public void addFilter(PresentationSection.Filter filter) {
        this.filters.add(filter);
    }

    public List<PresentationSection.Joiner> getJoiners() {
        return joiners;
    }

    public void setJoiners(List<PresentationSection.Joiner> joiners) {
        this.joiners = joiners;
    }

    public void addJoiner(PresentationSection.Joiner joiner) {
        this.joiners.add(joiner);
    }

    public List<PresentationSection.Grouper> getGroupers() {
        return groupers;
    }

    public void setGroupers(List<PresentationSection.Grouper> groupers) {
        this.groupers = groupers;
    }

    public void addGrouper(PresentationSection.Grouper grouper) {
        this.groupers.add(grouper);
    }

    public List<PresentationSection.Sorter> getSorters() {
        return sorters;
    }

    public void setSorters(List<PresentationSection.Sorter> sorters) {
        this.sorters = sorters;
    }

    public void addSorter(PresentationSection.Sorter sorter) {
        this.sorters.add(sorter);
    }

    public void mergeWith(AnalysisRequest other) {
        this.selections.addAll(other.getSelections());
        this.sorters.addAll(other.getSorters());
        this.groupers.addAll(other.getGroupers());
        this.joiners.addAll(other.getJoiners());
        this.filters.addAll(other.getFilters());
        this.involvedRecords.addAll(other.getInvolvedRecords());
    }
}
