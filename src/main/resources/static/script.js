let socket =  new WebSocket("ws://localhost:8080/game");
let turn = false;
let playerName;

socket.addEventListener("open",function() {
    while (!playerName) {
        playerName = prompt("Enter your name:");
        if (playerName === null) {
            alert("You must enter a name to play.");
        }
    }

    if(playerName !== null) {
        document.getElementById('player').innerHTML = "Player: " + playerName;
    }

    socket.send("JOIN|" + playerName);
    console.log("connect to websocket server");
});



function makeMove(cell) {
    if(!turn) return;
    socket.send(cell);
    turn = false;
}

socket.onmessage = function(event) {
    console.log("Received:", event.data);
    let data = event.data;

    if (data.includes("win:")) {
        data = data.substring(4);
        gameOver(data);
    } else if (data.includes('turn:')) {
        let name = data.substring(5);
        if (data.substring(5) === playerName) {
            turn = true;
            name = "Your"
        } else {
            name = data.substring(5);
        }
        document.getElementById('head').innerHTML = name + " turn";
    }
    else if (data.includes("|")) {
        updateBoard(data);
    } else if (data === 'Draw') {
        gameOver(data);
    }
     else {
        console.log("Unknown message:", data);
    }
};

function drawColor() {
    const board = document.getElementById('board');
    const cells = board.children;

    // Check vertical wins
    for (let i = 0; i < 3; i++) {
        if (cells[i].innerText !== "" && 
            cells[i].innerText === cells[i + 3].innerText && 
            cells[i].innerText === cells[i + 6].innerText) {
            
            cells[i].style.color = "red";
            cells[i + 3].style.color = "red";
            cells[i + 6].style.color = "red";
            return;
        }
    }

    // Check horizontal wins
    for (let i = 0; i < 3; i++) {
        let row = i * 3;
        if (cells[row].innerText !== "" && 
            cells[row].innerText === cells[row + 1].innerText && 
            cells[row].innerText === cells[row + 2].innerText) {
            
            cells[row].style.color = "red";
            cells[row + 1].style.color = "red";
            cells[row + 2].style.color = "red";
            return;
        }
    }

    // Check diagonal wins
    if (cells[0].innerText !== "" && 
        cells[0].innerText === cells[4].innerText && 
        cells[0].innerText === cells[8].innerText) {
        
        cells[0].style.color = "red";
        cells[4].style.color = "red";
        cells[8].style.color = "red";
        return;
    }
    
    if (cells[2].innerText !== "" && 
        cells[2].innerText === cells[4].innerText && 
        cells[2].innerText === cells[6].innerText) {
        
        cells[2].style.color = "red";
        cells[4].style.color = "red";
        cells[6].style.color = "red";
        return;
    }
}


function updateBoard(data) {
    let rows = data.split("\n");
    let cells = document.querySelectorAll(".cell");

    rows.forEach((row, rowIndex) => {
        let cols = row.split("|");
        cols.forEach((col, colIndex) => {
            let index = rowIndex * 3 + colIndex;
            if (cols[colIndex] !== "null" ) {
                cells[index].textContent = cols[colIndex]; // Update UI
            }
        });
    });
}

function gameOver(player) {
    drawColor(); // Ensure the winning combination is highlighted first
    setTimeout(() => {
        if (player === "Draw") {
            alert("It's a draw!");
        } else {
            alert("Game Over! " + player + " wins");
        }

        setTimeout(resetGame, 500);
    }, 300); // Small delay to allow UI update
    
}

function resetGame() {
    document.getElementById('head').innerHTML = "Tic-Tac-Toe";
    document.querySelectorAll('.cell').forEach(cell => {
        cell.textContent = "";
        cell.style.color = "black";
    });
    alert("new game");
    socket.send("+");
}
