/****************************************************************************
Copyright (c) 2010 cocos2d-x.org

http://www.cocos2d-x.org

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
****************************************************************************/

#include "CCHTMLTextFieldTTF.h"

#include "CCDirector.h"
#include "CCEGLView.h"

NS_CC_EXT_BEGIN

static int _calcCharCount(const char * pszText)
{
    int n = 0;
    char ch = 0;
    while ((ch = *pszText))
    {
        CC_BREAK_IF(! ch);

        if (0x80 != (0xC0 & ch))
        {
            ++n;
        }
        ++pszText;
    }
    return n;
}


//free char* after used!
static char * _splitChar(const char * pszText,int maxLenth)
{
    int n = 0;
	int i = 0;
    char ch = 0;
	const char *tmpText = pszText;
	char *cpText = new char[maxLenth*4];
	memset(cpText,0,maxLenth*4);
    while ((ch = *pszText))
    {
        CC_BREAK_IF(! ch);
		cpText[i] = ch;
        if (0x80 != (0xC0 & ch))
        {
            ++n;
			if(n>maxLenth){		
				cpText[i] = 0;
				return  cpText;
			}
        }
        ++pszText;
		++i;
    }
    return cpText;
}


//////////////////////////////////////////////////////////////////////////
// constructor and destructor
//////////////////////////////////////////////////////////////////////////

CCHTMLTextFieldTTF::CCHTMLTextFieldTTF(const CCSize& size,const char *fontName,const char* colorStr)
: m_pDelegate(0)
, m_nCharCount(0)
, m_ColorFont(colorStr)
, m_pInputText(new std::string)
, m_pPlaceHolder(new std::string)   // prevent CCLabelTTF initWithString assertion
, m_maxLenth(10)
, m_bSecureTextEntry(false)
{
	CCHTMLLabel::initWithString("",size,fontName);	
}

CCHTMLTextFieldTTF::~CCHTMLTextFieldTTF()
{
    CC_SAFE_DELETE(m_pInputText);
    CC_SAFE_DELETE(m_pPlaceHolder);
}

//////////////////////////////////////////////////////////////////////////
// static constructor
//////////////////////////////////////////////////////////////////////////
CCHTMLTextFieldTTF * CCHTMLTextFieldTTF::textFieldWithPlaceHolder(const CCSize& size,const char *fontName,const char* colorStr)
{
	CCHTMLTextFieldTTF *pRet = new CCHTMLTextFieldTTF(size,fontName,colorStr);
	if(pRet)
    {
        pRet->autorelease();      
        return pRet;
    }
    CC_SAFE_DELETE(pRet);
    return NULL;
}

//////////////////////////////////////////////////////////////////////////
// CCIMEDelegate
//////////////////////////////////////////////////////////////////////////

bool CCHTMLTextFieldTTF::attachWithIME()
{
    bool bRet = CCIMEDelegate::attachWithIME();
    if (bRet)
    {
        // open keyboard
        CCEGLView * pGlView = CCDirector::sharedDirector()->getOpenGLView();
        if (pGlView)
        {
            pGlView->setIMEKeyboardState(true);
        }
    }
    return bRet;
}

bool CCHTMLTextFieldTTF::detachWithIME()
{
    bool bRet = CCIMEDelegate::detachWithIME();
    if (bRet)
    {
        // close keyboard
        CCEGLView * pGlView = CCDirector::sharedDirector()->getOpenGLView();
        if (pGlView)
        {
            pGlView->setIMEKeyboardState(false);
        }
    }
    return bRet;
}

bool CCHTMLTextFieldTTF::canAttachWithIME()
{
    return (m_pDelegate) ? (! m_pDelegate->onTextFieldAttachWithIME(this)) : true;
}

bool CCHTMLTextFieldTTF::canDetachWithIME()
{
    return (m_pDelegate) ? (! m_pDelegate->onTextFieldDetachWithIME(this)) : true;
}

void CCHTMLTextFieldTTF::setStringWithColor(std::string str,bool isHolder)
{
	string& strColor = isHolder ? m_ColorSpaceHolder:m_ColorFont;	
	if(strColor != ""){
		string strWithColor = "<font color='" + strColor+"'>"+str+"</font>";
		CCHTMLLabel::setString(strWithColor.c_str());		
	}
	else{
		CCHTMLLabel::setString(str.c_str());
	}	
}

