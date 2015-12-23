#include "NodeParser.h"

NodeParser::NodeParser(CCNode *cNode):
	parentNode(cNode), horizontalMargin(5), verticalMargin(0),rowGap(0),
	imgHandle(NULL), textHandle(NULL), 
	imgHandleObj(NULL), textHandleObj(NULL),curRowHight(0),
	parentWidth(0),height(0),
	font("Arial"),fontSize("24"),color("ffffff"),align("center")
{}
NodeParser* NodeParser::create(CCNode *cNode)
{
	NodeParser *parser= new NodeParser(cNode);
	if (parser!=NULL)
	{
		parser->autorelease();
		return parser;
	}
	CC_SAFE_DELETE(parser);
	return NULL;
	
}
void NodeParser::parseStr(const char *text)
{
	 CCSize parentSize= parentNode->getContentSize();
	parentWidth = parentNode->getContentSize().width - 2 * horizontalMargin;

	curRowStart = ccp(horizontalMargin, parentSize.height - verticalMargin);
	firstRowStart=curRowStart;
	height=2*verticalMargin;
	CCSAXParser *parser = new CCSAXParser();
	parser->init("utf-8");
	parser->setDelegator(this);
	parser->parse(text,strlen(text));
}

void NodeParser::registerImageHandler(cocos2d::CCObject *obj,ImageNode::Handler handler)
{
	imgHandleObj = obj;
	imgHandle = handler;
}

void NodeParser::registerTextHandler(cocos2d::CCObject *obj, TextNode::Handler handler)
{
	textHandleObj = obj;
	textHandle = handler;
}



void NodeParser::startElement(void *ctx, const char *name, const char **attrs)
{
	int type = findNodeType(name);
	if(type != TYPE_NONE)
	{
		(this->*nodeInfos[type].startFunc)(attrs);
	}
	lebalStack.push(string(name));
}

void NodeParser::textHandler(void *ctx, const char *name, int len)//只处理用p节点包围的字符，其他部分字符被忽略
{
	string lebel=lebalStack.top();
	if(len == 0||!(lebel==string("p"))) 
		return;

	string val(name, 0, len);
	CCNode* node = rowNodeQueue.back();
	TextNode *textNode = dynamic_cast<TextNode*>(node);
	if(textNode != NULL)
	{
		textNode->setString(val.c_str());  //设置文本
		if(textNode->getStrokeState())
		{
			//textNode->loadFile("scripts/func/Stroke.lua");
		}
		
	}
	/*if (curRowHight < textNode->getContentSize().height)
	{
		curRowHight=textNode->getContentSize().height;
	}*/
	
}

void NodeParser::endElement(void *ctx, const char *name)
{
	//fixPlainText();
	int type = findNodeType(name);
	if(type != TYPE_NONE)
	{
		(this->*nodeInfos[type].endFunc)();
	}
	lebalStack.pop();
}



void NodeParser::trStart(const char **attrs)
{
	curRowStart.x=firstRowStart.x;  //设置行坐标位置
	curRowStart.y-=curRowHight+rowGap;
	height+=curRowHight;            //当前所有行总高
	curRowHight=0;
	if (attrs!=NULL)
	{
		setRowAttrs(attrs);
	}
	rowElems.clear();
}

