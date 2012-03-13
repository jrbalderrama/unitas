package unitas.execution;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Container to save the information associated to an implementation without the 
 * abstraction of jigsaw objects. It is used to pass attributes to the executor.
 * @author javier
 */
public class ImplementationContainer {
    
    private List<URI> attachments;
    private Map<String, String> configuration;
    private Map<String, String> attributes; //specially for grid app's vars

    public ImplementationContainer() {

        attachments = new ArrayList<URI>();
        configuration = new HashMap<String, String>();
        attributes = new HashMap<String, String>();
    }  

    public List<URI> getAttachments() {
        return attachments;
    }

    public Map<String, String> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Map<String, String> configuration) {
        this.configuration = configuration;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public void addAttachment(URI attachment) {
        attachments.add(attachment);
    }

    public void addVariable(String name, String value) {
        configuration.put(name, value);
    }

    public void addAttribute(String name, String value) {
        attributes.put(name, value);
    }

    public String getVariable(String name) {
        return configuration.get(name);
    }

    public String getAttribute(String name) {
        return attributes.get(name);
    }
}