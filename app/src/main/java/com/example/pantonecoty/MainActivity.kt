package com.example.pantonecoty

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat

import com.example.pantonecoty.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), GestureDetector.OnGestureListener {
    private lateinit var binding: ActivityMainBinding
    private val colorData = mutableListOf<ColorYear>()
    private var currentIndex = 0
    private lateinit var gestureDetector: GestureDetectorCompat
    private var initialY = 0f
    private var currentTranslationY = 0f
    private val SCREEN_HEIGHT by lazy { resources.displayMetrics.heightPixels }
    private val SWIPE_THRESHOLD by lazy { SCREEN_HEIGHT * 0.2f } // 20% of screen height

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
        
        // 设置触摸监听器到根视图和ScrollView，确保整个屏幕都能响应滑动
        val touchListener = View.OnTouchListener {
            _, event -> 
            val handled = gestureDetector.onTouchEvent(event)
            
            // 处理触摸结束事件
            if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                handleTouchEnd()
            }
            handled
        }
        
        binding.root.setOnTouchListener(touchListener)
        binding.scrollView.setOnTouchListener(touchListener)

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
        val screenHeight = SCREEN_HEIGHT
        
        // 计算目标位置
        val targetTranslationY = if (direction > 0) -screenHeight.toFloat() else screenHeight.toFloat()
        
        // 执行退出动画，从当前位置开始
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
                    .withEndAction {
                        currentTranslationY = 0f
                    }
                    .start()
            }
            .start()
    }
    
    // 处理触摸结束事件
    private fun handleTouchEnd() {
        val direction: Int
        val shouldChangeColor: Boolean
        
        if (currentTranslationY < -SWIPE_THRESHOLD) {
            // 向上滑动超过阈值，显示上一年
            direction = 1
            shouldChangeColor = true
        } else if (currentTranslationY > SWIPE_THRESHOLD) {
            // 向下滑动超过阈值，显示下一年
            direction = -1
            shouldChangeColor = true
        } else {
            // 未超过阈值，恢复原位
            direction = 0
            shouldChangeColor = false
        }
        
        if (shouldChangeColor) {
            // 执行颜色切换动画
            animateColorChange(direction)
        } else {
            // 恢复原位
            binding.root.animate()
                .translationY(0f)
                .setDuration(300L)
                .setInterpolator(DecelerateInterpolator())
                .withEndAction {
                    currentTranslationY = 0f
                }
                .start()
        }
    }
    
    // 手势检测器方法
    override fun onDown(e: MotionEvent): Boolean {
        initialY = e.y
        currentTranslationY = 0f
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
            // 计算当前偏移量，跟随手指移动
            currentTranslationY = e2.y - e1.y
            
            // 将视图跟随手指移动
            binding.root.translationY = currentTranslationY
        }
        return true
    }
    
    override fun onLongPress(e: MotionEvent) {
        // 不需要处理
    }
    
    override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        // Fling 手势由 handleTouchEnd() 处理
        return true
    }
    
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }

    data class ColorYear(
        val year: Int,
        val englishName: String,
        val chineseName: String,
        val pantoneNumber: String,
        val hexColor: String
    )
}
