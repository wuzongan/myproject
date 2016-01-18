package poker.util
{
	import flash.globalization.DateTimeFormatter;
	import flash.globalization.LocaleID;
	
	import spark.components.gridClasses.GridColumn;
	
	import poker.Config;

	public class DataUtil
	{
		public static function getPage(model : Object) : int
		{
			return calculatePage(int(model.from) + model.list.length);
		}
		
		public static function getTotal(model : Object) : int
		{
			return calculatePage(model.total);
		}
		
		public static function isReading(model : Object) : Boolean{
			return model.hasOwnProperty("currentLine");
		}
		
		public static function getCurrentLine(model : Object) : int{
			return int(model.currentLine);
		}
		
		public static function getTotalLine(model : Object) : int{
			return int(model.totalLine);
		}
		
		private static function calculatePage(count : int) : int
		{
			return Math.ceil(count / Config.PAGESIZE);
		}

		public static function showDateTime(item : Object, col : GridColumn) : String
		{
			var timestamp : Number = item[col.dataField]  * 1000;
			var formatter : DateTimeFormatter = new DateTimeFormatter(LocaleID.DEFAULT);
			formatter.setDateTimePattern("yyyy-MM-dd HH:mm:ss");
			return formatter.format(new Date(timestamp));
		}
	}
}