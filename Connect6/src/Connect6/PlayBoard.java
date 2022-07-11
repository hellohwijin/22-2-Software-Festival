package Connect6;

import java.awt.*;

public class PlayBoard {
    // 게임판
    static int visualBoard[][][];
    static int playBoard[][]; // 내부

    static int num; // 중립구 갯수!!!
    static int countJoong; // 중립구 count

    static int c; // 돌 색깔..! 1이면 백돌 2면 흑돌 5면 거시기 그거 중립구
    static int count = 0; // 몇번쨰 두는건지

    static boolean on = false; // 게임중인가?(돌이 놓아지는가에 대한 어쩌구)

    static int UserC = 1; // 유저가 선택한 컬러. 1 또는 2.
    static int ComC = 2;

    static Graphics g = Gui.backGround.getGraphics();

    PlayBoard() {
        countJoong = 0;
        playBoard = new int[19][19]; // 0-18의 가로세로공간 부여
        visualBoard = new int[19][19][2]; // 0-18의 가로세로공간,

        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                playBoard[i][j] = 0;
                visualBoard[i][j][0] = 24 + i * 40; // x좌표
                visualBoard[i][j][1] = 60 + j * 40; // y좌표

            }
        }
    }

    // 게임세팅
    static void startGame() {
        // 진영선택
        Message.chooseColor(); // 색상선택
        countJoong = 0;
        count = 0;

        // 중립구 놓기는 색 선택 끝나면 자동으로
        new PlayBoard();
        on = true; // 게임시쟉

    }

    // 돌 배치하기
    static void setStone() {
        if (Gui.x < 19 && Gui.y < 19) { // 범위 넘지 않는 선에서
            if (playBoard[Gui.x][Gui.y] == 0) {// 빈자리라면 돌 배치

                if (true) { // 원래 c!=5엿음
                    Gui.Info.setText("[system] (" + (Gui.x + 1) + ", " + (Gui.y + 1) + ") 에 "
                            + ((c == 1) ? "흑돌" : "백돌") + "을 배치했습니다\n");
                    putStone(Gui.x, Gui.y); // 돌 놓기
                    changeTurn(); // 현재차례 표시 바꾸기
                }

            } else {
                Gui.Info.setText("[system] 이미 놓여진 자리입니다.");
            }
        }
    }

    // 돌 넣는 메서드
    static void putStone(int x, int y) {

        if (count == -1) { // 회색
            g.setColor(Color.gray);
            c = 5;
        } else if (count < 2) { // 흑돌
            c = 1;
            g.setColor(Color.black);
        } else { // 백돌
            c = 2;
            g.setColor(Color.white);
        }

        g.fillOval(PlayBoard.visualBoard[x][y][0] - 20, PlayBoard.visualBoard[x][y][1] - 20, 40, 40);
        playBoard[x][y] = c; // 돌 넣기
        if (ScanBoard.scan(UserC)) { // 승리판정해서 게임 끝이라면
            Message.winPopUp(); // 팝업띄우기
            return;
        } else if (ScanBoard.scan(ComC)) { // 승리판정해서 게임 끝이라면
            Message.winPopUp(); // 팝업띄우기
            return;
        }

        if (UserC == c && count % 2 != 0) { // 방금 둔게 유저가 둔 수라면, 그리고 이게 두번째 두는거라면 (1, 3)
            Alphago.addWeight(Gui.x, Gui.y); // 유저가 둔 두번쨰 수 일반가중치 더하기

            count++;
            if (count > 3) {
                count = 0;
            }

            // 알파고가 수를 두기 직전에 현재 판의 특수가중치 계산하고 한번 보여줌 글고 수 두번 두기
            Alphago.addSuperWeight();
            Alphago.showWeight();

            Alphago.returnPoint(Alphago.weight);
            putStone(Alphago.x, Alphago.y); // 현재 산출되어있는 좌표에 돌 두기
            Alphago.addWeight(Alphago.x, Alphago.y); // 방금 둔거의 일반가중치 더하기

            if (!on) {
                return;
            }

            Alphago.returnPoint(Alphago.weight);
            putStone(Alphago.x, Alphago.y); // 두번
            Alphago.addWeight(Alphago.x, Alphago.y); // 방금 둔거의 일반가중치 더하기

        } else {
            if (UserC == c) {
                Alphago.addWeight(Gui.x, Gui.y);
                Alphago.showWeight();
            }

            count++;
            if (count > 3) {
                count = 0;
            }
        }

    }

    // 화면상 표시되는 패널색 바꾸기(인데 컴퓨터랑 할떄는 의미가 음슴)
    static void changeTurn() {

        if (count == -1) { // 회색
            // 블랙
        } else if (count < 2) { // 흑돌
            Gui.showTurn.setBackground(Color.black);
        } else { // 백돌
            Gui.showTurn.setBackground(Color.WHITE);
        }
    }

    // 인자로 받은 좌표위치 돌에 빨간 막 씌우는 메서드(승리구 표시용)
    static void markStone(int x, int y) {
        g.setColor(new Color(255, 0, 0, 80));
        PlayBoard.g.fillOval(PlayBoard.visualBoard[x][y][0] - 21, PlayBoard.visualBoard[x][y][1] - 21, 42, 42);
    }

}
