package org.example.contract.pojo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPdfElements {
    private String fid;

    private String account;

    private List<Element> elements;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Element {
        private String pageNum;
        private String x;
        private String y;
        private String type;
        private String value;
    }
}

