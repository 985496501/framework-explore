package cc.jinyun.contract.external;


import java.util.Map;

public interface BestSignContractTemplateService {
    TemplateDetail getContractDetail(String tid, Map<String, Object> extras);
}
