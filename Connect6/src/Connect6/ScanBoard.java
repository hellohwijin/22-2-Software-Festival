package Connect6;

//판정하는놈...!! 
public class ScanBoard {

    static int x;
    static int y;
    static boolean flag = false; // 승리판정 및 반환용
    static int connect[] = { 1, 1, 1, 1 }; // 0세로 1가로 2우대각 3좌대각 연결점 갯수
    static int i; // 뭐...
    // 각각 인자로 받은 c의 연결점을 찾아 갯수를 connect에 저장한다.

    // 세로 연결점 세기(아래로/위로)
    static void sero(int c) {
        i = 1;
        while (true) {
            if (y + i > 18)
                break; // 인덱스 넘어갈라카면 스탑
            // System.out.println(x+","+y+"="+playBoard[x][y+i] );
            if (PlayBoard.playBoard[x][y + i] == c) {
                if (flag) { // 판정 난 상태라면 빨간테두리 씌워주기
                    PlayBoard.markStone(x, y + i);
                } else {
                    connect[0]++; // 일치시 카운트 증가
                }
            } else
                break; // 불일치시 즉시스탑
            i++;
        }
        i = 1;
        while (true) {
            if (y - i < 0)
                break; // 인덱스 넘어갈라카면 스탑
            if (PlayBoard.playBoard[x][y - i] == c) {
                if (flag) { // 판정 난 상태라면 빨간테두ㄹㅣ 씌워주기
                    PlayBoard.markStone(x, y - i);
                } else {
                    connect[0]++; // 일치시 카운트 증가
                }
            } else
                break; // 불일치시 즉시스탑
            i++;
        }
        // System.out.println("세로연속점"+connect[0]+"개");

    }

    // 가로 연결점 세기
    static void garo(int c) {
        i = 1;
        while (true) {
            if (x + i > 18)
                break; // 인덱스 넘어갈라카면 스탑
            // System.out.println(x+","+y+"="+playBoard[x][y+i] );
            if (PlayBoard.playBoard[x + i][y] == c) {
                if (flag) { // 판정 난 상태라면 빨간테두ㄹㅣ 씌워주기
                    PlayBoard.markStone(x + i, y);
                } else {
                    connect[1]++; // 일치시 카운트 증가
                }
            } else
                break; // 불일치시 즉시스탑
            i++;
        }
        i = 1;
        while (true) {
            if (x - i < 0)
                break; // 인덱스 넘어갈라카면 스탑
            if (PlayBoard.playBoard[x - i][y] == c) {
                if (flag) { // 판정 난 상태라면 빨간테두ㄹㅣ 씌워주기
                    PlayBoard.markStone(x - i, y);
                } else {
                    connect[1]++; // 일치시 카운트 증가
                }
            } else
                break; // 불일치시 즉시스탑
            i++;
        }
        // System.out.println("가로연속점"+connect[1]+"개");

    }

    // 우대각 연결점 세기
    static void wo(int c) {
        i = 1;
        while (true) {
            if (y + i > 18 || x - i < 0)
                break; // 인덱스 넘어갈라카면 스탑
            // System.out.println(x+","+y+"="+playBoard[x][y+i] );
            if (PlayBoard.playBoard[x - i][y + i] == c) {
                if (flag) { // 판정 난 상태라면 빨간테두ㄹㅣ 씌워주기
                    PlayBoard.markStone(x - i, y + i);
                } else {
                    connect[2]++; // 일치시 카운트 증가
                }
            } else
                break; // 불일치시 즉시스탑
            i++;
        }
        i = 1;
        while (true) {
            if (x + i > 18 || y - i < 0)
                break; // 인덱스 넘어갈라카면 스탑
            if (PlayBoard.playBoard[x + i][y - i] == c) {
                if (flag) { // 판정 난 상태라면 빨간테두ㄹㅣ 씌워주기
                    PlayBoard.markStone(x + i, y - i);
                } else {
                    connect[2]++; // 일치시 카운트 증가
                }
            } else
                break; // 불일치시 즉시스탑
            i++;
        }
        // System.out.println("우대각연속점"+connect[2]+"개");

    }

    // 좌대각 연결점 세기
    static void jwa(int c) {
        i = 1;
        while (true) {
            if (y + i > 18 || x + i > 18)
                break; // 인덱스 넘어갈라카면 스탑
            // System.out.println(x+","+y+"="+playBoard[x][y+i] );
            if (PlayBoard.playBoard[x + i][y + i] == c) {
                if (flag) { // 판정 난 상태라면 빨간테두ㄹㅣ 씌워주기
                    PlayBoard.markStone(x + i, y + i);
                } else {
                    connect[3]++; // 일치시 카운트 증가
                }
            } else
                break; // 불일치시 즉시스탑
            i++;
        }
        i = 1;
        while (true) {
            if (x - i < 0 || y - i < 0)
                break; // 인덱스 넘어갈라카면 스탑
            if (PlayBoard.playBoard[x - i][y - i] == c) {
                if (flag) { // 판정 난 상태라면 빨간테두ㄹㅣ 씌워주기
                    PlayBoard.markStone(x - i, y - i);
                } else {
                    connect[3]++; // 일치시 카운트 증가
                }
            } else
                break; // 불일치시 즉시스탑
            i++;
        }
        // System.out.println("좌대각연속점"+connect[3]+"개");

    }

    // 여섯개짜리 연결점이 있는지 판정
    static boolean scan(int c) {
        flag = false;
        // 현시점의 x,y 받아오기
        if (PlayBoard.UserC == c) {
            x = Gui.x;
            y = Gui.y;

        } else {
            x = Alphago.x;
            y = Alphago.y;
        }
        for (i = 0; i < 4; i++) { // 초기화
            connect[i] = 1;
        }
        // 팔방으로 뒤지기..!
        sero(c);
        garo(c);
        wo(c);
        jwa(c);

        // 판별
        for (i = 0; i < 4; i++) {
            if (connect[i] == 6) {
                flag = true;
                System.out.println("승리");
                // 빨갛게 씌우기..!!
                PlayBoard.markStone(x, y);
                if (i == 0) { // 세로..
                    sero(c);
                    return flag;
                } else if (i == 1) { // 가로...
                    garo(c);
                    return flag;
                } else if (i == 2) {// 우대각..
                    wo(c);
                    return flag;
                } else { // 좌대각..
                    jwa(c);
                    return flag;
                }
            }
        }
        return flag;
    }

}
