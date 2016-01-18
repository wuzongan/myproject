package poker.comps
{
	import flash.display.DisplayObject;
	
	import mx.core.FlexGlobals;
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
	
	import spark.components.TitleWindow;
	
	public class Window extends TitleWindow
	{
		public function Window()
		{
			super();
			
			addEventListener(CloseEvent.CLOSE, closeEventHandler);
		}
		
		protected function closeEventHandler(event:CloseEvent):void
		{
			close();
		}
		
		public function open(): void
		{
			PopUpManager.addPopUp(this, DisplayObject(FlexGlobals.topLevelApplication));
			PopUpManager.centerPopUp(this);
		}
		
		public function close() : void
		{
			PopUpManager.removePopUp(this);
		}
	}
}