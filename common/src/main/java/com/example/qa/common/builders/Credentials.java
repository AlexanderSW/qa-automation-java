// common/src/main/java/com/example/qa/common/builders/Credentials.java
package com.example.qa.common.builders;

public class Credentials {
    private final String username;
    private final String password;

    private Credentials(Builder b) {
        this.username = b.username;
        this.password = b.password;
    }

    public String username() { return username; }
    public String password() { return password; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String username;
        private String password;

        public Builder username(String v) { this.username = v; return this; }
        public Builder password(String v) { this.password = v; return this; }
        public Credentials build() { return new Credentials(this); }
    }
}
