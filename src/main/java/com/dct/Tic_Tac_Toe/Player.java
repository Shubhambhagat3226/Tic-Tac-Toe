package com.dct.Tic_Tac_Toe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Player {
    private String name;
    private WebSocketSession session;
    private String  symbol;
    private boolean turn;
}