void NodeParser::setRowAttrs(const char**attrs)
{
	while(*attrs != NULL)
	{

		switch(findAttrType(attrs[0]))
		{

		case ATTR_FONT_NAME:font=attrs[1];break;
		case ATTR_FONT_SIZE:fontSize=attrs[1];break;
		case ATTR_FONT_COLOR:color=attrs[1];break;
		case ATTR_ALIGN:align=attrs[1];break;

		default:
			break;
		}

		attrs += 2;
		if(*attrs == NULL)
			break;
	}
	
}
bool NodeParser::isOverOfEdge(CCNode *node)
{
    TextNode *tNode=(TextNode*)node;
	return (curRowStart.x+tNode->getContentSize().width > parentWidth)?true:false;
}
void NodeParser::autoWrapText(CCNode *node)
{
	TextNode *tNode=(TextNode*)node;
	tNode->removeAllChildren();
	CCSize size=tNode->getContentSize();
	TextNode *textNode=TextNode::create();//创建新的文本节点存储换行的文本
	textNode->copyAttrs(tNode);
	textNode->registerTextHandler(textHandleObj,textHandle);
	textNode->registerScriptListener(textListener);
	
	string str=tNode->cutForWidth(parentWidth-curRowStart.x);
	tNode->setPosition(ccp(curRowStart.x,curRowStart.y-curRowHight));
	addToNodes(tNode);
	textNode->setString(str.c_str());
	if(tNode->getStrokeState())
	{
		//textNode->loadFile("scripts/func/Stroke.lua");
		//tNode->loadFile("scripts/func/Stroke.lua");
	}
	trStart(NULL);
	curRowHight=size.height;
	rowNodeQueue.push_front(textNode);
	trEnd();
	curRowStart.x=parentWidth;//防止文本对齐两次
}
void NodeParser::trEnd()       //行结束对这行所有节点进行布局
{
	CCNode *node;
	CCSize nodeSize;
	while(rowNodeQueue.size()!=0)  //调整布局本行节点，超出则自动换行
	{
		node=rowNodeQueue.front();
		rowNodeQueue.pop_front();
		nodeSize=node->getContentSize();

		if (curRowHight<nodeSize.height)//当前行高小于节点高度则调整更新已加入到父节点的本行节点位置，使其与本行最高的对齐
		{
			updateRowY(nodeSize.height-curRowHight);
			curRowHight=nodeSize.height;
		}
		
		if (isOverOfEdge(node))       //超出本行进行换行
		{
			
			TextNode *text=dynamic_cast<TextNode*>(node);
			if(text!=NULL) //文本节点换行
			{
				autoWrapText(node);
			}
			else      //图片节点换行
			{
				trStart(NULL);
				curRowHight=nodeSize.height;
				rowNodeQueue.push_front(node);
			}
		}
		else
		{
			node->setPosition(curRowStart.x,curRowStart.y-curRowHight);
			addToNodes(node);
		}
		curRowStart.x+=nodeSize.width;
	}

	//对齐设置
	if (curRowStart.x<parentWidth)
	{
		int distance=parentWidth-curRowStart.x;
		if (align==string("left"))
		{
			return;
		}
		else if (align==string("center"))
		{
			distance/=2;
		}
		else if(align==string("right"))
		{

		}
		else
		{
			return;
		}

		updateRowX(distance);
	}
}

void NodeParser::addToNodes(CCNode* node)
{
	allNode.push_back(node);
	rowElems.push_back(node);
}
void NodeParser::updateRowX(float distance)
{
	for (vector<CCNode*>::iterator iter=rowElems.begin();iter!=rowElems.end();iter++)
	{
		(*iter)->setPositionX((*iter)->getPositionX()+distance);
	}
}
void NodeParser::updateRowY(float distance)
{
	for (vector<CCNode*>::iterator iter=rowElems.begin();iter != rowElems.end();iter++)
	{
		(*iter)->setPositionY((*iter)->getPositionY()-distance);
	}
}
void NodeParser::tdStart(const char **attrs)
{
	
}

void NodeParser::tdEnd()
{
	
}

void NodeParser::textStart(const char **attrs)
{
	TextNode *node = TextNode::create();
	node->initTextNode(font,fontSize,color);
	node->setAttrs(attrs);
	node->registerTextHandler(textHandleObj,textHandle);
	node->registerScriptListener(textListener);
	rowNodeQueue.push_back(node);	
}

void NodeParser::textEnd()
{
	
}

void NodeParser::imgStart(const char **attrs)
{
	
		ImageNode *node = ImageNode::create();
		node->setAttrs(attrs);
		node->registerImageHandler(imgHandleObj,imgHandle);
		node->registerScriptListener(imageListener);
		rowNodeQueue.push_back(node);
}

void NodeParser::mStart(const char **attrs)
{

}

void NodeParser::mEnd()
{
}

void NodeParser::defaultEnd()
{
	
}


NodeParser::~NodeParser()
{
	rowElems.clear();
	allNode.clear();
}

int NodeParser::findNodeType(const char *tagName)
{
	int type = TYPE_NONE;
	int i = 0;
	while(i < nodeTypeCount)
	{
		if(strcmp(nodeInfos[i].name, tagName) == 0)
		{
			type = i;
			break;
		}
		i++;
	}
	return type;
}

int NodeParser::findAttrType(const char *name)
{
	for (int i=0;i<attrCount;i++)
	{
		if (strcmp(attrNames[i],name)==0)
		{
			return i;
			break;
		}

	}
	return -1;
}

void NodeParser::deploy()
{
	float offsetY = height+curRowHight - parentNode->getContentSize().height;
	parentNode->setContentSize(CCSizeMake(parentNode->getContentSize().width,parentNode->getContentSize().height+offsetY));
   
	for (vector<CCNode*>::iterator iter=allNode.begin();iter!=allNode.end();iter++)
	{
		(*iter)->setPositionY((*iter)->getPositionY()+offsetY);
		parentNode->addChild(*iter);
	}
	
}


ImageNode::ImageNode():handleObj(NULL),imageHandler(NULL),touchState(false),id(0)
{}

