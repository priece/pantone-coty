package com.example.pantonecoty

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import com.example.pantonecoty.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), GestureDetector.OnGestureListener {
    private lateinit var binding: ActivityMainBinding
    private val colorData = mutableListOf<ColorYear>()
    private var currentIndex = 0
    private lateinit var gestureDetector: GestureDetectorCompat
    private var isScrolling = false
    private var translationY = 0f
    private lateinit var springAnimation: SpringAnimation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 设置全屏显示，隐藏状态栏和导航栏
        window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // 初始化手势检测器
        gestureDetector = GestureDetectorCompat(this, this)
        
        // 设置触摸监听器到根视图，确保整个屏幕都能响应滑动
        binding.root.setOnTouchListener {
            _, event -> gestureDetector.onTouchEvent(event)
        }
        
        // 初始化弹簧动画
        springAnimation = SpringAnimation(binding.root, DynamicAnimation.TRANSLATION_Y)
        springAnimation.spring = SpringForce()
            .setStiffness(SpringForce.STIFFNESS_MEDIUM)
            .setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY)

        // 读取CSV数据
        readCsvData()
        
        // 默认显示2025年的颜色
        val initialYear = 2025
        currentIndex = colorData.indexOfFirst { it.year == initialYear }
        if (currentIndex == -1) currentIndex = 0
        
        // 显示当前颜色
        displayColorData()
    }
    
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            // 隐藏虚拟导航栏和状态栏
            val decorView = window.decorView
            decorView.systemUiVisibility = (
                android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
                or android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            )
        }
    }

    private fun readCsvData() {
        val inputStream = resources.openRawResource(R.raw.coty)
        inputStream.bufferedReader().useLines { lines ->
            lines.forEachIndexed { index, line ->
                if (index == 0) return@forEachIndexed // 跳过表头
                val parts = line.split(",")
                if (parts.size >= 5) {
                    val year = parts[0].toInt()
                    val englishName = parts[1]
                    val chineseName = parts[2]
                    val pantoneNumber = parts[3]
                    val hexColor = parts[4].removeSurrounding("\"").trim()
                    colorData.add(ColorYear(year, englishName, chineseName, pantoneNumber, hexColor))
                }
            }
        }
    }

    private fun displayColorData() {
        val color = colorData[currentIndex]
        binding.colorView.setBackgroundColor(android.graphics.Color.parseColor(color.hexColor))
        binding.yearText.text = color.year.toString()
        binding.englishNameText.text = color.englishName
        binding.chineseNameText.text = color.chineseName
        binding.pantoneNumberText.text = color.pantoneNumber
    }
    
    // 处理滑动过渡动画
    private fun animateColorChange(direction: Int) {
        val animationDuration = 300L
        val screenHeight = resources.displayMetrics.heightPixels
        
        // 计算目标位置
        val targetTranslationY = if (direction > 0) -screenHeight.toFloat() else screenHeight.toFloat()
        
        // 执行退出动画
        binding.root.animate()
            .translationY(targetTranslationY)
            .setDuration(animationDuration / 2)
            .setInterpolator(DecelerateInterpolator())
            .withEndAction {
                // 更新当前索引
                if (direction > 0) {
                    // 向上滑动：显示上一年
                    currentIndex = if (currentIndex == 0) colorData.size - 1 else currentIndex - 1
                } else {
                    // 向下滑动：显示下一年
                    currentIndex = (currentIndex + 1) % colorData.size
                }
                
                // 显示新数据
                displayColorData()
                
                // 重置位置并执行进入动画
                binding.root.translationY = -targetTranslationY
                binding.root.animate()
                    .translationY(0f)
                    .setDuration(animationDuration / 2)
                    .setInterpolator(DecelerateInterpolator())
                    .start()
            }
            .start()
    }
    
    // 手势检测器方法
    override fun onDown(e: MotionEvent): Boolean {
        return true
    }
    
    override fun onShowPress(e: MotionEvent) {
        // 不需要处理
    }
    
    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return false
    }
    
    override fun onScroll(e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
        if (e1 != null) {
            // 更新偏移量
            translationY = e2.y - e1.y
            binding.root.translationY = translationY
            isScrolling = true
        }
        return true
    }
    
    override fun onLongPress(e: MotionEvent) {
        // 不需要处理
    }
    
    override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        if (e1 != null) {
            val diffY = e2.y - e1.y
            val SWIPE_THRESHOLD = 100f
            val SWIPE_VELOCITY_THRESHOLD = 100f
            
            // 检测滑动阈值和速度
            if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffY > 0) {
                    // 向下滑动：显示下一年
                    animateColorChange(-1)
                } else {
                    // 向上滑动：显示上一年
                    animateColorChange(1)
                }
                return true
            }
        }
        return false
    }
    
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val handled = gestureDetector.onTouchEvent(event)
        
        // 处理触摸结束事件，恢复原位
        if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
            if (isScrolling) {
                isScrolling = false
                // 使用弹簧动画恢复原位
                springAnimation.animateToFinalPosition(0f)
            }
        }
        
        return handled || super.onTouchEvent(event)
    }

    data class ColorYear(
        val year: Int,
        val englishName: String,
        val chineseName: String,
        val pantoneNumber: String,
        val hexColor: String
    )
}
