package Connect6;

import java.awt.*;
import javax.swing.*;

public class Message {
    // 중립구 갯수 선택하기
    static void setBasicStone() {

        JFrame f = new JFrame();
        f.setSize(300, 150); // 프레임의 사이즈 설정
        f.setResizable(false);// 사용자가 임의로 프레임의 크기를 변경시킬 수 있는가>> 앙대
        f.setLocationRelativeTo(null);// 화면의 어느 위치에서 첫 등장할지>> null이면 자동 센터지정
        f.setLayout(null); // 레이아웃설정

        JLabel info = new JLabel("      중립구의 갯수를 입력하세요");
        info.setBounds(5, 5, 280, 30);
        f.add(info);

        JTextField getNum = new JTextField();
        getNum.setBounds(5, 40, 280, 30);
        f.add(getNum);

        JButton get = new JButton("confirm");
        get.setBounds(5, 77, 280, 30);
        get.setBackground(new Color(225, 215, 200));
        f.add(get);
        get.addActionListener(event -> {

            // int count = 0;

            try {
                // 입력받은 숫자로 num세팅하기
                PlayBoard.num = Integer.parseInt(getNum.getText());

                /*
                 * //입력받은 갯수만큼의 난수위치에 중립구 놓기 while(count < n) { x = (int)(Math.random()*18);
                 * //0~18범위의 난수 생성 y = (int)(Math.random()*18); //0~18범위의 난수 생성
                 * if(PlayBoard.playBoard[x][y] == 0){ //빈자리라면 PlayBoard.count = -1; //회색으로 염색시킨
                 * 중립구를 PlayBoard.putStone(); //난수위치에 둔다 count ++; } } PlayBoard.count = 1;
                 * //다음차례때 흑돌 하나로 시작하도록 세팅
                 */
                if (PlayBoard.num != 0)
                    Gui.Info.setText("[System] " + PlayBoard.num + "개의 중립구를 배치해주세요");
                f.dispose(); // 창닫기

            } catch (Exception e) {
                JFrame pop = new JFrame(); // 팝업창
                JOptionPane.showMessageDialog(pop, "정수값을 입력해주세요");
            }

        });

        f.setVisible(true);

    }

    // 진영 선택하기
    static void chooseColor() {

        JFrame f = new JFrame();
        f.setSize(300, 150); // 프레임의 사이즈 설정
        f.setResizable(false);// 사용자가 임의로 프레임의 크기를 변경시킬 수 있는가>> 앙대
        f.setLocationRelativeTo(null);// 화면의 어느 위치에서 첫 등장할지>> null이면 자동 센터지정
        f.setLayout(null); // 레이아웃설정

        JLabel info = new JLabel("      당신의 진영을 선택하세요");
        info.setBounds(5, 5, 280, 30);
        f.add(info);

        JCheckBox chooseBlcak = new JCheckBox("흑돌");
        chooseBlcak.setBounds(5, 40, 100, 30);
        f.add(chooseBlcak);
        JCheckBox chooseWhite = new JCheckBox("백돌");
        chooseWhite.setBounds(105, 40, 100, 30);
        f.add(chooseWhite);
        // 선택시 userC 변경되며 서로 상충되도록.
        chooseBlcak.addActionListener(event -> {
            chooseWhite.setSelected(false);
            PlayBoard.UserC = 1;
            PlayBoard.ComC = 2;
        });
        chooseWhite.addActionListener(event -> {
            chooseBlcak.setSelected(false);
            PlayBoard.UserC = 2;
            PlayBoard.ComC = 1;
        });

        JButton get = new JButton("confirm");
        get.setBounds(5, 77, 280, 30);
        get.setBackground(new Color(225, 215, 200));
        f.add(get);
        get.addActionListener(event -> {
            f.dispose();// 창닫고
            setBasicStone(); // 중립돌선택 화면으로
        });

        f.setVisible(true);

        // 게임세팅에도 넣어줘야함
    }

    // 승리메세지
    static void winPopUp() {
        Gui.Info.setText("[system] " + "게임 종료! (승자: " + ((PlayBoard.c == 1) ? "흑돌)" : "백돌)"));
        JFrame pop = new JFrame(); // 팝업창
        JOptionPane.showMessageDialog(pop, "게임 종료! (승자: " + ((PlayBoard.c == 1) ? "흑돌)" : "백돌)"));
        PlayBoard.on = false; // 게임끗
    }
}