ImageNode* ImageNode::create()
{
	ImageNode *image = new ImageNode();
	if(image != NULL)
	{
		image->autorelease();
		return image;
	}
	CC_SAFE_DELETE(image);
	return NULL;
}

ImageNode::~ImageNode()
{
	if(getTouchState())
		CCDirector::sharedDirector()->getTouchDispatcher()->removeDelegate(this);
}

bool ImageNode::ccTouchBegan(CCTouch *pTouch, CCEvent *pEvent)
{
	CCNode *parent=this->getParent();
	if (parent==NULL)
	{
		return false;
	}

	if(boundingBox().containsPoint(parent->convertTouchToNodeSpace(pTouch)))
	{
		if(imageHandler != NULL && handleObj != NULL)
		{
			(handleObj->*imageHandler)(id,this);
			return true;
		}
		
		if (m_listener && parent->isVisible())
		{
			CCLuaStack* stack = CCLuaEngine::defaultEngine()->getLuaStack();
			stack->clean();
			stack->pushString(id);
			stack->pushString(id);
			stack->pushCCObject(this,"CCSprite");
			stack->executeFunctionByHandler(m_listener, 2);
		}
	}
	
	return false;
}

void ImageNode::registerImageHandler(cocos2d::CCObject *obj, ImageNode::Handler hander)
{
	handleObj = obj;
	imageHandler = hander;
}

const int ImageNode::getAttrType(const char * attrname)
{
	for (int i=0;i<attrCount;i++)
	{
		if (strcmp(attrNames[i],attrname)==0)
		{
			return i;
			break;
		}
		
	}
	return -1;
	
}

void ImageNode::setAttrs(const char **attrs)
{
	while(*attrs != NULL)
	{
		
		switch(getAttrType(attrs[0]))
		{
		case ATTR_IMG_SRC:

			initWithFile(attrs[1]);
			ignoreAnchorPointForPosition(true);
			break;

		case ATTR_ID:
			if (!getTouchState())
			{
				setTouchState(true);
			}
			setID(const_cast<char*>(attrs[1]));
			break;
		default:
			break;
		}
				
		attrs += 2;
		if(*attrs == NULL)
			break;
	}
}


TextNode::TextNode():handleObj(NULL),textHandler(NULL),
	touchState(false),id(0),underLineState(false),
	lineWidth(1.0),lineColor("ff0000"),m_listener(0),
	strokeState(false)
{}
TextNode* TextNode::create()
{
	TextNode *text = new TextNode();
	;
	if(text != NULL&&text->init())
	{
		text->autorelease();
		text->ignoreAnchorPointForPosition(true);
		return text;
	}
	CC_SAFE_DELETE(text);
	return NULL;
}

bool TextNode::updateTexture()
{
    CCTexture2D *tex;
    tex = new CCTexture2D();
    
    if (!tex)
        return false;
    
    #if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID) || (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
    
        ccFontDefinition texDef = _prepareTextDefinition(true);
        tex->initWithString( m_string.c_str(), &texDef );
    
    #else
    
        tex->initWithString( m_string.c_str(),
                            m_pFontName->c_str(),
                            m_fFontSize * CC_CONTENT_SCALE_FACTOR(),
                            CC_SIZE_POINTS_TO_PIXELS(m_tDimensions),
                            m_hAlignment,
                            m_vAlignment);
    
    #endif
    
    // set the texture
    this->setTexture(tex);
    // release it
    tex->release();
    
    // set the size in the sprite
    CCRect rect =CCRectZero;
    rect.size   = m_pobTexture->getContentSize();
    this->setTextureRect(rect);
	
	if (getStrokeState())
	{
		loadFile("scripts/func/Stroke.lua");
	}

    //ok
    return true;
}

TextNode::~TextNode()
{
	if(getTouchState())
		CCDirector::sharedDirector()->getTouchDispatcher()->removeDelegate(this);
}

bool TextNode::ccTouchBegan(CCTouch *pTouch, CCEvent *pEvent)
{
	CCNode *parent=this->getParent();
	if (parent==NULL)
	{
		return false;
	}
	if(boundingBox().containsPoint(parent->convertTouchToNodeSpace(pTouch)))
	{
		if(textHandler != NULL && handleObj != NULL)
		{
			(handleObj->*textHandler)(id);
			return true;
		}
		if (m_listener && parent->isVisible())
		{
			CCLuaStack* stack = CCLuaEngine::defaultEngine()->getLuaStack();
			stack->clean();
			stack->pushString(id);
			stack->pushString(id);
			stack->executeFunctionByHandler(m_listener,1);
		}
	}
	
	return false;
}

void TextNode::registerTextHandler(cocos2d::CCObject *obj, TextNode::Handler hander)
{
	handleObj = obj;
	textHandler = hander;
}

