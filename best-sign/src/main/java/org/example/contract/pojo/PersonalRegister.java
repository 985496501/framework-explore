package org.example.contract.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class PersonalRegister {
    private String account;
    private String name;
    private String userType;
    private String applyCert;
    private Credential credential;

    public void setCredential(String identity) {
        this.credential = new Credential(identity);
    }

    public PersonalRegister(String account, String name, String userType, String applyCert, String identity) {
        this.account = account;
        this.name = name;
        this.userType = userType;
        this.applyCert = applyCert;
        this.credential = new Credential(identity);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Credential {
        private String identity;

        public String getIdentity() {
            return identity;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }
    }

}
