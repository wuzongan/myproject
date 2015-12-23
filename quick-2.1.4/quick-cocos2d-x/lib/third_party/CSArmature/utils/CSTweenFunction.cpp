/*
 * Copyright (c) 2012 Chukong Technologies, Inc.
 *
 * http://www.cocostudio.com
 * http://tools.cocoachina.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
 * NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

#include "CSTweenFunction.h"
#include "CSUtilMath.h"

namespace cs {
    
float TweenFunction::tweenTo(float from, float change, float time, float duration, TweenType tweenType)
{
    float delta = 0;
    
    switch (tweenType) {
        case Linear:
            delta = linear(time, 0, 1, duration);
            break;
            
        case Sine_EaseIn:
            delta = sineEaseIn(time, 0, 1, duration);
            break;
        case Sine_EaseOut:
            delta = sineEaseOut(time, 0, 1, duration);
            break;
        case Sine_EaseInOut:
            delta = sineEaseInOut(time, 0, 1, duration);
            break;
            
        case Quad_EaseIn:
            delta = quadEaseIn(time, 0, 1, duration);
            break;
        case Quad_EaseOut:
            delta = quadEaseOut(time, 0, 1, duration);
            break;
        case Quad_EaseInOut:
            delta = quadEaseInOut(time, 0, 1, duration);
            break;
            
        case Cubic_EaseIn:
            delta = cubicEaseIn(time, 0, 1, duration);
            break;
        case Cubic_EaseOut:
            delta = cubicEaseOut(time, 0, 1, duration);
            break;
        case Cubic_EaseInOut:
            delta = cubicEaseInOut(time, 0, 1, duration);
            break;
            
        case Quart_EaseIn:
            delta = quartEaseIn(time, 0, 1, duration);
            break;
        case Quart_EaseOut:
            delta = quartEaseOut(time, 0, 1, duration);
            break;
        case Quart_EaseInOut:
            delta = quartEaseInOut(time, 0, 1, duration);
            break;
            
        case Quint_EaseIn:
            delta = quintEaseIn(time, 0, 1, duration);
            break;
        case Quint_EaseOut:
            delta = quintEaseOut(time, 0, 1, duration);
            break;
        case Quint_EaseInOut:
            delta = quintEaseInOut(time, 0, 1, duration);
            break;
            
        case Expo_EaseIn:
            delta = expoEaseIn(time, 0, 1, duration);
            break;
        case Expo_EaseOut:
            delta = expoEaseOut(time, 0, 1, duration);
            break;
        case Expo_EaseInOut:
            delta = expoEaseInOut(time, 0, 1, duration);
            break;
            
        case Circ_EaseIn:
            delta = circEaseIn(time, 0, 1, duration);
            break;
        case Circ_EaseOut:
            delta = circEaseOut(time, 0, 1, duration);
            break;
        case Circ_EaseInOut:
            delta = circEaseInOut(time, 0, 1, duration);
            break;
            
        case Elastic_EaseIn:
            delta = elasticEaseIn(time, 0, 1, duration);
            break;
        case Elastic_EaseOut:
            delta = elasticEaseOut(time, 0, 1, duration);
            break;
        case Elastic_EaseInOut:
            delta = elasticEaseInOut(time, 0, 1, duration);
            break;
           
            
        case Back_EaseIn:
            delta = backEaseIn(time, 0, 1, duration);
            break;
        case Back_EaseOut:
            delta = backEaseOut(time, 0, 1, duration);
            break;
        case Back_EaseInOut:
            delta = backEaseInOut(time, 0, 1, duration);
            break;
            
        case Bounce_EaseIn:
            delta = bounceEaseIn(time, 0, 1, duration);
            break;
        case Bounce_EaseOut:
            delta = bounceEaseOut(time, 0, 1, duration);
            break;
        case Bounce_EaseInOut:
            delta = bounceEaseInOut(time, 0, 1, duration);
            break;
            
        default:
            delta = sineEaseInOut(time, 0, 1, duration);
            break;
    }
    
    return delta;
}

float TweenFunction::linear(float t, float b, float c, float d)
{
    return c * t / d + b;
}

float TweenFunction::quadEaseIn(float t, float b, float c, float d)
{
    return c * (t /= d) * t + b;
}
float TweenFunction::quadEaseOut(float t, float b, float c, float d)
{
    return -c * (t /= d) * (t - 2) + b;
}
float TweenFunction::quadEaseInOut(float t, float b, float c, float d)
{
    if ((t /= d / 2) < 1)
        return c / 2 * t * t + b;
    return -c / 2 * ((--t) * (t - 2) - 1) + b;
}

float TweenFunction::cubicEaseIn(float t, float b, float c, float d)
{
    return c * (t /= d) * t * t + b;
}
float TweenFunction::cubicEaseOut(float t, float b, float c, float d)
{
    return c * (( t = t / d - 1) * t * t + 1) + b;
}
float TweenFunction::cubicEaseInOut(float t, float b, float c, float d)
{
    if ((t /= d / 2) < 1)
        return c / 2 * t * t * t + b;
    return c / 2 * ((t -= 2) * t * t + 2) + b;
}

float TweenFunction::quartEaseIn(float t, float b, float c, float d)
{
    return c * (t /= d) * t * t * t + b;
}
float TweenFunction::quartEaseOut(float t, float b, float c, float d)
{
    return -c * (( t = t / d - 1) * t * t * t - 1) + b;
}
float TweenFunction::quartEaseInOut(float t, float b, float c, float d)
{
    if ((t /= d / 2) < 1)
        return c / 2 * t * t * t * t + b;
    return -c / 2 * ((t -= 2) * t * t * t - 2) + b;
}

float TweenFunction::quintEaseIn(float t, float b, float c, float d)
{
    return c * (t /= d) * t * t * t * t + b;
}
float TweenFunction::quintEaseOut(float t, float b, float c, float d)
{
    return c * (( t = t / d - 1) * t * t * t * t + 1) + b;
}
float TweenFunction::quintEaseInOut(float t, float b, float c, float d)
{
    if ((t /= d / 2) < 1)
        return c / 2 * t * t * t * t * t + b;
    return c / 2 * ((t -= 2) * t * t * t * t + 2) + b;
}

float TweenFunction::sineEaseIn(float t, float b, float c, float d)
{
    return -c * cos(t / d * (CS_PI / 2)) + c + b;
}
float TweenFunction::sineEaseOut(float t, float b, float c, float d)
{
    return c * sin(t / d * (CS_PI / 2)) + b;
}
float TweenFunction::sineEaseInOut(float t, float b, float c, float d)
{
    return -c / 2 * (cos(CS_PI * t / d) - 1) + b;
}

float TweenFunction::expoEaseIn(float t, float b, float c, float d)
{
    return (t == 0) ? b : c * pow(2, 10 * (t / d - 1)) + b;
}
float TweenFunction::expoEaseOut(float t, float b, float c, float d)
{
    return (t == d) ? b + c : c * (-pow(2, -10 * t / d) + 1) + b;
}
float TweenFunction::expoEaseInOut(float t, float b, float c, float d)
{
    if (t == 0)
        return b;
    if (t == d)
        return b + c;
    if ((t /= d / 2) < 1)
        return c / 2 * pow(2, 10 * (t - 1)) + b;
    return c / 2 * (-pow(2, -10 * --t) + 2) + b;
}

float TweenFunction::circEaseIn(float t, float b, float c, float d)
{
    return -c * (sqrt(1 - (t /= d) * t) - 1) + b;
}
float TweenFunction::circEaseOut(float t, float b, float c, float d)
{
    return c * sqrt(1 - ( t = t / d - 1) * t) + b;
}
float TweenFunction::circEaseInOut(float t, float b, float c, float d)
{
    if ((t /= d / 2) < 1)
        return -c / 2 * (sqrt(1 - t * t) - 1) + b;
    return c / 2 * (sqrt(1 - (t -= 2) * t) + 1) + b;
}

float TweenFunction::elasticEaseIn(float t, float b, float c, float d, float a, float p)
{
    float s = 0;
    if (t == 0)
        return b;
    if ((t /= d) == 1)
        return b + c;
    if (!p)
        p = d * .3;
    if (!a || a < fabsf(c)) {
        a = c;
        s = p / 4;
    } else
        s = p / (2 * CS_PI) * asin(c / a);
    return -(a * pow(2, 10 * (t -= 1)) * sin((t * d - s) * (2 * CS_PI) / p)) + b;
}
float TweenFunction::elasticEaseOut(float t, float b, float c, float d, float a, float p)
{
    float s = 0;
    if (t == 0)
        return b;
    if ((t /= d) == 1)
        return b + c;
    if (!p)
        p = d * .3;
    if (!a || a < fabsf(c)) {
        a = c;
        s = p / 4;
    } else
        s = p / (2 * CS_PI) * asin(c / a);
    return (a * pow(2, -10 * t) * sin((t * d - s) * (2 * CS_PI) / p) + c + b);
}
float TweenFunction::elasticEaseInOut(float t, float b, float c, float d, float a, float p)
{
    float s = 0;
    if (t == 0)
        return b;
    if ((t /= d / 2) == 2)
        return b + c;
    if (!p)
        p = d * (.3 * 1.5);
    if (!a || a < fabsf(c)) {
        a = c;
        s = p / 4;
    } else
        s = p / (2 * CS_PI) * asin(c / a);
    if (t < 1)
        return -.5 * (a * pow(2, 10 * (t -= 1)) * sin((t * d - s) * (2 * CS_PI) / p)) + b;
    return a * pow(2, -10 * (t -= 1)) * sin((t * d - s) * (2 * CS_PI) / p) * .5 + c + b;
}

float TweenFunction::backEaseIn(float t, float b, float c, float d, float s)
{
    if (s == 0)
        s = 1.70158;
    return c * (t /= d) * t * ((s + 1) * t - s) + b;
}
float TweenFunction::backEaseOut(float t, float b, float c, float d, float s)
{
    if (s == 0)
        s = 1.70158;
    return c * (( t = t / d - 1) * t * ((s + 1) * t + s) + 1) + b;
}
float TweenFunction::backEaseInOut(float t, float b, float c, float d, float s)
{
    if (s == 0)
        s = 1.70158;
    if ((t /= d / 2) < 1)
        return c / 2 * (t * t * (((s *= (1.525)) + 1) * t - s)) + b;
    return c / 2 * ((t -= 2) * t * (((s *= (1.525)) + 1) * t + s) + 2) + b;
}

float TweenFunction::bounceEaseIn(float t, float b, float c, float d)
{
    return c - bounceEaseOut(d - t, 0, c, d) + b;
}
    
float TweenFunction::bounceEaseOut(float t, float b, float c, float d)
{
    if ((t /= d) < (1 / 2.75)) {
        return c * (7.5625 * t * t) + b;
    } else if (t < (2 / 2.75)) {
        return c * (7.5625 * (t -= (1.5 / 2.75)) * t + .75) + b;
    } else if (t < (2.5 / 2.75)) {
        return c * (7.5625 * (t -= (2.25 / 2.75)) * t + .9375) + b;
    } else {
        return c * (7.5625 * (t -= (2.625 / 2.75)) * t + .984375) + b;
    }
}
    
float TweenFunction::bounceEaseInOut(float t, float b, float c, float d)
{
    if (t < d / 2)
        return bounceEaseIn(t * 2, 0, c, d) * .5 + b;
    else
        return bounceEaseOut(t * 2 - d, 0, c, d) * .5 + c * .5 + b;
}


}