const int TextNode::getAttrType(const char * attrname)
{
	for (int i=0;i<attrCount;i++)
	{
		if (strcmp(attrNames[i],attrname)==0)
		{
			return i;
			break;
		}

	}
	return -1;
}

const ccColor3B TextNode::getColorFromStr(const char *attr)
{
	int r, g, b;
	long int d = strtol(attr, NULL, 16);
	r = d >> 16;
	g = (d >> 8) & (0xff);
	b = d & (0xff);
	return ccc3(r, g, b);
}

void TextNode::setAttrs(const char **attrs)
{
	while(*attrs != NULL)
	{
			
		switch(getAttrType(attrs[0]))
		{
		case ATTR_FONT_NAME:setFontName(attrs[1]);break;
		case ATTR_FONT_SIZE:setFontSize(atof(attrs[1]));break;
		case ATTR_FONT_COLOR:setColor(getColorFromStr(attrs[1]));break;
		case ATTR_LINECOLOR:lineColor=string(attrs[1]);
							setUnderLineState(true);break;
		case ATTR_LINEWIDTH:lineWidth=atof(attrs[1]);
							setUnderLineState(true);break;
		case ATTR_STROKEWIDTH:strokeWidth =atoi(attrs[1]);
							setStrokeState(true);break;
		case ATTR_STROKECOLOR:strokeColor = getColorFromStr(attrs[1]);
							setStrokeState(true);break;
		case ATTR_STROKEOPACITY:strokeOpacity = atoi(attrs[1]);
							setStrokeState(true);break;
	    case ATTR_STROKEDIR:strokeDir = string(attrs[1]);
							setStrokeState(true);break;
			

		case ATTR_ID:
			if (!getTouchState())
			{
				setTouchState(true);
			}
			setID(const_cast<char*>(attrs[1]));
			break;
		default:
			break;
		}

		attrs += 2;
		if(*attrs == NULL)
			break;
	}
}

void TextNode::copyAttrs(TextNode *textNode)
{
	this->setFontName(textNode->getFontName());
	this->setColor(textNode->getColor());
	this->setFontSize(textNode->getFontSize());
	this->setID(textNode->getID());
	this->setTouchState(textNode->getTouchState());
	this->setUnderLineState(textNode->getUnderLineState());
	this->setLineWidth(textNode->getLineWidth());
	this->setlineColor(textNode->getlineColor());

	this->setStrokeState(textNode->getStrokeState());
	this->setStrokeWidth(textNode->getStrokeWidth());
	this->setStrokeColor(textNode->getStrokeColor());
	this->setStrokeOpacity(textNode->getStrokeOpacity());
	this->setStrokeDir(textNode->getStrokeDir());
}

void TextNode::initTextNode(string font,string fontSize,string color)
{
	this->setFontName(font.c_str());
	this->setFontSize(atoi(fontSize.c_str()));
	this->setColor(getColorFromStr(color.c_str()));
}

void TextNode::draw(void)
{
	CCLabelTTF::draw();
	if (getUnderLineState())
	{
		CCPoint start=getPosition();
		glLineWidth(lineWidth);
		ccColor3B color;
		if (lineColor.length()==0)
		{
			color= getColor();
		}
		else
		{
			color=getColorFromStr(lineColor.c_str());
		}
		ccDrawColor4B(color.r, color.g, color.b, 255);
		ccDrawLine(ccp(0,0), ccp(getContentSize().width, 0));
	}
}


bool TextNode::utf8_isascii(unsigned char c)
{
	if(c < 0x80)
		return true;
	else
		return false;
}

int TextNode::utf8_char_size(const unsigned char c)
{
	if(c < 0x80) return 1;
	if((c & 0xc0) == 0x80) return 0;

	int mask = 0x80;
	int num = 0;
	while(mask & c)
	{
		++num;
		mask >>= 1;
	}

	return num;
}


float TextNode::widthForAscii(char c)
{

		char str[2];
		memset(str, 0, 2);
		str[0] = c;
		CCLabelTTF *newLabel = CCLabelTTF::create(str,getFontName(),getFontSize());
		float width =newLabel->getContentSize().width-newLabel->getHorizontalAlignment()*2;//getFontSize()/2; 
		newLabel->release();
		return width;
	
}

string TextNode::cutForWidth(float width)
{
	int i = 0;
	string text=getString();
	int len = text.length();

	int fontSize =getFontSize();

	while(i < len)
	{
		float offset = fontSize;
		if(utf8_isascii(text[i]))
		{
			offset = widthForAscii(text[i]);
		}

		if(width - offset < 0)
		{
			string dest = text.substr(i);
			setString(text.substr(0, i).c_str());
			return dest;
		}
		else
		{
			width -= offset;
		}

		i += utf8_char_size(text[i]);
	}
	return "";
}
