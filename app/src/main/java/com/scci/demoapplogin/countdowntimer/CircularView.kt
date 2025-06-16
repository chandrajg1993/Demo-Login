package com.scci.demoapplogin.countdowntimer

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.SweepGradient
import android.os.Handler
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.animation.LinearInterpolator
import androidx.appcompat.widget.AppCompatTextView
import com.scci.demoapplogin.R
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

class CircularView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr:
Int = 0) : AppCompatTextView(context!!, attrs, defStyleAttr) {
    //Paint Objects
    private var paintOuterCircle: Paint? = null
    private var paintInnerArc: Paint? = null

    //Outer Circle Properties
    private var circleRadiusInPX = 0f
    private var circleStrokeWidthInPx = 0f
    private var rotationDegrees = 0f
    private var colorCircleStroke = 0

    //Inner Arc Properties
    private var colorArcStroke = 0
    private var arcMargin = -1f
    private var arcRectContainer: RectF? = null

    //Timer Value in Seconds Or Custom Text
    private var counterInSeconds = OptionsBuilder.INFINITE.toLong()
    private var customText: String? = null

    //Rotation Animator Properties
    private var rotationAnimator: ValueAnimator? = null

    //Center Point of Screen Properties
    private var yLocation = 0f
    private var xLocation = 0f

    //Gradient Color Properties
    private lateinit var colorSequence: IntArray
    private lateinit var colorStartingPointSequence: FloatArray

    // Show Text or not
    private var shouldDisplayTimer = true
    private var shouldDisplayTimeUnit = true

    //Timer Callback
    private var circularViewCallback: CircularViewCallback? = null

    //Timer Handler,Runnable
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    private var isPause = false
    private val lock: Lock = ReentrantLock()

    init {
        if (attrs != null) {
            val typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CircularView)
            try {
                circleRadiusInPX = dpToPx(typedArray.getInt(R.styleable.CircularView_m_circle_radius, DEFAULT_CIRCLE_RADIUS_IN_DP).toFloat())
                circleStrokeWidthInPx = dpToPx(typedArray.getInt(R.styleable.CircularView_m_cicle_stroke_width, DEFAULT_CIRCLE_STROKE_WIDTH).toFloat())
                colorCircleStroke = typedArray.getColor(R.styleable.CircularView_m_circle_stroke_color, DEFAULT_CIRCLE_STROKE_COLOR)
                colorArcStroke = typedArray.getColor(R.styleable.CircularView_m_arc_stroke_color, DEFAULT_ARC_STROKE_COLOR)
                arcMargin = dpToPx(typedArray.getInt(R.styleable.CircularView_m_arc_margin, DEFAULT_ARC_MARGIN).toFloat())
            } finally {
                typedArray.recycle()
            }
        }
        init()
    }

    /**
     * Initialize Helper Objects
     */
    private fun init() {

        //Outer Circle Paint Object initialization
        paintOuterCircle = Paint(Paint.ANTI_ALIAS_FLAG)
        paintOuterCircle!!.color = colorCircleStroke
        paintOuterCircle!!.strokeWidth = circleStrokeWidthInPx
        paintOuterCircle!!.style = Paint.Style.STROKE

        //Inner Arc Paint Object Initialization
        paintInnerArc = Paint(Paint.ANTI_ALIAS_FLAG)
        paintInnerArc!!.color = colorArcStroke
        paintInnerArc!!.style = Paint.Style.STROKE
        paintInnerArc!!.strokeWidth = circleStrokeWidthInPx - arcMargin
        paintInnerArc!!.strokeCap = Paint.Cap.ROUND

        //Rotation Animator Object Initialization
        rotationAnimator = ValueAnimator.ofFloat(CIRCLE_MINIMUM_DEGREE.toFloat(), CIRCLE_MAX_DEGREES.toFloat())
        rotationAnimator?.setDuration(ONE_SEC_IN_MILLIS.toLong())
        rotationAnimator?.setRepeatCount(ValueAnimator.INFINITE)
        rotationAnimator?.setInterpolator(LinearInterpolator())
        rotationAnimator?.addUpdateListener(AnimatorUpdateListener { valueAnimator ->
            rotationDegrees = valueAnimator.animatedValue as Float
            invalidate()
        })

        //Gradient Color Sequence Initialization
        colorSequence = intArrayOf(
            colorCircleStroke, colorCircleStroke, colorCircleStroke, colorArcStroke, colorArcStroke
        )

        //Gradient Color Starting Point Sequence Initialization
        colorStartingPointSequence = floatArrayOf(
            0.0f, 0.25f, 0.50f, 0.60f, 1f
        )

        //Initialize Arc Object
        arcRectContainer = RectF()
        setTextViewProperties()
        setUpCountdownHandler()
    }

    /**
     * Setup Count Down Timer
     */
    private fun setUpCountdownHandler() {
        handler = Handler()
        runnable = object : Runnable {
            override fun run() {
                setTextOfTextViewWithSeconds(counterInSeconds)
                if (OptionsBuilder.INFINITE.toLong() != counterInSeconds) counterInSeconds--
                if (isCounterInRunningState) {
                    handler!!.postDelayed(this, ONE_SEC_IN_MILLIS.toLong())
                } else if (circularViewCallback != null && !isPause) {
                    lock.lock()
                    circularViewCallback!!.onTimerFinish()
                    circularViewCallback = null
                    stopTimer()
                    lock.unlock()
                }
            }
        }
    }

    private val isCounterInRunningState: Boolean
        private get() = counterInSeconds > -1 || counterInSeconds == OptionsBuilder.INFINITE.toLong()

    private fun setTextOfTextViewWithSeconds(value: Long) {
        var numberString: String
        val unit: String
        if (value/60 > 0) {
            numberString = value.toString()
            unit = ""
            //numberString = String.valueOf((value / 60) + 1);
            //unit = "\nminutes";
        } else {
            numberString = value.toString()
            unit = ""
        }
        val highlightedTextLength = numberString.length
        if (shouldDisplayTimeUnit) {
            numberString += unit
        }

        //To resize the text size according to length
        val spannableString = SpannableString(numberString)
        spannableString.setSpan(RelativeSizeSpan(getTextProportion(numberString)), 0, highlightedTextLength, 0)
        if (shouldDisplayTimer) {
            text = if (customText != null) {
                addLineSpacingIfNeeded(customText!!)
            } else {
                spannableString
            }
        }
    }

    private fun addLineSpacingIfNeeded(customText: String): String {
        var customText = customText
        if (circleRadiusInPX*1.5 < paint.measureText(customText)) {
            val array = customText.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val result = StringBuilder("")
            for (anArray in array) {
                result.append(anArray).append("\n")
            }
            customText = result.toString().trim { it <= ' ' }
        }
        return customText
    }

    /**
     * Setup Text View Properties
     */
    private fun setTextViewProperties() {
        setTextSize(TypedValue.COMPLEX_UNIT_PX, circleRadiusInPX/5)
        gravity = Gravity.CENTER
    }

    /**
     * Start Animator and Handler so that they are in sync
     */
    fun startTimer() {
        //Check if countdown is already in running state or not
        if (isCounterInRunningState && !rotationAnimator!!.isRunning) {
            handler!!.post(runnable!!)
            rotationAnimator!!.start()
        }
    }

    /**
     * Stop Timer before it finishes
     */
    fun stopTimer() {
        handler!!.removeCallbacks(runnable!!)
        rotationAnimator!!.cancel()
        if (circularViewCallback != null && isCounterInRunningState) {
            counterInSeconds = -1
            circularViewCallback!!.onTimerCancelled()
        }
        circularViewCallback = null
    }

    fun pauseTimer(): Boolean {
        if (isCounterInRunningState && rotationAnimator!!.isRunning) {
            lock.lock()
            handler!!.removeCallbacks(runnable!!)
            rotationAnimator!!.pause()
            isPause = true
            return true
        }
        return false
    }

    fun resumeTimer() {
        if (isCounterInRunningState && rotationAnimator!!.isRunning && isPause) {
            lock.unlock()
            handler!!.post(runnable!!)
            rotationAnimator!!.resume()
            isPause = false
        }
    }

    /**
     * Set the size of Text with respect to Number of digits, to fit it in circle.
     *
     * @param numberString The String contains Number in seconcds
     * @return Proportion Size of Number to normal text size.
     */
    private fun getTextProportion(numberString: String): Float {
        val len = numberString.length
        return if (len < 4) 4f else if (len < 5) 3f else 2f
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measuredWidth = measureWidth()
        val measuredHeight = measureHeight()
        yLocation = (measuredHeight/2).toFloat()
        xLocation = (measuredWidth/2).toFloat()
        paintInnerArc!!.shader = SweepGradient(xLocation, yLocation, colorSequence, colorStartingPointSequence)
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //Set Inner Arc Position
        arcRectContainer!![xLocation - circleRadiusInPX, yLocation - circleRadiusInPX, xLocation + circleRadiusInPX] = yLocation + circleRadiusInPX
        canvas.save()

        //Rotate and Draw Circle and Inner Arc
        canvas.rotate(rotationDegrees, xLocation, yLocation)
        canvas.drawCircle(xLocation, yLocation, circleRadiusInPX, paintOuterCircle!!)
        canvas.drawArc(arcRectContainer!!, START_ARC_ANGLE.toFloat(), SWEEP_ARC_ANGLE.toFloat(), false, paintInnerArc!!)
        canvas.restore()
    }

    /**
     * Set the height with respect to Circle Radius
     *
     * @return height
     */
    private fun measureHeight(): Int {
        return (circleRadiusInPX*2 + circleStrokeWidthInPx + paddingTop + paddingBottom).toInt()
    }

    /**
     * Set the width with respect to Circle Radius
     *
     * @return width
     */
    private fun measureWidth(): Int {
        return (circleRadiusInPX*2 + circleStrokeWidthInPx + paddingRight + paddingLeft).toInt()
    }

    /**
     * Convert Dp to Pixels
     *
     * @param value Value in Dp
     * @return Value in Pixels
     */
    private fun dpToPx(value: Float): Float {
        return value*context.resources.displayMetrics.density
    }

    fun setOptions(optionBuilder: OptionsBuilder) {
        counterInSeconds = optionBuilder.counterInSeconds
        circularViewCallback = optionBuilder.circularViewCallback
        shouldDisplayTimer = optionBuilder.shouldDisplayTimer
        customText = optionBuilder.customText
        shouldDisplayTimeUnit = optionBuilder.shouldDisplayTimeUnit
        setTextOfTextViewWithSeconds(counterInSeconds)
    }

    class OptionsBuilder {
        var circularViewCallback: CircularViewCallback? = null
        var shouldDisplayTimer = true
        var customText: String? = null
        var counterInSeconds = INFINITE.toLong()
        var shouldDisplayTimeUnit = true
        fun setCircularViewCallback(circularViewCallback: CircularViewCallback?): OptionsBuilder {
            this.circularViewCallback = circularViewCallback
            return this
        }

        fun shouldDisplayText(shouldDisplayText: Boolean): OptionsBuilder {
            shouldDisplayTimer = shouldDisplayText
            return this
        }

        fun setCustomText(text: String?): OptionsBuilder {
            customText = text
            return this
        }

        fun setCounterInSeconds(counterInSeconds: Long): OptionsBuilder {
            this.counterInSeconds = counterInSeconds
            return this
        }

        fun shouldDisplayTimeUnit(shouldDisplayTimeUnit: Boolean): OptionsBuilder {
            this.shouldDisplayTimeUnit = shouldDisplayTimeUnit
            return this
        }

        companion object {
            const val INFINITE = -23021996
        }
    }

    companion object {
        // Default Properties
        private const val DEFAULT_CIRCLE_STROKE_COLOR = 0xEEEEEE
        private const val DEFAULT_ARC_STROKE_COLOR = 0xFFFFFF
        private const val DEFAULT_CIRCLE_RADIUS_IN_DP = 70
        private const val DEFAULT_CIRCLE_STROKE_WIDTH = 20
        private const val DEFAULT_ARC_MARGIN = 5
        private const val START_ARC_ANGLE = 180
        private const val SWEEP_ARC_ANGLE = 80
        private const val CIRCLE_MINIMUM_DEGREE = 0
        private const val CIRCLE_MAX_DEGREES = 360
        private const val ONE_SEC_IN_MILLIS = 1000
    }
}