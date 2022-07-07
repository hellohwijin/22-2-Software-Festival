import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/*
 *����
 2021-01-03-11:00~ 2021-01-04 ??:??
 2021-winter ����5-1, ���� ����ȯ�� ����� 
 HGU �������ڰ��к�_ 22100579 ������
 with ������
 */

public class Main extends JFrame {
   private static final long serialVersionUID = 1L;

// �ڵ�����, �ٵ��� �׸���
   @Override // �׳�...����Ǵ³���
   public void paint(Graphics g) {
      super.paint(g);
      System.out.println("���ߴ���...");

      for (int i = 0; i < 19; i++) {

         g.drawLine(30 + i * 40, 90, 30 + i * 40, 810);
         g.drawLine(30, 20 + i * 40 + 70, 750, 20 + i * 40 + 70);

         // ��ǥ��
         g.drawString(i + 1 + "", 25 + i * 40, 80); // ����
         g.drawString(i + 1 + "", 13, 22 + i * 40 + 72); // ����

         // ��
         if ((i == 3 || i == 9) || i == 15) {
            g.fillOval(30 + i * 40 - 3, 20 + 3 * 40 + 70 - 3, 6, 6);
            g.fillOval(30 + i * 40 - 3, 20 + 9 * 40 + 70 - 3, 6, 6);
            g.fillOval(30 + i * 40 - 3, 20 + 15 * 40 + 70 - 3, 6, 6);
         }
      }
   }

   static JPanel backGround; // ��� �׷����� �� ������ �װ�
   static int x, y; // ������ǥ
   static Color COR; // ��
   static boolean on = false; // �������ΰ�?(���� �������°��� ���� ��¼��)
   static JPanel showTurn; // �������� ���� ǥ��
   static JLabel Info; // �޼��� ǥ��

   static int num; // �߸��� ����!!!
   static int countJoong = 0;

   static int UserC = 1; // ������ ������ �÷�. 1 �Ǵ� 2.
   static int ComC = 2;

   // ���� �� �����, �⺻����
   public Main() {

      int width = 900, height = 900;
      // �⺻������
      setSize(width, height); // �������� ������ ����
      setResizable(false);// ����ڰ� ���Ƿ� �������� ũ�⸦ �����ų �� �ִ°�>> �Ӵ�
      setLocationRelativeTo(null);// ȭ���� ��� ��ġ���� ù ��������>> null�̸� �ڵ� ��������
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // â������ ���α׷�����
      setLayout(null); // ���̾ƿ�����

      // ��׶��� �г�(����� �� �Ѱ���)
      backGround = new JPanel();
      backGround.setBounds(0, 0, width, height);
      backGround.setBackground(new Color(200, 160, 100));
      backGround.setLayout(null);
      add(backGround); // ������

      // ���� ���� ǥ���� ��¼��
      Info = new JLabel("[System] ���� ������ �����մϴ�.");
      Info.setBounds(20, 0, 500, 30);
      backGround.add(Info);

      // ���� ���� ǥ���� ��¼��
      JLabel turnInfo = new JLabel("����� ��");
      turnInfo.setBounds(770, 50, 200, 50);
      backGround.add(turnInfo);

      showTurn = new JPanel();
      showTurn.setBounds(770, 90, 70, 70);
      showTurn.setBackground(Color.black);
      backGround.add(showTurn);

      // �ٽ��ϱ�
      JButton reset = new JButton("���ο� ����");
      reset.setBounds(760, 180, 100, 45);
      reset.setBackground(new Color(225, 215, 200));
      backGround.add(reset);
      reset.addActionListener(event -> {
         repaint(); // ������ �����ϰ�
         Info.setText("[System] ���ο� ������ �����մϴ�.");
         countJoong = 0;
         for(int i = 0; i < 19; i++)
            for(int j = 0; j < 19; j++)
               alphago.weight[i][j] = 0;
         startGame(); // �� ���� ����
      });

      // ����~��~
      JLabel info = new JLabel("2021-winter | java | project5 | Leeejjju");
      info.setBounds(665, 0, 225, 30);
      backGround.add(info);

      setVisible(true); // º
      startGame();

      // Ŭ����
      backGround.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            if (on) { // �������϶��� �۵��ϵ���
               // �̻����� ��ġ����(���� ����� �����...)
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

               if (countJoong < num) {

                  PlayBoard.count = -1; // ȸ������ ������Ų �߸�����
                  PlayBoard.putStone(Main.x, Main.y);
                  alphago.addWeight(x, y);
                  alphago.showWeight();
                  if (countJoong == num - 1) {
                     Info.setText("[System] �߸��� ��ġ �Ϸ�. ������ �����մϴ�.");
                     if (UserC == 2) { // ������ �鵹 �����ߴٸ� �浹 �ϳ� �̸� ��������
                        PlayBoard.count = 1; // ȸ������ ������Ų �߸�����
                        PlayBoard.putStone(9, 9);
                        alphago.addWeight(9, 9);
                        alphago.showWeight();
                     }
                  }

                  else
                     Info.setText("[System] " + (num - countJoong - 1) + "���� �߸����� ��ġ���ּ���");
                  countJoong++;

               } else if (countJoong == num) { // ������ �߸��� ���� ��
                  PlayBoard.count = UserC; // �������� ���� ����,���� ����
                  PlayBoard.setStone(); // Ŭ����ġ�� �߸��� �ΰ�
                  countJoong++;

               } else {
                  PlayBoard.setStone();
               }

            }

         }
      });

   }

   // �����Լ�. ���� ������ ��
   public static void main(String[] args) {
      System.out.println("Hello World!");
      new Main();

   }

   // ���Ӽ���
   static void startGame() {

      // ��������
      chooseColor(); // ������
      countJoong = 0;
      PlayBoard.count = 0;
      // �߸��� ����� �� ���� ������ �ڵ�����
      new PlayBoard();
      on = true; // ���ӽ���

   }

   // ���� �����ϱ�
   static void chooseColor() {

      JFrame f = new JFrame();
      f.setSize(300, 150); // �������� ������ ����
      f.setResizable(false);// ����ڰ� ���Ƿ� �������� ũ�⸦ �����ų �� �ִ°�>> �Ӵ�
      f.setLocationRelativeTo(null);// ȭ���� ��� ��ġ���� ù ��������>> null�̸� �ڵ� ��������
      f.setLayout(null); // ���̾ƿ�����

      JLabel info = new JLabel("      ����� ������ �����ϼ���");
      info.setBounds(5, 5, 280, 30);
      f.add(info);

      JCheckBox chooseBlcak = new JCheckBox("�浹");
      chooseBlcak.setBounds(5, 40, 100, 30);
      f.add(chooseBlcak);
      JCheckBox chooseWhite = new JCheckBox("�鵹");
      chooseWhite.setBounds(105, 40, 100, 30);
      f.add(chooseWhite);
      // ���ý� userC ����Ǹ� ���� ����ǵ���.
      chooseBlcak.addActionListener(event -> {
         chooseWhite.setSelected(false);
         UserC = 1;
         ComC = 2;
      });
      chooseWhite.addActionListener(event -> {
         chooseBlcak.setSelected(false);
         UserC = 2;
         ComC = 1;
      });

      JButton get = new JButton("confirm");
      get.setBounds(5, 77, 280, 30);
      get.setBackground(new Color(225, 215, 200));
      f.add(get);
      get.addActionListener(event -> {
         f.dispose();// â�ݰ�
         setBasicStone(); // �߸������� ȭ������
      });

      f.setVisible(true);

      // ���Ӽ��ÿ��� �־������
   }

   // �߸��� ���� �����ϱ�
   static void setBasicStone() {

      JFrame f = new JFrame();
      f.setSize(300, 150); // �������� ������ ����
      f.setResizable(false);// ����ڰ� ���Ƿ� �������� ũ�⸦ �����ų �� �ִ°�>> �Ӵ�
      f.setLocationRelativeTo(null);// ȭ���� ��� ��ġ���� ù ��������>> null�̸� �ڵ� ��������
      f.setLayout(null); // ���̾ƿ�����

      JLabel info = new JLabel("      �߸����� ������ �Է��ϼ���");
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
            // �Է¹��� ���ڷ� num�����ϱ�
            num = Integer.parseInt(getNum.getText());

            /*
             * //�Է¹��� ������ŭ�� ������ġ�� �߸��� ���� while(count < n) { x = (int)(Math.random()*18);
             * //0~18������ ���� ���� y = (int)(Math.random()*18); //0~18������ ���� ����
             * if(PlayBoard.playBoard[x][y] == 0){ //���ڸ���� PlayBoard.count = -1; //ȸ������ ������Ų
             * �߸����� PlayBoard.putStone(); //������ġ�� �д� count ++; } } PlayBoard.count = 1;
             * //�������ʶ� �浹 �ϳ��� �����ϵ��� ����
             */
            if (num != 0)
               Info.setText("[System] " + num + "���� �߸����� ��ġ���ּ���");
            f.dispose(); // â�ݱ�

         } catch (Exception e) {
            JFrame pop = new JFrame(); // �˾�â
            JOptionPane.showMessageDialog(pop, "�������� �Է����ּ���");
         }

      });

      f.setVisible(true);

   }

   // �¸��޼���
   static void winPopUp() {
      Info.setText("[system] " + "���� ����! (����: " + ((PlayBoard.c == 1) ? "�浹)" : "�鵹)"));
      JFrame pop = new JFrame(); // �˾�â
      JOptionPane.showMessageDialog(pop, "���� ����! (����: " + ((PlayBoard.c == 1) ? "�浹)" : "�鵹)"));
      on = false; // ���Ӳ�
   }

}

//��.... �ð������� ���� ���� ��ġ��ǥ(�ȼ�����)�� ���������� �νĵ� ��ǥ(�迭 �ε���) ����
class PlayBoard {
   // ������
   static int visualBoard[][][];
   static int playBoard[][]; // ����

   PlayBoard() { // �ʱ⼳��.
      playBoard = new int[19][19]; // 0-18�� ���μ��ΰ��� �ο�
      visualBoard = new int[19][19][2]; // 0-18�� ���μ��ΰ���,

      for (int i = 0; i < 19; i++) {
         for (int j = 0; j < 19; j++) {
            playBoard[i][j] = 0;
            visualBoard[i][j][0] = 24 + i * 40; // x��ǥ
            visualBoard[i][j][1] = 60 + j * 40; // y��ǥ

         }
      }
   }

   static int c; // �� ����..! 1�̸� �鵹 2�� �浹 5�� �Žñ� �װ� �߸���
   static int count = 0; // ����� �δ°���

   static Graphics g = Main.backGround.getGraphics();

   // �� ��ġ�ϱ�
   static void setStone() {
      if (Main.x < 19 && Main.y < 19) { // ���� ���� �ʴ� ������
         if (playBoard[Main.x][Main.y] == 0) {// ���ڸ���� �� ��ġ

            if (true) { // ���� c!=5����
               Main.Info.setText("[system] (" + (Main.x + 1) + ", " + (Main.y + 1) + ") �� "
                     + ((c == 1) ? "�浹" : "�鵹") + "�� ��ġ�߽��ϴ�\n");
               putStone(Main.x, Main.y); // �� ����
               changeTurn(); // �������� ǥ�� �ٲٱ�
            }

         } else {
            Main.Info.setText("[system] �̹� ������ �ڸ��Դϴ�.");
         }
      }
   }

   // �� �ִ� �޼���
   static void putStone(int x, int y) {

      if (count == -1) { // ȸ��
         g.setColor(Color.gray);
         c = 5;
      } else if (count < 2) { // �浹
         c = 1;
         g.setColor(Color.black);
      } else { // �鵹
         c = 2;
         g.setColor(Color.white);
      }

      g.fillOval(PlayBoard.visualBoard[x][y][0] - 20, PlayBoard.visualBoard[x][y][1] - 20, 40, 40);
      playBoard[x][y] = c; // �� �ֱ�
      if (ScanBoard.scan(Main.UserC)) { // �¸������ؼ� ���� ���̶��
         Main.winPopUp(); // �˾�����
         return;
      }else if (ScanBoard.scan(Main.ComC)) { // �¸������ؼ� ���� ���̶��
         Main.winPopUp(); // �˾�����
         return;
      }

      if (Main.UserC == c && count % 2 != 0) { // ��� �а� ������ �� �����, �׸��� �̰� �ι�° �δ°Ŷ�� (1, 3)
    	  alphago.addWeight(Main.x, Main.y); //������ �� �ι��� �� �Ϲݰ���ġ ���ϱ� 

         count++;
         if (count > 3) {
            count = 0;
         }
         
         //���İ� ���� �α� ������ ���� ���� Ư������ġ ����ϰ� �ѹ� ������ �۰� �� �ι� �α�
         alphago.addSuperWeight();
         alphago.showWeight();
         
         alphago.returnPoint(alphago.weight);
         putStone(alphago.x, alphago.y); // ���� ����Ǿ��ִ� ��ǥ�� �� �α�
         alphago.addWeight(alphago.x, alphago.y); //��� �а��� �Ϲݰ���ġ ���ϱ�
         
         if(!Main.on) {
        	 return;
         }
         
         alphago.returnPoint(alphago.weight);
         putStone(alphago.x, alphago.y); // �ι�
         alphago.addWeight(alphago.x, alphago.y); //��� �а��� �Ϲݰ���ġ ���ϱ�
         


      } else {
         if (Main.UserC == c) {
            alphago.addWeight(Main.x, Main.y);
            alphago.showWeight();
         }

         count++;
         if (count > 3) {
            count = 0;
         }
      }

   }

   // ȭ��� ǥ�õǴ� �гλ� �ٲٱ�(�ε� ��ǻ�Ͷ� �ҋ��� �ǹ̰� ����)
   static void changeTurn() {

      if (count == -1) { // ȸ��
         // ��
      } else if (count < 2) { // �浹
         Main.showTurn.setBackground(Color.black);
      } else { // �鵹
         Main.showTurn.setBackground(Color.WHITE);
      }
   }

   // ���ڷ� ���� ��ǥ��ġ ���� ���� �� ����� �޼���(�¸��� ǥ�ÿ�)
   static void markStone(int x, int y) {
      g.setColor(new Color(255, 0, 0, 80));
      PlayBoard.g.fillOval(PlayBoard.visualBoard[x][y][0] - 21, PlayBoard.visualBoard[x][y][1] - 21, 42, 42);
   }

}

//�����ϴ³�...!! 
class ScanBoard {

