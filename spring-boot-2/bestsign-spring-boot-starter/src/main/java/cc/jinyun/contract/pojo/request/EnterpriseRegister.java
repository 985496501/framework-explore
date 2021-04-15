package cc.jinyun.contract.pojo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EnterpriseRegister extends Register {
    private Credential credential;

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    public EnterpriseRegister(String account, String name, String regCode, String legalPerson, String legalPersonIdentity) {
        this.account = account;
        this.name = name;
        this.userType = "2";
        this.applyCert = "1";
        this.credential = new Credential(regCode, legalPerson, legalPersonIdentity, account);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Credential {
        private String regCode;
        private String legalPerson;
        private String legalPersonIdentity;
        private String legalPersonIdentityType;
        private String contactMobile;

        public Credential(String regCode, String legalPerson, String legalPersonIdentity, String contactMobile) {
            this.regCode = regCode;
            this.legalPerson = legalPerson;
            this.legalPersonIdentity = legalPersonIdentity;
            this.legalPersonIdentityType = "1";
            this.contactMobile = contactMobile;
        }
    }
}
