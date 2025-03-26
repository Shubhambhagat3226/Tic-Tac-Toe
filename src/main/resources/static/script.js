let socket =  new WebSocket("ws://localhost:8080/game");
let turn = false;

socket.onopen = function(event) {
    console.log("connect to websocket server");
}

let currentPlayer;
function makeMove(cell) {
    // if(!turn) return;
    socket.send(cell);
    turn = false;
}

socket.onmessage = function(event) {
    console.log("Received:", event.data);
    let data = event.data;

    if (data === "true") {
        turn = true;
    } else if (data === "X" || data === "O" || data === "Draw") {
        gameOver(data);
    }
    else if (data.includes("|")) {
        updateBoard(data);
    } else if (data === "X turn" || data === "O turn" || data === "Your turn") {
        if (data === "Your turn") {
           turn = true;
        }
        document.getElementById('head').innerHTML = data;
        // alert(data); // Inform the players
    }
};

function updateBoard(data) {
    let rows = data.split("\n");
    let cells = document.querySelectorAll(".cell");

    rows.forEach((row, rowIndex) => {
        let cols = row.split("|");
        cols.forEach((col, colIndex) => {
            let index = rowIndex * 3 + colIndex;
            if (cols[colIndex] !== "null") {
                cells[index].textContent = cols[colIndex]; // Update UI
            }
        });
    });
}

function gameOver(player) {
    if (player === "Draw") {
        alert("It's a draw!");
    } else {
        alert("Game Over! " + player + " wins");
    }
    document.location.reload();
}