package poker.events
{
	import flash.events.Event;
	
	public class PaginationEvent extends Event
	{
		public static const PAGINATE : String = "paginate";
		
		public function PaginationEvent(type:String, page : int)
		{
			super(type, false, false);
			this._page = page;
		}
		
		private var _page : int;
		
		public function get page() : int
		{
			return _page;
		}
	}
}