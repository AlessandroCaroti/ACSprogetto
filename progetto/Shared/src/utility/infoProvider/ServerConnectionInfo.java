package utility.infoProvider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerConnectionInfo {
    private static final String SERVERNAME_PATTERN = "Server_([a-f0-9]{8}(-[a-f0-9]{4}){3}-[a-f0-9]{12})";

    static public void validateData(final String[] serverInfo) throws IllegalArgumentException {
        if (serverInfo == null || serverInfo.length != 3)
            throw new IllegalArgumentException("");
        validateIPAddress(serverInfo[0]);
        validateServerName(serverInfo[2]);
        validatePort(serverInfo[1]);
    }

    static public void validatePort(final String port) throws IllegalArgumentException {
        try {
            validatePort(Integer.parseInt(port));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(port + " is not valid as a port value.");
        }
    }

    static public void validatePort(final int port) throws IllegalArgumentException {
        if (port < 1024 || port > 65535)
            throw new IllegalArgumentException(port + " is not valid as a port value.");
    }

    static public void validateIPAddress(final String ipAddress) throws IllegalArgumentException {
        if (ipAddress == null)
            throw new IllegalArgumentException("RegistryAddressHost requires a not null value");

        String[] tokens = ipAddress.split("\\.");
        if (tokens.length != 4) {
            throw new IllegalArgumentException(ipAddress + " is not valid as a ipAddress value.");
        }
        for (String str : tokens) {
            int i = Integer.parseInt(str);
            if ((i < 0) || (i > 255)) {
                throw new IllegalArgumentException(ipAddress + " is not valid as a ipAddress value.");
            }
        }
    }

    static public void validateServerName(final String serverName) throws IllegalArgumentException {
        if (serverName == null)
            throw new IllegalArgumentException("ServerName requires a not null value");
        Pattern pattern = Pattern.compile(SERVERNAME_PATTERN);
        Matcher matcher = pattern.matcher(serverName);
        if (!matcher.matches())
            throw new IllegalArgumentException("\'" + serverName + "\' is not valid as a ServerName value.");
    }
}
