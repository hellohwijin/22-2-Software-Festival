package Connect6;

public class Alphago {
    // 현재 산출된 이상적 좌표
    static int x;
    static int y;
    // 가중치 설정을 위한 배열
    static int[][] weight = new int[19][19]; // 일반가중치(계속 누적)
    static int[][] superWeight = new int[19][19]; // 특수가중치(매 분석마다 리셋후 시작)

    // 가중치 기본 누적하기 (매 실행 후에)
    public static void addWeight(int x, int y) {
        int n = 1; // 누적할 가중치의 양. 내 돌인지 상대 돌인지에 따라 달라짐.

        if (PlayBoard.playBoard[x][y] == 1)
            n = 2;
        else if (PlayBoard.playBoard[x][y] == 2)
            n = 1;
        else if (PlayBoard.playBoard[x][y] == 5) { // 중립구 취급
            weight[x][y] = -1;
            superWeight[x][y] = -1;
            return;
        } else
            return; // 이미 놓여진곳 취급

        // 팔방을 뒤져서 이미 놓여진 곳만 아니라면 가중치 누적.
        for (int i = x - 1; i < x + 2; i++) {
            for (int j = y - 1; j < y + 2; j++) {
                if (i == x && j == y) { // 본인자리에는 -1
                    weight[x][y] = -1;
                    superWeight[x][y] = -1;
                } else {
                    try {
                        if (PlayBoard.playBoard[i][j] == 0)
                            weight[i][j] += n;
                    } catch (ArrayIndexOutOfBoundsException e) {
                    } // 인덱스 넘어서면 무시
                }
            }
        }

    }

