package unitas.execution;

/**
 * This class contains all information related to an execution profile. 
 * 
 * @author tram
 */
public class Context {
    
    // Infrastructure type (EGI, GRID5000, PBS or LOCAL)
    private String infrastructure;
    // Infrastructure middleware used in case infrastructure is a distributed one 
    private String middleware;      
    // Job type: could be normal or MPI, etc
    private String job;

    public Context(String infrastructure, String middleware, String job) {
        this.infrastructure = infrastructure;
        this.middleware = middleware;
        this.job = job;
    }

    public String getInfrastructure() {
        return infrastructure;
    }

    public void setInfrastructure(String infrastructure) {
        this.infrastructure = infrastructure;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getMiddleware() {
        return middleware;
    }

    public void setMiddleware(String middleware) {
        this.middleware = middleware;
    }
    
    
}
