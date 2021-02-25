package org.example.pdf;

/**
 * Created by wanghf on 2018/1/16.
 */
public class CharPosition {
    private float pageWidth;
    private float pageHeight;
    private int pageNum = 0;
    private float lt_x = 0; //关键字左上角x坐标
    private float lt_y = 0; //关键字左上角y坐标
    private float rb_x = 0; //关键字右下角x坐标
    private float rb_y = 0; //关键字右下角y坐标

    public CharPosition(float pageWidth, float pageHeight, int pageNum, float lt_x, float lt_y, float rb_x, float rb_y) {
        this.pageNum = pageNum;
        this.lt_x = lt_x;
        this.lt_y = lt_y;
        this.rb_x = rb_x;
        this.rb_y = rb_y;
        this.pageWidth = pageWidth;
        this.pageHeight = pageHeight;
    }

    public float getLt_x() {
        return lt_x;
    }

    public void setLt_x(float lt_x) {
        this.lt_x = lt_x;
    }

    public float getLt_y() {
        return lt_y;
    }

    public void setLt_y(float lt_y) {
        this.lt_y = lt_y;
    }

    public float getRb_x() {
        return rb_x;
    }

    public void setRb_x(float rb_x) {
        this.rb_x = rb_x;
    }

    public float getRb_y() {
        return rb_y;
    }

    public void setRb_y(float rb_y) {
        this.rb_y = rb_y;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public float getPageWidth() {
        return pageWidth;
    }

    public void setPageWidth(float pageWidth) {
        this.pageWidth = pageWidth;
    }

    public float getPageHeight() {
        return pageHeight;
    }

    public void setPageHeight(float pageHeight) {
        this.pageHeight = pageHeight;
    }

    @Override
    public String toString() {
        return "[pageNum=" + this.pageNum + ",lt_x=" + this.lt_x + ",lt_y=" + this.lt_y + ",rb_x=" + this.rb_x + ",rb_y=" + this.rb_y + "]";
    }
}