    // 판 읽고 특수가중치 누적하기
    public static void addSuperWeight() {

        // 특수가중치 판 초기화
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                superWeight[i][j] = 0;
            }
        }

        int myCount;
        int add = 0; // 현재 가중치 몇번 누적했는지(두번 넘어가면 그만찾고 리턴. 어차피 한 턴에 두번밖에 못 두니까..)

        //// 놓으면 이길 때(한방승리)
        //// -------------------------------------------------------------------------------------

        if (add >= 2)
            return;
        // 5 세로 공격
        for (int i = 0; i < 19; i++) {
            myCount = 0;
            for (int j = 0; j < 19; j++) {
                try {
                    if (PlayBoard.playBoard[i][j] == PlayBoard.ComC) {
                        myCount++;
                        if (myCount == 5) { // 양끝 중 아무 빈곳에 가중치 왕창 이벤트
                            if (j - 5 < 0 && PlayBoard.playBoard[i][j + 1] == 0) {
                                superWeight[i][j + 1] += 40;
                                add++;
                            }

                            else if (j + 1 > 18 && PlayBoard.playBoard[i][j - 5] == 0) {
                                superWeight[i][j - 5] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i][j - 5] == 0) {
                                superWeight[i][j - 5] += 80;
                                add++;
                            } else if (PlayBoard.playBoard[i][j + 1] == 0) {
                                superWeight[i][j + 1] += 80;
                                add++;
                            }
                        }
                    } else
                        myCount = 0;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }

        if (add >= 2)
            return;
        // 4 세로 공격
        for (int i = 0; i < 19; i++) {
            myCount = 0;
            for (int j = 0; j < 19; j++) {
                try {
                    if (PlayBoard.playBoard[i][j] == PlayBoard.ComC) {
                        myCount++;
                        if (myCount == 4) {
                            if (j - 4 < 0 && PlayBoard.playBoard[i][j + 1] == 0 && PlayBoard.playBoard[i][j + 2] == 0) {
                                superWeight[i][j + 1] += 40;
                                superWeight[i][j + 2] += 40;
                                add++;
                            }

                            else if (j + 1 > 18 && PlayBoard.playBoard[i][j - 4] == 0
                                    && PlayBoard.playBoard[i][j - 5] == 0) {
                                superWeight[i][j - 4] += 40;
                                superWeight[i][j - 5] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i][j - 4] == 0 && PlayBoard.playBoard[i][j + 1] == 0) {
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
                    } else
                        myCount = 0;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }

        if (add >= 2)
            return;
        // 3세로 1 공백 공격
        for (int i = 0; i < 19; i++) {
            myCount = 0;
            for (int j = 0; j < 19; j++) {
                try {
                    if (PlayBoard.playBoard[i][j] == PlayBoard.ComC) {
                        myCount++;
                        if (myCount == 3) { // 세번연속일때
                            // 111010
                            if (PlayBoard.playBoard[i][j + 1] == 0 && PlayBoard.playBoard[i][j + 2] == PlayBoard.ComC
                                    && PlayBoard.playBoard[i][j + 3] == 0) {
                                if (weight[i][j + 3] > weight[i][j + 1]) {
                                    superWeight[i][j + 3] += 40;
                                    add++;
                                } else {
                                    superWeight[i][j + 1] += 40;
                                    add++;
                                }
                            }
                            // 010111
                            else if (PlayBoard.playBoard[i][j - 3] == 0
                                    && PlayBoard.playBoard[i][j - 4] == PlayBoard.ComC
                                    && PlayBoard.playBoard[i][j - 5] == 0) {
                                if (weight[i][j - 3] > weight[i][j - 5]) {
                                    superWeight[i][j - 3] += 40;
                                    add++;
                                } else {
                                    superWeight[i][j - 5] += 40;
                                    add++;
                                }
                            }
                        }
                    } else
                        myCount = 0;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }

        if (add >= 2)
            return;
        // 3세로 2 공백 공격
        for (int i = 0; i < 19; i++) {
            myCount = 0;
            for (int j = 0; j < 19; j++) {
                try {
                    if (PlayBoard.playBoard[i][j] == PlayBoard.ComC) {
                        myCount++;
                        if (myCount == 3) { // 세번연속일때
                            // 111010
                            if (PlayBoard.playBoard[i][j + 1] == 0 && PlayBoard.playBoard[i][j + 2] == 0
                                    && PlayBoard.playBoard[i][j + 3] == PlayBoard.ComC) {
                                if (weight[i][j + 2] > weight[i][j + 1]) {
                                    superWeight[i][j + 2] += 40;
                                    add++;
                                } else {
                                    superWeight[i][j + 1] += 40;
                                    add++;
                                }
                            }
                            // 010111
                            else if (PlayBoard.playBoard[i][j - 3] == 0 && PlayBoard.playBoard[i][j - 4] == 0
                                    && PlayBoard.playBoard[i][j - 5] == PlayBoard.ComC) {
                                if (weight[i][j - 3] > weight[i][j - 4]) {
                                    superWeight[i][j - 3] += 40;
                                    add++;
                                } else {
                                    superWeight[i][j - 4] += 40;
                                    add++;
                                }
                            }
                        }
                    } else
                        myCount = 0;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }

        if (add >= 2)
            return;
        // 2 (공백2) 2 공격 세로
        for (int i = 0; i < 19; i++) {
            myCount = 0;
            for (int j = 0; j < 19; j++) {
                try {
                    if (PlayBoard.playBoard[i][j] == PlayBoard.ComC) {
                        myCount++;
                        if (myCount == 2) {
                            if (PlayBoard.playBoard[i][j + 1] == 0 && PlayBoard.playBoard[i][j + 2] == 0
                                    && PlayBoard.playBoard[i][j + 3] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i][j + 4] == PlayBoard.UserC) {
                                superWeight[i][j + 1] += 40;
                                add++;
                                superWeight[i][j + 2] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i][j - 2] == 0 && PlayBoard.playBoard[i][j - 3] == 0
                                    && PlayBoard.playBoard[i][j - 4] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i][j - 5] == PlayBoard.UserC) {
                                superWeight[i][j - 2] += 40;
                                add++;
                                superWeight[i][j - 3] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i][j - 2] == 0 && PlayBoard.playBoard[i][j + 1] == 0
                                    && PlayBoard.playBoard[i][j + 2] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i][j + 3] == PlayBoard.UserC) {
                                superWeight[i][j - 2] += 40;
                                add++;
                                superWeight[i][j + 1] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i][j - 2] == 0 && PlayBoard.playBoard[i][j - 5] == 0
                                    && PlayBoard.playBoard[i][j - 3] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i][j - 4] == PlayBoard.UserC) {
                                superWeight[i][j - 2] += 40;
                                add++;
                                superWeight[i][j - 5] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i][j - 2] == 0 && PlayBoard.playBoard[i][j - 4] == 0
                                    && PlayBoard.playBoard[i][j - 3] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i][j - 5] == PlayBoard.UserC) {
                                superWeight[i][j - 2] += 40;
                                add++;
                                superWeight[i][j - 4] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i][j - 2] == 0 && PlayBoard.playBoard[i][j + 1] == 0
                                    && PlayBoard.playBoard[i][j - 3] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i][j + 2] == PlayBoard.UserC) {
                                superWeight[i][j - 2] += 40;
                                add++;
                                superWeight[i][j + 1] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i][j + 1] == 0 && PlayBoard.playBoard[i][j + 3] == 0
                                    && PlayBoard.playBoard[i][j + 2] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i][j + 4] == PlayBoard.UserC) {
                                superWeight[i][j + 3] += 40;
                                add++;
                                superWeight[i][j + 1] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i][j + 1] == 0 && PlayBoard.playBoard[i][j + 4] == 0
                                    && PlayBoard.playBoard[i][j + 2] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i][j + 3] == PlayBoard.UserC) {
                                superWeight[i][j + 4] += 40;
                                add++;
                                superWeight[i][j + 1] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i][j + 1] == 0 && PlayBoard.playBoard[i][j - 2] == 0
                                    && PlayBoard.playBoard[i][j - 3] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i][j - 4] == PlayBoard.UserC) {
                                superWeight[i][j - 2] += 40;
                                add++;
                                superWeight[i][j + 1] += 40;
                                add++;
                            }
                        }
                    } else
                        myCount = 0;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }

        if (add >= 2)
            return;
        // 5 가로 공격
        for (int j = 0; j < 19; j++) {
            myCount = 0;
            for (int i = 0; i < 19; i++) {
                try {
                    if (PlayBoard.playBoard[i][j] == PlayBoard.ComC) {
                        myCount++;
                        if (myCount == 5) { // 양끝 중 아무 빈곳에 가중치 왕창 이벤트
                            if (i - 5 < 0 && PlayBoard.playBoard[i + 1][j] == 0) {
                                superWeight[i + 1][j] += 40;
                                add++;
                            }

                            else if (i + 1 > 18 && PlayBoard.playBoard[i - 5][j] == 0) {
                                superWeight[i - 5][j] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i - 5][j] == 0) {
                                superWeight[i - 5][j] += 80;
                                add++;
                            } else if (PlayBoard.playBoard[i + 1][j] == 0) {
                                superWeight[i + 1][j] += 80;
                                add++;
                            }
                        }
                    } else
                        myCount = 0;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }

        if (add >= 2)
            return;
        // 4 가로 공격
        for (int j = 0; j < 19; j++) {
            myCount = 0;
            for (int i = 0; i < 19; i++) {
                try {
                    if (PlayBoard.playBoard[i][j] == PlayBoard.ComC) {
                        myCount++;
                        if (myCount == 4) {
                            if (i - 4 < 0 && PlayBoard.playBoard[i + 1][j] == 0 && PlayBoard.playBoard[i + 2][j] == 0) {
                                superWeight[i + 1][j] += 40;
                                superWeight[i + 2][j] += 40;
                                add++;
                            }

                            else if (i + 1 > 18 && PlayBoard.playBoard[i - 5][j] == 0
                                    && PlayBoard.playBoard[i - 4][j] == 0) {
                                superWeight[i - 5][j] += 40;
                                superWeight[i - 4][j] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i - 4][j] == 0 && PlayBoard.playBoard[i + 1][j] == 0) {
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
                    } else
                        myCount = 0;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }

        if (add >= 2)
            return;
        // 3가로 1 공백 공격
        for (int j = 0; j < 19; j++) {
            myCount = 0;
            for (int i = 0; i < 19; i++) {
                try {
                    if (PlayBoard.playBoard[i][j] == PlayBoard.ComC) {
                        myCount++;
                        if (myCount == 3) {
                            if (PlayBoard.playBoard[i + 1][j] == 0 && PlayBoard.playBoard[i + 2][j] == PlayBoard.ComC
                                    && PlayBoard.playBoard[i + 3][j] == 0) {
                                if (weight[i + 3][j] > weight[i + 1][j]) {
                                    superWeight[i + 3][j] += 40;
                                    add++;
                                } else {
                                    superWeight[i + 1][j] += 40;
                                    add++;
                                }
                            } else if (PlayBoard.playBoard[i - 3][j] == 0
                                    && PlayBoard.playBoard[i - 4][j] == PlayBoard.ComC
                                    && PlayBoard.playBoard[i - 5][j] == 0) {
                                if (weight[i - 3][j] > weight[i - 5][j]) {
                                    superWeight[i - 3][j] += 40;
                                    add++;
                                } else {
                                    superWeight[i - 5][j] += 40;
                                    add++;
                                }
                            }
                        }
                    } else
                        myCount = 0;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }

        if (add >= 2)
            return;
        // 3가로 2 공백 공격
        for (int j = 0; j < 19; j++) {
            myCount = 0;
            for (int i = 0; i < 19; i++) {
                try {
                    if (PlayBoard.playBoard[i][j] == PlayBoard.ComC) {
                        myCount++;
                        if (myCount == 3) {
                            if (PlayBoard.playBoard[i + 1][j] == 0 && PlayBoard.playBoard[i + 2][j] == 0
                                    && PlayBoard.playBoard[i + 3][j] == PlayBoard.ComC) {
                                if (weight[i + 2][j] > weight[i + 1][j]) {
                                    superWeight[i + 2][j] += 40;
                                    add++;
                                } else {
                                    superWeight[i + 1][j] += 40;
                                    add++;
                                }
                            } else if (PlayBoard.playBoard[i - 3][j] == 0 && PlayBoard.playBoard[i - 4][j] == 0
                                    && PlayBoard.playBoard[i - 5][j] == PlayBoard.ComC) {
                                if (weight[i - 3][j] > weight[i - 4][j]) {
                                    superWeight[i - 3][j] += 40;
                                    add++;
                                } else {
                                    superWeight[i - 4][j] += 40;
                                    add++;
                                }
                            }
                        }
                    } else
                        myCount = 0;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }

        if (add >= 2)
            return;
        // 2 (공백2) 2 공격 가로
        for (int j = 0; j < 19; j++) {
            myCount = 0;
            for (int i = 0; i < 19; i++) {
                try {
                    if (PlayBoard.playBoard[i][j] == PlayBoard.ComC) {
                        myCount++;
                        if (myCount == 2) {
                            if (PlayBoard.playBoard[i + 1][j] == 0 && PlayBoard.playBoard[i + 2][j] == 0
                                    && PlayBoard.playBoard[i + 3][j] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i + 4][j] == PlayBoard.UserC) {
                                superWeight[i + 1][j] += 40;
                                add++;
                                superWeight[i + 2][j] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i - 2][j] == 0 && PlayBoard.playBoard[i - 3][j] == 0
                                    && PlayBoard.playBoard[i - 4][j] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i - 5][j] == PlayBoard.UserC) {
                                superWeight[i - 2][j] += 40;
                                add++;
                                superWeight[i - 3][j] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i - 2][j] == 0 && PlayBoard.playBoard[i + 1][j] == 0
                                    && PlayBoard.playBoard[i + 2][j] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i + 3][j] == PlayBoard.UserC) {
                                superWeight[i - 2][j] += 40;
                                add++;
                                superWeight[i + 1][j] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i - 2][j] == 0 && PlayBoard.playBoard[i - 5][j] == 0
                                    && PlayBoard.playBoard[i - 3][j] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i - 4][j] == PlayBoard.UserC) {
                                superWeight[i - 2][j] += 40;
                                add++;
                                superWeight[i - 5][j] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i - 2][j] == 0 && PlayBoard.playBoard[i - 4][j] == 0
                                    && PlayBoard.playBoard[i - 3][j] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i - 5][j] == PlayBoard.UserC) {
                                superWeight[i - 2][j] += 40;
                                add++;
                                superWeight[i - 4][j] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i - 2][j] == 0 && PlayBoard.playBoard[i + 1][j] == 0
                                    && PlayBoard.playBoard[i - 3][j] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i + 2][j] == PlayBoard.UserC) {
                                superWeight[i - 2][j] += 40;
                                add++;
                                superWeight[i + 1][j] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i + 1][j] == 0 && PlayBoard.playBoard[i + 3][j] == 0
                                    && PlayBoard.playBoard[i + 2][j] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i + 4][j] == PlayBoard.UserC) {
                                superWeight[i + 3][j] += 40;
                                add++;
                                superWeight[i + 1][j] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i + 1][j] == 0 && PlayBoard.playBoard[i + 4][j] == 0
                                    && PlayBoard.playBoard[i + 2][j] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i + 3][j] == PlayBoard.UserC) {
                                superWeight[i + 4][j] += 40;
                                add++;
                                superWeight[i + 1][j] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i + 1][j] == 0 && PlayBoard.playBoard[i - 2][j] == 0
                                    && PlayBoard.playBoard[i - 3][j] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i - 4][j] == PlayBoard.UserC) {
                                superWeight[i - 2][j] += 40;
                                add++;
                                superWeight[i + 1][j] += 40;
                                add++;
                            }
                        }
                    } else
                        myCount = 0;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }

        if (add >= 2)
            return;
        // 5 왼쪽 위에서 오른쪽 아래(좌대각\) 공격
        for (int i = 0; i < 19; i++) {
            myCount = 0;
            for (int j = 0; j < 19; j++) {
                int temp1 = i;
                int temp2 = j;
                for (int k = 0; k < 5; k++) {
                    try {
                        if (PlayBoard.playBoard[temp1][temp2] == PlayBoard.ComC) {
                            myCount++;
                            if (myCount == 5) {
                                // 양끝뚫림
                                if ((temp1 - 5 < 0 || temp2 - 5 < 0)
                                        && PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0) {
                                    superWeight[temp1 + 1][temp2 + 1] += 40;
                                    add++;
                                } else if ((temp1 + 1 > 18 || temp2 + 1 > 18)
                                        && PlayBoard.playBoard[temp1 - 5][temp2 - 5] == 0) {
                                    superWeight[temp1 - 5][temp2 - 5] += 40;
                                    add++;
                                } else if (PlayBoard.playBoard[temp1 - 5][temp2 - 5] == 0
                                        && PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0) {
                                    superWeight[temp1 - 5][temp2 - 5] += 80; // 양끝막기
                                    superWeight[temp1 + 1][temp2 + 1] += 80;
                                    add++;
                                    add++;
                                }
                                // 한끝뚫림 - 한쪽막기
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
                        } else
                            myCount = 0;
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
            }
        }

        if (add >= 2)
            return;
        // 4 왼쪽 위에서 오른쪽 아래(좌대각\) 공격
        for (int i = 0; i < 19; i++) {
            myCount = 0;
            for (int j = 0; j < 19; j++) {
                int temp1 = i;
                int temp2 = j;
                for (int k = 0; k < 4; k++) {
                    try {
                        if (PlayBoard.playBoard[temp1][temp2] == PlayBoard.ComC) {
                            myCount++;
                            if (myCount == 4) {
                                if ((temp1 - 4 < 0 || temp2 - 4 < 0) && PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0
                                        && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == 0) {
                                    superWeight[temp1 + 1][temp2 + 1] += 40;
                                    superWeight[temp1 + 2][temp2 + 2] += 40;
                                    add++;
                                } else if ((temp1 + 1 > 18 || temp2 + 1 > 18)
                                        && PlayBoard.playBoard[temp1 - 5][temp2 - 5] == 0
                                        && PlayBoard.playBoard[temp1 - 4][temp2 - 4] == 0) {
                                    superWeight[temp1 - 5][temp2 - 5] += 40;
                                    superWeight[temp1 - 4][temp2 - 4] += 40;
                                    add++;
                                } else if (PlayBoard.playBoard[temp1 - 4][temp2 - 4] == 0
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
                        } else
                            myCount = 0;
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
            }
        }

        if (add >= 2)
            return;
        // 3(좌대각\) 1공백 공격
        for (int i = 0; i < 19; i++) {
            myCount = 0;
            for (int j = 0; j < 19; j++) {
                int temp1 = i;
                int temp2 = j;
                for (int k = 0; k < 3; k++) {
                    try {
                        if (PlayBoard.playBoard[temp1][temp2] == PlayBoard.ComC) {
                            myCount++;
                            if (myCount == 3) {
                                if (PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0
                                        && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == PlayBoard.UserC
                                        && PlayBoard.playBoard[temp1 + 3][temp2 + 3] == 0) {
                                    if (weight[temp1 + 3][temp2 + 3] > weight[temp1 + 1][temp2 + 1]) {
                                        superWeight[temp1 + 3][temp2 + 3] += 40;
                                        add++;
                                    } else {
                                        weight[temp1 + 1][temp2 + 1] += 40;
                                        add++;
                                    }
                                } else if (PlayBoard.playBoard[temp1 - 3][temp2 - 3] == 0
                                        && PlayBoard.playBoard[temp1 - 4][temp2 - 4] == PlayBoard.UserC
                                        && PlayBoard.playBoard[temp1 - 5][temp2 - 5] == 0) {
                                    if (weight[temp1 - 3][temp2 - 3] > weight[temp1 - 5][temp2 - 5]) {
                                        superWeight[temp1 - 3][temp2 - 3] += 40;
                                        add++;
                                    } else {
                                        superWeight[temp1 - 5][temp2 - 5] += 40;
                                        add++;
                                    }
                                }
                            }
                            temp1++;
                            temp2++;
                        } else
                            myCount = 0;
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
            }
        }

        if (add >= 2)
            return;
        // 3(좌대각\) 2공백 1 공격
        for (int i = 0; i < 19; i++) {
            myCount = 0;
            for (int j = 0; j < 19; j++) {
                int temp1 = i;
                int temp2 = j;
                for (int k = 0; k < 3; k++) {
                    try {
                        if (PlayBoard.playBoard[temp1][temp2] == PlayBoard.ComC) {
                            myCount++;
                            if (myCount == 3) {
                                if (PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0
                                        && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == 0
                                        && PlayBoard.playBoard[temp1 + 3][temp2 + 3] == PlayBoard.ComC) {

                                    superWeight[temp1 + 1][temp2 + 1] += 40;
                                    add++;

                                    superWeight[temp1 + 2][temp2 + 2] += 40;
                                    add++;

                                } else if (PlayBoard.playBoard[temp1 - 3][temp2 - 3] == 0
                                        && PlayBoard.playBoard[temp1 - 4][temp2 - 4] == 0
                                        && PlayBoard.playBoard[temp1 - 5][temp2 - 5] == PlayBoard.ComC) {

                                    superWeight[temp1 - 3][temp2 - 3] += 40;
                                    add++;

                                    superWeight[temp1 - 4][temp2 - 4] += 40;
                                    add++;
                                }
                            }
                            temp1++;
                            temp2++;
                        } else
                            myCount = 0;
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
            }
        }

        if (add >= 2)
            return;
        // 2 (공백2) 2 공격 (좌대각\)왼쪽 위에서 오른쪽 아래
        for (int i = 0; i < 19; i++) {
            myCount = 0;
            for (int j = 0; j < 19; j++) {
                int temp1 = i;
                int temp2 = j;
                for (int k = 0; k < 2; k++) {
                    try {
                        if (PlayBoard.playBoard[temp1][temp2] == PlayBoard.ComC) {
                            myCount++;
                            try {
                                if (myCount == 2) {
                                    if (PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0
                                            && PlayBoard.playBoard[temp1 + 4][temp2 + 4] == 0
                                            && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == PlayBoard.UserC
                                            && PlayBoard.playBoard[temp1 + 3][temp2 + 3] == PlayBoard.UserC) {
                                        superWeight[temp1 + 1][temp2 + 1] += 40;
                                        add++;
                                        superWeight[temp1 + 4][temp2 + 4] += 40;
                                        add++;
                                    } else if (PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0
                                            && PlayBoard.playBoard[temp1 - 2][temp2 - 2] == 0
                                            && PlayBoard.playBoard[temp1 - 3][temp2 - 3] == PlayBoard.UserC
                                            && PlayBoard.playBoard[temp1 - 4][temp2 - 4] == PlayBoard.UserC) {
                                        superWeight[temp1 + 1][temp2 + 1] += 40;
                                        add++;
                                        superWeight[temp1 - 2][temp2 - 2] += 40;
                                        add++;
                                    } else if (PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0
                                            && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == 0
                                            && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == PlayBoard.UserC
                                            && PlayBoard.playBoard[temp1 + 4][temp2 + 4] == PlayBoard.UserC) {
                                        superWeight[temp1 + 1][temp2 + 1] += 40;
                                        add++;
                                        superWeight[temp1 + 2][temp2 + 2] += 40;
                                        add++;
                                    } else if (PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0
                                            && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == 0
                                            && PlayBoard.playBoard[temp1 + 3][temp2 + 3] == PlayBoard.UserC
                                            && PlayBoard.playBoard[temp1 + 4][temp2 + 4] == PlayBoard.UserC) {
                                        superWeight[temp1 + 1][temp2 + 1] += 40;
                                        add++;
                                        superWeight[temp1 + 2][temp2 + 2] += 40;
                                        add++;
                                    } else if (PlayBoard.playBoard[temp1 - 2][temp2 - 2] == 0
                                            && PlayBoard.playBoard[temp1 - 3][temp2 - 3] == 0
                                            && PlayBoard.playBoard[temp1 - 4][temp2 - 4] == PlayBoard.UserC
                                            && PlayBoard.playBoard[temp1 - 5][temp2 - 5] == PlayBoard.UserC) {
                                        superWeight[temp1 - 2][temp2 - 2] += 40;
                                        add++;
                                        superWeight[temp1 - 3][temp2 - 3] += 40;
                                        add++;
                                    } else if (PlayBoard.playBoard[temp1 - 2][temp2 - 2] == 0
                                            && PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0
                                            && PlayBoard.playBoard[temp1 - 3][temp2 - 3] == PlayBoard.UserC
                                            && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == PlayBoard.UserC) {
                                        superWeight[temp1 - 2][temp2 - 2] += 40;
                                        add++;
                                        superWeight[temp1 + 1][temp2 + 1] += 40;
                                        add++;
                                    } else if (PlayBoard.playBoard[temp1 - 4][temp2 - 4] == 0
                                            && PlayBoard.playBoard[temp1 - 2][temp2 - 2] == 0
                                            && PlayBoard.playBoard[temp1 - 3][temp2 - 3] == PlayBoard.UserC
                                            && PlayBoard.playBoard[temp1 - 5][temp2 - 5] == PlayBoard.UserC) {
                                        superWeight[temp1 - 4][temp2 - 4] += 40;
                                        add++;
                                        superWeight[temp1 - 2][temp2 - 2] += 40;
                                        add++;
                                    } else if (PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0
                                            && PlayBoard.playBoard[temp1 - 2][temp2 - 2] == 0
                                            && PlayBoard.playBoard[temp1 + 3][temp2 + 3] == PlayBoard.UserC
                                            && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == PlayBoard.UserC) {
                                        superWeight[temp1 + 1][temp2 + 1] += 40;
                                        add++;
                                        superWeight[temp1 - 2][temp2 - 2] += 40;
                                        add++;
                                    }
                                }
                            } catch (ArrayIndexOutOfBoundsException e) {
                            }
                            temp1++;
                            temp2++;
                        } else
                            myCount = 0;
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
            }
        }

        if (add >= 2)
            return;
        // 5 오른쪽위에서 왼쪽아래 (우대각/) 공격
        for (int j = 0; j < 19; j++) {
            myCount = 0;
            for (int i = 5; i < 19; i++) {
                int temp1 = i;
                int temp2 = j;
                for (int k = 0; k < 5; k++) {
                    try {
                        if (PlayBoard.playBoard[temp1][temp2] == PlayBoard.ComC) {
                            myCount++;
                            if (myCount == 5) {
                                if ((temp1 - 1 < 0 || temp2 + 1 > 18)
                                        && PlayBoard.playBoard[temp1 + 5][temp2 - 5] == 0) {
                                    superWeight[temp1 + 5][temp2 - 5] += 40;
                                    add++;
                                } else if ((temp1 + 5 > 18 || temp2 - 5 < 0)
                                        && PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0) {
                                    superWeight[temp1 - 1][temp2 + 1] += 40;
                                    add++;
                                } else if (PlayBoard.playBoard[temp1 + 5][temp2 - 5] == 0
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
                        } else
                            myCount = 0;
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
            }
        }

        if (add >= 2)
            return;
        // 4 오른쪽위에서 왼쪽아래 (우대각/) 공격
        for (int j = 0; j < 19; j++) {
            myCount = 0;
            for (int i = 5; i < 19; i++) {
                int temp1 = i;
                int temp2 = j;
                for (int k = 0; k < 4; k++) {
                    try {
                        if (PlayBoard.playBoard[temp1][temp2] == PlayBoard.ComC) {
                            myCount++;
                            if (myCount == 4) {
                                if ((temp1 - 1 < 0 || temp2 + 1 > 18) && PlayBoard.playBoard[temp1 + 5][temp2 - 5] == 0
                                        && PlayBoard.playBoard[temp1 + 4][temp2 - 4] == 0) {
                                    superWeight[temp1 + 5][temp2 - 5] += 40;
                                    superWeight[temp1 + 4][temp2 - 4] += 40;
                                    add++;
                                } else if ((temp1 + 4 > 18 || temp2 - 4 < 0)
                                        && PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0
                                        && PlayBoard.playBoard[temp1 - 2][temp2 + 2] == 0) {
                                    superWeight[temp1 - 1][temp2 + 1] += 40;
                                    superWeight[temp1 - 2][temp2 + 2] += 40;
                                    add++;
                                } else if (PlayBoard.playBoard[temp1 + 4][temp2 - 4] == 0
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
                        } else
                            myCount = 0;
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
            }
        }

        if (add >= 2)
            return;
        // 3(우대각/) 1공백 공격
        for (int j = 0; j < 19; j++) {
            myCount = 0;
            for (int i = 0; i < 19; i++) {
                int temp1 = i;
                int temp2 = j;
                for (int k = 0; k < 3; k++) {
                    try {
                        if (PlayBoard.playBoard[temp1][temp2] == PlayBoard.ComC) {
                            myCount++;
                            if (myCount == 3) {
                                if (PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0
                                        && PlayBoard.playBoard[temp1 - 2][temp2 + 2] == PlayBoard.UserC
                                        && PlayBoard.playBoard[temp1 - 3][temp2 + 3] == 0) {
                                    if (weight[temp1 - 3][temp2 + 3] > weight[temp1 - 1][temp2 + 1]) {
                                        superWeight[temp1 - 3][temp2 + 3] += 40;
                                        add++;
                                    } else {
                                        superWeight[temp1 - 1][temp2 + 1] += 40;
                                        add++;
                                    }
                                } else if (PlayBoard.playBoard[temp1 + 3][temp2 - 3] == 0
                                        && PlayBoard.playBoard[temp1 + 4][temp2 - 4] == PlayBoard.UserC
                                        && PlayBoard.playBoard[temp1 + 5][temp2 - 5] == 0) {
                                    if (weight[temp1 + 3][temp2 - 3] > weight[temp1 + 5][temp2 - 5]) {
                                        superWeight[temp1 + 3][temp2 - 3] += 40;
                                        add++;
                                    } else {
                                        superWeight[temp1 + 5][temp2 - 5] += 40;
                                        add++;
                                    }
                                }
                            }
                            temp1--;
                            temp2++;
                        } else
                            myCount = 0;
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
            }
        }

        if (add >= 2)
            return;
        // 3(우대각/) 2공백 1 공격
        for (int j = 0; j < 19; j++) {
            myCount = 0;
            for (int i = 0; i < 19; i++) {
                int temp1 = i;
                int temp2 = j;
                for (int k = 0; k < 3; k++) {
                    try {
                        if (PlayBoard.playBoard[temp1][temp2] == PlayBoard.ComC) {
                            myCount++;

                            if (myCount == 3) {
                                if (PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0
                                        && PlayBoard.playBoard[temp1 - 2][temp2 + 2] == 0
                                        && PlayBoard.playBoard[temp1 - 3][temp2 + 3] == PlayBoard.ComC) {

                                    superWeight[temp1 - 2][temp2 + 2] += 40;
                                    add++;

                                    superWeight[temp1 - 1][temp2 + 1] += 40;
                                    add++;

                                } else if (PlayBoard.playBoard[temp1 + 3][temp2 - 3] == 0
                                        && PlayBoard.playBoard[temp1 + 4][temp2 - 4] == 0
                                        && PlayBoard.playBoard[temp1 + 5][temp2 - 5] == PlayBoard.ComC) {

                                    superWeight[temp1 + 3][temp2 - 3] += 40;
                                    add++;

                                    superWeight[temp1 + 4][temp2 - 4] += 40;
                                    add++;

                                }
                            }
                            temp1--;
                            temp2++;
                        } else
                            myCount = 0;
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
            }
        }

        if (add >= 2)
            return;
        // 2 (공백2) 2 공격 (우대각/)오른쪽 위에서 왼쪽 아래
        for (int i = 0; i < 19; i++) {
            myCount = 0;
            for (int j = 0; j < 19; j++) {
                int temp1 = i;
                int temp2 = j;
                for (int k = 0; k < 2; k++) {
                    try {
                        if (PlayBoard.playBoard[temp1][temp2] == PlayBoard.ComC) {
                            myCount++;
                            try {
                                if (myCount == 2) {
                                    if (PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0
                                            && PlayBoard.playBoard[temp1 + 2][temp2 - 2] == 0
                                            && PlayBoard.playBoard[temp1 - 2][temp2 + 2] == PlayBoard.UserC
                                            && PlayBoard.playBoard[temp1 - 3][temp2 + 3] == PlayBoard.UserC) {
                                        superWeight[temp1 - 1][temp2 + 1] += 40;
                                        add++;
                                        superWeight[temp1 + 2][temp2 - 2] += 40;
                                        add++;
                                    } else if (PlayBoard.playBoard[temp1 + 2][temp2 - 2] == 0
                                            && PlayBoard.playBoard[temp1 + 5][temp2 - 5] == 0
                                            && PlayBoard.playBoard[temp1 + 3][temp2 - 3] == PlayBoard.UserC
                                            && PlayBoard.playBoard[temp1 + 4][temp2 - 4] == PlayBoard.UserC) {
                                        superWeight[temp1 + 2][temp2 - 2] += 40;
                                        add++;
                                        superWeight[temp1 + 5][temp2 - 5] += 40;
                                        add++;
                                    } else if (PlayBoard.playBoard[temp1 + 4][temp2 - 4] == 0
                                            && PlayBoard.playBoard[temp1 + 2][temp2 - 2] == 0
                                            && PlayBoard.playBoard[temp1 + 3][temp2 - 3] == PlayBoard.UserC
                                            && PlayBoard.playBoard[temp1 + 5][temp2 - 5] == PlayBoard.UserC) {
                                        superWeight[temp1 + 4][temp2 - 4] += 40;
                                        add++;
                                        superWeight[temp1 + 2][temp2 - 2] += 40;
                                        add++;
                                    } else if (PlayBoard.playBoard[temp1 + 2][temp2 - 2] == 0
                                            && PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0
                                            && PlayBoard.playBoard[temp1 - 2][temp2 + 2] == PlayBoard.UserC
                                            && PlayBoard.playBoard[temp1 + 3][temp2 - 3] == PlayBoard.UserC) {
                                        superWeight[temp1 + 2][temp2 - 2] += 40;
                                        add++;
                                        superWeight[temp1 - 1][temp2 + 1] += 40;
                                        add++;
                                    } else if (PlayBoard.playBoard[temp1 + 3][temp2 - 3] == 0
                                            && PlayBoard.playBoard[temp1 + 2][temp2 - 2] == 0
                                            && PlayBoard.playBoard[temp1 + 3][temp2 - 3] == PlayBoard.UserC
                                            && PlayBoard.playBoard[temp1 + 4][temp2 - 4] == PlayBoard.UserC) {
                                        superWeight[temp1 + 3][temp2 - 3] += 40;
                                        add++;
                                        superWeight[temp1 + 2][temp2 - 2] += 40;
                                        add++;
                                    } else if (PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0
                                            && PlayBoard.playBoard[temp1 - 2][temp2 + 2] == 0
                                            && PlayBoard.playBoard[temp1 - 3][temp2 + 3] == PlayBoard.UserC
                                            && PlayBoard.playBoard[temp1 - 4][temp2 + 4] == PlayBoard.UserC) {
                                        superWeight[temp1 - 1][temp2 + 1] += 40;
                                        add++;
                                        superWeight[temp1 - 2][temp2 + 2] += 40;
                                        add++;
                                    } else if (PlayBoard.playBoard[temp1 + 2][temp2 - 2] == 0
                                            && PlayBoard.playBoard[temp1 + 4][temp2 - 4] == 0
                                            && PlayBoard.playBoard[temp1 + 3][temp2 - 3] == PlayBoard.UserC
                                            && PlayBoard.playBoard[temp1 + 5][temp2 - 5] == PlayBoard.UserC) {
                                        superWeight[temp1 + 2][temp2 - 2] += 40;
                                        add++;
                                        superWeight[temp1 + 4][temp2 - 4] += 40;
                                        add++;
                                    } else if (PlayBoard.playBoard[temp1 + 2][temp2 - 2] == 0
                                            && PlayBoard.playBoard[temp1 + 5][temp2 - 5] == 0
                                            && PlayBoard.playBoard[temp1 + 3][temp2 - 3] == PlayBoard.UserC
                                            && PlayBoard.playBoard[temp1 + 4][temp2 - 4] == PlayBoard.UserC) {
                                        superWeight[temp1 + 2][temp2 - 2] += 40;
                                        add++;
                                        superWeight[temp1 + 5][temp2 - 5] += 40;
                                        add++;
                                    } else if (PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0
                                            && PlayBoard.playBoard[temp1 + 2][temp2 - 2] == 0
                                            && PlayBoard.playBoard[temp1 - 2][temp2 + 2] == PlayBoard.UserC
                                            && PlayBoard.playBoard[temp1 - 3][temp2 + 3] == PlayBoard.UserC) {
                                        superWeight[temp1 - 1][temp2 + 1] += 40;
                                        add++;
                                        superWeight[temp1 + 2][temp2 - 2] += 40;
                                        add++;
                                    }
                                }
                            } catch (ArrayIndexOutOfBoundsException e) {
                            }
                            temp1--;
                            temp2++;
                        } else
                            myCount = 0;
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
            }
        }

        //// 안놓으면 질 때, 한방방어
        //// ----------------------------------------------------------------------------------

        if (add >= 2)
            return;
        // 5 세로 방어
        for (int i = 0; i < 19; i++) {
            myCount = 0;
            for (int j = 0; j < 19; j++) {
                try {
                    if (PlayBoard.playBoard[i][j] == PlayBoard.UserC) {
                        myCount++;
                        if (myCount == 5) {
                            // 양쪽 다 비었으면 양쪽 시급하게 막고
                            if (j - 5 < 0 && PlayBoard.playBoard[i][j + 1] == 0) {
                                superWeight[i][j + 1] += 40;
                                add++;
                            }

                            else if (j + 1 > 18 && PlayBoard.playBoard[i][j - 5] == 0) {
                                superWeight[i][j - 5] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i][j - 5] == 0 && PlayBoard.playBoard[i][j + 1] == 0) {
                                superWeight[i][j - 5] += 40;
                                add++;
                                superWeight[i][j + 1] += 40;
                                add++;
                            }
                            // 둘중 한쪽만 비었으면 거기 막기
                            else if (PlayBoard.playBoard[i][j - 5] == 0) {
                                superWeight[i][j - 5] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i][j + 1] == 0) {
                                superWeight[i][j + 1] += 40;
                                add++;
                            }
                        }
                    } else
                        myCount = 0;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }

        if (add >= 2)
            return;
        // 4 세로 방어
        for (int i = 0; i < 19; i++) {
            myCount = 0;
            for (int j = 0; j < 19; j++) {
                try {
                    if (PlayBoard.playBoard[i][j] == PlayBoard.UserC) {
                        myCount++;
                        if (myCount == 4) { // 양끝 뚫려있으면 양끝에 가중치
                            if (j - 4 < 0 && PlayBoard.playBoard[i][j + 1] == 0) {
                                superWeight[i][j + 1] += 40;
                                add++;
                            }

                            else if (j + 1 > 18 && PlayBoard.playBoard[i][j - 4] == 0) {
                                superWeight[i][j - 4] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i][j - 4] == 0 && PlayBoard.playBoard[i][j + 1] == 0) {
                                superWeight[i][j - 4] += 40;
                                add++;
                                superWeight[i][j + 1] += 40;
                                add++;
                            } // 한쪽만 뚫려있으면
                            else if (PlayBoard.playBoard[i][j - 4] == 0) {
                                if (PlayBoard.playBoard[i][j - 5] == 0) { // 한쪽이 더 뚫려있으면 둘중 이득인곳에 두고
                                    if (weight[i][j - 4] > weight[i][j - 5]) {
                                        superWeight[i][j - 4] += 40;
                                        add++;
                                    } else {
                                        superWeight[i][j - 5] += 40;
                                        add++;
                                    }
                                } else { // 아니면 거따두고
                                    superWeight[i][j - 4] += 40;
                                    add++;
                                }
                            } else if (PlayBoard.playBoard[i][j + 1] == 0) {
                                if (PlayBoard.playBoard[i][j + 2] == 0) {
                                    if (weight[i][j + 2] > weight[i][j + 1]) {
                                        superWeight[i][j + 2] += 40;
                                        add++;
                                    } else {
                                        superWeight[i][j + 1] += 40;
                                        add++;
                                    }
                                } else {
                                    superWeight[i][j + 1] += 40;
                                    add++;
                                }
                            }
                        }
                    } else
                        myCount = 0;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }

        if (add >= 2)
            return;
        // 3세로 1 공백 방어
        for (int i = 0; i < 19; i++) {
            myCount = 0;
            for (int j = 0; j < 19; j++) {
                try {
                    if (PlayBoard.playBoard[i][j] == PlayBoard.UserC) {
                        myCount++;
                        if (myCount == 3) { // 세번연속일때
                            // 111010
                            if (PlayBoard.playBoard[i][j + 1] == 0 && PlayBoard.playBoard[i][j + 2] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i][j + 3] == 0) {
                                if (weight[i][j + 3] > weight[i][j + 1]) {
                                    superWeight[i][j + 3] += 40;
                                    add++;
                                } else {
                                    weight[i][j + 1] += 40;
                                    add++;
                                }
                            }
                            // 010111
                            else if (PlayBoard.playBoard[i][j - 3] == 0
                                    && PlayBoard.playBoard[i][j - 4] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i][j - 5] == 0) {
                                if (weight[i][j - 3] > weight[i][j - 5]) {
                                    superWeight[i][j - 3] += 40;
                                    add++;
                                } else {
                                    superWeight[i][j - 5] += 40;
                                    add++;
                                }
                            }
                        }
                    } else
                        myCount = 0;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }

        if (add >= 2)
            return;
        // 3세로 2 공백 방어
        for (int i = 0; i < 19; i++) {
            myCount = 0;
            for (int j = 0; j < 19; j++) {
                try {
                    if (PlayBoard.playBoard[i][j] == PlayBoard.UserC) {
                        myCount++;
                        if (myCount == 3) { // 세번연속일때
                            // 111001
                            if (PlayBoard.playBoard[i][j + 1] == 0 && PlayBoard.playBoard[i][j + 2] == 0
                                    && PlayBoard.playBoard[i][j + 3] == PlayBoard.UserC) {
                                if (weight[i][j + 2] > weight[i][j + 1]) {
                                    superWeight[i][j + 2] += 40;
                                    add++;
                                } else {
                                    superWeight[i][j + 1] += 40;
                                    add++;
                                }
                            }
                            // 100111
                            else if (PlayBoard.playBoard[i][j - 3] == 0 && PlayBoard.playBoard[i][j - 4] == 0
                                    && PlayBoard.playBoard[i][j - 5] == PlayBoard.UserC) {
                                if (weight[i][j - 4] > weight[i][j - 3]) {
                                    superWeight[i][j - 4] += 40;
                                    add++;
                                } else {
                                    superWeight[i][j - 3] += 40;
                                    add++;
                                }
                            }
                        }
                    } else
                        myCount = 0;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }

        if (add >= 2)
            return;
        // 2 (공백2) 2 방어 세로
        for (int i = 0; i < 19; i++) {
            myCount = 0;
            for (int j = 0; j < 19; j++) {
                try {
                    if (PlayBoard.playBoard[i][j] == PlayBoard.UserC) {
                        myCount++;
                        if (myCount == 2) {
                            if (PlayBoard.playBoard[i][j + 1] == 0 && PlayBoard.playBoard[i][j + 2] == 0
                                    && PlayBoard.playBoard[i][j + 3] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i][j + 4] == PlayBoard.UserC) {
                                superWeight[i][j + 1] += 40;
                                add++;
                                superWeight[i][j + 2] += 40;
                                add++;

                            } else if (PlayBoard.playBoard[i][j - 2] == 0 && PlayBoard.playBoard[i][j - 3] == 0
                                    && PlayBoard.playBoard[i][j - 4] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i][j - 5] == PlayBoard.UserC) {
                                superWeight[i][j - 2] += 40;
                                add++;
                                superWeight[i][j - 3] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i][j - 2] == 0 && PlayBoard.playBoard[i][j + 1] == 0
                                    && PlayBoard.playBoard[i][j + 2] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i][j + 3] == PlayBoard.UserC) {
                                superWeight[i][j - 2] += 40;
                                add++;
                                superWeight[i][j + 1] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i][j - 2] == 0 && PlayBoard.playBoard[i][j - 5] == 0
                                    && PlayBoard.playBoard[i][j - 3] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i][j - 4] == PlayBoard.UserC) {
                                superWeight[i][j - 2] += 40;
                                add++;
                                superWeight[i][j - 5] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i][j - 2] == 0 && PlayBoard.playBoard[i][j - 4] == 0
                                    && PlayBoard.playBoard[i][j - 3] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i][j - 5] == PlayBoard.UserC) {
                                superWeight[i][j - 2] += 40;
                                add++;
                                superWeight[i][j - 4] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i][j - 2] == 0 && PlayBoard.playBoard[i][j + 1] == 0
                                    && PlayBoard.playBoard[i][j - 3] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i][j + 2] == PlayBoard.UserC) {
                                superWeight[i][j - 2] += 40;
                                add++;
                                superWeight[i][j + 1] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i][j + 1] == 0 && PlayBoard.playBoard[i][j + 3] == 0
                                    && PlayBoard.playBoard[i][j + 2] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i][j + 4] == PlayBoard.UserC) {
                                superWeight[i][j + 3] += 40;
                                add++;
                                superWeight[i][j + 1] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i][j + 1] == 0 && PlayBoard.playBoard[i][j + 4] == 0
                                    && PlayBoard.playBoard[i][j + 2] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i][j + 3] == PlayBoard.UserC) {
                                superWeight[i][j + 4] += 40;
                                add++;
                                superWeight[i][j + 1] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i][j + 1] == 0 && PlayBoard.playBoard[i][j - 2] == 0
                                    && PlayBoard.playBoard[i][j - 3] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i][j - 4] == PlayBoard.UserC) {
                                superWeight[i][j - 2] += 40;
                                add++;
                                superWeight[i][j + 1] += 40;
                                add++;
                            }
                        }
                    } else
                        myCount = 0;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }

        if (add >= 2)
            return;
        // 5 가로 방어
        for (int j = 0; j < 19; j++) {
            myCount = 0;
            for (int i = 0; i < 19; i++) {
                try {
                    if (PlayBoard.playBoard[i][j] == PlayBoard.UserC) {
                        myCount++;
                        if (myCount == 5) {
                            // 양쪽 다 비었으면 양쪽 시급하게 막고
                            if (i - 5 < 0 && PlayBoard.playBoard[i + 1][j] == 0) {
                                superWeight[i + 1][j] += 40;
                                add++;
                            }

                            else if (i + 1 > 18 && PlayBoard.playBoard[i - 5][j] == 0) {
                                superWeight[i - 5][j] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i - 5][j] == 0 && PlayBoard.playBoard[i + 1][j] == 0) {
                                superWeight[i - 5][j] += 40;
                                add++;
                                superWeight[i + 1][j] += 40;
                                add++;
                            }
                            // 둘중 한쪽만 비었으면 거기 막기
                            else if (PlayBoard.playBoard[i - 5][j] == 0) {
                                superWeight[i - 5][j] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i + 1][j] == 0) {
                                superWeight[i + 1][j] += 40;
                                add++;
                            }
                        }
                    } else
                        myCount = 0;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }

        if (add >= 2)
            return;
        // 4 가로 방어
        for (int j = 0; j < 19; j++) {
            myCount = 0;
            for (int i = 0; i < 19; i++) {
                try {
                    if (PlayBoard.playBoard[i][j] == PlayBoard.UserC) {
                        myCount++;
                        if (myCount == 4) { // 양끝 뚫려있으면 양끝에 가중치
                            if (i - 4 < 0 && PlayBoard.playBoard[i + 1][j] == 0) {
                                superWeight[i + 1][j] += 40;
                                add++;
                            }

                            else if (i + 1 > 18 && PlayBoard.playBoard[i - 4][j] == 0) {
                                superWeight[i - 4][j] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i - 4][j] == 0 && PlayBoard.playBoard[i + 1][j] == 0) {
                                superWeight[i - 4][j] += 40;
                                add++;
                                superWeight[i + 1][j] += 40;
                                add++;
                            } // 한쪽만 뚫려있으면
                            else if (PlayBoard.playBoard[i - 4][j] == 0) {
                                if (PlayBoard.playBoard[i - 5][j] == 0) { // 한쪽이 더 뚫려있으면 둘중 이득인곳에 두고
                                    if (weight[i - 4][j] > weight[i - 5][j]) {
                                        superWeight[i - 4][j] += 40;
                                        add++;
                                    } else {
                                        superWeight[i - 5][j] += 40;
                                        add++;
                                    }
                                } else { // 아니면 거따두고
                                    superWeight[i - 4][j] += 40;
                                    add++;
                                }
                            } else if (PlayBoard.playBoard[i + 1][j] == 0) {
                                if (PlayBoard.playBoard[i + 2][j] == 0) {
                                    if (weight[i + 2][j] > weight[i + 1][j]) {
                                        superWeight[i + 2][j] += 40;
                                        add++;
                                    } else {
                                        superWeight[i + 1][j] += 40;
                                        add++;
                                    }
                                } else {
                                    superWeight[i + 1][j] += 40;
                                    add++;
                                }
                            }
                        }
                    } else
                        myCount = 0;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }

        if (add >= 2)
            return;
        // 3가로 1 공백 방어
        for (int j = 0; j < 19; j++) {
            myCount = 0;
            for (int i = 0; i < 19; i++) {
                try {
                    if (PlayBoard.playBoard[i][j] == PlayBoard.UserC) {
                        myCount++;
                        if (myCount == 3) {
                            if (PlayBoard.playBoard[i + 1][j] == 0 && PlayBoard.playBoard[i + 2][j] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i + 3][j] == 0) {
                                if (weight[i + 3][j] > weight[i + 1][j]) {
                                    superWeight[i + 3][j] += 40;
                                    add++;
                                } else {
                                    weight[i + 1][j] += 40;
                                    add++;
                                }
                            } else if (PlayBoard.playBoard[i - 3][j] == 0
                                    && PlayBoard.playBoard[i - 4][j] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i - 5][j] == 0) {
                                if (weight[i - 3][j] > weight[i - 5][j]) {
                                    superWeight[i - 3][j] += 40;
                                    add++;
                                } else {
                                    superWeight[i - 5][j] += 40;
                                    add++;
                                }
                            }
                        }
                    } else
                        myCount = 0;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }

        if (add >= 2)
            return;
        // 3가로 2 공백 방어
        for (int j = 0; j < 19; j++) {
            myCount = 0;
            for (int i = 0; i < 19; i++) {
                try {
                    if (PlayBoard.playBoard[i][j] == PlayBoard.UserC) {
                        myCount++;
                        if (myCount == 3) {
                            if (PlayBoard.playBoard[i + 1][j] == 0 && PlayBoard.playBoard[i + 2][j] == 0
                                    && PlayBoard.playBoard[i + 3][j] == PlayBoard.UserC) {
                                if (weight[i + 2][j] > weight[i + 1][j]) {
                                    superWeight[i + 2][j] += 40;
                                    add++;
                                } else {
                                    superWeight[i + 1][j] += 40;
                                    add++;
                                }
                            } else if (PlayBoard.playBoard[i - 3][j] == 0 && PlayBoard.playBoard[i - 4][j] == 0
                                    && PlayBoard.playBoard[i - 5][j] == PlayBoard.UserC) {
                                if (weight[i - 3][j] > weight[i - 4][j]) {
                                    superWeight[i - 3][j] += 40;
                                    add++;
                                } else {
                                    superWeight[i - 4][j] += 40;
                                    add++;
                                }
                            }
                        }
                    } else
                        myCount = 0;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }

        if (add >= 2)
            return;
        // 2 (공백2) 2 방어 가로
        for (int j = 0; j < 19; j++) {
            myCount = 0;
            for (int i = 0; i < 19; i++) {
                try {
                    if (PlayBoard.playBoard[i][j] == PlayBoard.UserC) {
                        myCount++;
                        if (myCount == 2) {
                            if (PlayBoard.playBoard[i + 1][j] == 0 && PlayBoard.playBoard[i + 2][j] == 0
                                    && PlayBoard.playBoard[i + 3][j] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i + 4][j] == PlayBoard.UserC) {
                                superWeight[i + 1][j] += 40;
                                add++;
                                superWeight[i + 2][j] += 40;
                                add++;

                            } else if (PlayBoard.playBoard[i - 2][j] == 0 && PlayBoard.playBoard[i - 3][j] == 0
                                    && PlayBoard.playBoard[i - 4][j] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i - 5][j] == PlayBoard.UserC) {
                                superWeight[i - 2][j] += 40;
                                add++;
                                superWeight[i - 3][j] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i - 2][j] == 0 && PlayBoard.playBoard[i + 1][j] == 0
                                    && PlayBoard.playBoard[i + 2][j] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i + 3][j] == PlayBoard.UserC) {
                                superWeight[i - 2][j] += 40;
                                add++;
                                superWeight[i + 1][j] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i - 2][j] == 0 && PlayBoard.playBoard[i - 5][j] == 0
                                    && PlayBoard.playBoard[i - 3][j] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i - 4][j] == PlayBoard.UserC) {
                                superWeight[i - 2][j] += 40;
                                add++;
                                superWeight[i - 5][j] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i - 2][j] == 0 && PlayBoard.playBoard[i - 4][j] == 0
                                    && PlayBoard.playBoard[i - 3][j] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i - 5][j] == PlayBoard.UserC) {
                                superWeight[i - 2][j] += 40;
                                add++;
                                superWeight[i - 4][j] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i - 2][j] == 0 && PlayBoard.playBoard[i + 1][j] == 0
                                    && PlayBoard.playBoard[i - 3][j] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i + 2][j] == PlayBoard.UserC) {
                                superWeight[i - 2][j] += 40;
                                add++;
                                superWeight[i + 1][j] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i + 1][j] == 0 && PlayBoard.playBoard[i + 3][j] == 0
                                    && PlayBoard.playBoard[i + 2][j] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i + 4][j] == PlayBoard.UserC) {
                                superWeight[i + 3][j] += 40;
                                add++;
                                superWeight[i + 1][j] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i + 1][j] == 0 && PlayBoard.playBoard[i + 4][j] == 0
                                    && PlayBoard.playBoard[i + 2][j] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i + 3][j] == PlayBoard.UserC) {
                                superWeight[i + 4][j] += 40;
                                add++;
                                superWeight[i + 1][j] += 40;
                                add++;
                            } else if (PlayBoard.playBoard[i + 1][j] == 0 && PlayBoard.playBoard[i - 2][j] == 0
                                    && PlayBoard.playBoard[i - 3][j] == PlayBoard.UserC
                                    && PlayBoard.playBoard[i - 4][j] == PlayBoard.UserC) {
                                superWeight[i - 2][j] += 40;
                                add++;
                                superWeight[i + 1][j] += 40;
                                add++;
                            }
                        }
                    } else
                        myCount = 0;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }

        if (add >= 2)
            return;
        // 5 왼쪽 위에서 오른쪽 아래(좌대각\) 방어
        for (int i = 0; i < 19; i++) {
            myCount = 0;
            for (int j = 0; j < 19; j++) {
                int temp1 = i;
                int temp2 = j;
                for (int k = 0; k < 5; k++) {
                    try {
                        if (PlayBoard.playBoard[temp1][temp2] == PlayBoard.UserC) {
                            myCount++;
                            if (myCount == 5) {
                                // 둘다뚫림
                                if ((temp1 - 5 < 0 || temp2 - 5 < 0)
                                        && PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0) {
                                    superWeight[temp1 + 1][temp2 + 1] += 40;
                                    add++;
                                } else if ((temp1 + 1 > 18 || temp2 + 1 > 18)
                                        && PlayBoard.playBoard[temp1 - 5][temp2 - 5] == 0) {
                                    superWeight[temp1 - 5][temp2 - 5] += 40;
                                    add++;
                                } else if (PlayBoard.playBoard[temp1 - 5][temp2 - 5] == 0
                                        && PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0) {
                                    superWeight[temp1 - 5][temp2 - 5] += 40;
                                    add++;
                                    superWeight[temp1 + 1][temp2 + 1] += 40;
                                    add++;
                                }
                                // 한쪽만뚫림 - 그 뚫린곳에 가중치
                                else if (PlayBoard.playBoard[temp1 - 5][temp2 - 5] == 0
                                        || PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0) {
                                    if (PlayBoard.playBoard[temp1 - 5][temp2 - 5] == 0) {
                                        superWeight[temp1 - 5][temp2 - 5] += 40;
                                        add++;
                                    } else if (PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0) {
                                        superWeight[temp1 + 1][temp2 + 1] += 40;
                                        add++;
                                    }
                                }
                            }
                            temp1++;
                            temp2++;
                        } else
                            myCount = 0;
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
            }
        }

        if (add >= 2)
            return;
        // 4 왼쪽 위에서 오른쪽 아래(좌대각\) 방어
        for (int i = 0; i < 19; i++) {
            myCount = 0;
            for (int j = 0; j < 19; j++) {
                int temp1 = i;
                int temp2 = j;
                for (int k = 0; k < 4; k++) {
                    try {
                        if (PlayBoard.playBoard[temp1][temp2] == PlayBoard.UserC) {
                            myCount++;
                            if (myCount == 4) {
                                if ((temp1 - 4 < 0 || temp2 - 4 < 0)
                                        && PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0) {
                                    superWeight[temp1 + 1][temp2 + 1] += 40;
                                    add++;
                                } else if ((temp1 + 1 > 18 || temp2 + 1 > 18)
                                        && PlayBoard.playBoard[temp1 - 4][temp2 - 4] == 0) {
                                    superWeight[temp1 - 4][temp2 - 4] += 40;
                                    add++;
                                } else if (PlayBoard.playBoard[temp1 - 4][temp2 - 4] == 0
                                        && PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0) {
                                    superWeight[temp1 - 4][temp2 - 4] += 40;
                                    add++;
                                    superWeight[temp1 + 1][temp2 + 1] += 40;
                                    add++;
                                } else if (PlayBoard.playBoard[temp1 - 4][temp2 - 4] == 0
                                        || PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0) {
                                    if (PlayBoard.playBoard[temp1 - 4][temp2 - 4] == 0
                                            && PlayBoard.playBoard[temp1 - 5][temp2 - 5] == 0) {
                                        if (weight[temp1 - 4][temp2 - 4] > weight[temp1 - 5][temp2 - 5]) {
                                            superWeight[temp1 - 4][temp2 - 4] += 40;
                                            add++;
                                        } else {
                                            superWeight[temp1 - 5][temp2 - 5] += 40;
                                            add++;
                                        }
                                    } else if (PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0
                                            && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == 0) {
                                        if (weight[temp1 + 1][temp2 + 1] > weight[temp1 + 2][temp2 + 2]) {
                                            superWeight[temp1 + 1][temp2 + 1] += 40;
                                            add++;
                                        } else {
                                            superWeight[temp1 + 2][temp2 + 2] += 40;
                                            add++;
                                        }
                                    }
                                }
                            }
                            temp1++;
                            temp2++;
                        } else
                            myCount = 0;
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
            }
        }

        if (add >= 2)
            return;
        // 3(좌대각\) 1공백 방어
        for (int i = 0; i < 19; i++) {
            myCount = 0;
            for (int j = 0; j < 19; j++) {
                int temp1 = i;
                int temp2 = j;
                for (int k = 0; k < 3; k++) {
                    try {
                        if (PlayBoard.playBoard[temp1][temp2] == PlayBoard.UserC) {
                            myCount++;
                            if (myCount == 3) {
                                if (PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0
                                        && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == PlayBoard.UserC
                                        && PlayBoard.playBoard[temp1 + 3][temp2 + 3] == 0) {
                                    if (weight[temp1 + 3][temp2 + 3] > weight[temp1 + 1][temp2 + 1]) {
                                        superWeight[temp1 + 3][temp2 + 3] += 40;
                                        add++;
                                    } else {
                                        weight[temp1 + 1][temp2 + 1] += 40;
                                        add++;
                                    }
                                } else if (PlayBoard.playBoard[temp1 - 3][temp2 - 3] == 0
                                        && PlayBoard.playBoard[temp1 - 4][temp2 - 4] == PlayBoard.UserC
                                        && PlayBoard.playBoard[temp1 - 5][temp2 - 5] == 0) {
                                    if (weight[temp1 - 3][temp2 - 3] > weight[temp1 - 5][temp2 - 5]) {
                                        superWeight[temp1 - 3][temp2 - 3] += 40;
                                        add++;
                                    } else {
                                        superWeight[temp1 - 5][temp2 - 5] += 40;
                                        add++;
                                    }
                                }
                            }
                            temp1++;
                            temp2++;
                        } else
                            myCount = 0;
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
            }
        }

        if (add >= 2)
            return;
        // 3(좌대각\) 2공백 1 방어
        for (int i = 0; i < 19; i++) {
            myCount = 0;
            for (int j = 0; j < 19; j++) {
                int temp1 = i;
                int temp2 = j;
                for (int k = 0; k < 3; k++) {
                    try {
                        if (PlayBoard.playBoard[temp1][temp2] == PlayBoard.UserC) {
                            myCount++;
                            if (myCount == 3) {
                                if (PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0
                                        && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == 0
                                        && PlayBoard.playBoard[temp1 + 3][temp2 + 3] == PlayBoard.UserC) {
                                    if (weight[temp1 + 1][temp2 + 1] > weight[temp1 + 2][temp2 + 2]) {
                                        superWeight[temp1 + 1][temp2 + 1] += 40;
                                        add++;
                                    } else {
                                        weight[temp1 + 2][temp2 + 2] += 40;
                                        add++;
                                    }
                                } else if (PlayBoard.playBoard[temp1 - 3][temp2 - 3] == 0
                                        && PlayBoard.playBoard[temp1 - 4][temp2 - 4] == 0
                                        && PlayBoard.playBoard[temp1 - 5][temp2 - 5] == PlayBoard.UserC) {
                                    if (weight[temp1 - 3][temp2 - 3] > weight[temp1 - 4][temp2 - 4]) {
                                        superWeight[temp1 - 3][temp2 - 3] += 40;
                                        add++;
                                    } else {
                                        superWeight[temp1 - 4][temp2 - 4] += 40;
                                        add++;
                                    }
                                }
                            }
                            temp1++;
                            temp2++;
                        } else
                            myCount = 0;
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
            }
        }

        if (add >= 2)
            return;
        // 2 (공백2) 2 방어 (좌대각\)왼쪽 위에서 오른쪽 아래
        for (int i = 0; i < 19; i++) {
            myCount = 0;
            for (int j = 0; j < 19; j++) {
                int temp1 = i;
                int temp2 = j;
                for (int k = 0; k < 2; k++) {
                    try {
                        if (PlayBoard.playBoard[temp1][temp2] == PlayBoard.UserC) {
                            myCount++;
                            try {
                                if (myCount == 2) {
                                    if (PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0
                                            && PlayBoard.playBoard[temp1 + 4][temp2 + 4] == 0
                                            && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == PlayBoard.UserC
                                            && PlayBoard.playBoard[temp1 + 3][temp2 + 3] == PlayBoard.UserC) {
                                        superWeight[temp1 + 1][temp2 + 1] += 40;
                                        add++;
                                        superWeight[temp1 + 4][temp2 + 4] += 40;
                                        add++;

                                    } else if (PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0
                                            && PlayBoard.playBoard[temp1 - 2][temp2 - 2] == 0
                                            && PlayBoard.playBoard[temp1 - 3][temp2 - 3] == PlayBoard.UserC
                                            && PlayBoard.playBoard[temp1 - 4][temp2 - 4] == PlayBoard.UserC) {
                                        superWeight[temp1 + 1][temp2 + 1] += 40;
                                        add++;
                                        superWeight[temp1 - 2][temp2 - 2] += 40;
                                        add++;
                                    } else if (PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0
                                            && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == 0
                                            && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == PlayBoard.UserC
                                            && PlayBoard.playBoard[temp1 + 4][temp2 + 4] == PlayBoard.UserC) {
                                        superWeight[temp1 + 1][temp2 + 1] += 40;
                                        add++;
                                        superWeight[temp1 + 2][temp2 + 2] += 40;
                                        add++;
                                    } else if (PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0
                                            && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == 0
                                            && PlayBoard.playBoard[temp1 + 3][temp2 + 3] == PlayBoard.UserC
                                            && PlayBoard.playBoard[temp1 + 4][temp2 + 4] == PlayBoard.UserC) {
                                        superWeight[temp1 + 1][temp2 + 1] += 40;
                                        add++;
                                        superWeight[temp1 + 2][temp2 + 2] += 40;
                                        add++;
                                    } else if (PlayBoard.playBoard[temp1 - 2][temp2 - 2] == 0
                                            && PlayBoard.playBoard[temp1 - 3][temp2 - 3] == 0
                                            && PlayBoard.playBoard[temp1 - 4][temp2 - 4] == PlayBoard.UserC
                                            && PlayBoard.playBoard[temp1 - 5][temp2 - 5] == PlayBoard.UserC) {
                                        superWeight[temp1 - 2][temp2 - 2] += 40;
                                        add++;
                                        superWeight[temp1 - 3][temp2 - 3] += 40;
                                        add++;
                                    } else if (PlayBoard.playBoard[temp1 - 2][temp2 - 2] == 0
                                            && PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0
                                            && PlayBoard.playBoard[temp1 - 3][temp2 - 3] == PlayBoard.UserC
                                            && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == PlayBoard.UserC) {
                                        superWeight[temp1 - 2][temp2 - 2] += 40;
                                        add++;
                                        superWeight[temp1 + 1][temp2 + 1] += 40;
                                        add++;
                                    } else if (PlayBoard.playBoard[temp1 - 4][temp2 - 4] == 0
                                            && PlayBoard.playBoard[temp1 - 2][temp2 - 2] == 0
                                            && PlayBoard.playBoard[temp1 - 3][temp2 - 3] == PlayBoard.UserC
                                            && PlayBoard.playBoard[temp1 - 5][temp2 - 5] == PlayBoard.UserC) {
                                        superWeight[temp1 - 4][temp2 - 4] += 40;
                                        add++;
                                        superWeight[temp1 - 2][temp2 - 2] += 40;
                                        add++;
                                    } else if (PlayBoard.playBoard[temp1 + 1][temp2 + 1] == 0
                                            && PlayBoard.playBoard[temp1 - 2][temp2 - 2] == 0
                                            && PlayBoard.playBoard[temp1 + 3][temp2 + 3] == PlayBoard.UserC
                                            && PlayBoard.playBoard[temp1 + 2][temp2 + 2] == PlayBoard.UserC) {
                                        superWeight[temp1 + 1][temp2 + 1] += 40;
                                        add++;
                                        superWeight[temp1 - 2][temp2 - 2] += 40;
                                        add++;
                                    }
                                }
                            } catch (ArrayIndexOutOfBoundsException e) {
                            }
                            temp1++;
                            temp2++;
                        } else
                            myCount = 0;
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
            }
        }

        if (add >= 2)
            return;
        // 5 오른쪽위에서 왼쪽아래(우대각/) 방어
        for (int j = 0; j < 19; j++) {
            myCount = 0;
            for (int i = 5; i < 19; i++) {
                int temp1 = i;
                int temp2 = j;
                for (int k = 0; k < 5; k++) {
                    try {
                        if (PlayBoard.playBoard[temp1][temp2] == PlayBoard.UserC) {
                            myCount++;
                            if (myCount == 5) {
                                if ((temp1 - 1 < 0 || temp2 + 1 > 18)
                                        && PlayBoard.playBoard[temp1 + 5][temp2 - 5] == 0) {
                                    superWeight[temp1 + 5][temp2 - 5] += 40;
                                    add++;
                                } else if ((temp1 + 5 > 18 || temp2 - 5 < 0)
                                        && PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0) {
                                    superWeight[temp1 - 1][temp2 + 1] += 40;
                                    add++;
                                } else if (PlayBoard.playBoard[temp1 + 5][temp2 - 5] == 0
                                        && PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0) {
                                    superWeight[temp1 + 5][temp2 - 5] += 40;
                                    add++;
                                    superWeight[temp1 - 1][temp2 + 1] += 40;
                                    add++;
                                } else if (PlayBoard.playBoard[temp1 + 4][temp2 - 4] == 0
                                        || PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0) {
                                    if (PlayBoard.playBoard[temp1 + 5][temp2 - 5] == 0) {
                                        superWeight[temp1 + 5][temp2 - 5] += 40;
                                        add++;
                                    } else if (PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0) {
                                        superWeight[temp1 - 1][temp2 + 1] += 40;
                                        add++;
                                    }
                                }
                            }
                            temp1--;
                            temp2++;
                        } else
                            myCount = 0;
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
            }
        }

        if (add >= 2)
            return;
        // 4 오른쪽위에서 왼쪽아래(우대각/) 방어
        for (int j = 0; j < 19; j++) {
            myCount = 0;
            for (int i = 5; i < 19; i++) {
                int temp1 = i;
                int temp2 = j;
                for (int k = 0; k < 4; k++) {
                    try {
                        if (PlayBoard.playBoard[temp1][temp2] == PlayBoard.UserC) {
                            myCount++;
                            if (myCount == 4) {
                                if ((temp1 - 1 < 0 || temp2 + 1 > 18)
                                        && PlayBoard.playBoard[temp1 + 4][temp2 - 4] == 0) {
                                    superWeight[temp1 + 4][temp2 - 4] += 40;
                                    add++;
                                } else if ((temp1 + 4 > 18 || temp2 - 4 < 0)
                                        && PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0) {
                                    superWeight[temp1 - 1][temp2 + 1] += 40;
                                    add++;
                                } else if (PlayBoard.playBoard[temp1 + 4][temp2 - 4] == 0
                                        && PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0) {
                                    superWeight[temp1 + 4][temp2 - 4] += 40;
                                    add++;
                                    superWeight[temp1 - 1][temp2 + 1] += 40;
                                    add++;
                                } else if (PlayBoard.playBoard[temp1 + 4][temp2 - 4] == 0
                                        || PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0) {
                                    if (PlayBoard.playBoard[temp1 + 4][temp2 - 4] == 0
                                            && PlayBoard.playBoard[temp1 + 5][temp2 - 5] == 0) {
                                        if (weight[temp1 + 4][temp2 - 4] > weight[temp1 + 5][temp2 - 5]) {
                                            superWeight[temp1 + 4][temp2 - 4] += 40;
                                            add++;
                                        } else {
                                            superWeight[temp1 + 5][temp2 - 5] += 40;
                                            add++;
                                        }
                                    } else if (PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0
                                            && PlayBoard.playBoard[temp1 - 2][temp2 + 2] == 0) {
                                        if (weight[temp1 - 1][temp2 + 1] > weight[temp1 - 1][temp2 + 1]) {
                                            superWeight[temp1 - 1][temp2 + 1] += 40;
                                            add++;
                                        } else {
                                            superWeight[temp1 - 1][temp2 + 1] += 40;
                                            add++;
                                        }
                                    }
                                }
                            }
                            temp1--;
                            temp2++;
                        } else
                            myCount = 0;
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
            }
        }

        if (add >= 2)
            return;
        // 3(우대각/) 1공백 방어
        for (int j = 0; j < 19; j++) {
            myCount = 0;
            for (int i = 0; i < 19; i++) {
                int temp1 = i;
                int temp2 = j;
                for (int k = 0; k < 3; k++) {
                    try {
                        if (PlayBoard.playBoard[temp1][temp2] == PlayBoard.UserC) {
                            myCount++;

                            if (myCount == 3) {
                                if (PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0
                                        && PlayBoard.playBoard[temp1 - 2][temp2 + 2] == PlayBoard.UserC
                                        && PlayBoard.playBoard[temp1 - 3][temp2 + 3] == 0) {
                                    if (weight[temp1 - 3][temp2 + 3] > weight[temp1 - 1][temp2 + 1]) {
                                        superWeight[temp1 - 3][temp2 + 3] += 40;
                                        add++;
                                    } else {
                                        weight[temp1 - 1][temp2 + 1] += 40;
                                        add++;
                                    }
                                } else if (PlayBoard.playBoard[temp1 + 3][temp2 - 3] == 0
                                        && PlayBoard.playBoard[temp1 + 4][temp2 - 4] == PlayBoard.UserC
                                        && PlayBoard.playBoard[temp1 + 5][temp2 - 5] == 0) {
                                    if (weight[temp1 + 3][temp2 - 3] > weight[temp1 + 5][temp2 - 5]) {
                                        superWeight[temp1 + 3][temp2 - 3] += 40;
                                        add++;
                                    } else {
                                        superWeight[temp1 + 5][temp2 - 5] += 40;
                                        add++;
                                    }
                                }

                            }
                            temp1--;
                            temp2++;
                        } else
                            myCount = 0;
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
            }
        }

        if (add >= 2)
            return;
        // 3(우대각/) 2공백 1 방어
        for (int j = 0; j < 19; j++) {
            myCount = 0;
            for (int i = 0; i < 19; i++) {
                int temp1 = i;
                int temp2 = j;
                for (int k = 0; k < 3; k++) {
                    try {
                        if (PlayBoard.playBoard[temp1][temp2] == PlayBoard.UserC) {
                            myCount++;

                            if (myCount == 3) {
                                if (PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0
                                        && PlayBoard.playBoard[temp1 - 2][temp2 + 2] == 0
                                        && PlayBoard.playBoard[temp1 - 3][temp2 + 3] == PlayBoard.UserC) {
                                    if (weight[temp1 - 2][temp2 + 2] > weight[temp1 - 1][temp2 + 1]) {
                                        superWeight[temp1 - 2][temp2 + 2] += 40;
                                        add++;
                                    } else {
                                        weight[temp1 - 1][temp2 + 1] += 40;
                                        add++;
                                    }
                                } else if (PlayBoard.playBoard[temp1 + 3][temp2 - 3] == 0
                                        && PlayBoard.playBoard[temp1 + 4][temp2 - 4] == 0
                                        && PlayBoard.playBoard[temp1 + 5][temp2 - 5] == PlayBoard.UserC) {
                                    if (weight[temp1 + 3][temp2 - 3] > weight[temp1 + 4][temp2 - 4]) {
                                        superWeight[temp1 + 3][temp2 - 3] += 40;
                                        add++;
                                    } else {
                                        superWeight[temp1 + 5][temp2 - 5] += 40;
                                        add++;
                                    }
                                }

                            }
                            temp1--;
                            temp2++;
                        } else
                            myCount = 0;
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
            }
        }

        if (add >= 2)
            return;
        // 2 (공백2) 2 방어 (우대각/)오른쪽 위에서 왼쪽 아래
        for (int i = 0; i < 19; i++) {
            myCount = 0;
            for (int j = 0; j < 19; j++) {
                int temp1 = i;
                int temp2 = j;
                for (int k = 0; k < 2; k++) {
                    try {
                        if (PlayBoard.playBoard[temp1][temp2] == PlayBoard.UserC) {
                            myCount++;
                            try {
                                if (myCount == 2) {
                                    if (PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0
                                            && PlayBoard.playBoard[temp1 + 2][temp2 - 2] == 0
                                            && PlayBoard.playBoard[temp1 - 2][temp2 + 2] == PlayBoard.UserC
                                            && PlayBoard.playBoard[temp1 - 3][temp2 + 3] == PlayBoard.UserC) {
                                        superWeight[temp1 - 1][temp2 + 1] += 40;
                                        add++;
                                        superWeight[temp1 + 2][temp2 - 2] += 40;
                                        add++;

                                    } else if (PlayBoard.playBoard[temp1 + 2][temp2 - 2] == 0
                                            && PlayBoard.playBoard[temp1 + 5][temp2 - 5] == 0
                                            && PlayBoard.playBoard[temp1 + 3][temp2 - 3] == PlayBoard.UserC
                                            && PlayBoard.playBoard[temp1 + 4][temp2 - 4] == PlayBoard.UserC) {
                                        superWeight[temp1 + 2][temp2 - 2] += 40;
                                        add++;
                                        superWeight[temp1 + 5][temp2 - 5] += 40;
                                        add++;
                                    } else if (PlayBoard.playBoard[temp1 + 4][temp2 - 4] == 0
                                            && PlayBoard.playBoard[temp1 + 2][temp2 - 2] == 0
                                            && PlayBoard.playBoard[temp1 + 3][temp2 - 3] == PlayBoard.UserC
                                            && PlayBoard.playBoard[temp1 + 5][temp2 - 5] == PlayBoard.UserC) {
                                        superWeight[temp1 + 4][temp2 - 4] += 40;
                                        add++;
                                        superWeight[temp1 + 2][temp2 - 2] += 40;
                                        add++;
                                    } else if (PlayBoard.playBoard[temp1 + 2][temp2 - 2] == 0
                                            && PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0
                                            && PlayBoard.playBoard[temp1 - 2][temp2 + 2] == PlayBoard.UserC
                                            && PlayBoard.playBoard[temp1 + 3][temp2 - 3] == PlayBoard.UserC) {
                                        superWeight[temp1 + 2][temp2 - 2] += 40;
                                        add++;
                                        superWeight[temp1 - 1][temp2 + 1] += 40;
                                        add++;
                                    } else if (PlayBoard.playBoard[temp1 + 3][temp2 - 3] == 0
                                            && PlayBoard.playBoard[temp1 + 2][temp2 - 2] == 0
                                            && PlayBoard.playBoard[temp1 + 3][temp2 - 3] == PlayBoard.UserC
                                            && PlayBoard.playBoard[temp1 + 4][temp2 - 4] == PlayBoard.UserC) {
                                        superWeight[temp1 + 3][temp2 - 3] += 40;
                                        add++;
                                        superWeight[temp1 + 2][temp2 - 2] += 40;
                                        add++;
                                    } else if (PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0
                                            && PlayBoard.playBoard[temp1 - 2][temp2 + 2] == 0
                                            && PlayBoard.playBoard[temp1 - 3][temp2 + 3] == PlayBoard.UserC
                                            && PlayBoard.playBoard[temp1 - 4][temp2 + 4] == PlayBoard.UserC) {
                                        superWeight[temp1 - 1][temp2 + 1] += 40;
                                        add++;
                                        superWeight[temp1 - 2][temp2 + 2] += 40;
                                        add++;
                                    } else if (PlayBoard.playBoard[temp1 + 2][temp2 - 2] == 0
                                            && PlayBoard.playBoard[temp1 + 4][temp2 - 4] == 0
                                            && PlayBoard.playBoard[temp1 + 3][temp2 - 3] == PlayBoard.UserC
                                            && PlayBoard.playBoard[temp1 + 5][temp2 - 5] == PlayBoard.UserC) {
                                        superWeight[temp1 + 2][temp2 - 2] += 40;
                                        add++;
                                        superWeight[temp1 + 4][temp2 - 4] += 40;
                                        add++;
                                    } else if (PlayBoard.playBoard[temp1 + 2][temp2 - 2] == 0
                                            && PlayBoard.playBoard[temp1 + 5][temp2 - 5] == 0
                                            && PlayBoard.playBoard[temp1 + 3][temp2 - 3] == PlayBoard.UserC
                                            && PlayBoard.playBoard[temp1 + 4][temp2 - 4] == PlayBoard.UserC) {
                                        superWeight[temp1 + 2][temp2 - 2] += 40;
                                        add++;
                                        superWeight[temp1 + 5][temp2 - 5] += 40;
                                        add++;
                                    } else if (PlayBoard.playBoard[temp1 - 1][temp2 + 1] == 0
                                            && PlayBoard.playBoard[temp1 + 2][temp2 - 2] == 0
                                            && PlayBoard.playBoard[temp1 - 2][temp2 + 2] == PlayBoard.UserC
                                            && PlayBoard.playBoard[temp1 - 3][temp2 + 3] == PlayBoard.UserC) {
                                        superWeight[temp1 - 1][temp2 + 1] += 40;
                                        add++;
                                        superWeight[temp1 + 2][temp2 - 2] += 40;
                                        add++;
                                    }
                                }
                            } catch (ArrayIndexOutOfBoundsException e) {
                            }
                            temp1--;
                            temp2++;
                        } else
                            myCount = 0;
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
            }
        }

        if (add >= 2)
            return;

        //// 본인 전개 플러스점수
        //// ---------------------------------------------------------------------------------
        // 연결 양끝으로 연결갯수*2의 가중치 더하기

        // 상대 방해 플러스점수
        // --------------------------------------------------------------------------------------
        // 연결 양끝으로 연결횟수만큼의 가중치 더하기

    }

    // 일반가중치+특수가중치 판에서 최대 가중치를 찾아 x,y 값 저장해주기
    public static void returnPoint(int[][] board) {

        int max = 0;
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (superWeight[i][j] + weight[i][j] > max) {
                    max = superWeight[i][j] + weight[i][j];
                    Alphago.x = i;
                    Alphago.y = j;
                }
            }
        }

    }

    // 현재 가중치 상태 콘솔에 출력
    public static void showWeight() {
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                System.out.printf("[%2d]", weight[j][i]);
            }
            System.out.println("");
        }
        System.out.println("");
    }

}
