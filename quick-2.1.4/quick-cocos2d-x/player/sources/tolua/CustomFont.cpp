#include "CustomFont.h"

USING_NS_CC_EXT;

using namespace dfont;
void CustomFont::add_font_style(const char* alias, const char* font_name, int color, int size_pt,
	int style, float strength, int secondary_colorconst,char* hack_fontname,int hack_shift_y)
{
	FontCatalog* font_catalog;
	switch(style)
	{
	case 1:
		font_catalog = FontFactory::instance()->create_font(
			alias,font_name,color,size_pt, e_strengthen,strength,secondary_colorconst);
		break;
	case 2:
		font_catalog = FontFactory::instance()->create_font(
			alias,font_name,color,size_pt, e_border,strength,secondary_colorconst);
		break;
	case 3:
		font_catalog = FontFactory::instance()->create_font(
			alias,font_name,color,size_pt, e_shadow,strength,secondary_colorconst);
		break;	
	default:
		font_catalog = FontFactory::instance()->create_font(
			alias,font_name,color,size_pt, e_plain,strength,secondary_colorconst);
	};
	if(strlen(hack_fontname))
		font_catalog->add_hackfont(hack_fontname, latin_charset(), hack_shift_y);
}