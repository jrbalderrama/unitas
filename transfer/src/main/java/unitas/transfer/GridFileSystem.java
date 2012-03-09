package unitas.transfer;

import grool.configuration.GroolDefaultValues;
import grool.transfer.*;
import java.io.File;
import java.net.URI;
import java.util.List;

/**
 *
 * @author javier
 */
public class GridFileSystem implements FileSystem {

    private File proxy;
    private String virtualOrganization;
    private List<String> storageElementsForReplicas;

    public GridFileSystem(File proxy) {
        this(proxy, GroolDefaultValues.getDefaultVirtualOrganization());
    }

    public GridFileSystem(File proxy, String virtualOrganization) {
        this(proxy, virtualOrganization, null);
    }

    public GridFileSystem(File proxy, String virtualOrganization, List<String> storageElementsForReplicas) {

        this.proxy = proxy;
        this.virtualOrganization = virtualOrganization;
        this.storageElementsForReplicas = storageElementsForReplicas;
    }

    @Override
    public void copy(URI source, URI destination)
            throws
            grool.transfer.FileSystemException {

        FileSystem fileSystem = this.getFileSystem(source, destination);
        try {
            fileSystem.copy(source, destination);
        } catch (grool.transfer.FileSystemException ex) {
            //throw new unitas.exception.UnitasException(ex);
        }
    }

    @Override
    public void copyTo(URI source, URI destination)
            throws
            grool.transfer.FileSystemException {

        FileSystem fileSystem = this.getFileSystem(source, destination);
        try {
            fileSystem.copyTo(source, destination);
        } catch (grool.transfer.FileSystemException ex) {
            // throw new unitas.exception.UnitasException(ex);
        }
    }

    @Override
    public void copyDirectory(URI source, URI destination)
            throws
            grool.transfer.FileSystemException {

        FileSystem fileSystem = this.getFileSystem(source, destination);
        try {
            fileSystem.copyDirectory(source, destination);
        } catch (grool.transfer.FileSystemException ex) {
            //throw new unitas.exception.UnitasException(ex);
        }
    }

    @Override
    public void copyDirectoryTo(URI source, URI destination)
            throws
            grool.transfer.FileSystemException {

        FileSystem fileSystem = this.getFileSystem(source, destination);
        try {
            fileSystem.copyDirectoryTo(source, destination);
        } catch (grool.transfer.FileSystemException ex) {
            // throw new unitas.exception.UnitasException(ex);
        }
    }

    @Override
    public boolean exists(URI file)
            throws
            grool.transfer.FileSystemException {

        boolean exist = false;
        FileSystem fileSystem = this.getFileSystem(file, file);
        try {
            exist = fileSystem.exists(file);
        } catch (grool.transfer.FileSystemException ex) {
//            throw new unitas.exception.UnitasException(ex);
        } finally {
            return exist;
        }
    }

    @Override
    public long getModificationTime(URI file)
            throws
            grool.transfer.FileSystemException {

        long time = 0L;
        FileSystem fileSystem = this.getFileSystem(file, file);
        try {
            time = fileSystem.getModificationTime(file);
        } catch (grool.transfer.FileSystemException ex) {
            throw new unitas.exception.UnitasException(ex);
        } finally {
            return time;
        }
    }

    private FileSystem getFileSystem(URI source, URI destination)
            throws
            grool.transfer.FileSystemException {

        FileSystem fileSystem = null;
        String sourceScheme = source.getScheme();
        String destinationScheme = destination.getScheme();
        if (sourceScheme.equalsIgnoreCase("lfn") || destinationScheme.equalsIgnoreCase("lfn")) {

            String method = GroolDefaultValues.getDefaultLfnCopyMethod();
            if (method.equalsIgnoreCase("ui")) {
                // this method is discontinued, preserved in case reintroduction is required
                fileSystem = new RemoteFileSystem(null, proxy);
            }
        } else {

            String method = GroolDefaultValues.getDefaultGsiftpCopyMethod();
            if (method.equalsIgnoreCase("globus")) {
                fileSystem = new GlobusFileSystem(proxy);
            }
        }

        if (fileSystem == null) {
            try {

//                if (EGIInformant.isAPITransfer()) {
//                    
                VletFileSystemContext context = new VletFileSystemContext(proxy, virtualOrganization, storageElementsForReplicas);
                fileSystem = new VletFileSystem(context);
//                } else {
//                    
//                    fileSystem = new LcgFileSystem(proxy, virtualOrganization, storageElementsForReplicas);
//
//                }
            } catch (grool.transfer.ContextException ex) {
                // throw new unitas.exception.UnitasException(ex);
            }
        }

        return fileSystem;
    }
}
