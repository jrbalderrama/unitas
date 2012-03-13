package unitas.transfer;

/**
 *
 * @author javier
 */
public enum SupportedTransferScheme {

    FILE("file"),
    HTTP("http"),
    HTTPS("https"),
    FTP("ftp"),
    SFTP("sftp"),
    LFN("lfn"),
    GSIFTP("gsftp");
    
    private String scheme;

    private SupportedTransferScheme(String scheme) {

        this.scheme = scheme;
    }

    public String getValue() {
        return scheme;
    }

    /**
     * Replaces the enum operation valueOf() 
     */
    public static SupportedTransferScheme createValueFromName(String name) {

        for (SupportedTransferScheme method : SupportedTransferScheme.values()) {
            if (name.equalsIgnoreCase(method.getValue())) {
                return method;
            }
        }

        throw new TypeNotPresentException(name, null);
    }
}
