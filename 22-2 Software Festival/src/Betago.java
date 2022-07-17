
public class Betago {
	
	//모든 돌이 놓일 때 마다 addWeight 실행시켜주고,
	//AI턴에서는 returnPoint로 받아오기
	
	static int color = DummyAI.getMyColor();
	static int opponent = DummyAI.getYourColor();
	private static int[][] playBoard = new int[19][19];
	
	
	//copy... (얕은복사라 주소지까지 연결된...)
	public static void getBoard(ConnectSix.Board B) {
		playBoard = B.board;
	}
	
	

	//integer 형태의 이상적 좌표를 형식에 맞춘 String으로 바꿔 리턴(다음에 놓을 그거임. 스톤 하나하나 기준.)
	public static String returnStringCoor() {
		
		
		returnPoint();
		//System.out.println("now x and y is "+x+", "+y);
		//x, y를 바탕으로 String형태의 머시깽이...
		String stone1 = String.format("%c%02d", (char)((x<8)?(x+'A'):(x+'A'+1)), y+1);
		
		
		//일단여기는절대두지말라는뜻!!!! board를 직접 수정하면 NOTEMPTY에러가 나서 임시방편으로... 
		weight[x][y] = -100000;
		//방금 자기가 놓은거 업데이트해주고 
		addWeight(x, y);
		

		returnPoint();
		//System.out.println("now x and y is "+x+", "+y);
		String stone2 = String.format("%c%02d", (char)((x<8)?(x+'A'):(x+'A'+1)), y+1);

		
		String result = stone1 + ":" + stone2;
		//System.out.println(result);
		return result;
		
	}
	
	
	

	
	
	
	