   static int x;
   static int y;
   static boolean flag = false; // �¸����� �� ��ȯ��
   static int connect[] = { 1, 1, 1, 1 }; // 0���� 1���� 2��밢 3�´밢 ������ ����
   static int i; // ��...
   // ���� ���ڷ� ���� c�� �������� ã�� ������ connect�� �����Ѵ�.
   
   // ���� ������ ����(�Ʒ���/����)
   static void sero(int c) {
      i = 1;
      while (true) {
         if (y + i > 18)
            break; // �ε��� �Ѿ��ī�� ��ž
         // System.out.println(x+","+y+"="+playBoard[x][y+i] );
         if (PlayBoard.playBoard[x][y + i] == c) {
            if (flag) { // ���� �� ���¶�� �����׵Τ��� �����ֱ�
               PlayBoard.markStone(x, y + i);
            } else {
               connect[0]++; // ��ġ�� ī��Ʈ ����
            }
         } else
            break; // ����ġ�� ��ý�ž
         i++;
      }
      i = 1;
      while (true) {
         if (y - i < 0)
            break; // �ε��� �Ѿ��ī�� ��ž
         if (PlayBoard.playBoard[x][y - i] == c) {
            if (flag) { // ���� �� ���¶�� �����׵Τ��� �����ֱ�
               PlayBoard.markStone(x, y - i);
            } else {
               connect[0]++; // ��ġ�� ī��Ʈ ����
            }
         } else
            break; // ����ġ�� ��ý�ž
         i++;
      }
      // System.out.println("���ο�����"+connect[0]+"��");

   }

   // ���� ������ ����
   static void garo(int c) {
      i = 1;
      while (true) {
         if (x + i > 18)
            break; // �ε��� �Ѿ��ī�� ��ž
         // System.out.println(x+","+y+"="+playBoard[x][y+i] );
         if (PlayBoard.playBoard[x + i][y] == c) {
            if (flag) { // ���� �� ���¶�� �����׵Τ��� �����ֱ�
               PlayBoard.markStone(x + i, y);
            } else {
               connect[1]++; // ��ġ�� ī��Ʈ ����
            }
         } else
            break; // ����ġ�� ��ý�ž
         i++;
      }
      i = 1;
      while (true) {
         if (x - i < 0)
            break; // �ε��� �Ѿ��ī�� ��ž
         if (PlayBoard.playBoard[x - i][y] == c) {
            if (flag) { // ���� �� ���¶�� �����׵Τ��� �����ֱ�
               PlayBoard.markStone(x - i, y);
            } else {
               connect[1]++; // ��ġ�� ī��Ʈ ����
            }
         } else
            break; // ����ġ�� ��ý�ž
         i++;
      }
      // System.out.println("���ο�����"+connect[1]+"��");

   }

   // ��밢 ������ ����
   static void wo(int c) {
      i = 1;
      while (true) {
         if (y + i > 18 || x - i < 0)
            break; // �ε��� �Ѿ��ī�� ��ž
         // System.out.println(x+","+y+"="+playBoard[x][y+i] );
         if (PlayBoard.playBoard[x - i][y + i] == c) {
            if (flag) { // ���� �� ���¶�� �����׵Τ��� �����ֱ�
               PlayBoard.markStone(x - i, y + i);
            } else {
               connect[2]++; // ��ġ�� ī��Ʈ ����
            }
         } else
            break; // ����ġ�� ��ý�ž
         i++;
      }
      i = 1;
      while (true) {
         if (x + i > 18 || y - i < 0)
            break; // �ε��� �Ѿ��ī�� ��ž
         if (PlayBoard.playBoard[x + i][y - i] == c) {
            if (flag) { // ���� �� ���¶�� �����׵Τ��� �����ֱ�
               PlayBoard.markStone(x + i, y - i);
            } else {
               connect[2]++; // ��ġ�� ī��Ʈ ����
            }
         } else
            break; // ����ġ�� ��ý�ž
         i++;
      }
      // System.out.println("��밢������"+connect[2]+"��");

   }

   // �´밢 ������ ����
   static void jwa(int c) {
      i = 1;
      while (true) {
         if (y + i > 18 || x + i > 18)
            break; // �ε��� �Ѿ��ī�� ��ž
         // System.out.println(x+","+y+"="+playBoard[x][y+i] );
         if (PlayBoard.playBoard[x + i][y + i] == c) {
            if (flag) { // ���� �� ���¶�� �����׵Τ��� �����ֱ�
               PlayBoard.markStone(x + i, y + i);
            } else {
               connect[3]++; // ��ġ�� ī��Ʈ ����
            }
         } else
            break; // ����ġ�� ��ý�ž
         i++;
      }
      i = 1;
      while (true) {
         if (x - i < 0 || y - i < 0)
            break; // �ε��� �Ѿ��ī�� ��ž
         if (PlayBoard.playBoard[x - i][y - i] == c) {
            if (flag) { // ���� �� ���¶�� �����׵Τ��� �����ֱ�
               PlayBoard.markStone(x - i, y - i);
            } else {
               connect[3]++; // ��ġ�� ī��Ʈ ����
            }
         } else
            break; // ����ġ�� ��ý�ž
         i++;
      }
      // System.out.println("�´밢������"+connect[3]+"��");

   }

   // ������¥�� �������� �ִ��� ����
   static boolean scan(int c) {
      flag = false;
      // �������� x,y �޾ƿ���
      if (Main.UserC == c) {
         x = Main.x;
         y = Main.y;

      } else {
         x = alphago.x;
         y = alphago.y;
      }
      for (i = 0; i < 4; i++) { // �ʱ�ȭ
         connect[i] = 1;
      }
      // �ȹ����� ������..!
      sero(c);
      garo(c);
      wo(c);
      jwa(c);

      // �Ǻ�
      for (i = 0; i < 4; i++) {
         if (connect[i] == 6) {
            flag = true;
            System.out.println("�¸�");
            // ������ �����..!!
            PlayBoard.markStone(x, y);
            if (i == 0) { // ����..
               sero(c);
               return flag;
            } else if (i == 1) { // ����...
               garo(c);
               return flag;
            } else if (i == 2) {// ��밢..
               wo(c);
               return flag;
            } else { // �´밢..
               jwa(c);
               return flag;
            }
         }
      }
      return flag;
   }

}

//��ǻ�Ͱ� �ϴ� �� 
class alphago {

   // ���� ����� �̻��� ��ǥ
   static int x;
   static int y;
   // ����ġ ������ ���� �迭
   static int[][] weight = new int[19][19]; //�Ϲݰ���ġ(��� ����)
   static int[][] superWeight = new int[19][19]; //Ư������ġ(�� �м����� ������ ����)

