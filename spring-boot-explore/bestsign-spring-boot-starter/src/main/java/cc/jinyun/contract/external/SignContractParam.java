package cc.jinyun.contract.external;


import java.util.Map;

public interface SignContractParam {
    /**
     * Can be null. default domain company.
     *
     * @return userid
     */
    Long getPartA();

    Long getPartB();

    String getTid();

    /**
     * used for passing extra variables
     *
     * @return extra ps
     */
    Map<String, Object> getExtras();
}
