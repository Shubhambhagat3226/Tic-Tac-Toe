package com.dct.Tic_Tac_Toe;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class GameSocketHandler extends TextWebSocketHandler {

    private static Player playerCross;
    private static Player playerCircle;
    private static GameState gameState;
    private static String[][] board = new String[3][3];
    private static String winner;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("id connect" + session.getId());
        if (playerCross == null) {
            playerCross = Player
                    .builder()
                    .session(session)
                    .symbol("X")
                    .turn(false)
                    .build();
            gameState = GameState.START;

        } else if (playerCircle == null){
            playerCircle = Player
                    .builder()
                    .session(session)
                    .turn(false)
                    .symbol("O")
                    .build();
            playerCross.setTurn(true);
            gameState = GameState.RUNNING;
        } else {
            session.close();
        }
        game();
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

        System.out.println("1");
        String text =  message.getPayload().toString();
        String[] index = text.split(",");

        int row = Integer.parseInt(index[0].trim());
        int col = Integer.parseInt(index[1].trim());

        System.out.println("2");

        if (board[row][col] == null &&
                playerCross != null &&
                playerCircle != null) {
            System.out.println("tu");
            if (playerCross.isTurn() &&
                    session == playerCross.getSession()) {
                board[row][col] = playerCross.getSymbol();
                playerCross.setTurn(false);
                playerCircle.setTurn(true);
                playerCircle.getSession().sendMessage(new TextMessage("true"));
            } else if (playerCircle.isTurn() &&
                    session == playerCircle.getSession()) {
                board[row][col] = playerCircle.getSymbol();
                playerCircle.setTurn(false);
                playerCross.setTurn(true);
                playerCross.getSession().sendMessage(new TextMessage("true"));
            }

            gameOver();
        }
        System.out.println("3");
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            b.append(String.join("|", board[i]));
            if (i < 2) {
                b.append("\n");
            }
        }
        System.out.println(b.toString());
        sendMessage(b.toString());


        game();
        System.out.println("Hrll");


    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {

        System.out.println("connect close: " + session.getId());
        if (playerCross != null && playerCross.getSession() == session) {
            playerCross = null;
        } else if (playerCircle != null && playerCircle.getSession() == session){
            playerCircle = null;
        }

        session.close();
    }

    private <T>  void printAllMessage(List<T> message) {
        for (T m : message) {
            System.out.println(m);
        }
    }


    private void game() throws IOException {
        System.out.println(gameState);
        switch (gameState) {
            case START -> {
                playerCross.getSession().sendMessage(new TextMessage("Wait for the 2nd player"));

            }
            case RUNNING -> {
                if (countNulls(board) == 0) {
                    winner = "Draw";
                    gameState = GameState.OVER;
                    game();
                }
                if (playerCross.isTurn()) {
                    playerCircle.getSession().sendMessage(new TextMessage("X turn"));
                    playerCross.getSession().sendMessage(new TextMessage("Your turn"));
                } else {
                    playerCross.getSession().sendMessage(new TextMessage("O turn"));
                    playerCircle.getSession().sendMessage(new TextMessage("Your turn"));
                }

            }
            case OVER -> {
                playerCross.setTurn(true);
                playerCircle.setTurn(false);
                gameState = GameState.START;
                board = new String[3][3];
                sendMessage(winner);
                playerCross.getSession().sendMessage(new TextMessage("Your turn"));
                playerCircle.getSession().sendMessage(new TextMessage("X turn"));
            }
        }
    }

    private void gameOver() {
        System.out.println("ob");

        for (int i = 0; i < 3; i++) {
            if (board[i][0] != null &&
                    Objects.equals(board[i][1], board[i][0]) &&
                    Objects.equals(board[i][2], board[i][0])
            ) {
                System.out.println("Player " + board[i][0] + " is win");
                gameState = GameState.OVER;
                winner = board[i][0];
            }
        }
        System.out.println("Oc");
        for (int i = 0; i < 3; i++) {
            if (board[0][i] != null &&
                    Objects.equals(board[1][i], board[0][i]) &&
                    Objects.equals(board[2][i], board[0][i])
            ) {
                gameState = GameState.OVER;
                System.out.println("Player " + board[0][i] + " is win");
                winner = board[0][i];
            }
        }
        System.out.println("Oc");
        if (board[0][0] != null &&
                Objects.equals(board[1][1], board[0][0]) &&
                Objects.equals(board[2][2], board[0][0])
        ) {
            gameState = GameState.OVER;
            System.out.println("Player " + board[0][0] + " is win");
            winner = board[0][0];
        }
        System.out.println("Oc");
        if (board[0][2] != null &&
                Objects.equals(board[1][1], board[0][2]) &&
                Objects.equals(board[2][0], board[0][2])
        ) {
            gameState = GameState.OVER;
            System.out.println("Player " + board[0][2] + " is win");
            winner = board[0][2];
        }

        System.out.println("oa");
    }

    private void sendMessage(String message) throws IOException {
        if (playerCross != null && playerCross.getSession().isOpen()) playerCross.getSession().sendMessage(new TextMessage(message));
        if (playerCircle != null && playerCircle.getSession().isOpen()) playerCircle.getSession().sendMessage(new TextMessage(message));

    }

    public static int countNulls(String[][] array) {
        int count = 0;
        for (String[] row : array) {
            for (String element : row) {
                if (element == null) {
                    count++;
                }
            }
        }
        return count;
    }

}
