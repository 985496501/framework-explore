package cc.jinyun.contract.external;

import java.util.List;
import java.util.Map;

public interface TemplateDetail {
    String getTid();

    String getTitle();

    Map<String, String> getVariablesOfPlaceHolder();

    /**
     * 0: partA
     * 1: partB
     *
     * @return list
     */
    List<List<String>> getVariablesOfCharter();
}
