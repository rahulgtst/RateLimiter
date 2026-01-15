package app.vercel.rahulgtst.entities;

public class Request {
    private final String userId;

    public Request(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
