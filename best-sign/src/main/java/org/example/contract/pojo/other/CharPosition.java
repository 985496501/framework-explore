package org.example.contract.pojo.other;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CharPosition {
    private float pageWidth;
    private float pageHeight;
    private int pageNum = 0;
    private float lt_x = 0;
    private float lt_y = 0;
    private float rb_x = 0;
    private float rb_y = 0;
}
