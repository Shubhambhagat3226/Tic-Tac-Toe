package com.dct.Tic_Tac_Toe;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class GameSocketHandler extends TextWebSocketHandler {

    private static Player playerCross;
    private static Player playerCircle;
    private static GameState gameState;
    private static String[][] board = new String[3][3];
    private static String winnerName;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        if (playerCross == null) {
            playerCross = Player
                    .builder()
                    .session(session)
                    .symbol("X")
                    .turn(false)
                    .build();
            gameState = GameState.START;
            setBoardDataEmpty();
        } else if (playerCircle == null){
            playerCircle = Player
                    .builder()
                    .session(session)
                    .turn(false)
                    .symbol("O")
                    .build();
            playerCross.setTurn(true);
            gameState = GameState.RUNNING;
            sendMessage("turn:" + playerCross.getName());
        }
        updateGameState();
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

        String text =  message.getPayload().toString();

        if (text == null || text.isBlank()) {
            System.err.println("Warning: Attempting to send a null message.");
            return;
        }

        if (text.contains(",")) {
            String[] index = text.split(",");
            int row = Integer.parseInt(index[0].trim());
            int col = Integer.parseInt(index[1].trim());

            if (board[row][col] == null &&
                    playerCross != null &&
                    playerCircle != null) {

                if (playerCross.isTurn() &&
                        session == playerCross.getSession()) {
                    board[row][col] = playerCross.getSymbol();
                    playerCross.setTurn(false);
                    playerCircle.setTurn(true);
                } else if (playerCircle.isTurn() &&
                        session == playerCircle.getSession()) {
                    board[row][col] = playerCircle.getSymbol();
                    playerCircle.setTurn(false);
                    playerCross.setTurn(true);
                }

                // board data send
                sendMessage(getBoardData());
                gameOver();
            }
        } else if (text.startsWith("JOIN|")) {
            String name = text.substring(5).trim();
            if (playerCross.getSession() != null && playerCross.getSession() == session) {
                playerCross.setName(name);
            } else if (playerCircle != null && session == playerCircle.getSession()) {
                playerCircle.setName(name);
            }
        }
        updateGameState();


    }

    private String getBoardData() {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            b.append(String.join("|", board[i]));
            if (i < 2) {
                b.append("\n");
            }
        }
        return b.toString();
    }

    private void setBoardDataEmpty() {
        for (int i = 0; i < board.length; i++) {
            Arrays.fill(board[i], null);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {

        System.out.println("connect close: " + session.getId());
        if (playerCross != null && playerCross.getSession() == session) {

            playerCross = null;
            resetGame();
        } else if (playerCircle != null && playerCircle.getSession() == session){
            playerCircle = null;
            resetGame();
        }

        session.close();
    }

    private void resetGame() {
        setBoardDataEmpty();
        gameState = GameState.START;
        winnerName = null;
    }


    private void updateGameState() throws IOException {
        System.out.println(gameState);
        switch (gameState) {
            case START -> {
                if (playerCross != null && playerCross.getSession().isOpen())
                    playerCross.getSession().sendMessage(new TextMessage("Wait for the 2nd player"));

                sendMessage(getBoardData());
            }
            case RUNNING -> {
                if (countNulls(board) == 0 && winnerName == null) {
                    winnerName = "Draw";
                    gameState = GameState.OVER;
                    updateGameState();
                } else {
                    String playerNameOfTurn = "turn:" + (playerCross.isTurn() ? playerCross.getName() : playerCircle.getName());
                    sendMessage(playerNameOfTurn);
                }

            }
            case OVER -> {
                sendMessage(winnerName);
                resetGame();

                gameState = GameState.RUNNING;
                playerCross.setTurn(true);
                playerCircle.setTurn(false);
                sendMessage(getBoardData());

                updateGameState();
            }
        }
    }

    private void gameOver() {

        for (int i = 0; i < 3; i++) {
            if (board[i][0] != null &&
                    Objects.equals(board[i][1], board[i][0]) &&
                    Objects.equals(board[i][2], board[i][0])
            ) {
                gameState = GameState.OVER;
                setWinnerName(board[i][0]);
                return;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (board[0][i] != null &&
                    Objects.equals(board[1][i], board[0][i]) &&
                    Objects.equals(board[2][i], board[0][i])
            ) {
                gameState = GameState.OVER;
                setWinnerName(board[0][i]);
                return;
            }
        }

        if (board[0][0] != null &&
                Objects.equals(board[1][1], board[0][0]) &&
                Objects.equals(board[2][2], board[0][0])
        ) {
            gameState = GameState.OVER;
            setWinnerName(board[0][0]);
            return;
        }

        if (board[0][2] != null &&
                Objects.equals(board[1][1], board[0][2]) &&
                Objects.equals(board[2][0], board[0][2])
        ) {
            gameState = GameState.OVER;
            setWinnerName(board[0][2]);
        }

    }

    private void setWinnerName(String symbol) {
        winnerName = "win:";
        if (Objects.equals(playerCross.getSymbol(), symbol)) {
            winnerName = winnerName + playerCross.getName();
        } else if (Objects.equals(playerCircle.getSymbol(), symbol)) {
            winnerName = winnerName + playerCircle.getName();
        }
    }

    private void sendMessage(String message) throws IOException {
        if (message == null) {
            System.out.println("Message is null!!");
            return;
        }

        System.out.println(winnerName);
        if (playerCross != null && playerCross.getSession().isOpen()) playerCross.getSession().sendMessage(new TextMessage(message));
        if (playerCircle != null && playerCircle.getSession().isOpen()) playerCircle.getSession().sendMessage(new TextMessage(message));

    }

    public static int countNulls(String[][] array) {
        int count = 0;
        for (String[] row : array) {
            for (String element : row) {
                if (element == null || element.isBlank()) {
                    count++;
                }
            }
        }
        return count;
    }

}
