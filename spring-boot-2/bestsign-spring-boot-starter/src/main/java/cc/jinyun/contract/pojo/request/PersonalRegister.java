package cc.jinyun.contract.pojo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PersonalRegister extends Register {
    private Credential credential;

    public void setCredential(String identity) {
        this.credential = new Credential(identity);
    }

    public PersonalRegister(String account, String name, String identity) {
        this.credential = new Credential(identity);
        this.account = account;
        this.name = name;
        this.userType = "1";
        this.applyCert = "1";
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Credential {
        private String identity;
    }
}
