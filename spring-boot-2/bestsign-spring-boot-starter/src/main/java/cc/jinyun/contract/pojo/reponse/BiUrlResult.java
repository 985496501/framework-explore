package cc.jinyun.contract.pojo.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BiUrlResult {
    /**
     * 24h timeout
     */
    private String shortUrl;

    private String longUrl;
}