   // ����ġ �⺻ �����ϱ� (�� ���� �Ŀ�)
   public static void addWeight(int x, int y) {
      int n = 1; //������ ����ġ�� ��. �� ������ ��� �������� ���� �޶���. 
      
      if (PlayBoard.playBoard[x][y] == 1) n = 2;
      else if (PlayBoard.playBoard[x][y] == 2) n = 1;
      else if (PlayBoard.playBoard[x][y] == 5) { //�߸��� ���
         weight[x][y] = -1;
         superWeight[x][y] = -1;
         return;
      } else  return; //�̹� �������� ��� 
      
      //�ȹ��� ������ �̹� ������ ���� �ƴ϶�� ����ġ ����. 
      for (int i = x - 1; i < x + 2; i++) {
         for (int j = y - 1; j < y + 2; j++) {
            if (i == x && j == y) { //�����ڸ����� -1 
               weight[x][y] = -1;
               superWeight[x][y] = -1;
            } else {
               try {
                  if (PlayBoard.playBoard[i][j] == 0) weight[i][j] += n;
               } catch (ArrayIndexOutOfBoundsException e) {} //�ε��� �Ѿ�� ���� 
            }
         }
      }
      
   }


   
   //�� �а� Ư������ġ �����ϱ� 
   public static void addSuperWeight() {
	   
	   //Ư������ġ �� �ʱ�ȭ 
	   for(int i = 0; i < 19; i++) {
		   for(int j = 0; j < 19; j++) {
			   superWeight[i][j] = 0;
		   }
	   }

	   int myCount;
	   int add = 0; //���� ����ġ ��� �����ߴ���(�ι� �Ѿ�� �׸�ã�� ����. ������ �� �Ͽ� �ι��ۿ� �� �δϱ�..)
	     
	   //// ������ �̱� ��(�ѹ�¸�) -------------------------------------------------------------------------------------
	      
	      if(add >= 2) return;
	      // 5 ���� ����
	      for (int i = 0; i < 19; i++) {
	         myCount = 0;
	         for (int j = 0; j < 19; j++) {
	            try {
	               if (PlayBoard.playBoard[i][j] == Main.ComC) {
	                  myCount++;
	                  if (myCount == 5) { // �糡 �� �ƹ� ����� ����ġ ��â �̺�Ʈ
	                	  if (j - 5 < 0 && PlayBoard.playBoard[i][j + 1] == 0) {
								superWeight[i][j + 1] += 40;
								add++;
	                	  }

	                	  else if (j + 1 > 18 && PlayBoard.playBoard[i][j - 5] == 0) {
								superWeight[i][j - 5] += 40;
								add++;
	                	  }
	                	  else if (PlayBoard.playBoard[i][j - 5] == 0) {
	                    	superWeight[i][j - 5] += 80;
	                        add++;
	                	  } else if (PlayBoard.playBoard[i][j + 1] == 0) {
	                    	superWeight[i][j + 1] += 80;
	                        add++;
	                     }
	                  }
	               } else myCount = 0;
	            } catch (ArrayIndexOutOfBoundsException e) {}
	         }
	      }
	      
	      
	      if(add >= 2) return;
	      // 4 ���� ����
	      for (int i = 0; i < 19; i++) {
	         myCount = 0;
	         for (int j = 0; j < 19; j++) {
	            try {
	               if (PlayBoard.playBoard[i][j] == Main.ComC) {
	                  myCount++;
	                  if (myCount == 4) {
	                	  if (j - 4 < 0 && PlayBoard.playBoard[i][j + 1] == 0 && PlayBoard.playBoard[i][j + 2] == 0) {
								superWeight[i][j + 1] += 40;
								superWeight[i][j + 2] += 40;
								add++;
							}

							else if (j + 1 > 18 && PlayBoard.playBoard[i][j - 4] == 0 && PlayBoard.playBoard[i][j - 5] == 0) {
								superWeight[i][j - 4] += 40;
								superWeight[i][j - 5] += 40;
								add++;
							}
							else if (PlayBoard.playBoard[i][j - 4] == 0 && PlayBoard.playBoard[i][j + 1] == 0) {
	                    	 superWeight[i][j - 4] += 80;
	                    	 superWeight[i][j + 1] += 80;
	                        add++;
	                        add++;
	                     } else if (PlayBoard.playBoard[i][j - 4] == 0 || PlayBoard.playBoard[i][j + 1] == 0) {
	                        if (PlayBoard.playBoard[i][j - 4] == 0 && PlayBoard.playBoard[i][j - 5] == 0) {
	                        	superWeight[i][j - 4] += 80;
	                        	superWeight[i][j - 5] += 80;
	                           add++;
	                           add++;
	                        } else if (PlayBoard.playBoard[i][j + 1] == 0 && PlayBoard.playBoard[i][j + 2] == 0) {
	                        	superWeight[i][j + 2] += 80;
	                        	superWeight[i][j + 1] += 80;
	                           add++;
	                           add++;
	                        }
	                     }
	                  }
	               } else myCount = 0;
	            } catch (ArrayIndexOutOfBoundsException e) {}
	         }
	      }
	      


	         if(add >= 2) return;
	         // 3���� 1 ���� ����
	         for (int i = 0; i < 19; i++) {
	            myCount = 0;
	            for (int j = 0; j < 19; j++) {
	               try {
	                  if (PlayBoard.playBoard[i][j] == Main.ComC) {
	                     myCount++;
	                     if (myCount == 3) { //���������϶� 
	                        //111010 
	                        if (PlayBoard.playBoard[i][j + 1] == 0 && PlayBoard.playBoard[i][j + 2] == Main.ComC && PlayBoard.playBoard[i][j + 3] == 0) {
	                           if(weight[i ][j+ 3] > weight[i ][j+ 1]) {
	                              superWeight[i][j + 3] += 40; add++;
	                           }else {
	                        	   superWeight[i][j + 1] += 40; add++;
	                           }
	                        } 
	                        //010111
	                        else if (PlayBoard.playBoard[i ][j- 3] == 0 && PlayBoard.playBoard[i ][j- 4] == Main.ComC && PlayBoard.playBoard[i ][j- 5] == 0) {
	                           if(weight[i ][j- 3]  > weight[i ][j- 5]) {
	                              superWeight[i ][j- 3] += 40; add++;}
	                           else {
	                              superWeight[i ][j- 5] += 40; add++;}
	                           }
	                     }
	                  } else myCount = 0;
	               } catch (ArrayIndexOutOfBoundsException e) {}
	            }
	         }
	         
	         if(add >= 2) return;
	         // 3���� 2 ���� ����
	         for (int i = 0; i < 19; i++) {
	            myCount = 0;
	            for (int j = 0; j < 19; j++) {
	               try {
	                  if (PlayBoard.playBoard[i][j] == Main.ComC) {
	                     myCount++;
	                     if (myCount == 3) { //���������϶� 
	                        //111010 
	                        if (PlayBoard.playBoard[i][j + 1] == 0 && PlayBoard.playBoard[i][j + 2] == 0 && PlayBoard.playBoard[i][j + 3] == Main.ComC) {
	                           if(weight[i ][j+ 2] > weight[i ][j+ 1]) {
	                              superWeight[i][j + 2] += 40; add++;
	                           }else {
	                        	   superWeight[i][j + 1] += 40; add++;
	                           }
	                        } 
	                        //010111
	                        else if (PlayBoard.playBoard[i ][j- 3] == 0 && PlayBoard.playBoard[i ][j- 4] == 0 && PlayBoard.playBoard[i ][j- 5] == Main.ComC) {
	                           if(weight[i ][j- 3]  > weight[i ][j- 4]) {
	                              superWeight[i ][j- 3] += 40; add++;}
	                           else {
	                              superWeight[i ][j- 4] += 40; add++;}
	                           }
	                     }
	                  } else myCount = 0;
	               } catch (ArrayIndexOutOfBoundsException e) {}
	            }
	         }
	         
	      
	      
	      if(add >= 2) return;
	         // 2 (����2) 2 ���� ����
	         for (int i = 0; i < 19; i++) {
	             myCount = 0;
	             for (int j = 0; j < 19; j++) {
	                try {
	                   if (PlayBoard.playBoard[i][j] == Main.ComC) {
	                      myCount++;
	                      if (myCount == 2) {
	                         if (PlayBoard.playBoard[i][j + 1] == 0 && PlayBoard.playBoard[i][j + 2] == 0 && PlayBoard.playBoard[i][j + 3] == Main.UserC && PlayBoard.playBoard[i][j + 4] == Main.UserC) {
	                            superWeight[i][j + 1] += 40; add++;
	                            superWeight[i][j + 2] += 40; add++;
	                         } 
	                         else if (PlayBoard.playBoard[i][j - 2] == 0 && PlayBoard.playBoard[i][j - 3] == 0 && PlayBoard.playBoard[i][j - 4] == Main.UserC && PlayBoard.playBoard[i][j - 5] == Main.UserC) {
	                            superWeight[i][j - 2] += 40; add++;
	                            superWeight[i][j - 3] += 40; add++;
	                         }
	                         else if(PlayBoard.playBoard[i][j - 2] == 0 && PlayBoard.playBoard[i][j + 1] == 0 && PlayBoard.playBoard[i][j + 2] == Main.UserC && PlayBoard.playBoard[i][j + 3] == Main.UserC) {
	                            superWeight[i][j - 2] += 40; add++;
	                            superWeight[i][j + 1] += 40; add++;
	                         }
	                         else if(PlayBoard.playBoard[i][j - 2] == 0 && PlayBoard.playBoard[i][j - 5] == 0 && PlayBoard.playBoard[i][j - 3] == Main.UserC && PlayBoard.playBoard[i][j - 4] == Main.UserC) {
	                            superWeight[i][j - 2] += 40; add++;
	                            superWeight[i][j - 5] += 40; add++;
	                         }
	                         else if(PlayBoard.playBoard[i][j - 2] == 0 && PlayBoard.playBoard[i][j - 4] == 0 && PlayBoard.playBoard[i][j - 3] == Main.UserC && PlayBoard.playBoard[i][j - 5] == Main.UserC) {
	                            superWeight[i][j - 2] += 40; add++;
	                            superWeight[i][j - 4] += 40; add++;
	                         }
	                         else if(PlayBoard.playBoard[i][j - 2] == 0 && PlayBoard.playBoard[i][j + 1] == 0 && PlayBoard.playBoard[i][j - 3] == Main.UserC && PlayBoard.playBoard[i][j + 2] == Main.UserC) {
	                            superWeight[i][j - 2] += 40; add++;
	                            superWeight[i][j + 1] += 40; add++;
	                         }
	                         else if(PlayBoard.playBoard[i][j + 1] == 0 && PlayBoard.playBoard[i][j + 3] == 0 && PlayBoard.playBoard[i][j + 2] == Main.UserC && PlayBoard.playBoard[i][j + 4] == Main.UserC) {
	                            superWeight[i][j + 3] += 40; add++;
	                            superWeight[i][j + 1] += 40; add++;
	                         }
	                         else if(PlayBoard.playBoard[i][j + 1] == 0 && PlayBoard.playBoard[i][j + 4] == 0 && PlayBoard.playBoard[i][j + 2] == Main.UserC && PlayBoard.playBoard[i][j + 3] == Main.UserC) {
	                            superWeight[i][j + 4] += 40; add++;
	                            superWeight[i][j + 1] += 40; add++;
	                         }
	                         else if(PlayBoard.playBoard[i][j + 1] == 0 && PlayBoard.playBoard[i][j - 2] == 0 && PlayBoard.playBoard[i][j - 3] == Main.UserC && PlayBoard.playBoard[i][j - 4] == Main.UserC) {
	                            superWeight[i][j - 2] += 40; add++;
	                            superWeight[i][j + 1] += 40; add++;
	                         }
	                      } 
	                   }else  myCount = 0;
	                } catch (ArrayIndexOutOfBoundsException e) {}
	             }
	          }
	      

	         if(add >= 2) return;
	      // 5 ���� ����
	      for (int j = 0; j < 19; j++) {
	         myCount = 0;
	         for (int i = 0; i < 19; i++) {
	            try {
	               if (PlayBoard.playBoard[i][j] == Main.ComC) {
	                  myCount++;
	                  if (myCount == 5) { // �糡 �� �ƹ� ����� ����ġ ��â �̺�Ʈ
	                	  if (i - 5 < 0 && PlayBoard.playBoard[i + 1][j] == 0) {
								superWeight[i + 1][j] += 40;
								add++;
							}

							else if (i + 1 > 18 && PlayBoard.playBoard[i - 5][j] == 0) {
								superWeight[i - 5][j] += 40;
								add++;
							}
							else if (PlayBoard.playBoard[i - 5][j] == 0) {
	                    	 superWeight[i - 5][j] += 80;
	                        add++;
	                     } else if (PlayBoard.playBoard[i + 1][j] == 0) {
	                    	 superWeight[i + 1][j] += 80;
	                        add++;
	                     }
	                  }
	               } else myCount = 0;
	            } catch (ArrayIndexOutOfBoundsException e) {}
	         }
	      }
	      

	      if(add >= 2) return;
	      // 4 ���� ����
	      for (int j = 0; j < 19; j++) {
	         myCount = 0;
	         for (int i = 0; i < 19; i++) {
	            try {
	               if (PlayBoard.playBoard[i][j] == Main.ComC) {
	                  myCount++;
	                  if (myCount == 4) {
	                	  if (i - 4 < 0 && PlayBoard.playBoard[i + 1][j] == 0 && PlayBoard.playBoard[i + 2][j] == 0) {
								superWeight[i + 1][j] += 40;
								superWeight[i + 2][j] += 40;
								add++;
							}

							else if (i + 1 > 18 && PlayBoard.playBoard[i - 5][j] == 0 && PlayBoard.playBoard[i - 4][j] == 0) {
								superWeight[i - 5][j] += 40;
								superWeight[i - 4][j] += 40;
								add++;
							}
							else  if (PlayBoard.playBoard[i - 4][j] == 0 && PlayBoard.playBoard[i + 1][j] == 0) {
	                    	 superWeight[i - 4][j] += 80;
	                    	 superWeight[i + 1][j] += 80;
	                        add++;
	                        add++;
	                     } else if (PlayBoard.playBoard[i - 4][j] == 0 || PlayBoard.playBoard[i + 1][j] == 0) {
	                        if (PlayBoard.playBoard[i - 4][j] == 0 && PlayBoard.playBoard[i - 5][j] == 0) {
	                        	superWeight[i - 4][j] += 80;
	                        	superWeight[i - 5][j] += 80;
	                           add++;
	                           add++;
	                        } else if (PlayBoard.playBoard[i + 1][j] == 0 && PlayBoard.playBoard[i + 2][j] == 0) {
	                        	superWeight[i + 2][j] += 80;
	                        	superWeight[i + 1][j] += 80;
	                           add++;
	                           add++;
	                        }
	                     }
	                  }
	               } else myCount = 0;
	            } catch (ArrayIndexOutOfBoundsException e) {}
	         }
	      }
	      
	      


	         if(add >= 2) return;
	         // 3���� 1 ���� ���� 
	         for (int j = 0; j < 19; j++) {
	            myCount = 0;
	            for (int i = 0; i < 19; i++) {
	               try {
	                  if (PlayBoard.playBoard[i][j] == Main.ComC) {
	                     myCount++;
	                     if (myCount == 3) {
	                        if (PlayBoard.playBoard[i + 1][j] == 0 && PlayBoard.playBoard[i + 2][j] == Main.ComC && PlayBoard.playBoard[i + 3][j] == 0) {
	                           if(weight[i + 3][j] > weight[i + 1][j]) {
	                              superWeight[i + 3][j] += 40; add++;
	                           }else {
	                        	   superWeight[i + 1][j] += 40; add++;
	                           }
	                        } 
	                        else if (PlayBoard.playBoard[i - 3][j] == 0 && PlayBoard.playBoard[i - 4][j] == Main.ComC && PlayBoard.playBoard[i - 5][j] == 0) {
	                           if(weight[i - 3][j]  > weight[i - 5][j]) {
	                              superWeight[i - 3][j] += 40; add++;}
	                           else {
	                              superWeight[i - 5][j] += 40; add++;}
	                           }
	                     }
	                  } else myCount = 0;
	               } catch (ArrayIndexOutOfBoundsException e) {}
	            }
	         }
	         
	         
	         if(add >= 2) return;
	         // 3���� 2 ���� ���� 
	         for (int j = 0; j < 19; j++) {
	            myCount = 0;
	            for (int i = 0; i < 19; i++) {
	               try {
	                  if (PlayBoard.playBoard[i][j] == Main.ComC) {
	                     myCount++;
	                     if (myCount == 3) {
	                        if (PlayBoard.playBoard[i + 1][j] == 0 && PlayBoard.playBoard[i + 2][j] == 0 && PlayBoard.playBoard[i + 3][j] == Main.ComC ) {
	                           if(weight[i + 2][j] > weight[i + 1][j]) {
	                              superWeight[i + 2][j] += 40; add++;
	                           }else {
	                        	   superWeight[i + 1][j] += 40; add++;
	                           }
	                        } 
	                        else if (PlayBoard.playBoard[i - 3][j] == 0 && PlayBoard.playBoard[i - 4][j] == 0 && PlayBoard.playBoard[i - 5][j] == Main.ComC) {
	                           if(weight[i - 3][j]  > weight[i - 4][j]) {
	                              superWeight[i - 3][j] += 40; add++;}
	                           else {
	                              superWeight[i - 4][j] += 40; add++;}
	                           }
	                     }
	                  } else myCount = 0;
	               } catch (ArrayIndexOutOfBoundsException e) {}
	            }
	         }

	      
	      if(add >= 2) return;
	         // 2 (����2) 2 ���� ����
	         for (int j = 0; j < 19; j++) {
	             myCount = 0;
	             for (int i = 0; i < 19; i++) {
	                try {
	                   if (PlayBoard.playBoard[i][j] == Main.ComC) {
	                      myCount++;
	                      if (myCount == 2) {
	                         if (PlayBoard.playBoard[i + 1][j] == 0 && PlayBoard.playBoard[i + 2][j] == 0 && PlayBoard.playBoard[i + 3][j] == Main.UserC && PlayBoard.playBoard[i + 4][j] == Main.UserC) {
	                            superWeight[i + 1][j] += 40; add++;
	                            superWeight[i + 2][j] += 40; add++;
	                         } 
	                         else if (PlayBoard.playBoard[i - 2][j] == 0 && PlayBoard.playBoard[i - 3][j] == 0 && PlayBoard.playBoard[i - 4][j] == Main.UserC && PlayBoard.playBoard[i - 5][j] == Main.UserC) {
	                            superWeight[i - 2][j] += 40; add++;
	                            superWeight[i - 3][j] += 40; add++;
	                         }
	                         else if(PlayBoard.playBoard[i - 2][j] == 0 && PlayBoard.playBoard[i + 1][j] == 0 && PlayBoard.playBoard[i + 2][j] == Main.UserC && PlayBoard.playBoard[i + 3][j] == Main.UserC) {
	                            superWeight[i - 2][j] += 40; add++;
	                            superWeight[i + 1][j] += 40; add++;
	                         }
	                         else if(PlayBoard.playBoard[i - 2][j] == 0 && PlayBoard.playBoard[i - 5][j] == 0 && PlayBoard.playBoard[i - 3][j] == Main.UserC && PlayBoard.playBoard[i - 4][j] == Main.UserC) {
	                            superWeight[i - 2][j] += 40; add++;
	                            superWeight[i - 5][j] += 40; add++;
	                         }
	                         else if(PlayBoard.playBoard[i - 2][j] == 0 && PlayBoard.playBoard[i - 4][j] == 0 && PlayBoard.playBoard[i - 3][j] == Main.UserC && PlayBoard.playBoard[i - 5][j] == Main.UserC) {
	                            superWeight[i - 2][j] += 40; add++;
	                            superWeight[i - 4][j] += 40; add++;
	                         }
	                         else if(PlayBoard.playBoard[i - 2][j] == 0 && PlayBoard.playBoard[i + 1][j] == 0 && PlayBoard.playBoard[i - 3][j] == Main.UserC && PlayBoard.playBoard[i + 2][j] == Main.UserC) {
	                            superWeight[i - 2][j] += 40; add++;
	                            superWeight[i + 1][j] += 40; add++;
	                         }
	                         else if(PlayBoard.playBoard[i + 1][j] == 0 && PlayBoard.playBoard[i + 3][j] == 0 && PlayBoard.playBoard[i + 2][j] == Main.UserC && PlayBoard.playBoard[i + 4][j] == Main.UserC) {
	                            superWeight[i + 3][j] += 40; add++;
	                            superWeight[i + 1][j] += 40; add++;
	                         }
	                         else if(PlayBoard.playBoard[i + 1][j] == 0 && PlayBoard.playBoard[i + 4][j] == 0 && PlayBoard.playBoard[i + 2][j] == Main.UserC && PlayBoard.playBoard[i + 3][j] == Main.UserC) {
	                            superWeight[i + 4][j] += 40; add++;
	                            superWeight[i + 1][j] += 40; add++;
	                         }
	                         else if(PlayBoard.playBoard[i + 1][j] == 0 && PlayBoard.playBoard[i - 2][j] == 0 && PlayBoard.playBoard[i - 3][j] == Main.UserC && PlayBoard.playBoard[i - 4][j] == Main.UserC) {
	                            superWeight[i - 2][j] += 40; add++;
	                            superWeight[i + 1][j] += 40; add++;
	                         }
	                      } 
	                   }else  myCount = 0;
	                } catch (ArrayIndexOutOfBoundsException e) {}
	             }
	          }

	      
	      
	      if(add >= 2) return;
	   // 5 ���� ������ ������ �Ʒ�(�´밢\) ���� 
	      for (int i = 0; i < 19; i++) {
	         myCount = 0;
	         for (int j = 0; j < 19; j++) {
	            int temp1 = i;
	            int temp2 = j;
	            for (int k = 0; k < 5; k++) {
	            	try {
	                if (PlayBoard.playBoard[temp1][temp2] == Main.ComC) {
	                  myCount++;
	                     if (myCount == 5) {
	                    	 //�糡�ո� 
	                    	 if((temp1 - 5 < 0 || temp2 - 5 < 0) && PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0) {
									superWeight[temp1 + 1][temp2 + 1] += 40;
									add++;
								}
								else if((temp1 + 1 > 18 || temp2 + 1 > 18) && PlayBoard.playBoard[temp1 - 5][temp2 - 5] == 0){
									superWeight[temp1 - 5][temp2 - 5] += 40;
									add++;
								}
								else if (PlayBoard.playBoard[temp1 - 5][temp2 - 5] == 0
	                              && PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0) {
	                        	superWeight[temp1 - 5][temp2 - 5] += 80; //�糡���� 
	                        	superWeight[temp1 + 1][temp2 + 1] += 80;
	                           add++;
	                           add++;
	                        } 
	                        //�ѳ��ո� - ���ʸ��� 
	                        else if (PlayBoard.playBoard[temp1 - 5][temp2 - 5] == 0
	                              || PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0) {
		                           if (PlayBoard.playBoard[temp1 - 4][temp2 - 5] == 0) {
		                        	   superWeight[temp1 - 5][temp2 - 5] += 80;
		                              add++;
		                           } else if (PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0) {
		                        	   superWeight[temp1 + 1][temp2 + 1] += 580;
		                              add++;
		                           }
	                        }
	                     }
	                  temp1++;
	                  temp2++;
	               } else myCount = 0;
	               } catch (ArrayIndexOutOfBoundsException e) {}
	            }
	         }
	      }
	      
	      
	      
	      if(add >= 2) return;
	      // 4 ���� ������ ������ �Ʒ�(�´밢\) ����
	      for (int i = 0; i < 19; i++) {
	         myCount = 0;
	         for (int j = 0; j < 19; j++) {
	            int temp1 = i;
	            int temp2 = j;
	            for (int k = 0; k < 4; k++) {
	            	try {
	                if (PlayBoard.playBoard[temp1][temp2] == Main.ComC) {
	                  myCount++;
	                     if (myCount == 4) {
	                    	 if((temp1 - 4 < 0 || temp2 - 4 < 0) && PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0 && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == 0) {
									superWeight[temp1 + 1][temp2 + 1] += 40;
									superWeight[temp1 + 2][temp2 + 2] += 40;
									add++;
								}
								else if((temp1 + 1 > 18 || temp2 + 1 > 18) && PlayBoard.playBoard[temp1 - 5][temp2 - 5] == 0 && PlayBoard.playBoard[temp1 - 4][temp2 - 4] == 0){
									superWeight[temp1 - 5][temp2 - 5] += 40;
									superWeight[temp1 - 4][temp2 - 4] += 40;
									add++;
								}
								else if (PlayBoard.playBoard[temp1 - 4][temp2 - 4] == 0
	                              && PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0) {
	                        	superWeight[temp1 - 4][temp2 - 4] += 80;
	                        	superWeight[temp1 + 1][temp2 + 1] += 80;
	                           add++;
	                           add++;
	                        } else if (PlayBoard.playBoard[temp1 - 4][temp2 - 4] == 0
	                              || PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0) {
	                           if (PlayBoard.playBoard[temp1 - 4][temp2 - 4] == 0
	                                 && PlayBoard.playBoard[temp1 - 5][temp2 - 5] == 0) {
	                        	   superWeight[temp1 - 4][temp2 - 4] += 80;
	                        	   superWeight[temp1 - 5][temp2 - 5] += 80;
	                              add++;
	                              add++;
	                           } else if (PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0
	                                 && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == 0) {
	                        	   superWeight[temp1 + 1][temp2 + 1] += 80;
	                        	   superWeight[temp1 + 2][temp2 + 2] += 80;
	                              add++;
	                              add++;
	                           }
	                        }
	                     }
	                  temp1++;
	                  temp2++;
	               } else myCount = 0;
	               } catch (ArrayIndexOutOfBoundsException e) {}
	            }
	         }
	      }
	      

		   if(add >= 2) return;
		   // 3(�´밢\) 1���� ����
		   for(int i = 0;i<19;i++){
		      myCount = 0;
		      for (int j = 0; j < 19; j++) {
		         int temp1 = i;
		         int temp2 = j;
		         for (int k = 0; k < 3; k++) {
		        	 try {
		             if (PlayBoard.playBoard[temp1][temp2] == Main.ComC) {
		               myCount++;
		                  if (myCount == 3) {
		                    if (PlayBoard.playBoard[temp1 + 1][temp2+ 1] == 0 && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == Main.UserC && PlayBoard.playBoard[temp1 + 3][temp2+ 3] == 0) {
			                    if(weight[temp1 + 3][temp2+ 3] > weight[temp1 + 1][temp2+ 1]) {
			                    	superWeight[temp1 + 3][temp2+ 3] += 40; add++;
			                    }else {
			                       weight[temp1 + 1][temp2+ 1] += 40; add++;
			                    }
			                 } 
			                 else if (PlayBoard.playBoard[temp1 - 3][temp2- 3] == 0 && PlayBoard.playBoard[temp1 - 4][temp2- 4] == Main.UserC && PlayBoard.playBoard[temp1 - 5][temp2- 5] == 0) {
			                    if(weight[temp1 - 3][temp2- 3]  > weight[temp1 - 5][temp2- 5]) {
			                    	superWeight[temp1- 3][temp2- 3] += 40; add++;}
			                    else {
			                    	superWeight[temp1 - 5][temp2- 5] += 40; add++;}
			                    }
		                  }
		               temp1++;
		               temp2++;
		            } else myCount = 0;
		            } catch (ArrayIndexOutOfBoundsException e) {}
		         }
		      }
		   }
		   
		   if(add >= 2) return;
		   // 3(�´밢\) 2���� 1 ����
		   for(int i = 0;i<19;i++){
		      myCount = 0;
		      for (int j = 0; j < 19; j++) {
		         int temp1 = i;
		         int temp2 = j;
		         for (int k = 0; k < 3; k++) {
		        	 try {
		            if (PlayBoard.playBoard[temp1][temp2] == Main.ComC) {
		               myCount++;
		                  if (myCount == 3) {
		                    if (PlayBoard.playBoard[temp1 + 1][temp2+ 1] == 0 && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == 0 && PlayBoard.playBoard[temp1 + 3][temp2+ 3] == Main.ComC) {
			                
			                    	superWeight[temp1 + 1][temp2+ 1] += 40; add++;
			                   
			                    	superWeight[temp1 + 2][temp2+ 2] += 40; add++;
			                    
			                 } 
			                 else if (PlayBoard.playBoard[temp1 - 3][temp2- 3] == 0 && PlayBoard.playBoard[temp1 - 4][temp2- 4] == 0 && PlayBoard.playBoard[temp1 - 5][temp2- 5] == Main.ComC) {
			                
			                    	superWeight[temp1- 3][temp2- 3] += 40; add++;
			                 
			                    	superWeight[temp1 - 4][temp2- 4] += 40; add++;
			                 }
		                  }
			                  temp1++;
			                  temp2++;
		                  } else myCount = 0;
		        	 } catch (ArrayIndexOutOfBoundsException e) {}
		         }
		      }
		   }
		   

		   if(add >= 2) return;
		      // 2 (����2) 2 ���� (�´밢\)���� ������ ������ �Ʒ�
		      for (int i = 0; i < 19; i++) {
		          myCount = 0;
		          for (int j = 0; j < 19; j++) {
		             int temp1 = i;
		              int temp2 = j;
		              for (int k = 0; k < 2; k++) {
		                 try {
		                      if (PlayBoard.playBoard[temp1][temp2] == Main.ComC) {
		                            myCount++;
		                            try {
		                               if (myCount == 2) {
		                                   if (PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0 && PlayBoard.playBoard[temp1 + 4][temp2 + 4] == 0 && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == Main.UserC && PlayBoard.playBoard[temp1 + 3][temp2 + 3] == Main.UserC) {
		                                       superWeight[temp1 + 1][temp2 + 1] += 40; add++;
		                                       superWeight[temp1 + 4][temp2 + 4] += 40; add++;
		                                    } 
		                                    else if (PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0 && PlayBoard.playBoard[temp1 - 2][temp2 - 2] == 0 && PlayBoard.playBoard[temp1 - 3][temp2 - 3] == Main.UserC && PlayBoard.playBoard[temp1 - 4][temp2 - 4] == Main.UserC) {
		                                       superWeight[temp1 + 1][temp2 + 1] += 40; add++;
		                                       superWeight[temp1 - 2][temp2 - 2] += 40; add++;
		                                    }
		                                    else if (PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0 && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == 0 && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == Main.UserC && PlayBoard.playBoard[temp1 + 4][temp2 + 4] == Main.UserC) {
		                                          superWeight[temp1 + 1][temp2 + 1] += 40; add++;
		                                          superWeight[temp1 + 2][temp2 + 2] += 40; add++;
		                                    }
		                                    else if (PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0 && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == 0 && PlayBoard.playBoard[temp1 + 3][temp2 + 3] == Main.UserC && PlayBoard.playBoard[temp1 + 4][temp2 + 4] == Main.UserC) {
		                                          superWeight[temp1 + 1][temp2 + 1] += 40; add++;
		                                          superWeight[temp1 + 2][temp2 + 2] += 40; add++;
		                                    }
		                                    else if (PlayBoard.playBoard[temp1 - 2][temp2 - 2] == 0 && PlayBoard.playBoard[temp1 - 3][temp2 - 3] == 0 && PlayBoard.playBoard[temp1 - 4][temp2 - 4] == Main.UserC && PlayBoard.playBoard[temp1 - 5][temp2 - 5] == Main.UserC) {
		                                          superWeight[temp1 - 2][temp2 - 2] += 40; add++;
		                                          superWeight[temp1 - 3][temp2 - 3] += 40; add++;
		                                    }
		                                    else if (PlayBoard.playBoard[temp1 - 2][temp2 - 2] == 0 && PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0 && PlayBoard.playBoard[temp1 - 3][temp2 - 3] == Main.UserC && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == Main.UserC) {
		                                          superWeight[temp1 - 2][temp2 - 2] += 40; add++;
		                                          superWeight[temp1 + 1][temp2 + 1] += 40; add++;
		                                    }
		                                    else if (PlayBoard.playBoard[temp1 - 4][temp2 - 4] == 0 && PlayBoard.playBoard[temp1 - 2][temp2 - 2] == 0 && PlayBoard.playBoard[temp1 - 3][temp2 - 3] == Main.UserC && PlayBoard.playBoard[temp1 - 5][temp2 - 5] == Main.UserC) {
		                                          superWeight[temp1 - 4][temp2 - 4] += 40; add++;
		                                          superWeight[temp1 - 2][temp2 - 2] += 40; add++;
		                                    }
		                                    else if (PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0 && PlayBoard.playBoard[temp1 - 2][temp2 - 2] == 0 && PlayBoard.playBoard[temp1 + 3][temp2 + 3] == Main.UserC && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == Main.UserC) {
		                                          superWeight[temp1 + 1][temp2 + 1] += 40; add++;
		                                          superWeight[temp1 - 2][temp2 - 2] += 40; add++;
		                                    }
		                               }
		                            } catch (ArrayIndexOutOfBoundsException e) {}
		                            temp1++;
		                            temp2++;
		                         } else myCount = 0;
		                 }
		                 catch(ArrayIndexOutOfBoundsException e) {}
		              }
		              }
		          }
		       

	      
	      if(add >= 2) return;
	   // 5 ������������ ���ʾƷ� (��밢/) ���� 
	      for (int j = 0; j < 19; j++) {
	         myCount = 0;
	         for (int i = 5; i < 19; i++) {
	            int temp1 = i;
	            int temp2 = j;
	            for (int k = 0; k < 5; k++) {
	            	try {
	                if (PlayBoard.playBoard[temp1][temp2] == Main.ComC) {
	                	myCount++;
	                    if (myCount == 5) {
	                    	if((temp1 - 1 < 0 || temp2 + 1 > 18) && PlayBoard.playBoard[temp1 + 5][temp2 - 5] == 0) {
								superWeight[temp1 + 5][temp2 - 5] += 40;
								add++;
								}
							else if((temp1 + 5 > 18 || temp2 - 5 < 0) && PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0){
								superWeight[temp1 - 1][temp2 + 1] += 40;
								add++;
							}
							else if (PlayBoard.playBoard[temp1 + 5][temp2 - 5] == 0
	                              && PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0) {
	                        	superWeight[temp1 + 5][temp2 - 5] += 50;
	                        	superWeight[temp1 - 1][temp2 + 1] += 50;
	                           add++;
	                           add++;
	                        } else if (PlayBoard.playBoard[temp1 + 5][temp2 - 5] == 0
	                              || PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0) {
	                           if (PlayBoard.playBoard[temp1 + 5][temp2 - 5] == 0) {
	                        	   superWeight[temp1 + 5][temp2 - 5] += 50;
	                              add++;
	                           } else if (PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0) {
	                        	   superWeight[temp1 - 1][temp2 + 1] += 50;
	                              add++;
	                           }
	                        }
	                     }
	                     temp1--;
	                     temp2++;
	                } else myCount = 0;
	                } catch (ArrayIndexOutOfBoundsException e) {}
	            }
	         }
	      }
	      
	      
	      if(add >= 2) return;
	      // 4 ������������ ���ʾƷ� (��밢/) ����
	      for (int j = 0; j < 19; j++) {
	         myCount = 0;
	         for (int i = 5; i < 19; i++) {
	            int temp1 = i;
	            int temp2 = j;
	            for (int k = 0; k < 4; k++) {
	            	try {
	                if (PlayBoard.playBoard[temp1][temp2] == Main.ComC) {
	                	myCount++;
	                    if (myCount == 4) {
	                    	if((temp1 - 1 < 0 || temp2 + 1 > 18) && PlayBoard.playBoard[temp1 + 5][temp2 - 5] == 0 && PlayBoard.playBoard[temp1 + 4][temp2 - 4] == 0) {
								superWeight[temp1 + 5][temp2 - 5] += 40;
								superWeight[temp1 + 4][temp2 - 4] += 40;
								add++;
							}
							else if((temp1 + 4 > 18 || temp2 - 4 < 0) && PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0 && PlayBoard.playBoard[temp1 - 2][temp2 + 2] == 0){
								superWeight[temp1 - 1][temp2 + 1] += 40;
								superWeight[temp1 - 2][temp2 + 2] += 40;
								add++;
							}
							else if (PlayBoard.playBoard[temp1 + 4][temp2 - 4] == 0
	                              && PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0) {
	                        	superWeight[temp1 + 4][temp2 - 4] += 50;
	                        	superWeight[temp1 - 1][temp2 + 1] += 50;
	                           add++;
	                           add++;
	                         } else if (PlayBoard.playBoard[temp1 + 4][temp2 - 4] == 0
	                              || PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0) {
	                           if (PlayBoard.playBoard[temp1 + 4][temp2 - 4] == 0
	                                 && PlayBoard.playBoard[temp1 + 5][temp2 - 5] == 0) {
	                        	   superWeight[temp1 + 4][temp2 - 4] += 50;
	                        	   superWeight[temp1 + 5][temp2 - 5] += 50;
	                              add++;
	                              add++;
	                           } else if (PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0
	                                 && PlayBoard.playBoard[temp1 - 2][temp2 + 2] == 0) {
	                        	   superWeight[temp1 - 1][temp2 + 1] += 50;
	                        	   superWeight[temp1 - 2][temp2 + 2] += 50;
	                              add++;
	                              add++;
	                           }
	                        }
	                     }
		                  temp1--;
		                  temp2++;
	                  } else myCount = 0;
	               } catch (ArrayIndexOutOfBoundsException e) {}
	            }
	         }
	      }
	      

		   if(add >= 2) return;
		   // 3(��밢/) 1���� ����
		   for(int j = 0;j<19;j++){
		      myCount = 0;
		      for (int i = 0; i < 19; i++) {
		         int temp1 = i;
		         int temp2 = j;
		         for (int k = 0; k < 3; k++) {
		        	try {
		            if (PlayBoard.playBoard[temp1][temp2] == Main.ComC) {
		               myCount++;
		                  if (myCount == 3) {
		                    if (PlayBoard.playBoard[temp1 - 1][temp2+ 1] == 0 && PlayBoard.playBoard[temp1 - 2][temp2 + 2] == Main.UserC && PlayBoard.playBoard[temp1 - 3][temp2+ 3] == 0) {
			                    if(weight[temp1 - 3][temp2+ 3] > weight[temp1 - 1][temp2+ 1]) {
			                    	superWeight[temp1 - 3][temp2+ 3] += 40; add++;
			                    }else {
			                       superWeight[temp1 - 1][temp2+ 1] += 40; add++;
			                    }
			                 } 
			                 else if (PlayBoard.playBoard[temp1 + 3][temp2- 3] == 0 && PlayBoard.playBoard[temp1 + 4][temp2- 4] == Main.UserC && PlayBoard.playBoard[temp1 + 5][temp2- 5] == 0) {
			                    if(weight[temp1 + 3][temp2- 3]  > weight[temp1 + 5][temp2 - 5]) {
			                    	superWeight[temp1+ 3][temp2 - 3] += 40; add++;}
			                    else {
			                    	superWeight[temp1 + 5][temp2 - 5] += 40; add++;}
			                    }
			                  }
			                  temp1--;
			                  temp2++;
		                  } else myCount = 0;
		            } catch (ArrayIndexOutOfBoundsException e) {}
		         }
		      }
		   }
		   
		   if(add >= 2) return;
		   // 3(��밢/) 2���� 1 ����
		   for(int j = 0;j<19;j++){
		      myCount = 0;
		      for (int i = 0; i < 19; i++) {
		         int temp1 = i;
		         int temp2 = j;
		         for (int k = 0; k < 3; k++) {
		        	try {
		            if (PlayBoard.playBoard[temp1][temp2] == Main.ComC) {
		               myCount++;
		               
		                  if (myCount == 3) {
		                    if (PlayBoard.playBoard[temp1 - 1][temp2+ 1] == 0 && PlayBoard.playBoard[temp1 - 2][temp2 + 2] == 0 && PlayBoard.playBoard[temp1 - 3][temp2+ 3] == Main.ComC) {
			                  
			                    	superWeight[temp1 - 2][temp2+ 2] += 40; add++;
			                    
			                    	superWeight[temp1 - 1][temp2+ 1] += 40; add++;
			                    
			                 } 
			                 else if (PlayBoard.playBoard[temp1 + 3][temp2- 3] == 0 && PlayBoard.playBoard[temp1 + 4][temp2- 4] == 0 && PlayBoard.playBoard[temp1 + 5][temp2- 5] == Main.ComC) {
			                    
			                    	superWeight[temp1+ 3][temp2 - 3] += 40; add++;
			                   
			                    	superWeight[temp1 + 4][temp2 - 4] += 40; add++;
			                   
			                 }
		                  }
			                  temp1--;
			                  temp2++;
		                  } else myCount = 0;
		        	} catch (ArrayIndexOutOfBoundsException e) {}
		         }
		      }
		   }

	      
		   if(add >= 2) return;
		      // 2 (����2) 2 ���� (��밢/)������ ������ ���� �Ʒ� 
		      for (int i = 0; i < 19; i++) {
		          myCount = 0;
		          for (int j = 0; j < 19; j++) {
		             int temp1 = i;
		              int temp2 = j;
		              for (int k = 0; k < 2; k++) {
		                 try {
		                      if (PlayBoard.playBoard[temp1][temp2] == Main.ComC) {
		                            myCount++;
		                            try {
		                               if (myCount == 2) {
		                                   if (PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0 && PlayBoard.playBoard[temp1 + 2][temp2 - 2] == 0 && PlayBoard.playBoard[temp1 - 2][temp2 + 2] == Main.UserC && PlayBoard.playBoard[temp1 - 3][temp2 + 3] == Main.UserC) {
		                                       superWeight[temp1 - 1][temp2 + 1] += 40; add++;
		                                       superWeight[temp1 + 2][temp2 - 2] += 40; add++;
		                                    } 
		                                    else if (PlayBoard.playBoard[temp1 + 2][temp2 - 2] == 0 && PlayBoard.playBoard[temp1 + 5][temp2 - 5] == 0 && PlayBoard.playBoard[temp1 + 3][temp2 - 3] == Main.UserC && PlayBoard.playBoard[temp1 + 4][temp2 - 4] == Main.UserC) {
		                                       superWeight[temp1 + 2][temp2 - 2] += 40; add++;
		                                       superWeight[temp1 + 5][temp2 - 5] += 40; add++;
		                                    }
		                                    else if (PlayBoard.playBoard[temp1 + 4][temp2 - 4] == 0 && PlayBoard.playBoard[temp1 + 2][temp2 - 2] == 0 && PlayBoard.playBoard[temp1 + 3][temp2 - 3] == Main.UserC && PlayBoard.playBoard[temp1 + 5][temp2 - 5] == Main.UserC) {
		                                          superWeight[temp1 + 4][temp2 - 4] += 40; add++;
		                                          superWeight[temp1 + 2][temp2 - 2] += 40; add++;
		                                    }
		                                    else if (PlayBoard.playBoard[temp1 + 2][temp2 - 2] == 0 && PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0 && PlayBoard.playBoard[temp1 - 2][temp2 + 2] == Main.UserC && PlayBoard.playBoard[temp1 + 3][temp2 - 3] == Main.UserC) {
		                                          superWeight[temp1 + 2][temp2 - 2] += 40; add++;
		                                          superWeight[temp1 - 1][temp2 + 1] += 40; add++;
		                                    }
		                                    else if (PlayBoard.playBoard[temp1 + 3][temp2 - 3] == 0 && PlayBoard.playBoard[temp1 + 2][temp2 - 2] == 0 && PlayBoard.playBoard[temp1 + 3][temp2 - 3] == Main.UserC && PlayBoard.playBoard[temp1 + 4][temp2 - 4] == Main.UserC) {
		                                          superWeight[temp1 + 3][temp2 - 3] += 40; add++;
		                                          superWeight[temp1 + 2][temp2 - 2] += 40; add++;
		                                    }
		                                    else if (PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0 && PlayBoard.playBoard[temp1 - 2][temp2 + 2] == 0 && PlayBoard.playBoard[temp1 - 3][temp2 + 3] == Main.UserC && PlayBoard.playBoard[temp1 - 4][temp2 + 4] == Main.UserC) {
		                                          superWeight[temp1 - 1][temp2 + 1] += 40; add++;
		                                          superWeight[temp1 - 2][temp2 + 2] += 40; add++;
		                                    }
		                                    else if (PlayBoard.playBoard[temp1 + 2][temp2 - 2] == 0 && PlayBoard.playBoard[temp1 + 4][temp2 - 4] == 0 && PlayBoard.playBoard[temp1 + 3][temp2 - 3] == Main.UserC && PlayBoard.playBoard[temp1 + 5][temp2 - 5] == Main.UserC) {
		                                          superWeight[temp1 + 2][temp2 - 2] += 40; add++;
		                                          superWeight[temp1 + 4][temp2 - 4] += 40; add++;
		                                    }
		                                    else if (PlayBoard.playBoard[temp1 + 2][temp2 - 2] == 0 && PlayBoard.playBoard[temp1 + 5][temp2 - 5] == 0 && PlayBoard.playBoard[temp1 + 3][temp2 - 3] == Main.UserC && PlayBoard.playBoard[temp1 + 4][temp2 - 4] == Main.UserC) {
		                                          superWeight[temp1 + 2][temp2 - 2] += 40; add++;
		                                          superWeight[temp1 + 5][temp2 - 5] += 40; add++;
		                                    }
		                                    else if (PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0 && PlayBoard.playBoard[temp1 + 2][temp2 - 2] == 0 && PlayBoard.playBoard[temp1 - 2][temp2 + 2] == Main.UserC && PlayBoard.playBoard[temp1 - 3][temp2 + 3] == Main.UserC) {
		                                         superWeight[temp1 - 1][temp2 + 1] += 40; add++;
		                                         superWeight[temp1 + 2][temp2 - 2] += 40; add++;
		                                   }
		                               }
		                            } catch (ArrayIndexOutOfBoundsException e) {}
		                            temp1--;
		                            temp2++;
		                         } else myCount = 0;
		                 }
		                 catch(ArrayIndexOutOfBoundsException e) {}
		              }
		              }
		          }
		   
		   
   

	      //// �ȳ����� �� ��, �ѹ��� 
	      //// ----------------------------------------------------------------------------------
	      
	      if(add >= 2) return;
	      // 5 ���� ���
	      for (int i = 0; i < 19; i++) {
	         myCount = 0;
	         for (int j = 0; j < 19; j++) {
	            try {
	               if (PlayBoard.playBoard[i][j] == Main.UserC) {
	                  myCount++;
	                  if (myCount == 5) {
	                     // ���� �� ������� ���� �ñ��ϰ� ����
	                		if (j - 5 < 0 && PlayBoard.playBoard[i][j + 1] == 0) {
								superWeight[i][j + 1] += 40;
								add++;
							}

							else if (j + 1 > 18 && PlayBoard.playBoard[i][j - 5] == 0) {
								superWeight[i][j - 5] += 40;
								add++;
							}
							else if (PlayBoard.playBoard[i][j - 5] == 0 && PlayBoard.playBoard[i][j + 1] == 0) {
	                    	 superWeight[i][j - 5] += 40; add++;
	                    	 superWeight[i][j + 1] += 40; add++;
	                     }
	                     // ���� ���ʸ� ������� �ű� ����
	                     else if (PlayBoard.playBoard[i][j - 5] == 0) {
	                    	 superWeight[i][j - 5] += 40; add++;
	                     } else if (PlayBoard.playBoard[i][j + 1] == 0) {
	                    	 superWeight[i][j + 1] += 40; add++;
	                     }
	                  }
	               } else myCount = 0;
	            } catch (ArrayIndexOutOfBoundsException e) {}
	         }
	      }
	      
	      if(add >= 2) return;
	      // 4 ���� ���
	      for (int i = 0; i < 19; i++) {
	         myCount = 0;
	         for (int j = 0; j < 19; j++) {
	            try {
	               if (PlayBoard.playBoard[i][j] == Main.UserC) {
	                  myCount++;
	                  if (myCount == 4) { // �糡 �շ������� �糡�� ����ġ
	                	  if (j - 4 < 0 && PlayBoard.playBoard[i][j + 1] == 0) {
								superWeight[i][j + 1] += 40;
								add++;
							}

							else if (j + 1 > 18 && PlayBoard.playBoard[i][j - 4] == 0) {
								superWeight[i][j - 4] += 40;
								add++;
							}
							else if (PlayBoard.playBoard[i][j - 4] == 0 && PlayBoard.playBoard[i][j + 1] == 0) {
	                    	 superWeight[i][j - 4] += 40; add++;
	                    	 superWeight[i][j + 1] += 40; add++;
	                     } // ���ʸ� �շ�������
	                     else if (PlayBoard.playBoard[i][j - 4] == 0) {
	                        if (PlayBoard.playBoard[i][j - 5] == 0) { //������ �� �շ������� ���� �̵��ΰ��� �ΰ� 
	                        	if(weight[i][j - 4]>weight[i][j - 5] ) {
	                        		superWeight[i][j - 4] += 40; add++;
	                        	}else{
	                        		superWeight[i][j - 5] += 40; add++;
	                        	}
	                        }else { //�ƴϸ� �ŵ��ΰ� 
	                        	superWeight[i][j - 4] += 40; add++;
	                        }
	                     }else if(PlayBoard.playBoard[i][j + 1] == 0) {
	                    	 if (PlayBoard.playBoard[i][j + 2] == 0) {
		                        	if(weight[i][j + 2]>weight[i][j + 1]) {
		                        		superWeight[i][j + 2] += 40; add++;
		                        	}else{
		                        		superWeight[i][j + 1] += 40; add++;
		                        	}
		                      }else {
		                    	  superWeight[i][j + 1] += 40; add++;
		                      }
	                     }
	                  }
	               } else myCount = 0;
	            } catch (ArrayIndexOutOfBoundsException e) {}
	         }
	      }
	      
	      
	      if(add >= 2) return;
	      // 3���� 1 ���� ���
	      for (int i = 0; i < 19; i++) {
	         myCount = 0;
	         for (int j = 0; j < 19; j++) {
	            try {
	               if (PlayBoard.playBoard[i][j] == Main.UserC) {
	                  myCount++;
	                  if (myCount == 3) { //���������϶� 
	                	  //111010 
	                     if (PlayBoard.playBoard[i][j + 1] == 0 && PlayBoard.playBoard[i][j + 2] == Main.UserC && PlayBoard.playBoard[i][j + 3] == 0) {
	                        if(weight[i ][j+ 3] > weight[i ][j+ 1]) {
	                        	superWeight[i][j + 3] += 40; add++;
	                        }else {
	                           weight[i][j + 1] += 40; add++;
	                        }
	                     } 
	                     //010111
	                     else if (PlayBoard.playBoard[i ][j- 3] == 0 && PlayBoard.playBoard[i ][j- 4] == Main.UserC && PlayBoard.playBoard[i ][j- 5] == 0) {
	                        if(weight[i ][j- 3]  > weight[i ][j- 5]) {
	                        	superWeight[i ][j- 3] += 40; add++;}
	                        else {
	                        	superWeight[i ][j- 5] += 40; add++;}
	                        }
	                  }
	               } else myCount = 0;
	            } catch (ArrayIndexOutOfBoundsException e) {}
	         }
	      }
	      
	         if(add >= 2) return;
	         // 3���� 2 ���� ���
	         for (int i = 0; i < 19; i++) {
	            myCount = 0;
	            for (int j = 0; j < 19; j++) {
	               try {
	                  if (PlayBoard.playBoard[i][j] == Main.UserC) {
	                     myCount++;
	                     if (myCount == 3) { //���������϶� 
	                        //111001
	                        if (PlayBoard.playBoard[i][j + 1] == 0 && PlayBoard.playBoard[i][j + 2] == 0 && PlayBoard.playBoard[i][j + 3] == Main.UserC) {
	                           if(weight[i ][j+ 2] > weight[i ][j+ 1]) {
	                              superWeight[i][j + 2] += 40; add++;
	                           }else {
	                        	   superWeight[i][j + 1] += 40; add++;
	                           }
	                        } 
	                        //100111
	                        else if (PlayBoard.playBoard[i ][j- 3] == 0 && PlayBoard.playBoard[i ][j- 4] == 0 && PlayBoard.playBoard[i ][j- 5] == Main.UserC) {
	                           if(weight[i ][j- 4]  > weight[i ][j- 3]) {
	                              superWeight[i ][j- 4] += 40; add++;}
	                           else {
	                              superWeight[i ][j- 3] += 40; add++;}
	                           }
	                     }
	                  } else myCount = 0;
	               } catch (ArrayIndexOutOfBoundsException e) {}
	            }
	         }
	      
	      if(add >= 2) return;
	         // 2 (����2) 2 ��� ����
	         for (int i = 0; i < 19; i++) {
	             myCount = 0;
	             for (int j = 0; j < 19; j++) {
	                try {
	                   if (PlayBoard.playBoard[i][j] == Main.UserC) {
	                      myCount++;
	                      if (myCount == 2) {
	                         if (PlayBoard.playBoard[i][j + 1] == 0 && PlayBoard.playBoard[i][j + 2] == 0 && PlayBoard.playBoard[i][j + 3] == Main.UserC && PlayBoard.playBoard[i][j + 4] == Main.UserC) {
	                            superWeight[i][j + 1] += 40; add++;
	                            superWeight[i][j + 2] += 40; add++;
	                
	                         } 
	                         else if (PlayBoard.playBoard[i][j - 2] == 0 && PlayBoard.playBoard[i][j - 3] == 0 && PlayBoard.playBoard[i][j - 4] == Main.UserC && PlayBoard.playBoard[i][j - 5] == Main.UserC) {
	                            superWeight[i][j - 2] += 40; add++;
	                            superWeight[i][j - 3] += 40; add++;
	                         }
	                         else if(PlayBoard.playBoard[i][j - 2] == 0 && PlayBoard.playBoard[i][j + 1] == 0 && PlayBoard.playBoard[i][j + 2] == Main.UserC && PlayBoard.playBoard[i][j + 3] == Main.UserC) {
	                            superWeight[i][j - 2] += 40; add++;
	                            superWeight[i][j + 1] += 40; add++;
	                         }
	                         else if(PlayBoard.playBoard[i][j - 2] == 0 && PlayBoard.playBoard[i][j - 5] == 0 && PlayBoard.playBoard[i][j - 3] == Main.UserC && PlayBoard.playBoard[i][j - 4] == Main.UserC) {
	                            superWeight[i][j - 2] += 40; add++;
	                            superWeight[i][j - 5] += 40; add++;
	                         }
	                         else if(PlayBoard.playBoard[i][j - 2] == 0 && PlayBoard.playBoard[i][j - 4] == 0 && PlayBoard.playBoard[i][j - 3] == Main.UserC && PlayBoard.playBoard[i][j - 5] == Main.UserC) {
	                            superWeight[i][j - 2] += 40; add++;
	                            superWeight[i][j - 4] += 40; add++;
	                         }
	                         else if(PlayBoard.playBoard[i][j - 2] == 0 && PlayBoard.playBoard[i][j + 1] == 0 && PlayBoard.playBoard[i][j - 3] == Main.UserC && PlayBoard.playBoard[i][j + 2] == Main.UserC) {
	                            superWeight[i][j - 2] += 40; add++;
	                            superWeight[i][j + 1] += 40; add++;
	                         }
	                         else if(PlayBoard.playBoard[i][j + 1] == 0 && PlayBoard.playBoard[i][j + 3] == 0 && PlayBoard.playBoard[i][j + 2] == Main.UserC && PlayBoard.playBoard[i][j + 4] == Main.UserC) {
	                            superWeight[i][j + 3] += 40; add++;
	                            superWeight[i][j + 1] += 40; add++;
	                         }
	                         else if(PlayBoard.playBoard[i][j + 1] == 0 && PlayBoard.playBoard[i][j + 4] == 0 && PlayBoard.playBoard[i][j + 2] == Main.UserC && PlayBoard.playBoard[i][j + 3] == Main.UserC) {
	                            superWeight[i][j + 4] += 40; add++;
	                            superWeight[i][j + 1] += 40; add++;
	                         }
	                         else if(PlayBoard.playBoard[i][j + 1] == 0 && PlayBoard.playBoard[i][j - 2] == 0 && PlayBoard.playBoard[i][j - 3] == Main.UserC && PlayBoard.playBoard[i][j - 4] == Main.UserC) {
	                            superWeight[i][j - 2] += 40; add++;
	                            superWeight[i][j + 1] += 40; add++;
	                         }
	                      } 
	                   }else myCount = 0;
	                } catch (ArrayIndexOutOfBoundsException e) {}
	             }
	          }
	      
	      

	      if(add >= 2) return;
	      // 5 ���� ���
	      for (int j = 0; j < 19; j++) {
	         myCount = 0;
	         for (int i = 0; i < 19; i++) {
	            try {
	               if (PlayBoard.playBoard[i][j] == Main.UserC) {
	                  myCount++;
	                  if (myCount == 5) {
	                     // ���� �� ������� ���� �ñ��ϰ� ����
	                	  if (i - 5 < 0 && PlayBoard.playBoard[i + 1][j] == 0) {
								superWeight[i + 1][j] += 40;
								add++;
							}

							else if (i + 1 > 18 && PlayBoard.playBoard[i - 5][j] == 0) {
								superWeight[i - 5][j] += 40;
								add++;
							}
							else if (PlayBoard.playBoard[i - 5][j] == 0 && PlayBoard.playBoard[i + 1][j] == 0) {
	                    	 superWeight[i - 5][j] += 40; add++;
	                    	 superWeight[i + 1][j] += 40; add++;
	                     }
	                     // ���� ���ʸ� ������� �ű� ����
	                     else if (PlayBoard.playBoard[i - 5][j] == 0) {
	                    	 superWeight[i - 5][j] += 40; add++;
	                     } else if (PlayBoard.playBoard[i + 1][j] == 0) {
	                    	 superWeight[i + 1][j] += 40; add++;
	                     }
	                  }
	               } else myCount = 0;
	            } catch (ArrayIndexOutOfBoundsException e) {}
	         }
	      }

	      
	      if(add >= 2) return;
	      // 4 ���� ���
	      for (int j = 0; j < 19; j++) {
	         myCount = 0;
	         for (int i = 0; i < 19; i++) {
	            try {
	               if (PlayBoard.playBoard[i][j] == Main.UserC) {
	                  myCount++;
	                  if (myCount == 4) { // �糡 �շ������� �糡�� ����ġ
	                	  if (i - 4 < 0 && PlayBoard.playBoard[i + 1][j] == 0) {
								superWeight[i + 1][j] += 40;
								add++;
							}

							else if (i + 1 > 18 && PlayBoard.playBoard[i - 4][j] == 0) {
								superWeight[i - 4][j] += 40;
								add++;
							}
							else if (PlayBoard.playBoard[i - 4][j] == 0 && PlayBoard.playBoard[i + 1][j] == 0) {
	                    	 superWeight[i - 4][j] += 40; add++;
	                    	 superWeight[i + 1][j] += 40; add++;
	                     } // ���ʸ� �շ�������
	                     else if (PlayBoard.playBoard[i - 4][j] == 0) {
	                        if (PlayBoard.playBoard[i - 5][j] == 0) { //������ �� �շ������� ���� �̵��ΰ��� �ΰ� 
	                        	if(weight[i - 4][j]>weight[i - 5][j] ) {
	                        		superWeight[i - 4][j] += 40; add++;
	                        	}else{
	                        		superWeight[i - 5][j] += 40; add++;
	                        	}
	                        }else { //�ƴϸ� �ŵ��ΰ� 
	                        	superWeight[i - 4][j] += 40; add++;
	                        }
	                     }else if(PlayBoard.playBoard[i + 1][j] == 0) {
	                    	 if (PlayBoard.playBoard[i + 2][j] == 0) {
		                        	if(weight[i + 2][j]>weight[i + 1][j]) {
		                        		superWeight[i + 2][j] += 40; add++;
		                        	}else{
		                        		superWeight[i+ 1][j ] += 40; add++;
		                        	}
		                      }else {
		                    	  superWeight[i + 1][j] += 40; add++;
		                      }
	                     }
	                  }
	               } else myCount = 0;
	            } catch (ArrayIndexOutOfBoundsException e) {}
	         }
	      }
	      
	      
	      if(add >= 2) return;
	      // 3���� 1 ���� ��� 
	      for (int j = 0; j < 19; j++) {
	         myCount = 0;
	         for (int i = 0; i < 19; i++) {
	            try {
	               if (PlayBoard.playBoard[i][j] == Main.UserC) {
	                  myCount++;
	                  if (myCount == 3) {
	                     if (PlayBoard.playBoard[i + 1][j] == 0 && PlayBoard.playBoard[i + 2][j] == Main.UserC && PlayBoard.playBoard[i + 3][j] == 0) {
	                        if(weight[i + 3][j] > weight[i + 1][j]) {
	                        	superWeight[i + 3][j] += 40; add++;
	                        }else {
	                           weight[i + 1][j] += 40; add++;
	                        }
	                     } 
	                     else if (PlayBoard.playBoard[i - 3][j] == 0 && PlayBoard.playBoard[i - 4][j] == Main.UserC && PlayBoard.playBoard[i - 5][j] == 0) {
	                        if(weight[i - 3][j]  > weight[i - 5][j]) {
	                        	superWeight[i - 3][j] += 40; add++;}
	                        else {
	                        	superWeight[i - 5][j] += 40; add++;}
	                        }
	                  }
	               } else myCount = 0;
	            } catch (ArrayIndexOutOfBoundsException e) {}
	         }
	      }
	      

	         if(add >= 2) return;
	         // 3���� 2 ���� ��� 
	         for (int j = 0; j < 19; j++) {
	            myCount = 0;
	            for (int i = 0; i < 19; i++) {
	               try {
	                  if (PlayBoard.playBoard[i][j] == Main.UserC) {
	                     myCount++;
	                     if (myCount == 3) {
	                        if (PlayBoard.playBoard[i + 1][j] == 0 && PlayBoard.playBoard[i + 2][j] == 0 && PlayBoard.playBoard[i + 3][j] == Main.UserC) {
	                           if(weight[i + 2][j] > weight[i + 1][j]) {
	                              superWeight[i + 2][j] += 40; add++;
	                           }else {
	                        	   superWeight[i + 1][j] += 40; add++;
	                           }
	                        } 
	                        else if (PlayBoard.playBoard[i - 3][j] == 0 && PlayBoard.playBoard[i - 4][j] == 0 && PlayBoard.playBoard[i - 5][j] == Main.UserC ) {
	                           if(weight[i - 3][j]  > weight[i - 4][j]) {
	                              superWeight[i - 3][j] += 40; add++;}
	                           else {
	                              superWeight[i - 4][j] += 40; add++;}
	                           }
	                     }
	                  } else myCount = 0;
	               } catch (ArrayIndexOutOfBoundsException e) {}
	            }
	         }
	      
	      if(add >= 2) return;
	         // 2 (����2) 2 ��� ����
	         for (int j = 0; j < 19; j++) {
	             myCount = 0;
	             for (int i = 0; i < 19; i++) {
	                try {
	                   if (PlayBoard.playBoard[i][j] == Main.UserC) {
	                      myCount++;
	                      if (myCount == 2) {
	                         if (PlayBoard.playBoard[i + 1][j] == 0 && PlayBoard.playBoard[i + 2][j] == 0 && PlayBoard.playBoard[i + 3][j] == Main.UserC && PlayBoard.playBoard[i + 4][j] == Main.UserC) {
	                            superWeight[i + 1][j] += 40; add++;
	                            superWeight[i + 2][j] += 40; add++;
	                
	                         } 
	                         else if (PlayBoard.playBoard[i - 2][j] == 0 && PlayBoard.playBoard[i - 3][j] == 0 && PlayBoard.playBoard[i - 4][j] == Main.UserC && PlayBoard.playBoard[i - 5][j] == Main.UserC) {
	                            superWeight[i - 2][j] += 40; add++;
	                            superWeight[i - 3][j] += 40; add++;
	                         }
	                         else if(PlayBoard.playBoard[i - 2][j] == 0 && PlayBoard.playBoard[i + 1][j] == 0 && PlayBoard.playBoard[i + 2][j] == Main.UserC && PlayBoard.playBoard[i + 3][j] == Main.UserC) {
	                            superWeight[i - 2][j] += 40; add++;
	                            superWeight[i + 1][j] += 40; add++;
	                         }
	                         else if(PlayBoard.playBoard[i - 2][j] == 0 && PlayBoard.playBoard[i - 5][j] == 0 && PlayBoard.playBoard[i - 3][j] == Main.UserC && PlayBoard.playBoard[i - 4][j] == Main.UserC) {
	                            superWeight[i - 2][j] += 40; add++;
	                            superWeight[i - 5][j] += 40; add++;
	                         }
	                         else if(PlayBoard.playBoard[i - 2][j] == 0 && PlayBoard.playBoard[i - 4][j] == 0 && PlayBoard.playBoard[i - 3][j] == Main.UserC && PlayBoard.playBoard[i - 5][j] == Main.UserC) {
	                            superWeight[i - 2][j] += 40; add++;
	                            superWeight[i - 4][j] += 40; add++;
	                         }
	                         else if(PlayBoard.playBoard[i - 2][j] == 0 && PlayBoard.playBoard[i + 1][j] == 0 && PlayBoard.playBoard[i - 3][j] == Main.UserC && PlayBoard.playBoard[i + 2][j] == Main.UserC) {
	                            superWeight[i - 2][j] += 40; add++;
	                            superWeight[i + 1][j] += 40; add++;
	                         }
	                         else if(PlayBoard.playBoard[i + 1][j] == 0 && PlayBoard.playBoard[i + 3][j] == 0 && PlayBoard.playBoard[i + 2][j] == Main.UserC && PlayBoard.playBoard[i + 4][j] == Main.UserC) {
	                            superWeight[i + 3][j] += 40; add++;
	                            superWeight[i + 1][j] += 40; add++;
	                         }
	                         else if(PlayBoard.playBoard[i + 1][j] == 0 && PlayBoard.playBoard[i + 4][j] == 0 && PlayBoard.playBoard[i + 2][j] == Main.UserC && PlayBoard.playBoard[i + 3][j] == Main.UserC) {
	                            superWeight[i + 4][j] += 40; add++;
	                            superWeight[i + 1][j] += 40; add++;
	                         }
	                         else if(PlayBoard.playBoard[i + 1][j] == 0 && PlayBoard.playBoard[i - 2][j] == 0 && PlayBoard.playBoard[i - 3][j] == Main.UserC && PlayBoard.playBoard[i - 4][j] == Main.UserC) {
	                            superWeight[i - 2][j] += 40; add++;
	                            superWeight[i + 1][j] += 40; add++;
	                         }
	                      } 
	                   }else myCount = 0;
	                } catch (ArrayIndexOutOfBoundsException e) {}
	             }
	          }
	      
	      
	      
	      
	      if(add >= 2) return;
	  	// 5 ���� ������ ������ �Ʒ�(�´밢\) ���
	  	   for(int i = 0;i<19;i++){
	  	      myCount = 0;
	  	      for (int j = 0; j < 19; j++) {
	  	         int temp1 = i;
	  	         int temp2 = j;
	  	         for (int k = 0; k < 5; k++) {
	  	        	try {
	  	            if (PlayBoard.playBoard[temp1][temp2] == Main.UserC) {
	  	               myCount++;
	  	                  if (myCount == 5) {
	  	                	  //�Ѵٶո� 
	  	                	if((temp1 - 5 < 0 || temp2 - 5 < 0) && PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0) {
								superWeight[temp1 + 1][temp2 + 1] += 40;
								add++;
							}
							else if((temp1 + 1 > 18 || temp2 + 1 > 18) && PlayBoard.playBoard[temp1 - 5][temp2 - 5] == 0){
								superWeight[temp1 - 5][temp2 - 5] += 40;
								add++;
							}
							else if (PlayBoard.playBoard[temp1 - 5][temp2 - 5] == 0
	  	                           && PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0) {
	  	                    	superWeight[temp1 - 5][temp2 - 5] += 40; add++;
	  	                    	superWeight[temp1 + 1][temp2 + 1] += 40; add++;
	  	                     } 
	  	                     //���ʸ��ո� - �� �ո����� ����ġ 
	  	                     else if (PlayBoard.playBoard[temp1 - 5][temp2 - 5] == 0
	  	                           || PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0) {
	  	                        if (PlayBoard.playBoard[temp1 - 5][temp2 - 5] == 0) {
	  	                        	superWeight[temp1 - 5][temp2 - 5] += 40; add++;
	  	                        } else if (PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0) {
	  	                        	superWeight[temp1 + 1][temp2 + 1] += 40; add++;
	  	                        }
	  	                     }
	  	                  }
		  	               temp1++;
		  	               temp2++;
	  	               } else myCount = 0;
	  	            } catch (ArrayIndexOutOfBoundsException e) {}
	  	         }
	  	      }
	  	   }
	  	   
	  	   

	      if(add >= 2) return;
	   // 4 ���� ������ ������ �Ʒ�(�´밢\) ���
	   for(int i = 0;i<19;i++){
	      myCount = 0;
	      for (int j = 0; j < 19; j++) {
	         int temp1 = i;
	         int temp2 = j;
	         for (int k = 0; k < 4; k++) {
	        	 try {
	            if (PlayBoard.playBoard[temp1][temp2] == Main.UserC) {
	               myCount++;
	                  if (myCount == 4) {
	                	  if((temp1 - 4 < 0 || temp2 - 4 < 0) && PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0) {
								superWeight[temp1 + 1][temp2 + 1] += 40;
								add++;
							}
							else if((temp1 + 1 > 18 || temp2 + 1 > 18) && PlayBoard.playBoard[temp1 - 4][temp2 - 4] == 0){
								superWeight[temp1 - 4][temp2 - 4] += 40;
								add++;
							}
							else if (PlayBoard.playBoard[temp1 - 4][temp2 - 4] == 0
	                           && PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0) {
	                    	 superWeight[temp1 - 4][temp2 - 4] += 40; add++;
	                    	 superWeight[temp1 + 1][temp2 + 1] += 40; add++;
	                     } else if (PlayBoard.playBoard[temp1 - 4][temp2 - 4] == 0
	                           || PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0) {
	                        if (PlayBoard.playBoard[temp1 - 4][temp2 - 4] == 0
	                              && PlayBoard.playBoard[temp1 - 5][temp2 - 5] == 0) {
	                        	if(weight[temp1 - 4][temp2 - 4]>weight[temp1 - 5][temp2 - 5]) {
	                        		superWeight[temp1 - 4][temp2 - 4] += 40; add++;
	                        	}else{
	                        		superWeight[temp1 - 5][temp2 - 5] += 40; add++;
	                        	}
	                        } else if (PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0
	                              && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == 0) {
	                        	if(weight[temp1 + 1][temp2 + 1]>weight[temp1 + 2][temp2 + 2]) {
	                        		superWeight[temp1 + 1][temp2 + 1] += 40; add++;
	                        	}else{
	                        		superWeight[temp1 + 2][temp2 + 2] += 40; add++;
	                        	}
	                        }
	                     }
	                  }
			              temp1++;
			              temp2++;
		              } else myCount = 0;
	            } catch (ArrayIndexOutOfBoundsException e) {}
	         }
	      }
	   }
	   

	   if(add >= 2) return;
	   // 3(�´밢\) 1���� ���
	   for(int i = 0;i<19;i++){
	      myCount = 0;
	      for (int j = 0; j < 19; j++) {
	         int temp1 = i;
	         int temp2 = j;
	         for (int k = 0; k < 3; k++) {
	        	 try {
	            if (PlayBoard.playBoard[temp1][temp2] == Main.UserC) {
	               myCount++;
	                  if (myCount == 3) {
	                    if (PlayBoard.playBoard[temp1 + 1][temp2+ 1] == 0 && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == Main.UserC && PlayBoard.playBoard[temp1 + 3][temp2+ 3] == 0) {
		                    if(weight[temp1 + 3][temp2+ 3] > weight[temp1 + 1][temp2+ 1]) {
		                    	superWeight[temp1 + 3][temp2+ 3] += 40; add++;
		                    }else {
		                       weight[temp1 + 1][temp2+ 1] += 40; add++;
		                    }
		                 } 
		                 else if (PlayBoard.playBoard[temp1 - 3][temp2- 3] == 0 && PlayBoard.playBoard[temp1 - 4][temp2- 4] == Main.UserC && PlayBoard.playBoard[temp1 - 5][temp2- 5] == 0) {
		                    if(weight[temp1 - 3][temp2- 3]  > weight[temp1 - 5][temp2- 5]) {
		                    	superWeight[temp1- 3][temp2- 3] += 40; add++;}
		                    else {
		                    	superWeight[temp1 - 5][temp2- 5] += 40; add++;}
		                    }
	                  }
		                  temp1++;
		                  temp2++;
	                  } else myCount = 0;
	        	 } catch (ArrayIndexOutOfBoundsException e) {}
	         }
	      }
	   }
	   
	   if(add >= 2) return;
	   // 3(�´밢\) 2���� 1 ���
	   for(int i = 0;i<19;i++){
	      myCount = 0;
	      for (int j = 0; j < 19; j++) {
	         int temp1 = i;
	         int temp2 = j;
	         for (int k = 0; k < 3; k++) {
	        	 try {
	            if (PlayBoard.playBoard[temp1][temp2] == Main.UserC) {
	               myCount++;
	                  if (myCount == 3) {
	                    if (PlayBoard.playBoard[temp1 + 1][temp2+ 1] == 0 && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == 0 && PlayBoard.playBoard[temp1 + 3][temp2+ 3] == Main.UserC) {
		                    if(weight[temp1 + 1][temp2+ 1] > weight[temp1 + 2][temp2+ 2]) {
		                    	superWeight[temp1 + 1][temp2+ 1] += 40; add++;
		                    }else {
		                       weight[temp1 + 2][temp2+ 2] += 40; add++;
		                    }
		                 } 
		                 else if (PlayBoard.playBoard[temp1 - 3][temp2- 3] == 0 && PlayBoard.playBoard[temp1 - 4][temp2- 4] == 0 && PlayBoard.playBoard[temp1 - 5][temp2- 5] == Main.UserC) {
		                    if(weight[temp1 - 3][temp2- 3]  > weight[temp1 - 4][temp2- 4]) {
		                    	superWeight[temp1- 3][temp2- 3] += 40; add++;}
		                    else {
		                    	superWeight[temp1 - 4][temp2- 4] += 40; add++;}
		                    }
	                  }
		                  temp1++;
		                  temp2++;
	                  } else myCount = 0;
	        	 } catch (ArrayIndexOutOfBoundsException e) {}
	         }
	      }
	   }
	   
	   
	   if(add >= 2) return;
	      // 2 (����2) 2 ��� (�´밢\)���� ������ ������ �Ʒ�
	      for (int i = 0; i < 19; i++) {
	          myCount = 0;
	          for (int j = 0; j < 19; j++) {
	             int temp1 = i;
	              int temp2 = j;
	              for (int k = 0; k < 2; k++) {
	                 try {
	                      if (PlayBoard.playBoard[temp1][temp2] == Main.UserC) {
	                            myCount++;
	                            try {
	                               if (myCount == 2) {
	                                   if (PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0 && PlayBoard.playBoard[temp1 + 4][temp2 + 4] == 0 && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == Main.UserC && PlayBoard.playBoard[temp1 + 3][temp2 + 3] == Main.UserC) {
	                                       superWeight[temp1 + 1][temp2 + 1] += 40; add++;
	                                       superWeight[temp1 + 4][temp2 + 4] += 40; add++;
	                           
	                                    } 
	                                    else if (PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0 && PlayBoard.playBoard[temp1 - 2][temp2 - 2] == 0 && PlayBoard.playBoard[temp1 - 3][temp2 - 3] == Main.UserC && PlayBoard.playBoard[temp1 - 4][temp2 - 4] == Main.UserC) {
	                                       superWeight[temp1 + 1][temp2 + 1] += 40; add++;
	                                       superWeight[temp1 - 2][temp2 - 2] += 40; add++;
	                                    }
	                                    else if (PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0 && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == 0 && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == Main.UserC && PlayBoard.playBoard[temp1 + 4][temp2 + 4] == Main.UserC) {
	                                          superWeight[temp1 + 1][temp2 + 1] += 40; add++;
	                                          superWeight[temp1 + 2][temp2 + 2] += 40; add++;
	                                    }
	                                    else if (PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0 && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == 0 && PlayBoard.playBoard[temp1 + 3][temp2 + 3] == Main.UserC && PlayBoard.playBoard[temp1 + 4][temp2 + 4] == Main.UserC) {
	                                          superWeight[temp1 + 1][temp2 + 1] += 40; add++;
	                                          superWeight[temp1 + 2][temp2 + 2] += 40; add++;
	                                    }
	                                    else if (PlayBoard.playBoard[temp1 - 2][temp2 - 2] == 0 && PlayBoard.playBoard[temp1 - 3][temp2 - 3] == 0 && PlayBoard.playBoard[temp1 - 4][temp2 - 4] == Main.UserC && PlayBoard.playBoard[temp1 - 5][temp2 - 5] == Main.UserC) {
	                                          superWeight[temp1 - 2][temp2 - 2] += 40; add++;
	                                          superWeight[temp1 - 3][temp2 - 3] += 40; add++;
	                                    }
	                                    else if (PlayBoard.playBoard[temp1 - 2][temp2 - 2] == 0 && PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0 && PlayBoard.playBoard[temp1 - 3][temp2 - 3] == Main.UserC && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == Main.UserC) {
	                                          superWeight[temp1 - 2][temp2 - 2] += 40; add++;
	                                          superWeight[temp1 + 1][temp2 + 1] += 40; add++;
	                                    }
	                                    else if (PlayBoard.playBoard[temp1 - 4][temp2 - 4] == 0 && PlayBoard.playBoard[temp1 - 2][temp2 - 2] == 0 && PlayBoard.playBoard[temp1 - 3][temp2 - 3] == Main.UserC && PlayBoard.playBoard[temp1 - 5][temp2 - 5] == Main.UserC) {
	                                          superWeight[temp1 - 4][temp2 - 4] += 40; add++;
	                                          superWeight[temp1 - 2][temp2 - 2] += 40; add++;
	                                    }
	                                    else if (PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0 && PlayBoard.playBoard[temp1 - 2][temp2 - 2] == 0 && PlayBoard.playBoard[temp1 + 3][temp2 + 3] == Main.UserC && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == Main.UserC) {
	                                          superWeight[temp1 + 1][temp2 + 1] += 40; add++;
	                                          superWeight[temp1 - 2][temp2 - 2] += 40; add++;
	                                    }
	                               }
	                            } catch (ArrayIndexOutOfBoundsException e) {}
	                            temp1++;
	                            temp2++;
	                         } else myCount = 0;
	                 }catch(ArrayIndexOutOfBoundsException e) {}
	              }
	              }
	          }
	       
	   


	   if(add >= 2) return;
	// 5 ������������ ���ʾƷ�(��밢/) ���
	   for( int j = 0;j<19;j++){
	      myCount = 0;
	      for (int i = 5; i < 19; i++) {
	         int temp1 = i;
	         int temp2 = j;
	         for (int k = 0; k < 5; k++) {
	        	try {
	            if (PlayBoard.playBoard[temp1][temp2] == Main.UserC) {
	               myCount++;
	                  if (myCount == 5) {
	                	  if((temp1 - 1 < 0 || temp2 + 1 > 18) && PlayBoard.playBoard[temp1 + 5][temp2 - 5] == 0) {
								superWeight[temp1 + 5][temp2 - 5] += 40;
								add++;
							}
							else if((temp1 + 5 > 18 || temp2 - 5 < 0) && PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0){
								superWeight[temp1 - 1][temp2 + 1] += 40;
								add++;
							}
							else if (PlayBoard.playBoard[temp1 + 5][temp2 - 5] == 0
	                           && PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0) {
	                    	 superWeight[temp1 + 5][temp2 - 5] += 40; add++;
	                    	 superWeight[temp1 - 1][temp2 + 1] += 40; add++;
	                     } else if (PlayBoard.playBoard[temp1 + 4][temp2 - 4] == 0
	                           || PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0) {
	                        if (PlayBoard.playBoard[temp1 + 5][temp2 - 5] == 0) {
	                        	superWeight[temp1 + 5][temp2 - 5] += 40; add++;
	                        } else if (PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0) {
	                        	superWeight[temp1 - 1][temp2 + 1] += 40; add++;
	                        }
	                     }
	                  }
		                  temp1--;
		                  temp2++;
	                  } else myCount = 0;
	            } catch (ArrayIndexOutOfBoundsException e) {}
	         }
	      }
	   }
	   
	
	   
	   if(add >= 2) return;
	   // 4 ������������ ���ʾƷ�(��밢/) ���
	   for( int j = 0;j<19;j++){
	      myCount = 0;
	      for (int i = 5; i < 19; i++) {
	         int temp1 = i;
	         int temp2 = j;
	         for (int k = 0; k < 4; k++) {
	        	try {
	            if (PlayBoard.playBoard[temp1][temp2] == Main.UserC) {
	               myCount++;
	                  if (myCount == 4) {
	                	  if((temp1 - 1 < 0 || temp2 + 1 > 18) && PlayBoard.playBoard[temp1 + 4][temp2 - 4] == 0) {
								superWeight[temp1 + 4][temp2 - 4] += 40;
								add++;
							}
							else if((temp1 + 4 > 18 || temp2 - 4 < 0) && PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0){
								superWeight[temp1 - 1][temp2 + 1] += 40;
								add++;
							}
							else if (PlayBoard.playBoard[temp1 + 4][temp2 - 4] == 0
	                           && PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0) {
	                    	 superWeight[temp1 + 4][temp2 - 4] += 40; add++;
	                    	 superWeight[temp1 - 1][temp2 + 1] += 40; add++;
	                     } else if (PlayBoard.playBoard[temp1 + 4][temp2 - 4] == 0
	                           || PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0) {
	                        if (PlayBoard.playBoard[temp1 + 4][temp2 - 4] == 0
	                              && PlayBoard.playBoard[temp1 + 5][temp2 - 5] == 0) {
	                        	if(weight[temp1 + 4][temp2 - 4]>weight[temp1 + 5][temp2 - 5]) {
	                        		superWeight[temp1 + 4][temp2 - 4] += 40; add++;
	                        	}else{
	                        		superWeight[temp1 + 5][temp2 - 5] += 40; add++;
	                        	}
	                        } else if (PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0
	                              && PlayBoard.playBoard[temp1 - 2][temp2 + 2] == 0) {
	                        	if(weight[temp1 - 1][temp2 + 1]>weight[temp1 - 1][temp2 + 1]) {
	                        		superWeight[temp1 - 1][temp2 + 1] += 40; add++;
	                        	}else{
	                        		superWeight[temp1 - 1][temp2 + 1] += 40; add++;
	                        	}
	                        }
	                     }
	                  }
		                  temp1--;
		                  temp2++;
	                  } else  myCount = 0;
	        	} catch (ArrayIndexOutOfBoundsException e) {}
	         }
	      }
	   }
	   
	   if(add >= 2) return;
	   // 3(��밢/) 1���� ���
	   for(int j = 0;j<19;j++){
	      myCount = 0;
	      for (int i = 0; i < 19; i++) {
	         int temp1 = i;
	         int temp2 = j;
	         for (int k = 0; k < 3; k++) {
	        	try {
	            if (PlayBoard.playBoard[temp1][temp2] == Main.UserC) {
	               myCount++;
	               
	                  if (myCount == 3) {
	                    if (PlayBoard.playBoard[temp1 - 1][temp2+ 1] == 0 && PlayBoard.playBoard[temp1 - 2][temp2 + 2] == Main.UserC && PlayBoard.playBoard[temp1 - 3][temp2+ 3] == 0) {
		                    if(weight[temp1 - 3][temp2+ 3] > weight[temp1 - 1][temp2+ 1]) {
		                    	superWeight[temp1 - 3][temp2+ 3] += 40; add++;
		                    }else {
		                       weight[temp1 - 1][temp2+ 1] += 40; add++;
		                    }
		                 } 
		                 else if (PlayBoard.playBoard[temp1 + 3][temp2- 3] == 0 && PlayBoard.playBoard[temp1 + 4][temp2- 4] == Main.UserC && PlayBoard.playBoard[temp1 + 5][temp2- 5] == 0) {
		                    if(weight[temp1 + 3][temp2- 3]  > weight[temp1 + 5][temp2 - 5]) {
		                    	superWeight[temp1+ 3][temp2 - 3] += 40; add++;}
		                    else {
		                    	superWeight[temp1 + 5][temp2 - 5] += 40; add++;}
		                    }
	                  
	                  }
		                  temp1--;
		                  temp2++;
	                  } else myCount = 0;
	        	} catch (ArrayIndexOutOfBoundsException e) {}
	         }
	      }
	   }
	   
	   if(add >= 2) return;
	   // 3(��밢/) 2���� 1 ���
	   for(int j = 0;j<19;j++){
	      myCount = 0;
	      for (int i = 0; i < 19; i++) {
	         int temp1 = i;
	         int temp2 = j;
	         for (int k = 0; k < 3; k++) {
	        	try {
	            if (PlayBoard.playBoard[temp1][temp2] == Main.UserC) {
	               myCount++;
	               
	                  if (myCount == 3) {
	                    if (PlayBoard.playBoard[temp1 - 1][temp2+ 1] == 0 && PlayBoard.playBoard[temp1 - 2][temp2 + 2] == 0 && PlayBoard.playBoard[temp1 - 3][temp2+ 3] == Main.UserC) {
		                    if(weight[temp1 - 2][temp2+ 2] > weight[temp1 - 1][temp2+ 1]) {
		                    	superWeight[temp1 - 2][temp2+ 2] += 40; add++;
		                    }else {
		                       weight[temp1 - 1][temp2+ 1] += 40; add++;
		                    }
		                 } 
		                 else if (PlayBoard.playBoard[temp1 + 3][temp2- 3] == 0 && PlayBoard.playBoard[temp1 + 4][temp2- 4] == 0 && PlayBoard.playBoard[temp1 + 5][temp2- 5] == Main.UserC) {
		                    if(weight[temp1 + 3][temp2- 3]  > weight[temp1 + 4][temp2 - 4]) {
		                    	superWeight[temp1+ 3][temp2 - 3] += 40; add++;}
		                    else {
		                    	superWeight[temp1 + 5][temp2 - 5] += 40; add++;}
		                    }
	                  
	                  }
		                  temp1--;
		                  temp2++;
	                  } else myCount = 0;
	        	} catch (ArrayIndexOutOfBoundsException e) {}
	         }
	      }
	   }
	      
	   
	   if(add >= 2) return;
	      // 2 (����2) 2 ��� (��밢/)������ ������ ���� �Ʒ� 
	      for (int i = 0; i < 19; i++) {
	          myCount = 0;
	          for (int j = 0; j < 19; j++) {
	             int temp1 = i;
	              int temp2 = j;
	              for (int k = 0; k < 2; k++) {
	                 try {
	                      if (PlayBoard.playBoard[temp1][temp2] == Main.UserC) {
	                            myCount++;
	                            try {
	                               if (myCount == 2) {
	                                   if (PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0 && PlayBoard.playBoard[temp1 + 2][temp2 - 2] == 0 && PlayBoard.playBoard[temp1 - 2][temp2 + 2] == Main.UserC && PlayBoard.playBoard[temp1 - 3][temp2 + 3] == Main.UserC) {
	                                       superWeight[temp1 - 1][temp2 + 1] += 40; add++;
	                                       superWeight[temp1 + 2][temp2 - 2] += 40; add++;
	                           
	                                    } 
	                                    else if (PlayBoard.playBoard[temp1 + 2][temp2 - 2] == 0 && PlayBoard.playBoard[temp1 + 5][temp2 - 5] == 0 && PlayBoard.playBoard[temp1 + 3][temp2 - 3] == Main.UserC && PlayBoard.playBoard[temp1 + 4][temp2 - 4] == Main.UserC) {
	                                       superWeight[temp1 + 2][temp2 - 2] += 40; add++;
	                                       superWeight[temp1 + 5][temp2 - 5] += 40; add++;
	                                    }
	                                    else if (PlayBoard.playBoard[temp1 + 4][temp2 - 4] == 0 && PlayBoard.playBoard[temp1 + 2][temp2 - 2] == 0 && PlayBoard.playBoard[temp1 + 3][temp2 - 3] == Main.UserC && PlayBoard.playBoard[temp1 + 5][temp2 - 5] == Main.UserC) {
	                                          superWeight[temp1 + 4][temp2 - 4] += 40; add++;
	                                          superWeight[temp1 + 2][temp2 - 2] += 40; add++;
	                                    }
	                                    else if (PlayBoard.playBoard[temp1 + 2][temp2 - 2] == 0 && PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0 && PlayBoard.playBoard[temp1 - 2][temp2 + 2] == Main.UserC && PlayBoard.playBoard[temp1 + 3][temp2 - 3] == Main.UserC) {
	                                          superWeight[temp1 + 2][temp2 - 2] += 40; add++;
	                                          superWeight[temp1 - 1][temp2 + 1] += 40; add++;
	                                    }
	                                    else if (PlayBoard.playBoard[temp1 + 3][temp2 - 3] == 0 && PlayBoard.playBoard[temp1 + 2][temp2 - 2] == 0 && PlayBoard.playBoard[temp1 + 3][temp2 - 3] == Main.UserC && PlayBoard.playBoard[temp1 + 4][temp2 - 4] == Main.UserC) {
	                                          superWeight[temp1 + 3][temp2 - 3] += 40; add++;
	                                          superWeight[temp1 + 2][temp2 - 2] += 40; add++;
	                                    }
	                                    else if (PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0 && PlayBoard.playBoard[temp1 - 2][temp2 + 2] == 0 && PlayBoard.playBoard[temp1 - 3][temp2 + 3] == Main.UserC && PlayBoard.playBoard[temp1 - 4][temp2 + 4] == Main.UserC) {
	                                          superWeight[temp1 - 1][temp2 + 1] += 40; add++;
	                                          superWeight[temp1 - 2][temp2 + 2] += 40; add++;
	                                    }
	                                    else if (PlayBoard.playBoard[temp1 + 2][temp2 - 2] == 0 && PlayBoard.playBoard[temp1 + 4][temp2 - 4] == 0 && PlayBoard.playBoard[temp1 + 3][temp2 - 3] == Main.UserC && PlayBoard.playBoard[temp1 + 5][temp2 - 5] == Main.UserC) {
	                                          superWeight[temp1 + 2][temp2 - 2] += 40; add++;
	                                          superWeight[temp1 + 4][temp2 - 4] += 40; add++;
	                                    }
	                                    else if (PlayBoard.playBoard[temp1 + 2][temp2 - 2] == 0 && PlayBoard.playBoard[temp1 + 5][temp2 - 5] == 0 && PlayBoard.playBoard[temp1 + 3][temp2 - 3] == Main.UserC && PlayBoard.playBoard[temp1 + 4][temp2 - 4] == Main.UserC) {
	                                          superWeight[temp1 + 2][temp2 - 2] += 40; add++;
	                                          superWeight[temp1 + 5][temp2 - 5] += 40; add++;
	                                    }
	                                    else if (PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0 && PlayBoard.playBoard[temp1 + 2][temp2 - 2] == 0 && PlayBoard.playBoard[temp1 - 2][temp2 + 2] == Main.UserC && PlayBoard.playBoard[temp1 - 3][temp2 + 3] == Main.UserC) {
	                                         superWeight[temp1 - 1][temp2 + 1] += 40; add++;
	                                         superWeight[temp1 + 2][temp2 - 2] += 40; add++;
	                                   }
	                               }
	                            } catch (ArrayIndexOutOfBoundsException e) {}
	                            temp1--;
	                            temp2++;
	                         } else myCount = 0;
	                 }catch(ArrayIndexOutOfBoundsException e) {}
	              }
	              }
	          }
	   
	   


	   if(add >= 2) return;
	   
	   ////���� ���� �÷������� ---------------------------------------------------------------------------------
	   //���� �糡���� ���᰹��*2�� ����ġ ���ϱ� 
	   
	   
	   
	   
	   //��� ���� �÷������� --------------------------------------------------------------------------------------
	   //���� �糡���� ����Ƚ����ŭ�� ����ġ ���ϱ�
	   

	   
	   

   }
   

   
   // �Ϲݰ���ġ+Ư������ġ �ǿ��� �ִ� ����ġ�� ã�� x,y �� �������ֱ�
   public static void returnPoint(int[][] board) {
	   
	   int max = 0;
	   for(int i = 0;i<19;i++){
	      for (int j = 0; j < 19; j++) {
	         if (superWeight[i][j]+weight[i][j] > max) {
	            max = superWeight[i][j]+weight[i][j];
	            alphago.x = i;
	            alphago.y = j;
	         }
	      }
	   }
	   
   }
   
    
   // ���� ����ġ ���� �ֿܼ� ���
   public static void showWeight() {
      for (int i = 0; i < 19; i++) {
         for (int j = 0; j < 19; j++) {
        	 System.out.printf("[%2d]", weight[j][i] );
         }
         System.out.println("");
      }
      System.out.println("");
   }

   
}