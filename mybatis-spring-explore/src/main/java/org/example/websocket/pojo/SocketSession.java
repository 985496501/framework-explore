package org.example.websocket.pojo;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: jinyun
 * @date: 2021/2/23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocketSession implements Serializable {
    public static final int SESSION_EXPIRE = 2;

    private String sessionId;

    private WebSocketSession webSocketSession;

    private Date endSessionTime;
    private Date lastSessionTime;

    public Date getEndSessionTime() {
        if (this.lastSessionTime != null) {
            endSessionTime = DateUtil.offset(lastSessionTime, DateField.MINUTE, SESSION_EXPIRE);
        }
        return endSessionTime;
    }
}
