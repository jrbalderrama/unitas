package unitas.execution;


import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import unitas.util.io.FileType;

/**
 * Container to collect the information associated to an application.
 * It is used to pass attributes to the executor and save its raw in
 * form of filters.
 * @author javier
 */
public class ApplicationContainer {
    
    // application parameters
    private List<String> parameters;
    // application inputs
    private Map<URI, FileType> sources;
    // application outputs 
    private Map<Object, Object> sinks;

    public ApplicationContainer() {

        sources = new HashMap<URI, FileType>();
        parameters = new ArrayList<String>();
        sinks = new HashMap<Object, Object>();
    }

    public Map<URI, FileType> getSourcesAndTypes() {

        return sources;
    }

    public List<URI> getSources() {

        return new ArrayList(sources.keySet());
    }

    public void addSource(URI source, FileType type) {

        sources.put(source, type);
    }

    public void addSource(URI source) {

        sources.put(source, FileType.REGULAR);
    }

    public void mergeSources(List<URI> sources) {

        for (URI source : sources) {
            addSource(source);
        }
    }

    public void mergeSources(Map sources) {

        this.sources.putAll(sources);
    }

    public List<String> getParameters() {

        return parameters;
    }

    public void addParameter(String parameter) {

        parameters.add(parameter);
    }

    public void mergeParameters(List<String> parameters) {

        this.parameters.addAll(parameters);
    }

    public Map<Object, Object> getSinks() {

        return sinks;
    }

    public void addSink(Object argument, Object object) {

        sinks.put(argument, object);
    }

    public void mergeSinks(Map<Object, Object> sinks) {

        this.sinks.putAll(sinks);
    }
}