void CCHTMLTextFieldTTF::insertText(const char * text, int len)
{
    std::string sInsert(text, len);

    // insert \n means input end
    int nPos = sInsert.find('\n');
    if ((int)sInsert.npos != nPos)
    {
        len = nPos;
        sInsert.erase(nPos);
    }

    if (len > 0)
    {
        if (m_pDelegate && m_pDelegate->onTextFieldInsertText(this, sInsert.c_str(), len))
        {
            // delegate doesn't want to insert text
            return;
        }

        m_nCharCount += _calcCharCount(sInsert.c_str());
        std::string sText(*m_pInputText);
        sText.append(sInsert);
		setString(sText.c_str());
    }

    if ((int)sInsert.npos == nPos) {
        return;
    }

    // '\n' inserted, let delegate process first
    if (m_pDelegate && m_pDelegate->onTextFieldInsertText(this, "\n", 1))
    {
        return;
    }

    // if delegate hasn't processed, detach from IME by default
    detachWithIME();
}

void CCHTMLTextFieldTTF::deleteBackward()
{
    int nStrLen = m_pInputText->length();
    if (! nStrLen)
    {
        // there is no string
        return;
    }

    // get the delete byte number
    int nDeleteLen = 1;    // default, erase 1 byte

    while(0x80 == (0xC0 & m_pInputText->at(nStrLen - nDeleteLen)))
    {
        ++nDeleteLen;
    }

    if (m_pDelegate && m_pDelegate->onTextFieldDeleteBackward(this, m_pInputText->c_str() + nStrLen - nDeleteLen, nDeleteLen))
    {
        // delegate doesn't wan't to delete backwards
        return;
    }

    // if all text deleted, show placeholder string
    if (nStrLen <= nDeleteLen)
    {
        CC_SAFE_DELETE(m_pInputText);
        m_pInputText = new std::string;
        m_nCharCount = 0;
        setStringWithColor(*m_pPlaceHolder,true);
        return;
    }

    // set new input text
    std::string sText(m_pInputText->c_str(), nStrLen - nDeleteLen);
	setString(sText.c_str());
}

const char * CCHTMLTextFieldTTF::getContentText()
{
    return m_pInputText->c_str();
}

void CCHTMLTextFieldTTF::draw()
{
    if (m_pDelegate && m_pDelegate->onDraw(this))
    {
        return;
    }
    if (m_pInputText->length())
    {
        CCHTMLLabel::draw();
        return;
    }
    CCHTMLLabel::draw();
}

void CCHTMLTextFieldTTF::setSpaceHolder(const char *placeholder,const char* hoderColorStr)
{
    m_ColorSpaceHolder = hoderColorStr;
	CC_SAFE_DELETE(m_pPlaceHolder);
    m_pPlaceHolder = (placeholder) ? new std::string(placeholder) : new std::string;
    if (! m_pInputText->length())
    {
		setStringWithColor(*m_pPlaceHolder,true);
    }
}

//////////////////////////////////////////////////////////////////////////
// properties
//////////////////////////////////////////////////////////////////////////

// input text property
void CCHTMLTextFieldTTF::setString(const char *texttmp)
{
    static char bulletString[] = {(char)0xe2, (char)0x80, (char)0xa2, (char)0x00};
    std::string displayText;
    int length;
	const char *text_str = _splitChar(texttmp,m_maxLenth);	

	CC_SAFE_DELETE(m_pInputText);
    if (text_str)
    {
        m_pInputText = new std::string(text_str);		
        displayText = *m_pInputText;
        if (m_bSecureTextEntry)
        {
            displayText = "";
            length = m_pInputText->length();
            while (length)
            {
                displayText.append(bulletString);
                --length;
            }
        }
    }
    else
    {
        m_pInputText = new std::string;
    }
	delete text_str;
    // if there is no input text, display placeholder instead
    if (! m_pInputText->length())
    {
        setStringWithColor(*m_pPlaceHolder,true);
    }
    else
    {
		setStringWithColor(displayText,false);
    }
    m_nCharCount = _calcCharCount(m_pInputText->c_str());
}

const char* CCHTMLTextFieldTTF::getString(void)
{
    return m_pInputText->c_str();
}

void CCHTMLTextFieldTTF::setMaxLenth(int length){
	m_maxLenth = length;
	if(*m_pInputText != ""){
		setString(getString());
	}	
}

// secureTextEntry
void CCHTMLTextFieldTTF::setSecureTextEntry(bool value)
{
    if (m_bSecureTextEntry != value)
    {
        m_bSecureTextEntry = value;
        setString(getString());
    }
}



bool CCHTMLTextFieldTTF::isSecureTextEntry()
{
    return m_bSecureTextEntry;
}

NS_CC_EXT_END
