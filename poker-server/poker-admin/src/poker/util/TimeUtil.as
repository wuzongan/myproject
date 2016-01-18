package poker.util
{
	import flash.events.TimerEvent;
	import flash.utils.Timer;

	public class TimeUtil
	{
		private static var timer:Timer = new Timer(1000);
		private static var instance:TimeUtil = new TimeUtil();
		
		public function TimeUtil()
		{
			timer.start();
		}
		
		private static function getInstance(): TimeUtil{
			return instance;
		}

		public function addEvent(fun : Function): void{
			timer.addEventListener(TimerEvent.TIMER, fun); 
		}
		
		
		public static function addEvent(fun : Function): void{
			getInstance().addEvent(fun);	
		}
		
		
	}
}