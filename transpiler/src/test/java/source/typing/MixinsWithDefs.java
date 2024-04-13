package source.typing;

import static def.test.Globals.$;

import def.test.Globals;
import def.test.JQuery;

public class MixinsWithDefs {
	public static void main(String[] args) {
		$(".modal").modal();
		$("select").material_select().addClass("animated");
		Globals.$("test").modal("");
		$("test").animate("test").addClass("animated");
		$(".modal").attr("class");
		JQuery extendedJQuery = $("test").myExtension();
		extendedJQuery.addClass("test");
		
	}
}