  // 현재 산출된 이상적 좌표
  static int x;
  static int y;
  // 가중치 설정을 위한 배열
  static int[][] weight = new int[19][19]; // 일반가중치(계속 누적)
  static int[][] superWeight = new int[19][19]; // 특수가중치(매 분석마다 리셋후 시작)
  

  
  // 가중치 기본 누적하기 (매 실행 후에)
  public static void addWeight(int x, int y) {
	  

      int n = 1; // 누적할 가중치의 양. 내 돌인지 상대 돌인지에 따라 달라짐.

      if (playBoard[x][y] == 1) //black
          n = 2;
      else if (playBoard[x][y] == 2) //white
          n = 1;
      else if (playBoard[x][y] == 3) { // 중립구 취급
          weight[x][y] = -100;
          return;
      } else return; // 이미 놓여진곳 취급
      
      weight[x][y] = -10000;

      // 팔방을 뒤져서 이미 놓여진 곳만 아니라면 가중치 누적.
      for (int i = x - 1; i < x + 2; i++) {
          for (int j = y - 1; j < y + 2; j++) {
              if (i == x && j == y) { // 본인자리에는 -1
                  weight[x][y] = -10000;
              } else {
                  try {
                      if (playBoard[i][j] == 0)
                          weight[i][j] += n;
                  } catch (ArrayIndexOutOfBoundsException e) {
                  } // 인덱스 넘어서면 무시
              }
          }
      }

  }

  
  // 판 읽고 특수가중치 누적하기
  private static void addSuperWeight() {

      // 특수가중치 판 초기화
      for (int i = 0; i < 19; i++) {
          for (int j = 0; j < 19; j++) {
              superWeight[i][j] = 0;
          }
      }

      int myCount = 0;
      int add = 0; // 현재 가중치 몇번 누적했는지(두번 넘어가면 그만찾고 리턴. 어차피 한 턴에 두번밖에 못 두니까..)

      //// 놓으면 이길 때(한방승리)
      //// -------------------------------------------------------------------------------------

      // 세로 시작점 ----------------------------------------------------------------------

      if (add >= 2)
          return;
      // 공격/세로/5-1
      for (int i = 0; i < 19; i++) {
          myCount = 0;
          for (int j = 0; j < 19; j++) {
              try {
                  if (playBoard[i][j] == color) {
                      myCount++;
                      if (myCount == 5) {
                          // 양끝 중 아무 빈곳에 가중치 왕창 이벤트
                          if (j - 5 > 0 && playBoard[i][j - 5] == 0) { // 0[11111] left //made a change
                              superWeight[i][j - 5] += 80;
                              add++;
                          } else if (j + 1 < 19 && playBoard[i][j + 1] == 0) { // *[11111]0 right //also
                                                                                         // made a change
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
      // 공격/세로/1-4-1/2-4
      for (int i = 0; i < 19; i++) {
          myCount = 0;
          for (int j = 0; j < 19; j++) {
              try {
                  if (playBoard[i][j] == color) {
                      myCount++;

                      if (myCount == 4) {

                          if (j - 4 > 0 && j + 1 < 19 && playBoard[i][j - 4] == 0
                                  && playBoard[i][j + 1] == 0) { // 0[1111]0 mid
                              superWeight[i][j - 4] += 80;
                              superWeight[i][j + 1] += 80;
                              add += 2;
                          }

                          else if (j - 5 < 0 && playBoard[i][j - 4] == 0
                                  && playBoard[i][j - 5] == 0) { // 00[1111] left ??????????????
                              superWeight[i][j - 4] += 80;
                              superWeight[i][j - 5] += 80;
                              add += 2;
                          } else if (j + 2 < 19 && playBoard[i][j + 1] == 0
                                  && playBoard[i][j + 2] == 0) { // [1111]00 right
                              superWeight[i][j + 2] += 80;
                              superWeight[i][j + 1] += 80;
                              add += 2;
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
      // 공격/세로/3-1-1-1/1-3-1-1
      for (int i = 0; i < 19; i++) {
          myCount = 0;
          for (int j = 0; j < 19; j++) {
              try {
                  if (playBoard[i][j] == color) {
                      myCount++;
                      if (myCount == 3) { // 세번연속일때

                          if (j + 3 < 19 && playBoard[i][j + 1] == 0
                                  && playBoard[i][j + 2] == color
                                  && playBoard[i][j + 3] == 0) { // [111]010 right

                              superWeight[i][j + 1] += 40;
                              superWeight[i][j + 3] += 40;
                              add += 2;
                          }

                          else if (j - 5 > 0
                                  && (playBoard[i][j - 3] == 0
                                          && playBoard[i][j - 4] == color
                                          && playBoard[i][j - 5] == 0)) { // 010[111] left

                              superWeight[i][j - 3] += 40;
                              superWeight[i][j - 5] += 40;
                              add += 2;
                          }

                          else if (j - 3 > 0 && j + 2 < 19 && playBoard[i][j - 3] == 0
                                  && playBoard[i][j + 1] == 0
                                  && playBoard[i][j + 2] == color) { // 0[111]01 mid
                              superWeight[i][j - 3] += 40;
                              superWeight[i][j + 2] += 40;
                              add += 2;
                          } else if (j - 4 > 0 && j + 1 < 19 && playBoard[i][j - 3] == 0
                                  && playBoard[i][j - 4] == color
                                  && playBoard[i][j + 1] == 0) { // 10[111]0 mid
                              superWeight[i][j - 3] += 40;
                              superWeight[i][j + 1] += 40;
                              add += 2;
                          }
                      } else
                          myCount = 0;
                  }
              } catch (ArrayIndexOutOfBoundsException e) {
              }
          }
      }

      if (add >= 2)
          return;
      // 공격/세로/3-2-1
      for (int i = 0; i < 19; i++) {
          myCount = 0;
          for (int j = 0; j < 19; j++) {
              try {
                  if (playBoard[i][j] == color) {
                      myCount++;
                      if (myCount == 3) { // 세번연속일때

                          if (j + 3 < 19 && (playBoard[i][j + 1] == 0 && playBoard[i][j + 2] == 0
                                  && playBoard[i][j + 3] == color)) { // [111]001 right

                              superWeight[i][j + 1] += 40;
                              superWeight[i][j + 2] += 40;
                              add += 2;
                          }

                          else if (j - 5 > 0
                                  && (playBoard[i][j - 3] == 0 && playBoard[i][j - 4] == 0
                                          && playBoard[i][j - 5] == color)) { // 100[111] left

                              superWeight[i][j - 3] += 40;
                              superWeight[i][j - 4] += 40;
                              add += 2;

                          }
                      } else
                          myCount = 0;
                  }
              } catch (ArrayIndexOutOfBoundsException e) {
              }
          }
      }

      if (add >= 2)
          return;
      // 공격/세로/2-2-2/1-2-1-2/1-1-1-1-2/1-1-2-1-1
      for (int i = 0; i < 19; i++) {
          myCount = 0;
          for (int j = 0; j < 19; j++) {
              // try { WHY????????????
              if (playBoard[i][j] == color) {
                  myCount++;
                  if (myCount == 2) {

                      try {

                          if (j + 4 < 19 && playBoard[i][j + 1] == 0 && playBoard[i][j + 2] == 0
                                  && playBoard[i][j + 3] == color
                                  && playBoard[i][j + 4] == color) { // [11]0011 right
                              superWeight[i][j + 1] += 40;
                              superWeight[i][j + 2] += 40;
                              add += 2;
                          }else if (j - 2 > 0 && j + 3 > 19 && playBoard[i][j - 2] == 0
                                  && playBoard[i][j + 1] == 0
                                  && playBoard[i][j + 2] == color
                                  && playBoard[i][j + 3] == color) { // 0[11]011 mid
                              superWeight[i][j - 2] += 40;
                              superWeight[i][j + 1] += 40;
                              add += 2;
                          }else if (j - 5 > 0 && playBoard[i][j - 2] == 0
                                  && playBoard[i][j - 4] == 0
                                  && playBoard[i][j - 3] == color
                                  && playBoard[i][j - 5] == color) { // 1010[11] left
                              superWeight[i][j - 2] += 40;
                              superWeight[i][j - 4] += 40;
                              add += 2;
                          } else if (j - 3 > 0 && j + 2 < 19 && playBoard[i][j - 2] == 0
                                  && playBoard[i][j + 1] == 0
                                  && playBoard[i][j - 3] == color
                                  && playBoard[i][j + 2] == color) { // 10[11]01 mid
                              superWeight[i][j - 2] += 40;
                              superWeight[i][j + 1] += 40;
                              add += 2;
                          } else if (j + 4 < 19 && playBoard[i][j + 1] == 0
                                  && playBoard[i][j + 3] == 0
                                  && playBoard[i][j + 2] == color
                                  && playBoard[i][j + 4] == color) { // [11]0101 right
                              superWeight[i][j + 3] += 40;
                              superWeight[i][j + 1] += 40;
                              add += 2;
                          } else if (j + 4 < 19 && playBoard[i][j + 1] == 0
                                  && playBoard[i][j + 4] == 0
                                  && playBoard[i][j + 2] == color
                                  && playBoard[i][j + 3] == color) { // [11]0110 right
                              superWeight[i][j + 4] += 40;
                              superWeight[i][j + 1] += 40;
                              add += 2;
                          } 
                      } catch (ArrayIndexOutOfBoundsException e) {
                      }
                  }
              } else
                  myCount = 0;
              // }catch (ArrayIndexOutOfBoundsException e) {}
          }
      }

      // 가로 시작점
      // -------------------------------------------------------------------------------------------------

      if (add >= 2)
          return;
      // 공격/가로/5-1
      for (int j = 0; j < 19; j++) {
          myCount = 0;
          for (int i = 0; i < 19; i++) {
              try {
                  if (playBoard[i][j] == color) {
                      myCount++;
                      if (myCount == 5) { // 양끝 중 아무 빈곳에 가중치 왕창 이벤트
                          if (i - 5 > 0 && playBoard[i - 5][j] == 0) { // 0[11111] (left)
                              superWeight[i - 5][j] += 80;
                              add++;
                          }

                          else if (i + 1 < 19 && playBoard[i + 1][j] == 0) { // *[11111]0 (right)
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
      // 공격/가로/4-2/1-4-1
      for (int j = 0; j < 19; j++) {
          myCount = 0;
          for (int i = 0; i < 19; i++) {
              try {
                  if (playBoard[i][j] == color) {
                      myCount++;
                      if (myCount == 4) {

                          if (i - 4 > 0 && i + 1 < 19 && playBoard[i - 4][j] == 0
                                  && playBoard[i + 1][j] == 0) { // 0[1111]0 (mid)
                              superWeight[i - 4][j] += 80;
                              superWeight[i + 1][j] += 80;
                              add += 2;
                          }

                          if (i - 5 > 0 && playBoard[i - 4][j] == 0 && playBoard[i - 5][j] == 0) { // 00[1111]
                                                                                                                       // left
                              superWeight[i - 4][j] += 80;
                              superWeight[i - 5][j] += 80;
                              add += 2;
                          } else if (i + 2 < 19 && playBoard[i + 1][j] == 0
                                  && playBoard[i + 2][j] == 0) { // [1111]00 right
                              superWeight[i + 2][j] += 80;
                              superWeight[i + 1][j] += 80;
                              add += 2;
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
      // 공격/가로/3-1-1-1
      for (int j = 0; j < 19; j++) {
          myCount = 0;
          for (int i = 0; i < 19; i++) {
              try {
                  if (playBoard[i][j] == color) {
                      myCount++;
                      if (myCount == 3) {
                          if (i + 3 < 19 && playBoard[i + 1][j] == 0
                                  && playBoard[i + 2][j] == color
                                  && playBoard[i + 3][j] == 0) { // [111]010 right

                              superWeight[i + 3][j] += 40;
                              superWeight[i + 1][j] += 40;
                              add += 2;
                          } else if (i - 5 > 0 && playBoard[i - 3][j] == 0
                                  && playBoard[i - 4][j] == color
                                  && playBoard[i - 5][j] == 0) { // 010[111] left

                              superWeight[i - 3][j] += 40;
                              superWeight[i - 5][j] += 40;
                              add += 2;
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
      // 공격/가로/3-2-1/1-3-1-1
      for (int j = 0; j < 19; j++) {
          myCount = 0;
          for (int i = 0; i < 19; i++) {
              try {
                  if (playBoard[i][j] == color) {
                      myCount++;
                      if (myCount == 3) {
                          if (i + 3 < 19 && playBoard[i + 1][j] == 0 && playBoard[i + 2][j] == 0
                                  && playBoard[i + 3][j] == color) { // [111]001 right
                              superWeight[i + 1][j] += 40;
                              superWeight[i + 2][j] += 40;
                              add += 2;
                          } else if (i - 5 > 0 && playBoard[i - 3][j] == 0
                                  && playBoard[i - 4][j] == 0
                                  && playBoard[i - 5][j] == color) {// 100[111] left

                              superWeight[i - 3][j] += 40;
                              superWeight[i - 4][j] += 40;
                              add += 2;
                          }

                          else if (i - 3 > 0 && i + 2 < 19 && playBoard[i - 3][j] == 0
                                  && playBoard[i + 1][j] == 0
                                  && playBoard[i + 2][j] == color) { // 0[111]01 mid
                              superWeight[i - 3][j] += 40;
                              superWeight[i + 2][j] += 40;
                              add += 2;
                          } else if (i - 4 > 0 && i + 1 < 19 && playBoard[i - 3][j] == 0
                                  && playBoard[i - 4][j] == color
                                  && playBoard[i + 1][j] == 0) { // 10[111]0 mid
                              superWeight[i - 3][j] += 40;
                              superWeight[i + 1][j] += 40;
                              add += 2;
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
      // 공격/가로/2-2-2/1-2-1-2/1-1-1-1-2/1-1-2-1-1
      for (int j = 0; j < 19; j++) {
          myCount = 0; // initialize myCount when entering new row
          for (int i = 0; i < 19; i++) {
              try {
                  if (playBoard[i][j] == color) {
                      myCount++;
                      if (myCount == 2) {
                          if (i + 4 < 19 && playBoard[i + 1][j] == 0 && playBoard[i + 2][j] == 0
                                  && playBoard[i + 3][j] == color
                                  && playBoard[i + 4][j] == color) { // [11]0011 (right)
                              superWeight[i + 1][j] += 40;
                              superWeight[i + 2][j] += 40;
                              add += 2;
                          }else if (i - 2 > 0 && i + 3 < 19 && playBoard[i - 2][j] == 0
                                  && playBoard[i + 1][j] == 0
                                  && playBoard[i + 2][j] == color
                                  && playBoard[i + 3][j] == color) { // 0[11]011 (mid)
                              superWeight[i - 2][j] += 40;
                              superWeight[i + 1][j] += 40;
                              add += 2;
                          }else if (i - 5 > 0 && playBoard[i - 2][j] == 0
                                  && playBoard[i - 4][j] == 0
                                  && playBoard[i - 3][j] == color
                                  && playBoard[i - 5][j] == color) { // 1010[11] (left)
                              superWeight[i - 2][j] += 40;
                              superWeight[i - 4][j] += 40;
                              add += 2;
                          }else if (i - 3 > 0 && i + 2 < 19 && playBoard[i - 2][j] == 0
                                  && playBoard[i + 1][j] == 0
                                  && playBoard[i - 3][j] == color
                                  && playBoard[i + 2][j] == color) { // 10[11]01 mid
                              superWeight[i - 2][j] += 40;
                              superWeight[i + 1][j] += 40;
                              add += 2;
                          }else if (i + 4 < 19 && playBoard[i + 1][j] == 0
                                  && playBoard[i + 3][j] == 0
                                  && playBoard[i + 2][j] == color
                                  && playBoard[i + 4][j] == color) { // [11]0101 (right)
                              superWeight[i + 3][j] += 40;
                              superWeight[i + 1][j] += 40;
                              add += 2;
                          }else if (i + 4 < 19 && playBoard[i + 1][j] == 0
                                  && playBoard[i + 4][j] == 0
                                  && playBoard[i + 2][j] == color
                                  && playBoard[i + 3][j] == color) { // [11]0110 (right)
                              superWeight[i + 4][j] += 40;
                              superWeight[i + 1][j] += 40;
                              add += 2;
                          }
                      }
                  } else
                      myCount = 0;
              } catch (ArrayIndexOutOfBoundsException e) {
              }
          }
      }

      // 좌대각 시작점
      // ----------------------------------------------------------------------

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
                      if (playBoard[temp1][temp2] == color) {
                          myCount++;
                          if (myCount == 5) {
                              if ((temp1 - 5 < 0 || temp2 - 5 < 0)
                                      && playBoard[temp1 + 1][temp2 + 1] == 0) {
                                  superWeight[temp1 + 1][temp2 + 1] += 40;
                                  add++;
                              } else if ((temp1 + 1 > 18 || temp2 + 1 > 18)
                                      && playBoard[temp1 - 5][temp2 - 5] == 0) {
                                  superWeight[temp1 - 5][temp2 - 5] += 40;
                                  add++;
                              } else if ((temp1 - 5 < 0 || temp2 - 5 < 0)
                                      && playBoard[temp1 - 5][temp2 - 5] == 0) {
                                  superWeight[temp1 - 5][temp2 - 5] += 80;
                                  add++;
                              } else if ((temp1 + 1 > 18 || temp2 + 1 > 18)
                                      && playBoard[temp1 + 1][temp2 + 1] == 0) {
                                  superWeight[temp1 + 1][temp2 + 1] += 80;
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
      // 4 왼쪽 위에서 오른쪽 아래(좌대각\) 공격
      for (int i = 0; i < 19; i++) {
          myCount = 0;
          for (int j = 0; j < 19; j++) {
              int temp1 = i;
              int temp2 = j;
              for (int k = 0; k < 4; k++) {
                  try {
                      if (playBoard[temp1][temp2] == color) {
                          myCount++;
                          if (myCount == 4) {
                              if ((temp1 - 4 <= 0 || temp2 - 4 <= 0) && (temp1 + 2 <= 18 && temp2 + 2 <= 18)
                                      && playBoard[temp1 + 1][temp2 + 1] == 0
                                      && playBoard[temp1 + 2][temp2 + 2] == 0) {
                                  superWeight[temp1 + 1][temp2 + 1] += 40;
                                  add++;
                                  superWeight[temp1 + 2][temp2 + 2] += 40;
                                  add++;
                              } else if ((temp1 + 1 >= 18 || temp2 + 1 >= 18) && (temp1 - 5 >= 0 && temp2 - 5 >= 0)
                                      && playBoard[temp1 - 5][temp2 - 5] == 0
                                      && playBoard[temp1 - 4][temp2 - 4] == 0) {
                                  superWeight[temp1 - 5][temp2 - 5] += 40;
                                  add++;
                                  superWeight[temp1 - 4][temp2 - 4] += 40;
                                  add++;
                              } else if ((temp1 - 4 > 0 && temp2 - 4 > 0) && (temp1 + 1 < 19 && temp2 + 1 < 19)
                                      && playBoard[temp1 - 4][temp2 - 4] == 0
                                      && playBoard[temp1 + 1][temp2 + 1] == 0) {
                                  superWeight[temp1 - 4][temp2 - 4] += 80;
                                  add++;
                                  superWeight[temp1 + 1][temp2 + 1] += 80;
                                  add++;
                              } else if ((temp1 - 5 >= 0 && temp2 - 5 >= 0)
                                      && (playBoard[temp1 - 4][temp2 - 4] == 0
                                              && playBoard[temp1 - 5][temp2 - 5] == 0)) {
                                  superWeight[temp1 - 4][temp2 - 4] += 80;
                                  add++;
                                  superWeight[temp1 - 5][temp2 - 5] += 80;
                                  add++;
                              } else if ((temp1 + 2 <= 18 && temp2 + 2 <= 18)
                                      && (playBoard[temp1 + 1][temp2 + 1] == 0
                                              && playBoard[temp1 + 2][temp2 + 2] == 0)) {
                                  superWeight[temp1 + 1][temp2 + 1] += 80;
                                  add++;
                                  superWeight[temp1 + 2][temp2 + 2] += 80;
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
      // 3(좌대각\) 1공백 공격
      for (int i = 0; i < 19; i++) {
          myCount = 0;
          for (int j = 0; j < 19; j++) {
              int temp1 = i;
              int temp2 = j;
              for (int k = 0; k < 3; k++) {
                  try {
                      if (playBoard[temp1][temp2] == color) {
                          myCount++;
                          if (myCount == 3) {
                              if ((temp1 + 3 <= 18 && temp2 + 3 <= 18)
                                      && playBoard[temp1 + 1][temp2 + 1] == 0
                                      && playBoard[temp1 + 2][temp2 + 2] == color
                                      && playBoard[temp1 + 3][temp2 + 3] == 0) {
                                  superWeight[temp1 + 1][temp2 + 1] += 40;
                                  add++;
                                  superWeight[temp1 + 3][temp2 + 3] += 40;
                                  add++;

                              } else if ((temp1 - 5 >= 0 && temp2 - 5 >= 0)
                                      && playBoard[temp1 - 3][temp2 - 3] == 0
                                      && playBoard[temp1 - 4][temp2 - 4] == color
                                      && playBoard[temp1 - 5][temp2 - 5] == 0) {
                                  superWeight[temp1 - 5][temp2 - 5] += 40;
                                  add++;
                                  superWeight[temp1 - 3][temp2 - 3] += 40;
                                  add++;
                              }
                              temp1++;
                              temp2++;
                          } else
                              myCount = 0;
                      }
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
                      if (playBoard[temp1][temp2] == color) {
                          myCount++;
                          if (myCount == 3) {
                              if ((temp1 + 3 <= 18 && temp2 + 3 <= 18)
                                      && playBoard[temp1 + 1][temp2 + 1] == 0
                                      && playBoard[temp1 + 2][temp2 + 2] == 0
                                      && playBoard[temp1 + 3][temp2 + 3] == color) {
                                  superWeight[temp1 + 1][temp2 + 1] += 40;
                                  add++;
                                  superWeight[temp1 + 2][temp2 + 2] += 40;
                                  add++;

                              } else if ((temp1 - 5 >= 0 && temp2 - 5 >= 0)
                                      && playBoard[temp1 - 3][temp2 - 3] == 0
                                      && playBoard[temp1 - 4][temp2 - 4] == 0
                                      && playBoard[temp1 - 5][temp2 - 5] == color) {
                                  superWeight[temp1 - 4][temp2 - 4] += 40;
                                  add++;
                                  superWeight[temp1 - 3][temp2 - 3] += 40;
                                  add++;
                              }
                              temp1++;
                              temp2++;
                          } else
                              myCount = 0;
                      }
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
                      if (playBoard[temp1][temp2] == color) {
                          myCount++;
                          try {
                              if (myCount == 2) {
                                  // 101011(up)
                                  if ((temp1 - 5 >= 0 && temp2 - 5 >= 0)
                                          && playBoard[temp1 - 4][temp2 - 4] == 0
                                          && playBoard[temp1 - 2][temp2 - 2] == 0
                                          && playBoard[temp1 - 3][temp2 - 3] == color
                                          && playBoard[temp1 - 5][temp2 - 5] == color) {
                                      superWeight[temp1 - 4][temp2 - 4] += 40;
                                      add++;
                                      superWeight[temp1 - 2][temp2 - 2] += 40;
                                      add++;
                                  }
                                  // 110011(up)
                                  else if ((temp1 - 5 >= 0 && temp2 - 5 >= 0)
                                          && playBoard[temp1 - 2][temp2 - 2] == 0
                                          && playBoard[temp1 - 3][temp2 - 3] == 0
                                          && playBoard[temp1 - 4][temp2 - 4] == color
                                          && playBoard[temp1 - 5][temp2 - 5] == color) {
                                      superWeight[temp1 - 2][temp2 - 2] += 40;
                                      add++;
                                      superWeight[temp1 - 3][temp2 - 3] += 40;
                                      add++;
                                  }
                                  // 011011(up)
                                  else if ((temp1 - 5 >= 0 && temp2 - 5 >= 0)
                                          && playBoard[temp1 - 2][temp2 - 2] == 0
                                          && playBoard[temp1 - 5][temp2 - 5] == 0
                                          && playBoard[temp1 - 4][temp2 - 4] == color
                                          && playBoard[temp1 - 3][temp2 - 3] == color) {
                                      superWeight[temp1 - 2][temp2 - 2] += 40;
                                      add++;
                                      superWeight[temp1 - 5][temp2 - 5] += 40;
                                      add++;
                                  }
                                  // 110110(mid)
                                  else if ((temp1 - 4 >= 0 && temp2 - 4 >= 0 && temp1 + 1 <= 18 && temp2 + 1 <= 18)
                                          && playBoard[temp1 + 1][temp2 + 1] == 0
                                          && playBoard[temp1 - 2][temp2 - 2] == 0
                                          && playBoard[temp1 - 3][temp2 - 3] == color
                                          && playBoard[temp1 - 4][temp2 - 4] == color) {
                                      superWeight[temp1 + 1][temp2 + 1] += 40;
                                      add++;
                                      superWeight[temp1 - 2][temp2 - 2] += 40;
                                      add++;
                                  }
                                  // 011011(mid)
                                  else if ((temp1 - 2 >= 0 && temp2 - 2 >= 0 && temp1 + 3 <= 18 && temp2 + 3 <= 18)
                                          && playBoard[temp1 - 2][temp2 - 2] == 0
                                          && playBoard[temp1 + 1][temp2 + 1] == 0
                                          && playBoard[temp1 + 2][temp2 + 2] == color
                                          && playBoard[temp1 + 3][temp2 + 3] == color) {
                                      superWeight[temp1 - 2][temp2 - 2] += 40;
                                      add++;
                                      superWeight[temp1 + 1][temp2 + 1] += 40;
                                      add++;
                                  }
                                  // 110110(mid)
                                  else if ((temp1 - 4 >= 0 && temp2 - 4 >= 0 && temp1 + 1 <= 18 && temp2 + 1 <= 18)
                                          && playBoard[temp1 + 1][temp2 + 1] == 0
                                          && playBoard[temp1 - 2][temp2 - 2] == 0
                                          && playBoard[temp1 - 3][temp2 - 3] == color
                                          && playBoard[temp1 - 4][temp2 - 4] == color) {
                                      superWeight[temp1 + 1][temp2 + 1] += 40;
                                      add++;
                                      superWeight[temp1 - 2][temp2 - 2] += 40;
                                      add++;
                                  }
                                  // 110110(down)
                                  else if ((temp1 - 4 <= 18 && temp2 - 4 <= 18)
                                          && playBoard[temp1 + 1][temp2 + 1] == 0
                                          && playBoard[temp1 + 4][temp2 + 4] == 0
                                          && playBoard[temp1 + 2][temp2 + 2] == color
                                          && playBoard[temp1 + 3][temp2 + 3] == color) {
                                      superWeight[temp1 + 1][temp2 + 1] += 40;
                                      add++;
                                      superWeight[temp1 + 4][temp2 + 4] += 40;
                                      add++;
                                  }
                                  // 110011(down)
                                  else if ((temp1 - 4 <= 18 && temp2 - 4 <= 18)
                                          && playBoard[temp1 + 1][temp2 + 1] == 0
                                          && playBoard[temp1 + 2][temp2 + 2] == 0
                                          && playBoard[temp1 + 3][temp2 + 3] == color
                                          && playBoard[temp1 + 4][temp2 + 4] == color) {
                                      superWeight[temp1 + 1][temp2 + 1] += 40;
                                      add++;
                                      superWeight[temp1 + 2][temp2 + 2] += 40;
                                      add++;
                                  }
                                  // 110101(down)
                                  else if ((temp1 - 4 <= 18 && temp2 - 4 <= 18)
                                          && playBoard[temp1 + 1][temp2 + 1] == 0
                                          && playBoard[temp1 + 4][temp2 + 4] == 0
                                          && playBoard[temp1 + 3][temp2 + 3] == color
                                          && playBoard[temp1 + 2][temp2 + 2] == color) {
                                      superWeight[temp1 + 1][temp2 + 1] += 40;
                                      add++;
                                      superWeight[temp1 + 4][temp2 + 4] += 40;
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

      // 우대각 시작점
      // -----------------------------------------------------------------------------------------

      if (add >= 2)
          return;
      // 5 오른쪽위에서 왼쪽아래 (우대각/) 공격
      for (int j = 0; j < 19; j++) {
          myCount = 0;
          for (int i = 0; i < 19; i++) {
              int temp1 = i;
              int temp2 = j;
              for (int k = 0; k < 5; k++) {
                  try {
                      if (playBoard[temp1][temp2] == color) {
                          myCount++;
                          if (myCount == 5) {
                              // 우상단 막히고 좌하단 뚫림
                              if ((temp1 + 5 > 18 || temp2 - 5 < 0) && (temp1 - 1 >= 0 || temp2 + 1 <= 18)
                                      && playBoard[temp1 - 1][temp2 + 1] == 0) {
                                  superWeight[temp1 - 1][temp2 + 1] += 40;
                                  add++;
                              }
                              // 좌하단 막히고 우상단 뚫림
                              else if ((temp1 - 1 < 0 || temp2 + 1 > 18) && (temp1 + 5 <= 18 && temp2 - 5 >= 0)
                                      && playBoard[temp1 + 5][temp2 - 5] == 0) {
                                  superWeight[temp1 + 5][temp2 - 5] += 40;
                                  add++;
                              }
                              // 우상단 자리있음
                              else if ((temp1 + 5 <= 18 && temp2 - 5 >= 0)
                                      && playBoard[temp1 + 5][temp2 - 5] == 0) {
                                  superWeight[temp1 + 5][temp2 - 5] += 80;
                                  add++;
                              }
                              // 좌하단 자리있음
                              else if ((temp1 - 1 >= 0 || temp2 + 1 <= 18)
                                      && playBoard[temp1 - 1][temp2 + 1] == 0) {
                                  superWeight[temp1 - 1][temp2 + 1] += 80;
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
      // 4 오른쪽위에서 왼쪽아래 (우대각/) 공격
      for (int j = 0; j < 19; j++) {
          myCount = 0;
          for (int i = 0; i < 19; i++) {
              int temp1 = i;
              int temp2 = j;
              for (int k = 0; k < 4; k++) {
                  try {
                      if (playBoard[temp1][temp2] == color) {
                          myCount++;
                          if (myCount == 4) {
                              // 우상단 막히고 좌하단에 두개
                              if ((temp1 + 4 > 18 || temp2 - 4 < 0) && (temp1 - 2 >= 0 && temp2 + 2 <= 18)
                                      && playBoard[temp1 - 1][temp2 + 1] == 0
                                      && playBoard[temp1 - 2][temp2 + 2] == 0) {
                                  superWeight[temp1 - 1][temp2 + 1] += 40;
                                  add++;
                                  superWeight[temp1 - 2][temp2 + 2] += 40;
                                  add++;
                                  // 촤하단 막히고 우상단에 두개
                              } else if ((temp1 + 1 > 18 || temp2 - 1 < 0) && (temp1 + 5 >= 0 && temp2 - 5 >= 0)
                                      && playBoard[temp1 + 5][temp2 - 5] == 0
                                      && playBoard[temp1 + 4][temp2 - 4] == 0) {
                                  superWeight[temp1 + 5][temp2 - 5] += 40;
                                  add++;
                                  superWeight[temp1 + 4][temp2 - 4] += 40;
                                  add++;
                                  // 우상단 좌하단에 하나씩 뚫림
                              } else if ((temp1 + 4 <= 18 && temp2 - 4 >= 0) && (temp1 - 1 >= 0 && temp2 + 1 <= 18)
                                      && playBoard[temp1 + 4][temp2 - 4] == 0
                                      && playBoard[temp1 - 1][temp2 + 1] == 0) {
                                  superWeight[temp1 + 4][temp2 - 4] += 80;
                                  add++;
                                  superWeight[temp1 - 1][temp2 + 1] += 80;
                                  add++;
                                  // 우상단에 두개
                              } else if ((temp1 + 5 >= 0 && temp2 - 5 >= 0)
                                      && playBoard[temp1 + 5][temp2 - 5] == 0
                                      && playBoard[temp1 + 4][temp2 - 4] == 0) {
                                  superWeight[temp1 + 5][temp2 - 5] += 40;
                                  add++;
                                  superWeight[temp1 + 4][temp2 - 4] += 40;
                                  add++;
                                  // 좌하단에 두개
                              } else if ((temp1 - 2 >= 0 && temp2 + 2 <= 18)
                                      && playBoard[temp1 - 1][temp2 + 1] == 0
                                      && playBoard[temp1 - 2][temp2 + 2] == 0) {
                                  superWeight[temp1 - 1][temp2 + 1] += 40;
                                  add++;
                                  superWeight[temp1 - 2][temp2 + 2] += 40;
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
      // 3(우대각/) 1공백 공격
      for (int j = 0; j < 19; j++) {
          myCount = 0;
          for (int i = 0; i < 19; i++) {
              int temp1 = i;
              int temp2 = j;
              for (int k = 0; k < 3; k++) {
                  try {
                      if (playBoard[temp1][temp2] == color) {
                          myCount++;
                          if (myCount == 3) {
                              if ((temp1 - 3 >= 0 && temp2 + 3 <= 18)
                                      && playBoard[temp1 - 1][temp2 + 1] == 0
                                      && playBoard[temp1 - 2][temp2 + 2] == color
                                      && playBoard[temp1 - 3][temp2 + 3] == 0) {
                                  superWeight[temp1 - 1][temp2 + 1] += 40;
                                  add++;
                                  superWeight[temp1 - 3][temp2 + 3] += 40;
                                  add++;

                              } else if ((temp1 + 5 <= 18 && temp2 - 5 >= 0)
                                      && playBoard[temp1 + 3][temp2 - 3] == 0
                                      && playBoard[temp1 + 4][temp2 - 4] == color
                                      && playBoard[temp1 + 5][temp2 - 5] == 0) {
                                  superWeight[temp1 + 5][temp2 - 5] += 40;
                                  add++;
                                  superWeight[temp1 + 3][temp2 - 3] += 40;
                                  add++;
                              }
                              temp1--;
                              temp2++;
                          } else
                              myCount = 0;
                      }
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
                      if (playBoard[temp1][temp2] == color) {
                          myCount++;
                          if (myCount == 3) {
                              if ((temp1 - 3 >= 0 && temp2 + 3 <= 18)
                                      && playBoard[temp1 - 1][temp2 + 1] == 0
                                      && playBoard[temp1 - 2][temp2 + 2] == 0
                                      && playBoard[temp1 - 3][temp2 + 3] == color) {
                                  superWeight[temp1 - 1][temp2 + 1] += 40;
                                  add++;
                                  superWeight[temp1 - 2][temp2 + 2] += 40;
                                  add++;

                              } else if ((temp1 + 5 <= 18 && temp2 - 5 >= 0)
                                      && playBoard[temp1 + 3][temp2 - 3] == 0
                                      && playBoard[temp1 + 4][temp2 - 4] == 0
                                      && playBoard[temp1 + 5][temp2 - 5] == color) {
                                  superWeight[temp1 + 4][temp2 - 4] += 40;
                                  add++;
                                  superWeight[temp1 + 3][temp2 - 3] += 40;
                                  add++;
                              }
                              temp1--;
                              temp2++;
                          } else
                              myCount = 0;
                      }
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
                      if (playBoard[temp1][temp2] == color) {
                          myCount++;
                          try {
                              if (myCount == 2) {
                                  // 011011(up)
                                  if ((temp1 + 5 <= 18 && temp1 - 5 >= 0)
                                          && playBoard[temp1 + 2][temp2 - 2] == 0
                                          && playBoard[temp1 + 5][temp2 - 5] == 0
                                          && playBoard[temp1 + 3][temp2 - 3] == color
                                          && playBoard[temp1 + 4][temp2 - 4] == color) {
                                      superWeight[temp1 + 2][temp2 - 2] += 40;
                                      add++;
                                      superWeight[temp1 + 5][temp2 - 5] += 40;
                                      add++;
                                  }
                                  // 101011(up)
                                  else if ((temp1 + 5 <= 18 && temp1 - 5 >= 0)
                                          && playBoard[temp1 + 4][temp2 - 4] == 0
                                          && playBoard[temp1 + 2][temp2 - 2] == 0
                                          && playBoard[temp1 + 3][temp2 - 3] == color
                                          && playBoard[temp1 + 5][temp2 - 5] == color) {
                                      superWeight[temp1 + 2][temp2 - 2] += 40;
                                      add++;
                                      superWeight[temp1 + 4][temp2 - 4] += 40;
                                      add++;
                                  }
                                  // 110011(up)
                                  else if ((temp1 + 5 <= 18 && temp1 - 5 >= 0)
                                          && playBoard[temp1 + 3][temp2 - 3] == 0
                                          && playBoard[temp1 + 2][temp2 - 2] == 0
                                          && playBoard[temp1 + 4][temp2 - 4] == color
                                          && playBoard[temp1 + 5][temp2 - 5] == color) {
                                      superWeight[temp1 + 2][temp2 - 2] += 40;
                                      add++;
                                      superWeight[temp1 + 3][temp2 - 3] += 40;
                                      add++;
                                  }
                                  // 101101(mid)
                                  else if ((temp1 - 2 >= 0 && temp2 + 2 <= 18) && (temp1 + 3 <= 18 && temp2 - 3 >= 0)
                                          && playBoard[temp1 + 2][temp2 - 2] == 0
                                          && playBoard[temp1 - 1][temp2 + 1] == 0
                                          && playBoard[temp1 - 2][temp2 + 2] == color
                                          && playBoard[temp1 + 3][temp2 - 3] == color) {
                                      superWeight[temp1 + 2][temp2 - 2] += 40;
                                      add++;
                                      superWeight[temp1 - 1][temp2 + 1] += 40;
                                      add++;
                                  }
                                  // 011011(mid)
                                  else if ((temp1 - 3 >= 0 && temp2 + 3 <= 18) && (temp1 + 2 <= 18 && temp2 - 2 >= 0)
                                          && playBoard[temp1 - 1][temp2 + 1] == 0
                                          && playBoard[temp1 + 2][temp2 - 2] == 0
                                          && playBoard[temp1 - 2][temp2 + 2] == color
                                          && playBoard[temp1 - 3][temp2 + 3] == color) {
                                      superWeight[temp1 - 1][temp2 + 1] += 40;
                                      add++;
                                      superWeight[temp1 + 2][temp2 - 2] += 40;
                                      add++;
                                  }
                                  // 110110(mid)
                                  else if ((temp1 - 4 >= 0 && temp2 + 4 <= 18) && (temp1 + 1 <= 18 && temp2 - 1 >= 0)
                                          && playBoard[temp1 - 1][temp2 + 1] == 0
                                          && playBoard[temp1 + 2][temp2 - 2] == 0
                                          && playBoard[temp1 + 3][temp2 - 3] == color
                                          && playBoard[temp1 + 4][temp2 - 4] == color) {
                                      superWeight[temp1 - 1][temp2 + 1] += 40;
                                      add++;
                                      superWeight[temp1 + 2][temp2 - 2] += 40;
                                      add++;
                                  }
                                  // 110011(down)
                                  else if ((temp1 - 4 >= 0 && temp1 + 4 <= 18)
                                          && playBoard[temp1 - 1][temp2 + 1] == 0
                                          && playBoard[temp1 - 2][temp2 + 2] == 0
                                          && playBoard[temp1 - 3][temp2 + 3] == color
                                          && playBoard[temp1 - 4][temp2 + 4] == color) {
                                      superWeight[temp1 - 1][temp2 + 1] += 40;
                                      add++;
                                      superWeight[temp1 - 2][temp2 + 2] += 40;
                                      add++;
                                  }
                                  // 110101(down)
                                  else if ((temp1 - 4 >= 0 && temp1 + 4 <= 18)
                                          && playBoard[temp1 - 1][temp2 + 1] == 0
                                          && playBoard[temp1 - 3][temp2 + 3] == 0
                                          && playBoard[temp1 - 2][temp2 + 2] == color
                                          && playBoard[temp1 - 4][temp2 + 4] == color) {
                                      superWeight[temp1 - 1][temp2 + 1] += 40;
                                      add++;
                                      superWeight[temp1 - 3][temp2 + 3] += 40;
                                      add++;
                                  }
                                  // 110110(down)
                                  else if ((temp1 - 4 >= 0 && temp1 + 4 <= 18)
                                          && playBoard[temp1 - 1][temp2 + 1] == 0
                                          && playBoard[temp1 - 4][temp2 + 4] == 0
                                          && playBoard[temp1 - 3][temp2 + 3] == color
                                          && playBoard[temp1 - 2][temp2 + 2] == color) {
                                      superWeight[temp1 - 1][temp2 + 1] += 40;
                                      add++;
                                      superWeight[temp1 - 4][temp2 + 4] += 40;
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

      // 작업중입니다 ==========================================

      // ====================================================================

      testTest(myCount, add);

      //// 안놓으면 질 때, 한방방어
      //// ----------------------------------------------------------------------------------

      if (add >= 2)
          return;
      // 5 세로 방어
      for (int i = 0; i < 19; i++) {
          myCount = 0;
          for (int j = 0; j < 19; j++) {
              try {
                  if (playBoard[i][j] == opponent) {
                      myCount++;
                      if (myCount == 5) {
                          // 양쪽 다 비었으면 양쪽 시급하게 막고
                          if (j - 5 < 0 && playBoard[i][j + 1] == 0) {
                              superWeight[i][j + 1] += 40;
                              add++;
                          }

                          else if (j + 1 > 18 && playBoard[i][j - 5] == 0) {
                              superWeight[i][j - 5] += 40;
                              add++;
                          } else if (playBoard[i][j - 5] == 0 && playBoard[i][j + 1] == 0) {
                              superWeight[i][j - 5] += 40;
                              add++;
                              superWeight[i][j + 1] += 40;
                              add++;
                          }
                          // 둘중 한쪽만 비었으면 거기 막기
                          else if (playBoard[i][j - 5] == 0) {
                              superWeight[i][j - 5] += 40;
                              add++;
                          } else if (playBoard[i][j + 1] == 0) {
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
                  if (playBoard[i][j] == opponent) {
                      myCount++;
                      if (myCount == 4) { // 양끝 뚫려있으면 양끝에 가중치
                          if (j - 4 < 0 && playBoard[i][j + 1] == 0) {
                              superWeight[i][j + 1] += 40;
                              add++;
                          }

                          else if (j + 1 > 18 && playBoard[i][j - 4] == 0) {
                              superWeight[i][j - 4] += 40;
                              add++;
                          } else if (playBoard[i][j - 4] == 0 && playBoard[i][j + 1] == 0) {
                              superWeight[i][j - 4] += 40;
                              add++;
                              superWeight[i][j + 1] += 40;
                              add++;
                          } // 한쪽만 뚫려있으면
                          else if (playBoard[i][j - 4] == 0) {
                              if (playBoard[i][j - 5] == 0) { // 한쪽이 더 뚫려있으면 둘중 이득인곳에 두고
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
                          } else if (playBoard[i][j + 1] == 0) {
                              if (playBoard[i][j + 2] == 0) {
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
                  if (playBoard[i][j] == opponent) {
                      myCount++;
                      if (myCount == 3) { // 세번연속일때
                          // 111010
                          if (playBoard[i][j + 1] == 0 && playBoard[i][j + 2] == opponent
                                  && playBoard[i][j + 3] == 0) {
                              if (weight[i][j + 3] > weight[i][j + 1]) {
                                  superWeight[i][j + 3] += 40;
                                  add++;
                              } else {
                                  weight[i][j + 1] += 40;
                                  add++;
                              }
                          }
                          // 010111
                          else if (playBoard[i][j - 3] == 0
                                  && playBoard[i][j - 4] == opponent
                                  && playBoard[i][j - 5] == 0) {
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
                  if (playBoard[i][j] == opponent) {
                      myCount++;
                      if (myCount == 3) { // 세번연속일때
                          // 111001
                          if (playBoard[i][j + 1] == 0 && playBoard[i][j + 2] == 0
                                  && playBoard[i][j + 3] == opponent) {
                              if (weight[i][j + 2] > weight[i][j + 1]) {
                                  superWeight[i][j + 2] += 40;
                                  add++;
                              } else {
                                  superWeight[i][j + 1] += 40;
                                  add++;
                              }
                          }
                          // 100111
                          else if (playBoard[i][j - 3] == 0 && playBoard[i][j - 4] == 0
                                  && playBoard[i][j - 5] == opponent) {
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
                  if (playBoard[i][j] == opponent) {
                      myCount++;
                      if (myCount == 2) {
                          if (playBoard[i][j + 1] == 0 && playBoard[i][j + 2] == 0
                                  && playBoard[i][j + 3] == opponent
                                  && playBoard[i][j + 4] == opponent) {
                              superWeight[i][j + 1] += 40;
                              add++;
                              superWeight[i][j + 2] += 40;
                              add++;

                          } else if (playBoard[i][j - 2] == 0 && playBoard[i][j - 3] == 0
                                  && playBoard[i][j - 4] == opponent
                                  && playBoard[i][j - 5] == opponent) {
                              superWeight[i][j - 2] += 40;
                              add++;
                              superWeight[i][j - 3] += 40;
                              add++;
                          } else if (playBoard[i][j - 2] == 0 && playBoard[i][j + 1] == 0
                                  && playBoard[i][j + 2] == opponent
                                  && playBoard[i][j + 3] == opponent) {
                              superWeight[i][j - 2] += 40;
                              add++;
                              superWeight[i][j + 1] += 40;
                              add++;
                          } else if (playBoard[i][j - 2] == 0 && playBoard[i][j - 5] == 0
                                  && playBoard[i][j - 3] == opponent
                                  && playBoard[i][j - 4] == opponent) {
                              superWeight[i][j - 2] += 40;
                              add++;
                              superWeight[i][j - 5] += 40;
                              add++;
                          } else if (playBoard[i][j - 2] == 0 && playBoard[i][j - 4] == 0
                                  && playBoard[i][j - 3] == opponent
                                  && playBoard[i][j - 5] == opponent) {
                              superWeight[i][j - 2] += 40;
                              add++;
                              superWeight[i][j - 4] += 40;
                              add++;
                          } else if (playBoard[i][j - 2] == 0 && playBoard[i][j + 1] == 0
                                  && playBoard[i][j - 3] == opponent
                                  && playBoard[i][j + 2] == opponent) {
                              superWeight[i][j - 2] += 40;
                              add++;
                              superWeight[i][j + 1] += 40;
                              add++;
                          } else if (playBoard[i][j + 1] == 0 && playBoard[i][j + 3] == 0
                                  && playBoard[i][j + 2] == opponent
                                  && playBoard[i][j + 4] == opponent) {
                              superWeight[i][j + 3] += 40;
                              add++;
                              superWeight[i][j + 1] += 40;
                              add++;
                          } else if (playBoard[i][j + 1] == 0 && playBoard[i][j + 4] == 0
                                  && playBoard[i][j + 2] == opponent
                                  && playBoard[i][j + 3] == opponent) {
                              superWeight[i][j + 4] += 40;
                              add++;
                              superWeight[i][j + 1] += 40;
                              add++;
                          } else if (playBoard[i][j + 1] == 0 && playBoard[i][j - 2] == 0
                                  && playBoard[i][j - 3] == opponent
                                  && playBoard[i][j - 4] == opponent) {
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
                  if (playBoard[i][j] == opponent) {
                      myCount++;
                      if (myCount == 5) {
                          // 양쪽 다 비었으면 양쪽 시급하게 막고
                          if (i - 5 < 0 && playBoard[i + 1][j] == 0) {
                              superWeight[i + 1][j] += 40;
                              add++;
                          }

                          else if (i + 1 > 18 && playBoard[i - 5][j] == 0) {
                              superWeight[i - 5][j] += 40;
                              add++;
                          } else if (playBoard[i - 5][j] == 0 && playBoard[i + 1][j] == 0) {
                              superWeight[i - 5][j] += 40;
                              add++;
                              superWeight[i + 1][j] += 40;
                              add++;
                          }
                          // 둘중 한쪽만 비었으면 거기 막기
                          else if (playBoard[i - 5][j] == 0) {
                              superWeight[i - 5][j] += 40;
                              add++;
                          } else if (playBoard[i + 1][j] == 0) {
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
                  if (playBoard[i][j] == opponent) {
                      myCount++;
                      if (myCount == 4) { // 양끝 뚫려있으면 양끝에 가중치
                          if (i - 4 < 0 && playBoard[i + 1][j] == 0) {
                              superWeight[i + 1][j] += 40;
                              add++;
                          }

                          else if (i + 1 > 18 && playBoard[i - 4][j] == 0) {
                              superWeight[i - 4][j] += 40;
                              add++;
                          } else if (playBoard[i - 4][j] == 0 && playBoard[i + 1][j] == 0) {
                              superWeight[i - 4][j] += 40;
                              add++;
                              superWeight[i + 1][j] += 40;
                              add++;
                          } // 한쪽만 뚫려있으면
                          else if (playBoard[i - 4][j] == 0) {
                              if (playBoard[i - 5][j] == 0) { // 한쪽이 더 뚫려있으면 둘중 이득인곳에 두고
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
                          } else if (playBoard[i + 1][j] == 0) {
                              if (playBoard[i + 2][j] == 0) {
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
                  if (playBoard[i][j] == opponent) {
                      myCount++;
                      if (myCount == 3) {
                          if (playBoard[i + 1][j] == 0 && playBoard[i + 2][j] == opponent
                                  && playBoard[i + 3][j] == 0) {
                              if (weight[i + 3][j] > weight[i + 1][j]) {
                                  superWeight[i + 3][j] += 40;
                                  add++;
                              } else {
                                  weight[i + 1][j] += 40;
                                  add++;
                              }
                          } else if (playBoard[i - 3][j] == 0
                                  && playBoard[i - 4][j] == opponent
                                  && playBoard[i - 5][j] == 0) {
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
                  if (playBoard[i][j] == opponent) {
                      myCount++;
                      if (myCount == 3) {
                          if (playBoard[i + 1][j] == 0 && playBoard[i + 2][j] == 0
                                  && playBoard[i + 3][j] == opponent) {
                              if (weight[i + 2][j] > weight[i + 1][j]) {
                                  superWeight[i + 2][j] += 40;
                                  add++;
                              } else {
                                  superWeight[i + 1][j] += 40;
                                  add++;
                              }
                          } else if (playBoard[i - 3][j] == 0 && playBoard[i - 4][j] == 0
                                  && playBoard[i - 5][j] == opponent) {
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
                  if (playBoard[i][j] == opponent) {
                      myCount++;
                      if (myCount == 2) {
                          if (playBoard[i + 1][j] == 0 && playBoard[i + 2][j] == 0
                                  && playBoard[i + 3][j] == opponent
                                  && playBoard[i + 4][j] == opponent) {
                              superWeight[i + 1][j] += 40;
                              add++;
                              superWeight[i + 2][j] += 40;
                              add++;

                          } else if (playBoard[i - 2][j] == 0 && playBoard[i - 3][j] == 0
                                  && playBoard[i - 4][j] == opponent
                                  && playBoard[i - 5][j] == opponent) {
                              superWeight[i - 2][j] += 40;
                              add++;
                              superWeight[i - 3][j] += 40;
                              add++;
                          } else if (playBoard[i - 2][j] == 0 && playBoard[i + 1][j] == 0
                                  && playBoard[i + 2][j] == opponent
                                  && playBoard[i + 3][j] == opponent) {
                              superWeight[i - 2][j] += 40;
                              add++;
                              superWeight[i + 1][j] += 40;
                              add++;
                          } else if (playBoard[i - 2][j] == 0 && playBoard[i - 5][j] == 0
                                  && playBoard[i - 3][j] == opponent
                                  && playBoard[i - 4][j] == opponent) {
                              superWeight[i - 2][j] += 40;
                              add++;
                              superWeight[i - 5][j] += 40;
                              add++;
                          } else if (playBoard[i - 2][j] == 0 && playBoard[i - 4][j] == 0
                                  && playBoard[i - 3][j] == opponent
                                  && playBoard[i - 5][j] == opponent) {
                              superWeight[i - 2][j] += 40;
                              add++;
                              superWeight[i - 4][j] += 40;
                              add++;
                          } else if (playBoard[i - 2][j] == 0 && playBoard[i + 1][j] == 0
                                  && playBoard[i - 3][j] == opponent
                                  && playBoard[i + 2][j] == opponent) {
                              superWeight[i - 2][j] += 40;
                              add++;
                              superWeight[i + 1][j] += 40;
                              add++;
                          } else if (playBoard[i + 1][j] == 0 && playBoard[i + 3][j] == 0
                                  && playBoard[i + 2][j] == opponent
                                  && playBoard[i + 4][j] == opponent) {
                              superWeight[i + 3][j] += 40;
                              add++;
                              superWeight[i + 1][j] += 40;
                              add++;
                          } else if (playBoard[i + 1][j] == 0 && playBoard[i + 4][j] == 0
                                  && playBoard[i + 2][j] == opponent
                                  && playBoard[i + 3][j] == opponent) {
                              superWeight[i + 4][j] += 40;
                              add++;
                              superWeight[i + 1][j] += 40;
                              add++;
                          } else if (playBoard[i + 1][j] == 0 && playBoard[i - 2][j] == 0
                                  && playBoard[i - 3][j] == opponent
                                  && playBoard[i - 4][j] == opponent) {
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
                      if (playBoard[temp1][temp2] == opponent) {
                          myCount++;
                          if (myCount == 5) {
                              // 둘다뚫림
                              if ((temp1 - 5 < 0 || temp2 - 5 < 0)
                                      && playBoard[temp1 + 1][temp2 + 1] == 0) {
                                  superWeight[temp1 + 1][temp2 + 1] += 40;
                                  add++;
                              } else if ((temp1 + 1 > 18 || temp2 + 1 > 18)
                                      && playBoard[temp1 - 5][temp2 - 5] == 0) {
                                  superWeight[temp1 - 5][temp2 - 5] += 40;
                                  add++;
                              } else if (playBoard[temp1 - 5][temp2 - 5] == 0
                                      && playBoard[temp1 + 1][temp2 + 1] == 0) {
                                  superWeight[temp1 - 5][temp2 - 5] += 40;
                                  add++;
                                  superWeight[temp1 + 1][temp2 + 1] += 40;
                                  add++;
                              }
                              // 한쪽만뚫림 - 그 뚫린곳에 가중치
                              else if (playBoard[temp1 - 5][temp2 - 5] == 0
                                      || playBoard[temp1 + 1][temp2 + 1] == 0) {
                                  if (playBoard[temp1 - 5][temp2 - 5] == 0) {
                                      superWeight[temp1 - 5][temp2 - 5] += 40;
                                      add++;
                                  } else if (playBoard[temp1 + 1][temp2 + 1] == 0) {
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
                      if (playBoard[temp1][temp2] == opponent) {
                          myCount++;
                          if (myCount == 4) {
                              if ((temp1 - 4 < 0 || temp2 - 4 < 0)
                                      && playBoard[temp1 + 1][temp2 + 1] == 0) {
                                  superWeight[temp1 + 1][temp2 + 1] += 40;
                                  add++;
                              } else if ((temp1 + 1 > 18 || temp2 + 1 > 18)
                                      && playBoard[temp1 - 4][temp2 - 4] == 0) {
                                  superWeight[temp1 - 4][temp2 - 4] += 40;
                                  add++;
                              } else if (playBoard[temp1 - 4][temp2 - 4] == 0
                                      && playBoard[temp1 + 1][temp2 + 1] == 0) {
                                  superWeight[temp1 - 4][temp2 - 4] += 40;
                                  add++;
                                  superWeight[temp1 + 1][temp2 + 1] += 40;
                                  add++;
                              } else if (playBoard[temp1 - 4][temp2 - 4] == 0
                                      || playBoard[temp1 + 1][temp2 + 1] == 0) {
                                  if (playBoard[temp1 - 4][temp2 - 4] == 0
                                          && playBoard[temp1 - 5][temp2 - 5] == 0) {
                                      if (weight[temp1 - 4][temp2 - 4] > weight[temp1 - 5][temp2 - 5]) {
                                          superWeight[temp1 - 4][temp2 - 4] += 40;
                                          add++;
                                      } else {
                                          superWeight[temp1 - 5][temp2 - 5] += 40;
                                          add++;
                                      }
                                  } else if (playBoard[temp1 + 1][temp2 + 1] == 0
                                          && playBoard[temp1 + 2][temp2 + 2] == 0) {
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
                      if (playBoard[temp1][temp2] == opponent) {
                          myCount++;
                          if (myCount == 3) {
                              if (playBoard[temp1 + 1][temp2 + 1] == 0
                                      && playBoard[temp1 + 2][temp2 + 2] == opponent
                                      && playBoard[temp1 + 3][temp2 + 3] == 0) {
                                  if (weight[temp1 + 3][temp2 + 3] > weight[temp1 + 1][temp2 + 1]) {
                                      superWeight[temp1 + 3][temp2 + 3] += 40;
                                      add++;
                                  } else {
                                      weight[temp1 + 1][temp2 + 1] += 40;
                                      add++;
                                  }
                              } else if (playBoard[temp1 - 3][temp2 - 3] == 0
                                      && playBoard[temp1 - 4][temp2 - 4] == opponent
                                      && playBoard[temp1 - 5][temp2 - 5] == 0) {
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
                      if (playBoard[temp1][temp2] == opponent) {
                          myCount++;
                          if (myCount == 3) {
                              if (playBoard[temp1 + 1][temp2 + 1] == 0
                                      && playBoard[temp1 + 2][temp2 + 2] == 0
                                      && playBoard[temp1 + 3][temp2 + 3] == opponent) {
                                  if (weight[temp1 + 1][temp2 + 1] > weight[temp1 + 2][temp2 + 2]) {
                                      superWeight[temp1 + 1][temp2 + 1] += 40;
                                      add++;
                                  } else {
                                      weight[temp1 + 2][temp2 + 2] += 40;
                                      add++;
                                  }
                              } else if (playBoard[temp1 - 3][temp2 - 3] == 0
                                      && playBoard[temp1 - 4][temp2 - 4] == 0
                                      && playBoard[temp1 - 5][temp2 - 5] == opponent) {
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
                      if (playBoard[temp1][temp2] == opponent) {
                          myCount++;
                          try {
                              if (myCount == 2) {
                                  if (playBoard[temp1 + 1][temp2 + 1] == 0
                                          && playBoard[temp1 + 4][temp2 + 4] == 0
                                          && playBoard[temp1 + 2][temp2 + 2] == opponent
                                          && playBoard[temp1 + 3][temp2 + 3] == opponent) {
                                      superWeight[temp1 + 1][temp2 + 1] += 40;
                                      add++;
                                      superWeight[temp1 + 4][temp2 + 4] += 40;
                                      add++;

                                  } else if (playBoard[temp1 + 1][temp2 + 1] == 0
                                          && playBoard[temp1 - 2][temp2 - 2] == 0
                                          && playBoard[temp1 - 3][temp2 - 3] == opponent
                                          && playBoard[temp1 - 4][temp2 - 4] == opponent) {
                                      superWeight[temp1 + 1][temp2 + 1] += 40;
                                      add++;
                                      superWeight[temp1 - 2][temp2 - 2] += 40;
                                      add++;
                                  } else if (playBoard[temp1 + 1][temp2 + 1] == 0
                                          && playBoard[temp1 + 2][temp2 + 2] == 0
                                          && playBoard[temp1 + 2][temp2 + 2] == opponent
                                          && playBoard[temp1 + 4][temp2 + 4] == opponent) {
                                      superWeight[temp1 + 1][temp2 + 1] += 40;
                                      add++;
                                      superWeight[temp1 + 2][temp2 + 2] += 40;
                                      add++;
                                  } else if (playBoard[temp1 + 1][temp2 + 1] == 0
                                          && playBoard[temp1 + 2][temp2 + 2] == 0
                                          && playBoard[temp1 + 3][temp2 + 3] == opponent
                                          && playBoard[temp1 + 4][temp2 + 4] == opponent) {
                                      superWeight[temp1 + 1][temp2 + 1] += 40;
                                      add++;
                                      superWeight[temp1 + 2][temp2 + 2] += 40;
                                      add++;
                                  } else if (playBoard[temp1 - 2][temp2 - 2] == 0
                                          && playBoard[temp1 - 3][temp2 - 3] == 0
                                          && playBoard[temp1 - 4][temp2 - 4] == opponent
                                          && playBoard[temp1 - 5][temp2 - 5] == opponent) {
                                      superWeight[temp1 - 2][temp2 - 2] += 40;
                                      add++;
                                      superWeight[temp1 - 3][temp2 - 3] += 40;
                                      add++;
                                  } else if (playBoard[temp1 - 2][temp2 - 2] == 0
                                          && playBoard[temp1 + 1][temp2 + 1] == 0
                                          && playBoard[temp1 - 3][temp2 - 3] == opponent
                                          && playBoard[temp1 + 2][temp2 + 2] == opponent) {
                                      superWeight[temp1 - 2][temp2 - 2] += 40;
                                      add++;
                                      superWeight[temp1 + 1][temp2 + 1] += 40;
                                      add++;
                                  } else if (playBoard[temp1 - 4][temp2 - 4] == 0
                                          && playBoard[temp1 - 2][temp2 - 2] == 0
                                          && playBoard[temp1 - 3][temp2 - 3] == opponent
                                          && playBoard[temp1 - 5][temp2 - 5] == opponent) {
                                      superWeight[temp1 - 4][temp2 - 4] += 40;
                                      add++;
                                      superWeight[temp1 - 2][temp2 - 2] += 40;
                                      add++;
                                  } else if (playBoard[temp1 + 1][temp2 + 1] == 0
                                          && playBoard[temp1 - 2][temp2 - 2] == 0
                                          && playBoard[temp1 + 3][temp2 + 3] == opponent
                                          && playBoard[temp1 + 2][temp2 + 2] == opponent) {
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
                      if (playBoard[temp1][temp2] == opponent) {
                          myCount++;
                          if (myCount == 5) {
                              if ((temp1 - 1 < 0 || temp2 + 1 > 18)
                                      && playBoard[temp1 + 5][temp2 - 5] == 0) {
                                  superWeight[temp1 + 5][temp2 - 5] += 40;
                                  add++;
                              } else if ((temp1 + 5 > 18 || temp2 - 5 < 0)
                                      && playBoard[temp1 - 1][temp2 + 1] == 0) {
                                  superWeight[temp1 - 1][temp2 + 1] += 40;
                                  add++;
                              } else if (playBoard[temp1 + 5][temp2 - 5] == 0
                                      && playBoard[temp1 - 1][temp2 + 1] == 0) {
                                  superWeight[temp1 + 5][temp2 - 5] += 40;
                                  add++;
                                  superWeight[temp1 - 1][temp2 + 1] += 40;
                                  add++;
                              } else if (playBoard[temp1 + 4][temp2 - 4] == 0
                                      || playBoard[temp1 - 1][temp2 + 1] == 0) {
                                  if (playBoard[temp1 + 5][temp2 - 5] == 0) {
                                      superWeight[temp1 + 5][temp2 - 5] += 40;
                                      add++;
                                  } else if (playBoard[temp1 - 1][temp2 + 1] == 0) {
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
                      if (playBoard[temp1][temp2] == opponent) {
                          myCount++;
                          if (myCount == 4) {
                              if ((temp1 - 1 < 0 || temp2 + 1 > 18)
                                      && playBoard[temp1 + 4][temp2 - 4] == 0) {
                                  superWeight[temp1 + 4][temp2 - 4] += 40;
                                  add++;
                              } else if ((temp1 + 4 > 18 || temp2 - 4 < 0)
                                      && playBoard[temp1 - 1][temp2 + 1] == 0) {
                                  superWeight[temp1 - 1][temp2 + 1] += 40;
                                  add++;
                              } else if (playBoard[temp1 + 4][temp2 - 4] == 0
                                      && playBoard[temp1 - 1][temp2 + 1] == 0) {
                                  superWeight[temp1 + 4][temp2 - 4] += 40;
                                  add++;
                                  superWeight[temp1 - 1][temp2 + 1] += 40;
                                  add++;
                              } else if (playBoard[temp1 + 4][temp2 - 4] == 0
                                      || playBoard[temp1 - 1][temp2 + 1] == 0) {
                                  if (playBoard[temp1 + 4][temp2 - 4] == 0
                                          && playBoard[temp1 + 5][temp2 - 5] == 0) {
                                      if (weight[temp1 + 4][temp2 - 4] > weight[temp1 + 5][temp2 - 5]) {
                                          superWeight[temp1 + 4][temp2 - 4] += 40;
                                          add++;
                                      } else {
                                          superWeight[temp1 + 5][temp2 - 5] += 40;
                                          add++;
                                      }
                                  } else if (playBoard[temp1 - 1][temp2 + 1] == 0
                                          && playBoard[temp1 - 2][temp2 + 2] == 0) {
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
                      if (playBoard[temp1][temp2] == opponent) {
                          myCount++;

                          if (myCount == 3) {
                              if (playBoard[temp1 - 1][temp2 + 1] == 0
                                      && playBoard[temp1 - 2][temp2 + 2] == opponent
                                      && playBoard[temp1 - 3][temp2 + 3] == 0) {
                                  if (weight[temp1 - 3][temp2 + 3] > weight[temp1 - 1][temp2 + 1]) {
                                      superWeight[temp1 - 3][temp2 + 3] += 40;
                                      add++;
                                  } else {
                                      weight[temp1 - 1][temp2 + 1] += 40;
                                      add++;
                                  }
                              } else if (playBoard[temp1 + 3][temp2 - 3] == 0
                                      && playBoard[temp1 + 4][temp2 - 4] == opponent
                                      && playBoard[temp1 + 5][temp2 - 5] == 0) {
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
                      if (playBoard[temp1][temp2] == opponent) {
                          myCount++;

                          if (myCount == 3) {
                              if (playBoard[temp1 - 1][temp2 + 1] == 0
                                      && playBoard[temp1 - 2][temp2 + 2] == 0
                                      && playBoard[temp1 - 3][temp2 + 3] == opponent) {
                                  if (weight[temp1 - 2][temp2 + 2] > weight[temp1 - 1][temp2 + 1]) {
                                      superWeight[temp1 - 2][temp2 + 2] += 40;
                                      add++;
                                  } else {
                                      weight[temp1 - 1][temp2 + 1] += 40;
                                      add++;
                                  }
                              } else if (playBoard[temp1 + 3][temp2 - 3] == 0
                                      && playBoard[temp1 + 4][temp2 - 4] == 0
                                      && playBoard[temp1 + 5][temp2 - 5] == opponent) {
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
                      if (playBoard[temp1][temp2] == opponent) {
                          myCount++;
                          try {
                              if (myCount == 2) {
                                  if (playBoard[temp1 - 1][temp2 + 1] == 0
                                          && playBoard[temp1 + 2][temp2 - 2] == 0
                                          && playBoard[temp1 - 2][temp2 + 2] == opponent
                                          && playBoard[temp1 - 3][temp2 + 3] == opponent) {
                                      superWeight[temp1 - 1][temp2 + 1] += 40;
                                      add++;
                                      superWeight[temp1 + 2][temp2 - 2] += 40;
                                      add++;

                                  } else if (playBoard[temp1 + 2][temp2 - 2] == 0
                                          && playBoard[temp1 + 5][temp2 - 5] == 0
                                          && playBoard[temp1 + 3][temp2 - 3] == opponent
                                          && playBoard[temp1 + 4][temp2 - 4] == opponent) {
                                      superWeight[temp1 + 2][temp2 - 2] += 40;
                                      add++;
                                      superWeight[temp1 + 5][temp2 - 5] += 40;
                                      add++;
                                  } else if (playBoard[temp1 + 4][temp2 - 4] == 0
                                          && playBoard[temp1 + 2][temp2 - 2] == 0
                                          && playBoard[temp1 + 3][temp2 - 3] == opponent
                                          && playBoard[temp1 + 5][temp2 - 5] == opponent) {
                                      superWeight[temp1 + 4][temp2 - 4] += 40;
                                      add++;
                                      superWeight[temp1 + 2][temp2 - 2] += 40;
                                      add++;
                                  } else if (playBoard[temp1 + 2][temp2 - 2] == 0
                                          && playBoard[temp1 - 1][temp2 + 1] == 0
                                          && playBoard[temp1 - 2][temp2 + 2] == opponent
                                          && playBoard[temp1 + 3][temp2 - 3] == opponent) {
                                      superWeight[temp1 + 2][temp2 - 2] += 40;
                                      add++;
                                      superWeight[temp1 - 1][temp2 + 1] += 40;
                                      add++;
                                  } else if (playBoard[temp1 + 3][temp2 - 3] == 0
                                          && playBoard[temp1 + 2][temp2 - 2] == 0
                                          && playBoard[temp1 + 3][temp2 - 3] == opponent
                                          && playBoard[temp1 + 4][temp2 - 4] == opponent) {
                                      superWeight[temp1 + 3][temp2 - 3] += 40;
                                      add++;
                                      superWeight[temp1 + 2][temp2 - 2] += 40;
                                      add++;
                                  } else if (playBoard[temp1 - 1][temp2 + 1] == 0
                                          && playBoard[temp1 - 2][temp2 + 2] == 0
                                          && playBoard[temp1 - 3][temp2 + 3] == opponent
                                          && playBoard[temp1 - 4][temp2 + 4] == opponent) {
                                      superWeight[temp1 - 1][temp2 + 1] += 40;
                                      add++;
                                      superWeight[temp1 - 2][temp2 + 2] += 40;
                                      add++;
                                  } else if (playBoard[temp1 + 2][temp2 - 2] == 0
                                          && playBoard[temp1 + 4][temp2 - 4] == 0
                                          && playBoard[temp1 + 3][temp2 - 3] == opponent
                                          && playBoard[temp1 + 5][temp2 - 5] == opponent) {
                                      superWeight[temp1 + 2][temp2 - 2] += 40;
                                      add++;
                                      superWeight[temp1 + 4][temp2 - 4] += 40;
                                      add++;
                                  } else if (playBoard[temp1 + 2][temp2 - 2] == 0
                                          && playBoard[temp1 + 5][temp2 - 5] == 0
                                          && playBoard[temp1 + 3][temp2 - 3] == opponent
                                          && playBoard[temp1 + 4][temp2 - 4] == opponent) {
                                      superWeight[temp1 + 2][temp2 - 2] += 40;
                                      add++;
                                      superWeight[temp1 + 5][temp2 - 5] += 40;
                                      add++;
                                  } else if (playBoard[temp1 - 1][temp2 + 1] == 0
                                          && playBoard[temp1 + 2][temp2 - 2] == 0
                                          && playBoard[temp1 - 2][temp2 + 2] == opponent
                                          && playBoard[temp1 - 3][temp2 + 3] == opponent) {
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
  public static void returnPoint() {
	  
	  addSuperWeight();
	  
      int max = 0;
      for (int i = 0; i < 19; i++) {
          for (int j = 0; j < 19; j++) {
              if (superWeight[i][j] + weight[i][j] > max) {
                  max = superWeight[i][j] + weight[i][j];
                  x = i;
                  y = j;
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

  
  
  //공격을 그대로 카피해서 만든 방어...수정 요함. TODO 
  static void testTest(int myCount, int add) {

      //// 한방오리백숙
      //// -------------------------------------------------------------------------------------

      // 세로 시작점 ----------------------------------------------------------------------

      if (add >= 2)
          return;
      // 공격/세로/5-1
      for (int i = 0; i < 19; i++) {
          myCount = 0;
          for (int j = 0; j < 19; j++) {
              try {
                  if (playBoard[i][j] == opponent) {
                      myCount++;
                      if (myCount == 5) {
                          // 양끝 중 아무 빈곳에 가중치 왕창 이벤트
                          /*
                           * if (j - 5 < 0 && playBoard[i][j + 1] == 0) { // |[11111]0 right
                           * superWeight[i][j + 1] += 40; add++; } else if (j + 1 > 18 &&
                           * playBoard[i][j - 5] == 0) { // 0[11111]| left superWeight[i][j - 5]
                           * += 40; add++; } else
                           */ if (j - 5 > 0 && playBoard[i][j - 5] == 0) { // 0[11111] left //made a change
                              superWeight[i][j - 5] += 80;
                              add++;
                          } else if (j + 1 < 19 && playBoard[i][j + 1] == 0) { // *[11111]0 right //also
                                                                                         // made a change
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
      // 공격/세로/1-4-1/2-4
      for (int i = 0; i < 19; i++) {
          myCount = 0;
          for (int j = 0; j < 19; j++) {
              try {
                  if (playBoard[i][j] == opponent) {
                      myCount++;

                      if (myCount == 4) {

                          /*
                           * if (j - 4 < 0 && playBoard[i][j + 1] == 0 &&
                           * playBoard[i][j + 2] == 0) { // |[1111]00 right superWeight[i][j +
                           * 1] += 40; superWeight[i][j + 2] += 40; add += 2; } else if (j + 1 > 18 &&
                           * playBoard[i][j - 4] == 0 && playBoard[i][j - 5] == 0) {
                           * // 00[1111]| left superWeight[i][j - 4] += 40; superWeight[i][j - 5] += 40;
                           * add += 2; } else
                           */ if (j - 4 > 0 && j + 1 < 19 && playBoard[i][j - 4] == 0
                                  && playBoard[i][j + 1] == 0) { // 0[1111]0 mid
                              superWeight[i][j - 4] += 80;
                              superWeight[i][j + 1] += 80;
                              add += 2;
                          }

                          else if (j - 5 < 0 && playBoard[i][j - 4] == 0
                                  && playBoard[i][j - 5] == 0) { // 00[1111] left ??????????????
                              superWeight[i][j - 4] += 80;
                              superWeight[i][j - 5] += 80;
                              add += 2;
                          } else if (j + 2 < 19 && playBoard[i][j + 1] == 0
                                  && playBoard[i][j + 2] == 0) { // [1111]00 right
                              superWeight[i][j + 2] += 80;
                              superWeight[i][j + 1] += 80;
                              add += 2;
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
      // 공격/세로/3-1-1-1/1-3-1-1
      for (int i = 0; i < 19; i++) {
          myCount = 0;
          for (int j = 0; j < 19; j++) {
              try {
                  if (playBoard[i][j] == opponent) {
                      myCount++;
                      if (myCount == 3) { // 세번연속일때

                          if (j + 3 < 19 && playBoard[i][j + 1] == 0
                                  && playBoard[i][j + 2] == opponent
                                  && playBoard[i][j + 3] == 0) { // [111]010 right

                              superWeight[i][j + 1] += 40;
                              superWeight[i][j + 3] += 40;
                              add += 2;
                          }

                          else if (j - 5 > 0
                                  && (playBoard[i][j - 3] == 0
                                          && playBoard[i][j - 4] == opponent
                                          && playBoard[i][j - 5] == 0)) { // 010[111] left

                              superWeight[i][j - 3] += 40;
                              superWeight[i][j - 5] += 40;
                              add += 2;
                          }

                          else if (j - 3 > 0 && j + 2 < 19 && playBoard[i][j - 3] == 0
                                  && playBoard[i][j + 1] == 0
                                  && playBoard[i][j + 2] == opponent) { // 0[111]01 mid
                              superWeight[i][j - 3] += 40;
                              superWeight[i][j + 2] += 40;
                              add += 2;
                          } else if (j - 4 > 0 && j + 1 < 19 && playBoard[i][j - 3] == 0
                                  && playBoard[i][j - 4] == opponent
                                  && playBoard[i][j + 1] == 0) { // 10[111]0 mid
                              superWeight[i][j - 3] += 40;
                              superWeight[i][j + 1] += 40;
                              add += 2;
                          }
                      } else
                          myCount = 0;
                  }
              } catch (ArrayIndexOutOfBoundsException e) {
              }
          }
      }

      if (add >= 2)
          return;
      // 공격/세로/3-2-1
      for (int i = 0; i < 19; i++) {
          myCount = 0;
          for (int j = 0; j < 19; j++) {
              try {
                  if (playBoard[i][j] == opponent) {
                      myCount++;
                      if (myCount == 3) { // 세번연속일때

                          if (j + 3 < 19 && (playBoard[i][j + 1] == 0 && playBoard[i][j + 2] == 0
                                  && playBoard[i][j + 3] == opponent)) { // [111]001 right

                              superWeight[i][j + 1] += 40;
                              superWeight[i][j + 2] += 40;
                              add += 2;
                          }

                          else if (j - 5 > 0
                                  && (playBoard[i][j - 3] == 0 && playBoard[i][j - 4] == 0
                                          && playBoard[i][j - 5] == opponent)) { // 100[111] left

                              superWeight[i][j - 3] += 40;
                              superWeight[i][j - 4] += 40;
                              add += 2;

                          }
                      } else
                          myCount = 0;
                  }
              } catch (ArrayIndexOutOfBoundsException e) {
              }
          }
      }

      if (add >= 2)
          return;
      // 공격/세로/2-2-2/1-2-1-2/1-1-1-1-2/1-1-2-1-1
      for (int i = 0; i < 19; i++) {
          myCount = 0;
          for (int j = 0; j < 19; j++) {
              // try { WHY????????????
              if (playBoard[i][j] == opponent) {
                  myCount++;
                  if (myCount == 2) {

                      try {

                          if (j + 4 < 19 && playBoard[i][j + 1] == 0 && playBoard[i][j + 2] == 0
                                  && playBoard[i][j + 3] == opponent
                                  && playBoard[i][j + 4] == opponent) { // [11]0011 right
                              superWeight[i][j + 1] += 40;
                              superWeight[i][j + 2] += 40;
                              add += 2;
                          } /*
                             * else if (j - 5 > 0 && playBoard[i][j - 2] == 0 &&
                             * playBoard[i][j - 3] == 0 && playBoard[i][j - 4] ==
                             * opponent && playBoard[i][j - 5] == opponent) { //
                             * 1100[11] left
                             * superWeight[i][j - 2] += 40; superWeight[i][j - 3] += 40; add += 2; }
                             */ else if (j - 2 > 0 && j + 3 > 19 && playBoard[i][j - 2] == 0
                                  && playBoard[i][j + 1] == 0
                                  && playBoard[i][j + 2] == opponent
                                  && playBoard[i][j + 3] == opponent) { // 0[11]011 mid
                              superWeight[i][j - 2] += 40;
                              superWeight[i][j + 1] += 40;
                              add += 2;
                          } /*
                             * else if (j - 5 > 0 && playBoard[i][j - 2] == 0 &&
                             * playBoard[i][j - 5] == 0 && playBoard[i][j - 3] ==
                             * opponent && playBoard[i][j - 4] == opponent) { //
                             * 0110[11] left
                             * superWeight[i][j - 2] += 40; superWeight[i][j - 5] += 40; add += 2; }
                             */ else if (j - 5 > 0 && playBoard[i][j - 2] == 0
                                  && playBoard[i][j - 4] == 0
                                  && playBoard[i][j - 3] == opponent
                                  && playBoard[i][j - 5] == opponent) { // 1010[11] left
                              superWeight[i][j - 2] += 40;
                              superWeight[i][j - 4] += 40;
                              add += 2;
                          } else if (j - 3 > 0 && j + 2 < 19 && playBoard[i][j - 2] == 0
                                  && playBoard[i][j + 1] == 0
                                  && playBoard[i][j - 3] == opponent
                                  && playBoard[i][j + 2] == opponent) { // 10[11]01 mid
                              superWeight[i][j - 2] += 40;
                              superWeight[i][j + 1] += 40;
                              add += 2;
                          } else if (j + 4 < 19 && playBoard[i][j + 1] == 0
                                  && playBoard[i][j + 3] == 0
                                  && playBoard[i][j + 2] == opponent
                                  && playBoard[i][j + 4] == opponent) { // [11]0101 right
                              superWeight[i][j + 3] += 40;
                              superWeight[i][j + 1] += 40;
                              add += 2;
                          } else if (j + 4 < 19 && playBoard[i][j + 1] == 0
                                  && playBoard[i][j + 4] == 0
                                  && playBoard[i][j + 2] == opponent
                                  && playBoard[i][j + 3] == opponent) { // [11]0110 right
                              superWeight[i][j + 4] += 40;
                              superWeight[i][j + 1] += 40;
                              add += 2;
                          } /*
                             * else if (j - 4 > 0 && j + 1 < 19 && playBoard[i][j + 1] == 0 &&
                             * playBoard[i][j - 2] == 0 && playBoard[i][j - 3] ==
                             * opponent && playBoard[i][j - 4] == opponent) { //
                             * 110[11]0 mid
                             * superWeight[i][j - 2] += 40; superWeight[i][j + 1] += 40; add += 2; }
                             */
                      } catch (ArrayIndexOutOfBoundsException e) {
                      }
                  }
              } else
                  myCount = 0;
              // }catch (ArrayIndexOutOfBoundsException e) {}
          }
      }

      // 가로 시작점
      // -------------------------------------------------------------------------------------------------

      if (add >= 2)
          return;
      // 공격/가로/5-1
      for (int j = 0; j < 19; j++) {
          myCount = 0;
          for (int i = 0; i < 19; i++) {
              try {
                  if (playBoard[i][j] == opponent) {
                      myCount++;
                      if (myCount == 5) { // 양끝 중 아무 빈곳에 가중치 왕창 이벤트
                          /*
                           * if (i - 5 < 0 && playBoard[i + 1][j] == 0) { // |[11111]0 (right) ?
                           * superWeight[i + 1][j] += 40; add++; }
                           * 
                           * else if (i + 1 > 18 && playBoard[i - 5][j] == 0) { // 0[11111]|
                           * (left) ? superWeight[i - 5][j] += 40; add++; }
                           */

                          /* else */ if (i - 5 > 0 && playBoard[i - 5][j] == 0) { // 0[11111] (left)
                              superWeight[i - 5][j] += 80;
                              add++;
                          }

                          else if (i + 1 < 19 && playBoard[i + 1][j] == 0) { // *[11111]0 (right)
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
      // 공격/가로/4-2/1-4-1
      for (int j = 0; j < 19; j++) {
          myCount = 0;
          for (int i = 0; i < 19; i++) {
              try {
                  if (playBoard[i][j] == opponent) {
                      myCount++;
                      if (myCount == 4) {

                          /*
                           * if (i - 4 < 0 && playBoard[i + 1][j] == 0 && playBoard[i
                           * + 2][j] == 0) { // |[1111]00 right //? superWeight[i + 1][j] += 40;
                           * superWeight[i + 2][j] += 40; add += 2; }
                           * 
                           * else if (i + 1 > 18 && playBoard[i - 5][j] == 0 &&
                           * playBoard[i - 4][j] == 0) { // 00[1111]| //(left) //? superWeight[i
                           * - 5][j] += 40; superWeight[i - 4][j] += 40; add += 2; }
                           */

                          /* else */ if (i - 4 > 0 && i + 1 < 19 && playBoard[i - 4][j] == 0
                                  && playBoard[i + 1][j] == 0) { // 0[1111]0 (mid)
                              superWeight[i - 4][j] += 80;
                              superWeight[i + 1][j] += 80;
                              add += 2;
                          }

                          if (i - 5 > 0 && playBoard[i - 4][j] == 0 && playBoard[i - 5][j] == 0) { // 00[1111]
                                                                                                                       // left
                              superWeight[i - 4][j] += 80;
                              superWeight[i - 5][j] += 80;
                              add += 2;
                          } else if (i + 2 < 19 && playBoard[i + 1][j] == 0
                                  && playBoard[i + 2][j] == 0) { // [1111]00 right
                              superWeight[i + 2][j] += 80;
                              superWeight[i + 1][j] += 80;
                              add += 2;
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
      // 공격/가로/3-1-1-1
      for (int j = 0; j < 19; j++) {
          myCount = 0;
          for (int i = 0; i < 19; i++) {
              try {
                  if (playBoard[i][j] == opponent) {
                      myCount++;
                      if (myCount == 3) {
                          if (i + 3 < 19 && playBoard[i + 1][j] == 0
                                  && playBoard[i + 2][j] == opponent
                                  && playBoard[i + 3][j] == 0) { // [111]010 right

                              superWeight[i + 3][j] += 40;
                              superWeight[i + 1][j] += 40;
                              add += 2;
                          } else if (i - 5 > 0 && playBoard[i - 3][j] == 0
                                  && playBoard[i - 4][j] == opponent
                                  && playBoard[i - 5][j] == 0) { // 010[111] left

                              superWeight[i - 3][j] += 40;
                              superWeight[i - 5][j] += 40;
                              add += 2;
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
      // 공격/가로/3-2-1/1-3-1-1
      for (int j = 0; j < 19; j++) {
          myCount = 0;
          for (int i = 0; i < 19; i++) {
              try {
                  if (playBoard[i][j] == opponent) {
                      myCount++;
                      if (myCount == 3) {
                          if (i + 3 < 19 && playBoard[i + 1][j] == 0 && playBoard[i + 2][j] == 0
                                  && playBoard[i + 3][j] == opponent) { // [111]001 right
                              superWeight[i + 1][j] += 40;
                              superWeight[i + 2][j] += 40;
                              add += 2;
                          } else if (i - 5 > 0 && playBoard[i - 3][j] == 0
                                  && playBoard[i - 4][j] == 0
                                  && playBoard[i - 5][j] == opponent) {// 100[111] left

                              superWeight[i - 3][j] += 40;
                              superWeight[i - 4][j] += 40;
                              add += 2;
                          }

                          else if (i - 3 > 0 && i + 2 < 19 && playBoard[i - 3][j] == 0
                                  && playBoard[i + 1][j] == 0
                                  && playBoard[i + 2][j] == opponent) { // 0[111]01 mid
                              superWeight[i - 3][j] += 40;
                              superWeight[i + 2][j] += 40;
                              add += 2;
                          } else if (i - 4 > 0 && i + 1 < 19 && playBoard[i - 3][j] == 0
                                  && playBoard[i - 4][j] == opponent
                                  && playBoard[i + 1][j] == 0) { // 10[111]0 mid
                              superWeight[i - 3][j] += 40;
                              superWeight[i + 1][j] += 40;
                              add += 2;
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
      // 공격/가로/2-2-2/1-2-1-2/1-1-1-1-2/1-1-2-1-1
      for (int j = 0; j < 19; j++) {
          myCount = 0; // initialize myCount when entering new row
          for (int i = 0; i < 19; i++) {
              try {
                  if (playBoard[i][j] == opponent) {
                      myCount++;
                      if (myCount == 2) {
                          if (i + 4 < 19 && playBoard[i + 1][j] == 0 && playBoard[i + 2][j] == 0
                                  && playBoard[i + 3][j] == opponent
                                  && playBoard[i + 4][j] == opponent) { // [11]0011 (right)
                              superWeight[i + 1][j] += 40;
                              superWeight[i + 2][j] += 40;
                              add += 2;
                          }
                          /*
                           * else if (i - 5 > 0 && playBoard[i - 2][j] == 0 &&
                           * playBoard[i - 3][j] == 0 && playBoard[i - 4][j] ==
                           * opponent && playBoard[i - 5][j] == opponent) { //
                           * 1100[11] left
                           * // same as above superWeight[i - 2][j] += 40; superWeight[i - 3][j] += 40;
                           * add += 2; }
                           */
                          else if (i - 2 > 0 && i + 3 < 19 && playBoard[i - 2][j] == 0
                                  && playBoard[i + 1][j] == 0
                                  && playBoard[i + 2][j] == opponent
                                  && playBoard[i + 3][j] == opponent) { // 0[11]011 (mid)
                              superWeight[i - 2][j] += 40;
                              superWeight[i + 1][j] += 40;
                              add += 2;
                          }

                          /*
                           * else if (i - 5 > 0 && playBoard[i - 2][j] == 0 &&
                           * playBoard[i - 5][j] == 0 && playBoard[i - 3][j] ==
                           * opponent && playBoard[i - 4][j] == opponent) { //
                           * 0110[11] (left)
                           * // same as above superWeight[i - 2][j] += 40; superWeight[i - 5][j] += 40;
                           * add += 2; }
                           */

                          else if (i - 5 > 0 && playBoard[i - 2][j] == 0
                                  && playBoard[i - 4][j] == 0
                                  && playBoard[i - 3][j] == opponent
                                  && playBoard[i - 5][j] == opponent) { // 1010[11] (left)
                              superWeight[i - 2][j] += 40;
                              superWeight[i - 4][j] += 40;
                              add += 2;
                          }

                          else if (i - 3 > 0 && i + 2 < 19 && playBoard[i - 2][j] == 0
                                  && playBoard[i + 1][j] == 0
                                  && playBoard[i - 3][j] == opponent
                                  && playBoard[i + 2][j] == opponent) { // 10[11]01 mid
                              superWeight[i - 2][j] += 40;
                              superWeight[i + 1][j] += 40;
                              add += 2;
                          }

                          else if (i + 4 < 19 && playBoard[i + 1][j] == 0
                                  && playBoard[i + 3][j] == 0
                                  && playBoard[i + 2][j] == opponent
                                  && playBoard[i + 4][j] == opponent) { // [11]0101 (right)
                              superWeight[i + 3][j] += 40;
                              superWeight[i + 1][j] += 40;
                              add += 2;
                          }

                          else if (i + 4 < 19 && playBoard[i + 1][j] == 0
                                  && playBoard[i + 4][j] == 0
                                  && playBoard[i + 2][j] == opponent
                                  && playBoard[i + 3][j] == opponent) { // [11]0110 (right)
                              superWeight[i + 4][j] += 40;
                              superWeight[i + 1][j] += 40;
                              add += 2;
                          }

                          /*
                           * else if (i - 4 > 0 && i + 1 < 19 && playBoard[i + 1][j] == 0 &&
                           * playBoard[i - 2][j] == 0 && playBoard[i - 3][j] ==
                           * opponent && playBoard[i - 4][j] == opponent) { //
                           * 110[11]0 (mid)
                           * //same as above superWeight[i - 2][j] += 40; superWeight[i + 1][j] += 40;
                           * add++; }
                           */
                      }
                  } else
                      myCount = 0;
              } catch (ArrayIndexOutOfBoundsException e) {
              }
          }
      }

      // 좌대각 시작점
      // ----------------------------------------------------------------------

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
                      if (playBoard[temp1][temp2] == opponent) {
                          myCount++;
                          if (myCount == 5) {
                              if ((temp1 - 5 < 0 || temp2 - 5 < 0)
                                      && playBoard[temp1 + 1][temp2 + 1] == 0) {
                                  superWeight[temp1 + 1][temp2 + 1] += 40;
                                  add++;
                              } else if ((temp1 + 1 > 18 || temp2 + 1 > 18)
                                      && playBoard[temp1 - 5][temp2 - 5] == 0) {
                                  superWeight[temp1 - 5][temp2 - 5] += 40;
                                  add++;
                              } else if ((temp1 - 5 < 0 || temp2 - 5 < 0)
                                      && playBoard[temp1 - 5][temp2 - 5] == 0) {
                                  superWeight[temp1 - 5][temp2 - 5] += 80;
                                  add++;
                              } else if ((temp1 + 1 > 18 || temp2 + 1 > 18)
                                      && playBoard[temp1 + 1][temp2 + 1] == 0) {
                                  superWeight[temp1 + 1][temp2 + 1] += 80;
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
      // 4 왼쪽 위에서 오른쪽 아래(좌대각\) 공격
      for (int i = 0; i < 19; i++) {
          myCount = 0;
          for (int j = 0; j < 19; j++) {
              int temp1 = i;
              int temp2 = j;
              for (int k = 0; k < 4; k++) {
                  try {
                      if (playBoard[temp1][temp2] == opponent) {
                          myCount++;
                          if (myCount == 4) {
                              if ((temp1 - 4 <= 0 || temp2 - 4 <= 0) && (temp1 + 2 <= 18 && temp2 + 2 <= 18)
                                      && playBoard[temp1 + 1][temp2 + 1] == 0
                                      && playBoard[temp1 + 2][temp2 + 2] == 0) {
                                  superWeight[temp1 + 1][temp2 + 1] += 40;
                                  add++;
                                  superWeight[temp1 + 2][temp2 + 2] += 40;
                                  add++;
                              } else if ((temp1 + 1 >= 18 || temp2 + 1 >= 18) && (temp1 - 5 >= 0 && temp2 - 5 >= 0)
                                      && playBoard[temp1 - 5][temp2 - 5] == 0
                                      && playBoard[temp1 - 4][temp2 - 4] == 0) {
                                  superWeight[temp1 - 5][temp2 - 5] += 40;
                                  add++;
                                  superWeight[temp1 - 4][temp2 - 4] += 40;
                                  add++;
                              } else if ((temp1 - 4 > 0 && temp2 - 4 > 0) && (temp1 + 1 < 19 && temp2 + 1 < 19)
                                      && playBoard[temp1 - 4][temp2 - 4] == 0
                                      && playBoard[temp1 + 1][temp2 + 1] == 0) {
                                  superWeight[temp1 - 4][temp2 - 4] += 80;
                                  add++;
                                  superWeight[temp1 + 1][temp2 + 1] += 80;
                                  add++;
                              } else if ((temp1 - 5 >= 0 && temp2 - 5 >= 0)
                                      && (playBoard[temp1 - 4][temp2 - 4] == 0
                                              && playBoard[temp1 - 5][temp2 - 5] == 0)) {
                                  superWeight[temp1 - 4][temp2 - 4] += 80;
                                  add++;
                                  superWeight[temp1 - 5][temp2 - 5] += 80;
                                  add++;
                              } else if ((temp1 + 2 <= 18 && temp2 + 2 <= 18)
                                      && (playBoard[temp1 + 1][temp2 + 1] == 0
                                              && playBoard[temp1 + 2][temp2 + 2] == 0)) {
                                  superWeight[temp1 + 1][temp2 + 1] += 80;
                                  add++;
                                  superWeight[temp1 + 2][temp2 + 2] += 80;
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
      // 3(좌대각\) 1공백 공격
      for (int i = 0; i < 19; i++) {
          myCount = 0;
          for (int j = 0; j < 19; j++) {
              int temp1 = i;
              int temp2 = j;
              for (int k = 0; k < 3; k++) {
                  try {
                      if (playBoard[temp1][temp2] == opponent) {
                          myCount++;
                          if (myCount == 3) {
                              if ((temp1 + 3 <= 18 && temp2 + 3 <= 18)
                                      && playBoard[temp1 + 1][temp2 + 1] == 0
                                      && playBoard[temp1 + 2][temp2 + 2] == opponent
                                      && playBoard[temp1 + 3][temp2 + 3] == 0) {
                                  superWeight[temp1 + 1][temp2 + 1] += 40;
                                  add++;
                                  superWeight[temp1 + 3][temp2 + 3] += 40;
                                  add++;

                              } else if ((temp1 - 5 >= 0 && temp2 - 5 >= 0)
                                      && playBoard[temp1 - 3][temp2 - 3] == 0
                                      && playBoard[temp1 - 4][temp2 - 4] == opponent
                                      && playBoard[temp1 - 5][temp2 - 5] == 0) {
                                  superWeight[temp1 - 5][temp2 - 5] += 40;
                                  add++;
                                  superWeight[temp1 - 3][temp2 - 3] += 40;
                                  add++;
                              }
                              temp1++;
                              temp2++;
                          } else
                              myCount = 0;
                      }
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
                      if (playBoard[temp1][temp2] == opponent) {
                          myCount++;
                          if (myCount == 3) {
                              if ((temp1 + 3 <= 18 && temp2 + 3 <= 18)
                                      && playBoard[temp1 + 1][temp2 + 1] == 0
                                      && playBoard[temp1 + 2][temp2 + 2] == 0
                                      && playBoard[temp1 + 3][temp2 + 3] == opponent) {
                                  superWeight[temp1 + 1][temp2 + 1] += 40;
                                  add++;
                                  superWeight[temp1 + 2][temp2 + 2] += 40;
                                  add++;

                              } else if ((temp1 - 5 >= 0 && temp2 - 5 >= 0)
                                      && playBoard[temp1 - 3][temp2 - 3] == 0
                                      && playBoard[temp1 - 4][temp2 - 4] == 0
                                      && playBoard[temp1 - 5][temp2 - 5] == opponent) {
                                  superWeight[temp1 - 4][temp2 - 4] += 40;
                                  add++;
                                  superWeight[temp1 - 3][temp2 - 3] += 40;
                                  add++;
                              }
                              temp1++;
                              temp2++;
                          } else
                              myCount = 0;
                      }
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
                      if (playBoard[temp1][temp2] == opponent) {
                          myCount++;
                          try {
                              if (myCount == 2) {
                                  // 101011(up)
                                  if ((temp1 - 5 >= 0 && temp2 - 5 >= 0)
                                          && playBoard[temp1 - 4][temp2 - 4] == 0
                                          && playBoard[temp1 - 2][temp2 - 2] == 0
                                          && playBoard[temp1 - 3][temp2 - 3] == opponent
                                          && playBoard[temp1 - 5][temp2 - 5] == opponent) {
                                      superWeight[temp1 - 4][temp2 - 4] += 40;
                                      add++;
                                      superWeight[temp1 - 2][temp2 - 2] += 40;
                                      add++;
                                  }
                                  // 110011(up)
                                  else if ((temp1 - 5 >= 0 && temp2 - 5 >= 0)
                                          && playBoard[temp1 - 2][temp2 - 2] == 0
                                          && playBoard[temp1 - 3][temp2 - 3] == 0
                                          && playBoard[temp1 - 4][temp2 - 4] == opponent
                                          && playBoard[temp1 - 5][temp2 - 5] == opponent) {
                                      superWeight[temp1 - 2][temp2 - 2] += 40;
                                      add++;
                                      superWeight[temp1 - 3][temp2 - 3] += 40;
                                      add++;
                                  }
                                  // 011011(up)
                                  else if ((temp1 - 5 >= 0 && temp2 - 5 >= 0)
                                          && playBoard[temp1 - 2][temp2 - 2] == 0
                                          && playBoard[temp1 - 5][temp2 - 5] == 0
                                          && playBoard[temp1 - 4][temp2 - 4] == opponent
                                          && playBoard[temp1 - 3][temp2 - 3] == opponent) {
                                      superWeight[temp1 - 2][temp2 - 2] += 40;
                                      add++;
                                      superWeight[temp1 - 5][temp2 - 5] += 40;
                                      add++;
                                  }
                                  // 110110(mid)
                                  else if ((temp1 - 4 >= 0 && temp2 - 4 >= 0 && temp1 + 1 <= 18 && temp2 + 1 <= 18)
                                          && playBoard[temp1 + 1][temp2 + 1] == 0
                                          && playBoard[temp1 - 2][temp2 - 2] == 0
                                          && playBoard[temp1 - 3][temp2 - 3] == opponent
                                          && playBoard[temp1 - 4][temp2 - 4] == opponent) {
                                      superWeight[temp1 + 1][temp2 + 1] += 40;
                                      add++;
                                      superWeight[temp1 - 2][temp2 - 2] += 40;
                                      add++;
                                  }
                                  // 011011(mid)
                                  else if ((temp1 - 2 >= 0 && temp2 - 2 >= 0 && temp1 + 3 <= 18 && temp2 + 3 <= 18)
                                          && playBoard[temp1 - 2][temp2 - 2] == 0
                                          && playBoard[temp1 + 1][temp2 + 1] == 0
                                          && playBoard[temp1 + 2][temp2 + 2] == opponent
                                          && playBoard[temp1 + 3][temp2 + 3] == opponent) {
                                      superWeight[temp1 - 2][temp2 - 2] += 40;
                                      add++;
                                      superWeight[temp1 + 1][temp2 + 1] += 40;
                                      add++;
                                  }
                                  // 110110(mid)
                                  else if ((temp1 - 4 >= 0 && temp2 - 4 >= 0 && temp1 + 1 <= 18 && temp2 + 1 <= 18)
                                          && playBoard[temp1 + 1][temp2 + 1] == 0
                                          && playBoard[temp1 - 2][temp2 - 2] == 0
                                          && playBoard[temp1 - 3][temp2 - 3] == opponent
                                          && playBoard[temp1 - 4][temp2 - 4] == opponent) {
                                      superWeight[temp1 + 1][temp2 + 1] += 40;
                                      add++;
                                      superWeight[temp1 - 2][temp2 - 2] += 40;
                                      add++;
                                  }
                                  // 110110(down)
                                  else if ((temp1 - 4 <= 18 && temp2 - 4 <= 18)
                                          && playBoard[temp1 + 1][temp2 + 1] == 0
                                          && playBoard[temp1 + 4][temp2 + 4] == 0
                                          && playBoard[temp1 + 2][temp2 + 2] == opponent
                                          && playBoard[temp1 + 3][temp2 + 3] == opponent) {
                                      superWeight[temp1 + 1][temp2 + 1] += 40;
                                      add++;
                                      superWeight[temp1 + 4][temp2 + 4] += 40;
                                      add++;
                                  }
                                  // 110011(down)
                                  else if ((temp1 - 4 <= 18 && temp2 - 4 <= 18)
                                          && playBoard[temp1 + 1][temp2 + 1] == 0
                                          && playBoard[temp1 + 2][temp2 + 2] == 0
                                          && playBoard[temp1 + 3][temp2 + 3] == opponent
                                          && playBoard[temp1 + 4][temp2 + 4] == opponent) {
                                      superWeight[temp1 + 1][temp2 + 1] += 40;
                                      add++;
                                      superWeight[temp1 + 2][temp2 + 2] += 40;
                                      add++;
                                  }
                                  // 110101(down)
                                  else if ((temp1 - 4 <= 18 && temp2 - 4 <= 18)
                                          && playBoard[temp1 + 1][temp2 + 1] == 0
                                          && playBoard[temp1 + 4][temp2 + 4] == 0
                                          && playBoard[temp1 + 3][temp2 + 3] == opponent
                                          && playBoard[temp1 + 2][temp2 + 2] == opponent) {
                                      superWeight[temp1 + 1][temp2 + 1] += 40;
                                      add++;
                                      superWeight[temp1 + 4][temp2 + 4] += 40;
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

      // 우대각 시작점
      // -----------------------------------------------------------------------------------------

      if (add >= 2)
          return;
      // 5 오른쪽위에서 왼쪽아래 (우대각/) 공격
      for (int j = 0; j < 19; j++) {
          myCount = 0;
          for (int i = 0; i < 19; i++) {
              int temp1 = i;
              int temp2 = j;
              for (int k = 0; k < 5; k++) {
                  try {
                      if (playBoard[temp1][temp2] == opponent) {
                          myCount++;
                          if (myCount == 5) {
                              // 우상단 막히고 좌하단 뚫림
                              if ((temp1 + 5 > 18 || temp2 - 5 < 0) && (temp1 - 1 >= 0 || temp2 + 1 <= 18)
                                      && playBoard[temp1 - 1][temp2 + 1] == 0) {
                                  superWeight[temp1 - 1][temp2 + 1] += 40;
                                  add++;
                              }
                              // 좌하단 막히고 우상단 뚫림
                              else if ((temp1 - 1 < 0 || temp2 + 1 > 18) && (temp1 + 5 <= 18 && temp2 - 5 >= 0)
                                      && playBoard[temp1 + 5][temp2 - 5] == 0) {
                                  superWeight[temp1 + 5][temp2 - 5] += 40;
                                  add++;
                              }
                              // 우상단 자리있음
                              else if ((temp1 + 5 <= 18 && temp2 - 5 >= 0)
                                      && playBoard[temp1 + 5][temp2 - 5] == 0) {
                                  superWeight[temp1 + 5][temp2 - 5] += 80;
                                  add++;
                              }
                              // 좌하단 자리있음
                              else if ((temp1 - 1 >= 0 || temp2 + 1 <= 18)
                                      && playBoard[temp1 - 1][temp2 + 1] == 0) {
                                  superWeight[temp1 - 1][temp2 + 1] += 80;
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
      // 4 오른쪽위에서 왼쪽아래 (우대각/) 공격
      for (int j = 0; j < 19; j++) {
          myCount = 0;
          for (int i = 0; i < 19; i++) {
              int temp1 = i;
              int temp2 = j;
              for (int k = 0; k < 4; k++) {
                  try {
                      if (playBoard[temp1][temp2] == opponent) {
                          myCount++;
                          if (myCount == 4) {
                              // 우상단 막히고 좌하단에 두개
                              if ((temp1 + 4 > 18 || temp2 - 4 < 0) && (temp1 - 2 >= 0 && temp2 + 2 <= 18)
                                      && playBoard[temp1 - 1][temp2 + 1] == 0
                                      && playBoard[temp1 - 2][temp2 + 2] == 0) {
                                  superWeight[temp1 - 1][temp2 + 1] += 40;
                                  add++;
                                  superWeight[temp1 - 2][temp2 + 2] += 40;
                                  add++;
                                  // 촤하단 막히고 우상단에 두개
                              } else if ((temp1 + 1 > 18 || temp2 - 1 < 0) && (temp1 + 5 >= 0 && temp2 - 5 >= 0)
                                      && playBoard[temp1 + 5][temp2 - 5] == 0
                                      && playBoard[temp1 + 4][temp2 - 4] == 0) {
                                  superWeight[temp1 + 5][temp2 - 5] += 40;
                                  add++;
                                  superWeight[temp1 + 4][temp2 - 4] += 40;
                                  add++;
                                  // 우상단 좌하단에 하나씩 뚫림
                              } else if ((temp1 + 4 <= 18 && temp2 - 4 >= 0) && (temp1 - 1 >= 0 && temp2 + 1 <= 18)
                                      && playBoard[temp1 + 4][temp2 - 4] == 0
                                      && playBoard[temp1 - 1][temp2 + 1] == 0) {
                                  superWeight[temp1 + 4][temp2 - 4] += 80;
                                  add++;
                                  superWeight[temp1 - 1][temp2 + 1] += 80;
                                  add++;
                                  // 우상단에 두개
                              } else if ((temp1 + 5 >= 0 && temp2 - 5 >= 0)
                                      && playBoard[temp1 + 5][temp2 - 5] == 0
                                      && playBoard[temp1 + 4][temp2 - 4] == 0) {
                                  superWeight[temp1 + 5][temp2 - 5] += 40;
                                  add++;
                                  superWeight[temp1 + 4][temp2 - 4] += 40;
                                  add++;
                                  // 좌하단에 두개
                              } else if ((temp1 - 2 >= 0 && temp2 + 2 <= 18)
                                      && playBoard[temp1 - 1][temp2 + 1] == 0
                                      && playBoard[temp1 - 2][temp2 + 2] == 0) {
                                  superWeight[temp1 - 1][temp2 + 1] += 40;
                                  add++;
                                  superWeight[temp1 - 2][temp2 + 2] += 40;
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
      // 3(우대각/) 1공백 공격
      for (int j = 0; j < 19; j++) {
          myCount = 0;
          for (int i = 0; i < 19; i++) {
              int temp1 = i;
              int temp2 = j;
              for (int k = 0; k < 3; k++) {
                  try {
                      if (playBoard[temp1][temp2] == opponent) {
                          myCount++;
                          if (myCount == 3) {
                              if ((temp1 - 3 >= 0 && temp2 + 3 <= 18)
                                      && playBoard[temp1 - 1][temp2 + 1] == 0
                                      && playBoard[temp1 - 2][temp2 + 2] == opponent
                                      && playBoard[temp1 - 3][temp2 + 3] == 0) {
                                  superWeight[temp1 - 1][temp2 + 1] += 40;
                                  add++;
                                  superWeight[temp1 - 3][temp2 + 3] += 40;
                                  add++;

                              } else if ((temp1 + 5 <= 18 && temp2 - 5 >= 0)
                                      && playBoard[temp1 + 3][temp2 - 3] == 0
                                      && playBoard[temp1 + 4][temp2 - 4] == opponent
                                      && playBoard[temp1 + 5][temp2 - 5] == 0) {
                                  superWeight[temp1 + 5][temp2 - 5] += 40;
                                  add++;
                                  superWeight[temp1 + 3][temp2 - 3] += 40;
                                  add++;
                              }
                              temp1--;
                              temp2++;
                          } else
                              myCount = 0;
                      }
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
                      if (playBoard[temp1][temp2] == opponent) {
                          myCount++;
                          if (myCount == 3) {
                              if ((temp1 - 3 >= 0 && temp2 + 3 <= 18)
                                      && playBoard[temp1 - 1][temp2 + 1] == 0
                                      && playBoard[temp1 - 2][temp2 + 2] == 0
                                      && playBoard[temp1 - 3][temp2 + 3] == opponent) {
                                  superWeight[temp1 - 1][temp2 + 1] += 40;
                                  add++;
                                  superWeight[temp1 - 2][temp2 + 2] += 40;
                                  add++;

                              } else if ((temp1 + 5 <= 18 && temp2 - 5 >= 0)
                                      && playBoard[temp1 + 3][temp2 - 3] == 0
                                      && playBoard[temp1 + 4][temp2 - 4] == 0
                                      && playBoard[temp1 + 5][temp2 - 5] == opponent) {
                                  superWeight[temp1 + 4][temp2 - 4] += 40;
                                  add++;
                                  superWeight[temp1 + 3][temp2 - 3] += 40;
                                  add++;
                              }
                              temp1--;
                              temp2++;
                          } else
                              myCount = 0;
                      }
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
                      if (playBoard[temp1][temp2] == opponent) {
                          myCount++;
                          try {
                              if (myCount == 2) {
                                  // 011011(up)
                                  if ((temp1 + 5 <= 18 && temp1 - 5 >= 0)
                                          && playBoard[temp1 + 2][temp2 - 2] == 0
                                          && playBoard[temp1 + 5][temp2 - 5] == 0
                                          && playBoard[temp1 + 3][temp2 - 3] == opponent
                                          && playBoard[temp1 + 4][temp2 - 4] == opponent) {
                                      superWeight[temp1 + 2][temp2 - 2] += 40;
                                      add++;
                                      superWeight[temp1 + 5][temp2 - 5] += 40;
                                      add++;
                                  }
                                  // 101011(up)
                                  else if ((temp1 + 5 <= 18 && temp1 - 5 >= 0)
                                          && playBoard[temp1 + 4][temp2 - 4] == 0
                                          && playBoard[temp1 + 2][temp2 - 2] == 0
                                          && playBoard[temp1 + 3][temp2 - 3] == opponent
                                          && playBoard[temp1 + 5][temp2 - 5] == opponent) {
                                      superWeight[temp1 + 2][temp2 - 2] += 40;
                                      add++;
                                      superWeight[temp1 + 4][temp2 - 4] += 40;
                                      add++;
                                  }
                                  // 110011(up)
                                  else if ((temp1 + 5 <= 18 && temp1 - 5 >= 0)
                                          && playBoard[temp1 + 3][temp2 - 3] == 0
                                          && playBoard[temp1 + 2][temp2 - 2] == 0
                                          && playBoard[temp1 + 4][temp2 - 4] == opponent
                                          && playBoard[temp1 + 5][temp2 - 5] == opponent) {
                                      superWeight[temp1 + 2][temp2 - 2] += 40;
                                      add++;
                                      superWeight[temp1 + 3][temp2 - 3] += 40;
                                      add++;
                                  }
                                  // 101101(mid)
                                  else if ((temp1 - 2 >= 0 && temp2 + 2 <= 18) && (temp1 + 3 <= 18 && temp2 - 3 >= 0)
                                          && playBoard[temp1 + 2][temp2 - 2] == 0
                                          && playBoard[temp1 - 1][temp2 + 1] == 0
                                          && playBoard[temp1 - 2][temp2 + 2] == opponent
                                          && playBoard[temp1 + 3][temp2 - 3] == opponent) {
                                      superWeight[temp1 + 2][temp2 - 2] += 40;
                                      add++;
                                      superWeight[temp1 - 1][temp2 + 1] += 40;
                                      add++;
                                  }
                                  // 011011(mid)
                                  else if ((temp1 - 3 >= 0 && temp2 + 3 <= 18) && (temp1 + 2 <= 18 && temp2 - 2 >= 0)
                                          && playBoard[temp1 - 1][temp2 + 1] == 0
                                          && playBoard[temp1 + 2][temp2 - 2] == 0
                                          && playBoard[temp1 - 2][temp2 + 2] == opponent
                                          && playBoard[temp1 - 3][temp2 + 3] == opponent) {
                                      superWeight[temp1 - 1][temp2 + 1] += 40;
                                      add++;
                                      superWeight[temp1 + 2][temp2 - 2] += 40;
                                      add++;
                                  }
                                  // 110110(mid)
                                  else if ((temp1 - 4 >= 0 && temp2 + 4 <= 18) && (temp1 + 1 <= 18 && temp2 - 1 >= 0)
                                          && playBoard[temp1 - 1][temp2 + 1] == 0
                                          && playBoard[temp1 + 2][temp2 - 2] == 0
                                          && playBoard[temp1 + 3][temp2 - 3] == opponent
                                          && playBoard[temp1 + 4][temp2 - 4] == opponent) {
                                      superWeight[temp1 - 1][temp2 + 1] += 40;
                                      add++;
                                      superWeight[temp1 + 2][temp2 - 2] += 40;
                                      add++;
                                  }
                                  // 110011(down)
                                  else if ((temp1 - 4 >= 0 && temp1 + 4 <= 18)
                                          && playBoard[temp1 - 1][temp2 + 1] == 0
                                          && playBoard[temp1 - 2][temp2 + 2] == 0
                                          && playBoard[temp1 - 3][temp2 + 3] == opponent
                                          && playBoard[temp1 - 4][temp2 + 4] == opponent) {
                                      superWeight[temp1 - 1][temp2 + 1] += 40;
                                      add++;
                                      superWeight[temp1 - 2][temp2 + 2] += 40;
                                      add++;
                                  }
                                  // 110101(down)
                                  else if ((temp1 - 4 >= 0 && temp1 + 4 <= 18)
                                          && playBoard[temp1 - 1][temp2 + 1] == 0
                                          && playBoard[temp1 - 3][temp2 + 3] == 0
                                          && playBoard[temp1 - 2][temp2 + 2] == opponent
                                          && playBoard[temp1 - 4][temp2 + 4] == opponent) {
                                      superWeight[temp1 - 1][temp2 + 1] += 40;
                                      add++;
                                      superWeight[temp1 - 3][temp2 + 3] += 40;
                                      add++;
                                  }
                                  // 110110(down)
                                  else if ((temp1 - 4 >= 0 && temp1 + 4 <= 18)
                                          && playBoard[temp1 - 1][temp2 + 1] == 0
                                          && playBoard[temp1 - 4][temp2 + 4] == 0
                                          && playBoard[temp1 - 3][temp2 + 3] == opponent
                                          && playBoard[temp1 - 2][temp2 + 2] == opponent) {
                                      superWeight[temp1 - 1][temp2 + 1] += 40;
                                      add++;
                                      superWeight[temp1 - 4][temp2 + 4] += 40;
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

      // 한방오리백숙 ==========================================

      // ====================================================================

  }
}
