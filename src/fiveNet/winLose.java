package fiveNet;

public class winLose {
	public static boolean winLost(int[][]SaveGame ,int x ,int y ) {
		boolean flag = false;// 输赢
		int count = 1;// 相连数
		int color = SaveGame[x][y];// 记录棋子颜色
		
//		 for (int i = 0; i < 15; i++) {
//             for (int j = 0; j < 15; j++) {
//                 System.out.print(SaveGame[i][j]);
//                 }
//             }
//         System.out.println();
		
		// 判断横向棋子是否相连
		//System.out.println("Flag"+flag);
		for (int i = 1; x + i < SaveGame[0].length; i++) {
			if (color == SaveGame[x + i][y]) {
				count++;
				//System.out.print("count"+count);
			} else {
				break;
			}
		}
		for (int i = 1; x - i >= 0; i++) {
			if (color == SaveGame[x - i][y]) {
				count++;
			} else {
				break;
			}
		}
		if (count >= 5) {
			flag = true;
		}
		// 判断纵向向棋子是否相连
		count = 1;
		for (int i = 1; y + i < SaveGame.length; i++) {
			if (color == SaveGame[x][y + i]) {
				count++;
			} else {
				break;
			}
		}
		for (int i = 1; y - i >= 0; i++) {
			if (color == SaveGame[x][y - i]) {
				count++;
			} else {
				break;
			}
		}
		if (count >= 5) {
			flag = true;
		}
		// 判断斜向棋子是否相连（左上右下）
		count = 1;
		for (int i = 1; x - i >= 0 && y - i >= 0; i++) {
			if (color == SaveGame[x - i][y - i]) {
				count++;
			} else {
				break;
			}
		}
		for (int i = 1; x + i < SaveGame[0].length && y + i < SaveGame.length; i++) {
			if (color == SaveGame[x + i][y + i]) {
				count++;
			} else {
				break;
			}
		}
		if (count >= 5) {
			flag = true;
		}
		// 判断斜向棋子是否相连（左下右上）
		count = 1;
		for (int i = 1; x + i < SaveGame[0].length && y - i >= 0; i++) {
			if (color == SaveGame[x + i][y - i]) {
				count++;
			} else {
				break;
			}
		}
		for (int i = 1; x - i >= 0 && y + i < SaveGame.length; i++) {
			if (color == SaveGame[x - i][y + i]) {
				count++;
			} else {
				break;
			}
		}
		if (count >= 5) {
			flag = true;
		}
		return flag;
	}
}
