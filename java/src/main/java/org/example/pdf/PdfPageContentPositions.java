package org.example.pdf;

import java.util.List;

/**
 * Created by xian jie on 17-5-5.
 */
public class PdfPageContentPositions {

    private String content;
    private List<CharPosition> positions;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<CharPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<CharPosition> positions) {
        this.positions = positions;
    }

}
