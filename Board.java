package com.company;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

public class Board extends JPanel implements KeyListener,ActionListener{

    private final int WIDTH = 200;
    private final int HEIGHT = 200;

    private int x;
    private int y;

    private int gegner_x;
    private int gegner_y;
    private int richtung_x;
    private int richtung_y;

    char[][] Spielfeld = new char[WIDTH][HEIGHT];

    boolean newline;
    boolean gameover;

    int progress;

    private int difficulty = 20;

    Deque<Character> Stack = new LinkedList<>();

    Timer timer = null;

    int richtung_Spieler_x;
    int richtung_Spieler_y;

    float r = (float)Math.random();
    float g = (float)Math.random();
    float b = (float)Math.random();

    Character randomColour;

    public Board() {

        super();

        setBackground(Color.WHITE);

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setDoubleBuffered(true);
        addKeyListener(this);
        setFocusable(true);
        reset();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //draws the board with 1 Pixel small Rectangles

        for(int i = 0; i < Spielfeld.length;i++){
            for(int j = 0; j < Spielfeld[i].length;j++){

                if(Spielfeld[i][j] == 'R')g2d.setColor(Color.RED);
                else if(Spielfeld[i][j] == 'G')g2d.setColor(Color.YELLOW);
                else if(Spielfeld[i][j] == 'B')g2d.setColor(Color.BLUE);
                else if(Spielfeld[i][j] == 'S' || Spielfeld[i][j] == 'N')g2d.setColor(Color.BLACK);
                else if(Spielfeld[i][j] == 'H')g2d.setColor(Color.LIGHT_GRAY);
                if(Spielfeld[i][j] != '.') g2d.fillRect(i, j, 1, 1);
            }
        }
        //Player
        g2d.setColor(Color.pink);
        g2d.fillOval(x - 4, y - 4, 8, 8);

        //Enemy
        g2d.setColor(new Color(r, this.g, b,r));
        g2d.fillOval(gegner_x - 5, gegner_y - 5, 12, 12);

        //Progressbar
        g2d.setColor(Color.GREEN);
        g2d.drawString(String.valueOf(progress) + "%", 170, 195);

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(!gameover) {
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                if (x < WIDTH - 3) {
                    if(richtung_Spieler_x == -1) richtung_Spieler_x = 0;
                    if(richtung_Spieler_x == 0) richtung_Spieler_x = 1;
                    if(richtung_Spieler_y == 1 || richtung_Spieler_y == -1) richtung_Spieler_y = 0;
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                if (x > 0) {
                    if(richtung_Spieler_x == 1) richtung_Spieler_x = 0;
                    if(richtung_Spieler_x == 0) richtung_Spieler_x = -1;
                    if(richtung_Spieler_y == 1 || richtung_Spieler_y == -1) richtung_Spieler_y = 0;
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                if (y > 0) {
                    if(richtung_Spieler_y == 1) richtung_Spieler_y = 0;
                    if(richtung_Spieler_y == 0) richtung_Spieler_y = -1;
                    if(richtung_Spieler_x == 1 || richtung_Spieler_x == -1) richtung_Spieler_x = 0;
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                if (y < HEIGHT - 3) {
                    if(richtung_Spieler_y == -1) richtung_Spieler_y = 0;
                    if(richtung_Spieler_y == 0) richtung_Spieler_y = 1;
                    if(richtung_Spieler_x == 1 || richtung_Spieler_x == -1) richtung_Spieler_x = 0;
                }

            }
            if(e.getKeyCode() == KeyEvent.VK_SPACE){
                if(timer.isRunning())timer.stop();
                else timer.restart();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public char[][] flaechefuellen(char[][] spielfeld,int x, int y,int versuch) {

        /*
        Der Linie entlang laufen bis eine Stelle gefunden wird an der zwei gegenüberliegende Flächen gefunden wurden die gefüllt werden können.
         */
        char[][]Spielfeld = arraycopy(spielfeld);
        if(versuch == 1) {
             do {
                 char c = Stack.pop();
                 if (c == 'r') x--;
                 else if (c == 'l') x++;
                 else if (c == 'u') y++;
                 else if (c == 'd') y--;
             }
             while (((Spielfeld[x + 1][y] != '.' || Spielfeld[x - 1][y] != '.') && (Spielfeld[x][y + 1] != '.' || Spielfeld[x][y - 1] != '.')) && !Stack.isEmpty());
         }

        for (int i = 0; i < Spielfeld.length; i++) {
            for (int j = 0; j < Spielfeld[i].length; j++) {
                if (Spielfeld[i][j] == 'N') Spielfeld[i][j] = randomColour;
            }
        }
        /*
        Falls es keine Fläche zum Füllen gibt weiteren Algorithmus nicht ausführen
         */
        if(Stack.isEmpty())return Spielfeld;

        Deque<Point> Stack = new LinkedList<>();

        Point Seed = null;


        if (Spielfeld[x + 1][y] == '.' && versuch < 2) Seed = new Point(x + 1, y);
        else if (Spielfeld[x - 1][y] == '.' && versuch < 3) Seed = new Point(x - 1, y);
        else if (Spielfeld[x][y + 1] == '.' && versuch < 4) Seed = new Point(x, y + 1);
        else if (Spielfeld[x][y - 1] == '.') Seed = new Point(x, y - 1);
        while (Spielfeld[Seed.x - 1][Seed.y] == '.') Seed.translate(-1, 0);

        Stack.push(Seed);

        /*
        Span-Floodfill algorithmus aus VC
        Idee: Rekursionstiefe nicht zu tief werden lassen dadruch das man ganze Zeilen füllt;
        Für eine ganze Zeile wird jeweil nur der linkeste Pixel auf den Stack gelegt, dann wird beim füllen jedes Pixels
        überprüft ob es darunter oder darüber noch Pixel gibt die man noch füllen muss
         */
        boolean gegnerFound = false;
        while (!Stack.isEmpty() && !gegnerFound) {
            Point pixel = Stack.pop();
            while (Spielfeld[pixel.x][pixel.y] == '.') {
                if (pixel.x == gegner_x && pixel.y == gegner_y) gegnerFound = true;
                Spielfeld[pixel.x][pixel.y] = randomColour;
                //nach unten schauen
                if (Spielfeld[pixel.x][pixel.y + 1] == '.') {
                    Point help = new Point(pixel.x, pixel.y + 1);
                    while (Spielfeld[help.x - 1][help.y] == '.') help.translate(-1, 0);
                    if (!Stack.isEmpty()) {
                        Point last = Stack.peek();
                        if (!last.equals(help)) Stack.push(help);
                    } else Stack.push(help);
                }
                // nach oben schauen
                if (Spielfeld[pixel.x][pixel.y - 1] == '.') {
                    Point help = new Point(pixel.x, pixel.y - 1);
                    while (Spielfeld[help.x - 1][help.y] == '.') help.translate(-1, 0);
                    if (!Stack.isEmpty()) {
                        Point last = Stack.peek();
                        if (!last.equals(help)) Stack.push(help);
                    } else Stack.push(help);
                }
                pixel.translate(+1, 0);
            }
        }
        if (gegnerFound) {
            return flaechefuellen(arraycopy(spielfeld), x, y, versuch + 1);
        }else if (!this.Stack.isEmpty()) {
            return flaechefuellen(Spielfeld,x,y,1);
        }
        else return Spielfeld;
    }

    public char[][] arraycopy(char[][] tocopy){
        char[][] copied = new char[tocopy.length][tocopy[1].length];
        for(int i = 0; i < tocopy.length;i++){
            for(int j = 0; j < tocopy[i].length;j++){
                copied[i][j] = tocopy[i][j];
            }
        }
        return copied;
    }

    public void checkWin(){
        double Pixels = WIDTH*HEIGHT;
        double counter = 0;
        for(char[] ca: Spielfeld){
            for(char c : ca){
                if(c != '.')counter++;
            }
        }
        progress = (int)((counter/Pixels)*100);
        if(counter/Pixels > 0.8){
            repaint();
            gameover("GRATULATION! GEWONNEN!");
        }
    }
    public void deleteLoop(int x,int y){
        /*
        Achtung x und y sind nicht die globalen sondern die lokalen
         */
        do{
            char c = Stack.pop();
            if (c == 'r') x--;
            else if (c == 'l') x++;
            else if (c == 'u') y++;
            else if (c == 'd') y--;
            Spielfeld[x][y] = '.';
        }while ((x != this.x || y != this.y));
        Spielfeld[x][y] = 'N';
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /*  Gegner folgt dem Spieler
        if(x < gegner_x && Spielfeld[gegner_x-1][gegner_y] != 'A') gegner_x--;
        else if(x > gegner_x && Spielfeld[gegner_x+1][gegner_y] != 'A')gegner_x++;
        if(y < gegner_y && Spielfeld[gegner_x][gegner_y-1] != 'A') gegner_y--;
        else if(y > gegner_y && Spielfeld[gegner_x][gegner_y+1] != 'A')gegner_y++;
         */

        /*
        Gegner prallt immer in der entgegengesetzten Richtung von der er gekommen ist von der Wand ab
         */
        if(Spielfeld[gegner_x+richtung_x][gegner_y+richtung_y] == '.' || Spielfeld[gegner_x+richtung_x][gegner_y+richtung_y] == 'N'){
            gegner_x+=richtung_x;
            gegner_y+=richtung_y;
        }else {
            r = (float)Math.random();
            g = (float)Math.random();
            b = (float)Math.random();
            if(Spielfeld[gegner_x][gegner_y-1] != '.'||Spielfeld[gegner_x][gegner_y+1] != '.')richtung_y = -richtung_y;
            if(Spielfeld[gegner_x-1][gegner_y] != '.' || Spielfeld[gegner_x+1][gegner_y] != '.')richtung_x = -richtung_x;

        }
        /*
        Zufällige Richtungsänderung in die Richtung des Spielers
         */
        if(Math.random() < 0.01){
            if(x < gegner_x) richtung_x = -1;
            else richtung_x = 1;
            if(y < gegner_y) richtung_y = -1;
            else richtung_y = 1;

        }

        /*
        Spieler fährt automatisch in die gewählte Richtung
         */
        if (richtung_Spieler_x == 1 && x < WIDTH - 3) {
            x += richtung_Spieler_x;
            codeVereinfachung('r');
        }
        if (richtung_Spieler_x == -1 && x > 0) {
            x += richtung_Spieler_x;
            codeVereinfachung('l');
        }
        if (richtung_Spieler_y == -1 && y > 0) {
            y += richtung_Spieler_y;
            codeVereinfachung('u');
        }
        if (richtung_Spieler_y == 1 && y < HEIGHT - 3) {
            y += richtung_Spieler_y;
            codeVereinfachung('d');
        }

        if(Spielfeld[gegner_x][gegner_y] == 'N'|| gegner_x == x && gegner_y == y){
            gameover("GAME OVER!");
        }
        repaint();
    }

    public void reset(){

        x = 0;
        y = 0;

        gegner_x = WIDTH/2;
        gegner_y = HEIGHT/2;

        newline = false;
        gameover = false;

        progress = 0;

        Stack.clear();
        /*
         Prepare the Array: fill everything with dots except the border

         '.' means not visited yet
         'A' means already filled
         'N' means there is a newline but the Area isn't filled yet
          */
        createRandomColour();
        for(int i = 0; i < Spielfeld.length;i++){
            for(int j = 0; j < Spielfeld[i].length;j++){
                if(i < 5 ||i > 195 || j < 5 || j > 195) Spielfeld[i][j] = randomColour;
                else Spielfeld[i][j] = '.';
            }
        }

        // Timer is needed to move the Enemy
        /*
        Dieser Timer ruft alle 120 ms "this" auf; da this ein ActionListener implementiert wird dann immer die
        implementierte methode actionPerformed aufgerufen ( Es wird eigentlich nur alle 120 ms ein ActionEvent gefeuert).
         */
        timer = new Timer(difficulty,this);
        timer.start();

        gegner_x = 5 + (int)(Math.random()*191);
        gegner_y = 5 + (int)(Math.random()*191);

        richtung_x = (int)(Math.random()*3)-1;
        richtung_y = (int)(Math.random()*3)-1;
        if(richtung_x == 0 && richtung_y == 0) richtung_x++;

        richtung_Spieler_x = 0;
        richtung_Spieler_y = 0;


    }

    public void gameover (String Message){
        timer.stop();
        int answer = JOptionPane.showConfirmDialog(this,"Wollen Sie nochmal spielen?" , Message, JOptionPane.YES_NO_OPTION);
        if(answer == 0)reset();
        else System.exit(0);
    }

    public void setdifficulty(int difficulty){
        this.difficulty = difficulty;
        timer.setDelay(difficulty);
    }

    public void codeVereinfachung(Character c){

        if (Spielfeld[x][y] == '.') {
            Stack.push(c);
            Spielfeld[x][y] = 'N';
            newline = true;
        }
        else if(Spielfeld[x][y] == 'N'){
            Stack.push(c);
            deleteLoop(x,y);
        }
        else if (newline) {
            /*
            Oberes If-dient dazu herauszufinden ob der Spieler die eigene Linie wieder zurückgefahren ist, dann gibt es nämlich
            keine Fläche die ausgefüllt werden kann. Es muss dann nur der letzte Pixel noch auf unbesucht gesetzt werden.
             */
            if(Stack.size() == 1) {
                char hilfe = Stack.pop();
                if (hilfe == 'r') Spielfeld[x+1][y] = '.';
                else if (hilfe == 'l') Spielfeld[x-1][y] = '.';
                else if (hilfe == 'u') Spielfeld[x][y-1] = '.';
                else if (hilfe == 'd') Spielfeld[x][y + 1] = '.';
            }else{
                createRandomColour();
                Stack.push(c);
                Spielfeld = flaechefuellen(Spielfeld, x, y, 1);
                checkWin();
                Stack.clear();
            }
            newline = false;
        }

        if (x == gegner_x && y == gegner_y) {
            gameover("GAME OVER!");
        }
        repaint();
    }

    public void createRandomColour(){
        double randomNumber = Math.random();
        if(randomNumber < 0.2)randomColour = 'R';
        else if(randomNumber < 0.4)randomColour = 'G';
        else if(randomNumber < 0.6)randomColour = 'B';
        else if(randomNumber < 0.8)randomColour = 'S';
        else if(randomNumber < 1)randomColour = 'H';
    }
}
