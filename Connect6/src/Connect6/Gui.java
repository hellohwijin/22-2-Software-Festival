package Connect6;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Gui extends JFrame {
    static JPanel backGround; // 배경 그려지고 돌 놓아질 그곳
    static JPanel showTurn; // 배경색으로 차례 표시
    static JLabel Info; // 메세지 표시
    static JLabel turnInfo; // 차례 표시하는 label
    static JButton reset; // reset button

    private int width;
    private int height;

    static int x, y; // 현재좌표

    // 육목 판 만들기, 기본세팅
    Gui() {
        width = 900;
        height = 900;

        // 기본껍데기
        setSize(width, height); // 프레임의 사이즈 설정
        setResizable(false);// 사용자가 임의로 프레임의 크기를 변경시킬 수 있는가>> 앙대
        setLocationRelativeTo(null);// 화면의 어느 위치에서 첫 등장할지>> null이면 자동 센터지정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창닫으면 프로그램종료
        setLayout(null); // 레이아웃설정

        // 백그라운드 패널(여기다 돌 둘거임)
        backGround = new JPanel();
        backGround.setBounds(0, 0, width, height);
        backGround.setBackground(new Color(200, 160, 100));
        backGround.setLayout(null);
        add(backGround); // 보여랏

        // 현재 차례 표시할 어쩌구
        Info = new JLabel("[System] 육목 게임을 시작합니다.");
        Info.setBounds(20, 0, 500, 30);
        backGround.add(Info);

        // 현재 차례 표시할 어쩌구
        turnInfo = new JLabel("당신의 색");
        turnInfo.setBounds(770, 50, 200, 50);
        backGround.add(turnInfo);

        showTurn = new JPanel();
        showTurn.setBounds(770, 90, 70, 70);
        showTurn.setBackground(Color.black);
        backGround.add(showTurn);

        // 다시하기
        reset = new JButton("새로운 게임");
        reset.setBounds(760, 180, 100, 45);
        reset.setBackground(new Color(225, 215, 200));
        backGround.add(reset);

        // add action
        reset_action();
        backGround_action();

        setVisible(true); // 쨘
        PlayBoard.startGame();
    }

    void reset_action() {
        reset.addActionListener(event -> {
            repaint(); // 게임판 리셋하고
            Info.setText("[System] 새로운 게임을 시작합니다.");
            PlayBoard.countJoong = 0;
            for (int i = 0; i < 19; i++)
                for (int j = 0; j < 19; j++)
                    Alphago.weight[i][j] = 0;

            PlayBoard.startGame(); // 새 게임 시작
        });
    }

    void backGround_action() {
        backGround.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (PlayBoard.on) { // 게임중일때만 작동하도록
                    // 이상적인 위치선정(공식 만들어 사용함...)
                    double n = ((float) e.getX() - (float) 44) / (float) 40;
                    if (n < 0.05) {
                        x = 0;
                    } else {
                        x = (int) n + 1;
                    }
                    n = ((float) e.getY() - (float) 80) / (float) 40;
                    if (n < 0.05) {
                        y = 0;
                    } else {
                        y = (int) n + 1;
                    }

                    if (PlayBoard.countJoong < PlayBoard.num) {

                        PlayBoard.count = -1; // 회색으로 염색시킨 중립구를
                        PlayBoard.putStone(x, y);
                        Alphago.addWeight(x, y);
                        Alphago.showWeight();
                        if (PlayBoard.countJoong == PlayBoard.num - 1) {
                            Info.setText("[System] 중립구 배치 완료. 게임을 시작합니다.");
                            if (PlayBoard.UserC == 2) { // 유저가 백돌 선택했다면 흑돌 하나 미리 놓여지게
                                PlayBoard.count = 1; // 회색으로 염색시킨 중립구를
                                PlayBoard.putStone(9, 9);
                                Alphago.addWeight(9, 9);
                                Alphago.showWeight();
                            }
                        }

                        else
                            Info.setText("[System] " + (PlayBoard.num - PlayBoard.countJoong - 1) + "개의 중립구를 배치해주세요");

                        PlayBoard.countJoong++;

                    } else if (PlayBoard.countJoong == PlayBoard.num) { // 마지막 중립구 넣을 떄
                        PlayBoard.count = PlayBoard.UserC; // 다음놓일 돌의 종류,갯수 조절
                        PlayBoard.setStone(); // 클릭위치에 중립구 두고
                        PlayBoard.countJoong++;

                    } else {
                        PlayBoard.setStone();
                    }

                }

            }
        });
    }

    // 자동실행, 바둑판 그리기
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        for (int i = 0; i < 19; i++) {

            g.drawLine(30 + i * 40, 90, 30 + i * 40, 810);
            g.drawLine(30, 20 + i * 40 + 70, 750, 20 + i * 40 + 70);

            // 좌표라벨
            g.drawString(i + 1 + "", 25 + i * 40, 80); // 가로
            g.drawString(i + 1 + "", 13, 22 + i * 40 + 72); // 세로

            // 점
            if ((i == 3 || i == 9) || i == 15) {
                g.fillOval(30 + i * 40 - 3, 20 + 3 * 40 + 70 - 3, 6, 6);
                g.fillOval(30 + i * 40 - 3, 20 + 9 * 40 + 70 - 3, 6, 6);
                g.fillOval(30 + i * 40 - 3, 20 + 15 * 40 + 70 - 3, 6, 6);
            }

        }
    }
}
