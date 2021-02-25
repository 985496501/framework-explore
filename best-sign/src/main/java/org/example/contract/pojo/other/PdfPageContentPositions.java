package org.example.contract.pojo.other;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PdfPageContentPositions {
    private String content;
    private List<CharPosition> positions;
}
