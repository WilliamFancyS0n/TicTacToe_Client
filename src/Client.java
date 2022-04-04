

import java.io.*;
import java.net.*;
import java.util.Scanner;

// client
public class Client {
    private static PrintWriter out;
    private static BufferedReader in;
    private static char[][] board;
    private static int row, col;


    public static void main(String[] args) {
        try {
            String hostName = "localhost";
            Socket toServerSocket = new Socket(hostName, 8787);
            DataInputStream instream = new DataInputStream(toServerSocket.getInputStream());
            DataOutputStream outstream = new DataOutputStream(toServerSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(instream));
            out = new PrintWriter(outstream, true);

        } catch (IOException e) {
            e.printStackTrace();
        }

        board = new char[4][4];
        for (int x = 0; x <= 3; x++)
            for (int y = 0; y <= 3; y++)
                board[x][y] = ' ';
        row = -1;
        col = -1;

        try {
            playgame(in, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void playgame(BufferedReader in, PrintWriter out) throws IOException {
        final Scanner inp = new Scanner(System.in);
        String response;
        boolean turn = false;
        boolean gameover = false;

        while (!gameover) {
            if (turn) { // if it's my turn
                do {
                    System.out.print("\nEnter Row : ");
                    row = inp.nextInt();
                    System.out.print("Enter Column : ");
                    col = inp.nextInt();
                } while (row < 0 || row > 3 || col > 3 || col < 0 || board[row][col] != ' '); // ensure a legal move

                board[row][col] = 'O';
                out.println("MOVE " + row + " " + col);
            }
            else { // servers trn
                response = in.readLine();
                if (!response.equals("CLIENT")) { // servers move
                    String [] args = response.split("\\s+"); // tokenize the string
                    if (args.length > 3) {
                        row = Integer.parseInt(args[1]);
                        col = Integer.parseInt(args[2]);
                        if (!args[3].equals("WIN") && row != -1)
                            board[row][col] = 'X';
                        switch (args[3]) {
                            case "WIN" : {
                                System.out.println("\n\n Congratulations! You won!");
                                break;
                            }
                            case "TIE" : {
                                System.out.println("\nGame resulted in a tie!");
                                break;
                            }
                            case "LOSS" : {
                                System.out.print("\nSorry! You have lost the game!");
                                break;
                            }
                        }
                        gameover = true;
                    }
                    else {
                        row = Integer.parseInt(args[1]);
                        col = Integer.parseInt(args[2]);
                        board[row][col] = 'X';
                    }
                }
                else {
                    System.out.println("Your move first, Chief");
                }
            }
            printBoard();
            turn = !turn;
        }
        System.out.println("Final game board : ");
        printBoard();
    }

    public static void printBoard() { // prints the contents of our board
        System.out.println("\n\n\n");
        System.out.println("  0      1     2      3");
        System.out.println("      |     |     |");
        for (int i = 0; i < 4; i++) {
            System.out.println("   " + board[i][0] + "  |  " + board[i][1] + "  |  " + board[i][2] + "  |  " + board[i][3] + "       " + i);
            if (i != 3)
                System.out.println("______|_____|_____|______");
            else
                System.out.println("      |     |     |");
        }
    }
}
