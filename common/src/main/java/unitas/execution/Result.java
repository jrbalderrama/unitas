package unitas.execution;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author javier
 */
public class Result {

    private int status;
    private URI output;
    private URI error;
    private List<URI> results;

    private Result(int status, List<URI> results, URI output, URI error) {

        this.status = status;
        this.results = results;
        this.output = output;
        this.error = error;
    }

    public List<URI> getResults() {
        return results;
    }

    public void setResults(List<URI> results) {
        this.results = results;
    }

    public URI getError() {
        return error;
    }

    public void setError(URI error) {
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public URI getOutput() {
        return output;
    }

    public void setOutput(URI output) {
        this.output = output;
    }

    public static Result getInstance() {

        return new Result(-1, new ArrayList<URI>(), null, null);
    }
}
