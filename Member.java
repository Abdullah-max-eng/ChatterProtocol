public class Member {
    private String screenName;
    private String ip;
    private int port;

    public Member(String screenName, String ip, int port) {
        this.screenName = screenName;
        this.ip = ip;
        this.port = port;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}