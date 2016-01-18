package com.kunlun.poker.game.system;


/**
 * Hello world!
 *
 */
public class Test {
//	private static final Logger logger = LoggerFactory.getLogger(Test.class);

	// private static final Card[] cardFirst = new Card[5];
	// private static final Card[] cardSecond = new Card[5];
	// private static final Card[] cardThird = new Card[5];
	// private static final Card[] cardPublic = new Card[5];
	// private static int playerNum = 0;

	public static void main(String[] args) {
		int ip = 2130706433;

		System.out.println((ip & 0xff000000) >> 24);
		System.out.println((ip & 0x00ff0000) >> 16);
		System.out.println((ip & 0x0000ff00) >> 8);
		System.out.println(ip & 0x000000ff);
//		Config config = Config.getInstance();
//		try {
//			config.load(new FileInputStream("config.properties"));
//		} catch (IOException e) {
//			logger.error(e.getMessage());
//		}
//		
//		@SuppressWarnings("resource")
//		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
//				"applicationContext.xml");
//		
//		RoomService service = (RoomService)applicationContext.getBean("roomService");
//		System.out.println(service.getRoom().getName());

		// String name = ManagementFactory.getRuntimeMXBean().getName();
		// System.out.println(name);
		// // get pid
		// String pid = name.split("@")[0];
		// System.out.println("Pid is:" + pid);
		// Room room = new Room();
		// room.setDeskCapacity(100);
		// room.setSmallBlind(10);
		// room.setNumberOfUsers(1000);
		// room.setCarry(400);
		// room.setType(RoomType.SIX);
		// room.setName("老年棋牌室");
		//
		// Scanner sc = new Scanner(System.in);
		// List<User> users = new ArrayList<>();
		// OUTER:
		// while (true) {
		// System.out.print("Poker> ");
		// String cmd = sc.nextLine().trim();
		// if ("exit".equals(cmd)) {
		// sc.close();
		// break;
		//
		// } else if (cmd.startsWith("nu ")) {
		// User user = new User();
		// user.setCurrency(10000);
		// user.setName(cmd.substring(3));
		// user.join(room); //玩家进入房间
		// users.add(user);
		//
		// } else if (cmd.startsWith("hand2 ")) {
		// for(int i=0; i<2; i++){
		// String colorString = cmd.substring(i*4 + 6, i*4 + 7);
		// String cardString = cmd.substring(i*4 + 7, i*4 + 9);
		// int num =Integer.parseInt(cardString);
		// Card card = null;
		// switch (colorString) {
		// case "1":
		// card = new Card(ColorSuit.HEART, num);
		// break;
		// case "2":
		// card = new Card(ColorSuit.DIAMOND, num);
		// break;
		// case "3":
		// card = new Card(ColorSuit.CLUB, num);
		// break;
		// case "4":
		// card = new Card(ColorSuit.SPADE, num);
		// break;
		// default:
		// break;
		// }
		// switch(playerNum){
		// case 0:
		// cardFirst[i] = card;
		// break;
		// case 1:
		// cardSecond[i] = card;
		// break;
		// case 2:
		// cardThird[i] = card;
		// break;
		// default:
		// playerNum = 0;
		// break;
		// }
		// }
		// switch(playerNum){
		// case 0:
		// System.out.println("玩家1号社会大哥，输入的牌型为,"+cardFirst[0].toString()+" "+cardFirst[1].toString());
		// break;
		// case 1:
		// System.out.println("玩家2号公司白领，输入的牌型为,"+cardSecond[0].toString()+" "+cardSecond[1].toString());
		// break;
		// case 2:
		// System.out.println("玩家3号天桥乞丐，输入的牌型为,"+cardThird[0].toString()+" "+cardThird[1].toString());
		// break;
		// default:
		// playerNum = 0;
		// break;
		// }
		// playerNum++;
		// System.out.println(""+playerNum);
		// }
		// else if(cmd.startsWith("hand3 ")){
		// for(int i=0; i<5; i++){
		// String colorString = cmd.substring(i*4 + 6, i*4 + 7);
		// String cardString = cmd.substring(i*4 + 7, i*4 + 9);
		// int num =Integer.parseInt(cardString);
		// Card card = null;
		// switch (colorString) {
		// case "1":
		// card = new Card(ColorSuit.HEART, num);
		// break;
		// case "2":
		// card = new Card(ColorSuit.DIAMOND, num);
		// break;
		// case "3":
		// card = new Card(ColorSuit.CLUB, num);
		// break;
		// case "4":
		// card = new Card(ColorSuit.SPADE, num);
		// break;
		// default:
		// break OUTER;
		// }
		// cardPublic[i] = card;
		// }
		// System.out.println("公共牌,  "+ cardPublic[0].toString()+"  "+
		// cardPublic[1].toString()+"  "+
		// cardPublic[2].toString()+"  "+
		// cardPublic[3].toString()+"  "+
		// cardPublic[4].toString());
		// }else if(cmd.startsWith("compare")){
		// PokerHand firstHand = PokerHand.determine(cardFirst, cardPublic,
		// cardFirst);
		// System.out.println("*****************玩家1号，最佳牌型为" +
		// firstHand+" *****************");
		// PokerHand secondHand = PokerHand.determine(cardSecond, cardPublic,
		// cardSecond);
		// System.out.println("*****************玩家2号，最佳牌型为" +
		// secondHand+" *****************");
		// PokerHand thirdHand = PokerHand.determine(cardThird, cardPublic,
		// cardThird);
		// System.out.println("*****************玩家2号，最佳牌型为" +
		// thirdHand+" *****************");
		// int refFirst = firstHand.compareTo(secondHand);
		// System.out.println("*****************1和2比较" +
		// refFirst+" *****************");
		// int refSecond;
		// if(refFirst < 0){
		// refSecond = firstHand.compareTo(thirdHand);
		// System.out.println("*****************1和3比较" +
		// refSecond+" *****************");
		// if(refSecond < 0){
		// System.out.println("*****************赢家是1号，牌型为" +
		// firstHand+" *****************");
		// }else if(refSecond == 0){
		// System.out.println("*****************赢家是1和3号，牌型为" +
		// firstHand+" *****************");
		// }else if(refSecond > 0){
		// System.out.println("*****************赢家是3号，牌型为" +
		// thirdHand+" *****************");
		// }
		// }else if(refFirst == 0){
		// refSecond = firstHand.compareTo(thirdHand);
		// System.out.println("*****************1和3比较" +
		// refSecond+" *****************");
		// if(refSecond < 0){
		// System.out.println("*****************赢家是1号和2号，牌型为" +
		// firstHand+" *****************");
		// }else if(refSecond == 0){
		// System.out.println("*****************赢家是1和2号和3号，牌型为" +
		// firstHand+" *****************");
		// }else if(refSecond > 0){
		// System.out.println("*****************赢家是3号，牌型为" +
		// thirdHand+" *****************");
		// }
		// }else if(refFirst > 0){
		// refSecond = secondHand.compareTo(thirdHand);
		// System.out.println("*****************2和3比较" +
		// refSecond+" *****************");
		// if(refSecond < 0){
		// System.out.println("*****************赢家是2号，牌型为" +
		// secondHand+" *****************");
		// }else if(refSecond == 0){
		// System.out.println("*****************赢家是2号和3号，牌型为" +
		// secondHand+" *****************");
		// }else if(refSecond > 0){
		// System.out.println("*****************赢家是3号，牌型为" +
		// thirdHand+" *****************");
		// }
		// }
		// }else if(cmd.startsWith("? "))
		// {
		// /**
		// * 拿到第一个玩家，根据房间获取用户
		// */
		// Desk desk = users.get(0).getPlayer().getDesk();
		// String[] cmds = cmd.substring(2).split("\\s+");
		// Action action = ActionFactory.getInstance().getAction(cmds[0],
		// cmds.length > 1 ? Integer.parseInt(cmds[1]) : 0);
		//
		// if(action == null)
		// logger.debug("无效的输入");
		// else
		// logger.debug(action.isValid(desk.getDealer()) ? "可以" : "不可以");
		// }
		// else
		// {
		// Desk desk = users.get(0).getPlayer().getDesk();
		//
		// String[] cmds = cmd.split("\\s+");
		// Action action = ActionFactory.getInstance().getAction(cmds[0],
		// cmds.length > 1 ? Integer.parseInt(cmds[1]) : 0);
		// if(action == null)
		// logger.debug("无效的输入");
		// else
		// desk.response(action);
		// }
		// }
	}
}
