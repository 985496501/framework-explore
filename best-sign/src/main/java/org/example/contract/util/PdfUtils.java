package org.example.contract.util;


import cn.hutool.json.JSONObject;
import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.*;
import org.example.contract.pojo.other.CharPosition;
import org.example.contract.pojo.other.PdfPageContentPositions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PdfUtils {

    public static List<PdfPageContentPositions> getPdfContentPositionsList(byte[] pdfData) throws IOException {
        PdfReader reader = new PdfReader(pdfData);

        List<PdfPageContentPositions> result = new ArrayList<>();

        int pages = reader.getNumberOfPages();
        for (int pageNum = 1; pageNum <= pages; pageNum++) {
            float width = reader.getPageSize(pageNum).getWidth();
            float height = reader.getPageSize(pageNum).getHeight();
            PdfRenderListener pdfRenderListener = new PdfRenderListener(pageNum, width, height);
            PdfContentStreamProcessor processor = new PdfContentStreamProcessor(pdfRenderListener);
            PdfDictionary pageDic = reader.getPageN(pageNum);
            PdfDictionary resourcesDic = pageDic.getAsDict(PdfName.RESOURCES);
            try {
                processor.processContent(ContentByteUtils.getContentBytesForPage(reader, pageNum), resourcesDic);
            }
            catch (IOException e) {
                reader.close();
                throw e;
            }

            String content = pdfRenderListener.getContent();
            List<CharPosition> charPositions = pdfRenderListener.getcharPositions();

            PdfPageContentPositions pdfPageContentPositions = new PdfPageContentPositions();
            pdfPageContentPositions.setContent(content);
            pdfPageContentPositions.setPositions(charPositions);

            result.add(pdfPageContentPositions);
        }
        reader.close();
        return result;
    }

    public static List<CharPosition> findPositions(String keyword, PdfPageContentPositions pdfPageContentPositions) {

        List<CharPosition> result = new ArrayList<>();

        String content = pdfPageContentPositions.getContent();
        List<CharPosition> charPositions = pdfPageContentPositions.getPositions();

        for (int pos = 0; pos < content.length();) {
            int positionIndex = content.indexOf(keyword, pos);
            if (positionIndex == -1) {
                break;
            }
            //查找keyword的坐标，需要结合keyword的第一个字符和最后一个字符计算
            char[] keywordChars = keyword.toCharArray();
            CharPosition startCharPos = charPositions.get(positionIndex);
            CharPosition endCharPos = charPositions.get(positionIndex + keywordChars.length -1);
            //左上位置取第一个字符的左上，右下位置取最后一个字符的右下
            CharPosition keywordPostion = new CharPosition(startCharPos.getPageWidth(), startCharPos.getPageHeight(), startCharPos.getPageNum(), startCharPos.getLt_x(), startCharPos.getLt_y(), endCharPos.getRb_x(), endCharPos.getRb_y());
            result.add(keywordPostion);
            pos = positionIndex + 1;
        }
        return result;
    }

    public static JSONObject getAlignPostion(int imageWidth, int imageHeight, String align, CharPosition charPosition) throws Exception{
        JSONObject pos = new JSONObject();
        pos.put("pageNum", String.valueOf(charPosition.getPageNum()));
        float lt_x = charPosition.getLt_x();
        float lt_y = charPosition.getLt_y();
        float rb_x = charPosition.getRb_x();
        float rb_y = charPosition.getRb_y();
        float x = 0;
        float y = 0;
        float pageWidth = charPosition.getPageWidth();
        float pageHeight = charPosition.getPageHeight();
        float image_scale_width = imageWidth/pageWidth;
        float image_scale_height = imageHeight/pageHeight;

        if("lt".equals(align)){
            //左上角
            //签名图片在关键字左上角，需要关键字的左上角偏移图片的位置
            x = lt_x - image_scale_width;
            y = lt_y - image_scale_height;
        }
        else if("lc".equals(align)){
            //左中
            float lc_x = lt_x;
            float lc_y = lt_y + (rb_y - lt_y)/2;
            x = lc_x - image_scale_width;
            y = lc_y - image_scale_height/2;
        }
        else if("lb".equals(align)){
            //左下
            float lb_x = lt_x;
            float lb_y = rb_y;
            x = lb_x - image_scale_width;
            y = lb_y;
        }
        else if("rt".equals(align)){
            //右上
            x = rb_x;
            y = lt_y - image_scale_height;
        }
        else if("rb".equals(align)){
            //右下
            x = rb_x;
            y = rb_y;
        }
        else if("rc".equals(align)){
            //右中
            float rc_x = rb_x;
            float rc_y = lt_y + (rb_y - lt_y)/2;
            x = rc_x;
            y = rc_y - image_scale_height/2;
        }
        else if("ct".equals(align)){
            //上中
            float ct_x = lt_x + (rb_x - lt_x)/2;
            float ct_y = lt_y;
            x = ct_x - image_scale_width/2;
            y = ct_y - image_scale_height;
        }
        else if("cb".equals(align)){
            //下中
            float cb_x = lt_x + (rb_x - lt_x)/2;
            float cb_y = rb_y;
            x = cb_x - image_scale_width/2;
            y = cb_y;
        }
        else if("cc".equals(align)){
            //正中
            float cc_x = lt_x + (rb_x - lt_x)/2;
            float cc_y = lt_y + (rb_y - lt_y)/2;
            x = cc_x - image_scale_width/2;
            y = cc_y - image_scale_height/2;
        }else{
            throw new RuntimeException("bad align");
        }
        if(x <= 0.0){
            x = 0.0f;
        }
        if(x > (1.0 - image_scale_width)){
            x = 1.0f - image_scale_width;
        }
        if(y <= 0.0){
            y = 0.0f;
        }
        if(y > (1.0 - image_scale_height)){
            y = 1.0f - image_scale_height;
        }
        pos.put("x", String.valueOf(x));
        pos.put("y", String.valueOf(y));
        return pos;
    }

    private static class PdfRenderListener implements RenderListener {
        private int pageNum;
        private float pageWidth;
        private float pageHeight;
        private StringBuilder contentBuilder = new StringBuilder();
        private List<CharPosition> charPositions = new ArrayList<>();

        public PdfRenderListener(int pageNum, float pageWidth, float pageHeight) {
            this.pageNum = pageNum;
            this.pageWidth = pageWidth;
            this.pageHeight = pageHeight;
        }

        @Override
        public void beginTextBlock() {

        }

        @Override
        public void renderText(TextRenderInfo renderInfo) {
            List<TextRenderInfo> characterRenderInfos = renderInfo.getCharacterRenderInfos();
            for (TextRenderInfo textRenderInfo : characterRenderInfos) {
                String word = textRenderInfo.getText();
                //System.out.println(word);
                if (word.length() > 1) {
                    // 把word里的字符读出来, ascii字符都保留放到sb; 其他字符需要判断一下: 如果sb里最后一个字符是ascii字符, 就放进去, 否则不放
                    StringBuilder sb = new StringBuilder();
                    char[] chs = new char[word.length()];
                    word.getChars(0, word.length(), chs, 0);
                    for (int i = chs.length - 1; i >= 0; i--) {
                        int nCode = (int)chs[i];
                        if (nCode >= 0 && nCode <= 255) {
                            sb.insert(0, chs[i]);
                        }
                        else {
                            if (sb.length() < 1) {
                                sb.append(chs[i]);
                            }
                            else {
                                char lstCh = sb.charAt(sb.length() - 1);
                                int lstCode = (int) lstCh;
                                if (lstCode <= 255) {
                                    sb.insert(0, chs[i]);
                                }
                            }
                        }
                    }
                    word = sb.toString();
                }

                LineSegment ascentLine = textRenderInfo.getAscentLine();
                Rectangle2D.Float rectangle = ascentLine.getBoundingRectange();

                Rectangle rec = new Rectangle(textRenderInfo.getBaseline().getStartPoint().get(0), textRenderInfo.getBaseline().getStartPoint().get(1), ascentLine.getEndPoint().get(0), ascentLine.getEndPoint().get(1));
                float fontHeight = rec.getHeight();

                //rectangle 以一页的右下角为原点，y的最大最小值相等
                double lt_x = rectangle.getMinX(); //关键字左上角x
                double lt_y = rectangle.getMaxY(); //关键字左上角y

                double rb_x = rectangle.getMaxX(); //关键字右下角x
                double rb_y = rectangle.getMaxY() - fontHeight; //关键字右下角y

                float lt_xPercent = Math.round(lt_x / pageWidth * 10000) / 10000f;
                float lt_yPercent = Math.round((1 - lt_y / pageHeight) * 10000) / 10000f;

                float rb_xPercent = Math.round(rb_x / pageWidth * 10000) / 10000f;
                float rb_yPercent = Math.round((1 - rb_y / pageHeight) * 10000) / 10000f;

                CharPosition charPosition = new CharPosition(this.pageWidth, this.pageHeight, pageNum, lt_xPercent, lt_yPercent, rb_xPercent, rb_yPercent);
                charPositions.add(charPosition);
                contentBuilder.append(word);
            }
        }

        @Override
        public void endTextBlock() {

        }

        @Override
        public void renderImage(ImageRenderInfo renderInfo) {

        }

        public String getContent() {
            return contentBuilder.toString();
        }

        public List<CharPosition> getcharPositions() {
            return charPositions;
        }
    }

    public static byte[] getFileData(final String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream s = new FileInputStream(file);
        byte[] result = new byte[(int) file.length()];
        try {
            s.read(result);
        }
        catch (IOException e) {
            throw e;
        }
        finally {
            try {
                s.close();
            }
            catch (IOException e) {
                ;
            }
        }
        return result;
    }
}
