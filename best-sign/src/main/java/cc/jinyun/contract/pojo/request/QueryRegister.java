package cc.jinyun.contract.pojo.request;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryRegister {
    private String account;
    private String taskId;

    @Data
    public static class QueryRegisterResult {
        private String message;
        private String status;
    }

    @Getter
    @RequiredArgsConstructor
    public enum RegisterEnum {
        NEW("1"),
        APPLYING("2"),
        TIMEOUT("3"),
        FAIL("4"),
        SUCCESS("5"),
        RETRY("6"),
        INVALID("-1"),
        NONE("0");
        private final String status;

        public static boolean success(String status) {
            return SUCCESS.status.equals(status);
        }
    }
}
