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

#ifndef __CC_HTMLTEXT_FIELD_H__
#define __CC_HTMLTEXT_FIELD_H__

#include "CCHTMLLabel.h"
#include "text_input_node/CCIMEDelegate.h"
#include "touch_dispatcher/CCTouchDelegateProtocol.h"
NS_CC_EXT_BEGIN

class CCHTMLTextFieldTTF;

/**
 * @addtogroup input
 * @{
 */

class CC_DLL CCHTMLTextFieldDelegate
{
public:
    /**
    @brief    If the sender doesn't want to attach to the IME, return true;
    */
    virtual bool onTextFieldAttachWithIME(CCHTMLTextFieldTTF * sender)
    {
        CC_UNUSED_PARAM(sender);
        return false;
    }

    /**
    @brief    If the sender doesn't want to detach from the IME, return true;
    */
    virtual bool onTextFieldDetachWithIME(CCHTMLTextFieldTTF * sender)
    {
        CC_UNUSED_PARAM(sender);
        return false;
    }

    /**
    @brief    If the sender doesn't want to insert the text, return true;
    */
    virtual bool onTextFieldInsertText(CCHTMLTextFieldTTF * sender, const char * text, int nLen)
    {
        CC_UNUSED_PARAM(sender);
        CC_UNUSED_PARAM(text);
        CC_UNUSED_PARAM(nLen);
        return false;
    }

    /**
    @brief    If the sender doesn't want to delete the delText, return true;
    */
    virtual bool onTextFieldDeleteBackward(CCHTMLTextFieldTTF * sender, const char * delText, int nLen)
    {
        CC_UNUSED_PARAM(sender);
        CC_UNUSED_PARAM(delText);
        CC_UNUSED_PARAM(nLen);
        return false;
    }

    /**
    @brief    If the sender doesn't want to draw, return true.
    */
    virtual bool onDraw(CCHTMLTextFieldTTF * sender)
    {
        CC_UNUSED_PARAM(sender);
        return false;
    }
};

/**
@brief    A simple text input field with TTF font.
*/
class CC_DLL CCHTMLTextFieldTTF : public CCHTMLLabel, public CCIMEDelegate
{
public:
    CCHTMLTextFieldTTF(const CCSize& size,const char *fontName,const char* colorStr);
    virtual ~CCHTMLTextFieldTTF();

    /** creates a CCLabelTTF from a fontname and font size */
    static CCHTMLTextFieldTTF * textFieldWithPlaceHolder(const CCSize& size,const char *fontName,const char* colorStr);

    /**
    @brief    Open keyboard and receive input text.
    */
    virtual bool attachWithIME();

    /**
    @brief    End text input and close keyboard.
    */
    virtual bool detachWithIME();

    //////////////////////////////////////////////////////////////////////////
    // properties
    //////////////////////////////////////////////////////////////////////////

    CC_SYNTHESIZE(CCHTMLTextFieldDelegate *, m_pDelegate, Delegate);
    CC_SYNTHESIZE_READONLY(int, m_nCharCount, CharCount);        

    // input text property
public:
	virtual void setSpaceHolder(const char *placeholder,const char* hoderColorStr);
    virtual void setString(const char *text);
    virtual const char* getString(void);
	virtual void setMaxLenth(int length);
protected:
    std::string * m_pInputText;
protected:
    std::string * m_pPlaceHolder;
    std::string m_ColorSpaceHolder;
	std::string m_ColorFont;
	int m_maxLenth;
public:
    virtual void setSecureTextEntry(bool value);
    virtual bool isSecureTextEntry();
protected:
    bool m_bSecureTextEntry;
protected:

    virtual void draw();

    //////////////////////////////////////////////////////////////////////////
    // CCIMEDelegate interface
    //////////////////////////////////////////////////////////////////////////

    virtual bool canAttachWithIME();
    virtual bool canDetachWithIME();
    virtual void insertText(const char * text, int len);
    virtual void deleteBackward();
    virtual const char * getContentText();
	
private:
	void setStringWithColor(std::string str,bool isHolder);
    class LengthStack;
    LengthStack * m_pLens;
};

// end of input group
/// @}

NS_CC_EXT_END

#endif    // __CC_TEXT_FIELD_H__
