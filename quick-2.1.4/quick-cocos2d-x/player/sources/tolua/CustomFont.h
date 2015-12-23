#ifndef __CUSTOMFONT_
#define __CUSTOMFONT_

#include "cocos2d.h"
#include "cocos-ext.h"

using namespace cocos2d::extension;
using namespace cocos2d;

class CustomFont
{
public:
	CustomFont(void){};
	//alias->注册名
	//font_name->字体库
	//color->默认色值
	//size_pt->字体大小,会选择最相近的使用
	//style:0:默认 1:掏空 2:阴影 3:描边
	//strength:在style不为0时有效,掏空/阴影/描边 强度比率
	//secondary_color在阴影/描边时有效,阴影/描边颜色
	//hack_fontname:当fontname没有时替换字体
	//hack_shift_y:替换字体高度偏移
	static void add_font_style(const char* alias, const char* font_name, int color, int size_pt,
		int style, float strength, int secondary_colorconst,char* hack_fontname,int hack_shift_y);
};

#endif //__CUSTOMFONTL_
