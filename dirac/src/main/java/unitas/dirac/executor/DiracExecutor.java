package unitas.dirac.executor;

import java.net.URI;
import unitas.exception.UnitasException;
import unitas.execution.DCIExecutor;
import unitas.execution.UnitasExecutor;

/**
 *
 * @author tram
 */
public class DiracExecutor extends UnitasExecutor implements DCIExecutor {

    @Override
    public void generateJDL() {
        // implement the DIRAC jdl generation here
    }

    @Override
    public void submit(String command, URI location, URI path) throws UnitasException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
